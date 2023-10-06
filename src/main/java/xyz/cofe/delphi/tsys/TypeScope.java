package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.Result;
import xyz.cofe.delphi.ast.*;

import java.util.*;

/**
 * Область видимости типов
 * <p/>
 *
 * Задача
 *
 *   Импортировать типы определенные в Unit
 *      Определенные типы - добавить как определение
 *         Для типов на которые ссылки - добавить как ссылки заглушки
 *
 *   По завершению импорта Unit-ов
 *      Рекурсивно обойти все ссылки и
 *          заменить на найденные
 *          заменить на нен найденые
 * </ul>
 */
public class TypeScope implements Freeze {
    private volatile boolean frozen;

    private final Set<Type> types = new HashSet<>();
    private final Map<TypeName, Type> typeByName = new HashMap<>();

    public TypeScope() {
        initSimpleTypes();
    }

    //region freeze
    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(){
        types.forEach(t -> {
            if( t instanceof Freeze f )f.freeze();
        });
        typeByName.values().forEach( t -> {
            if( t instanceof Freeze f )f.freeze();
        });
        this.frozen = true;
    }
    //endregion

    private void initSimpleTypes() {
        SimpleType.typeMap().forEach(this::add);
    }

    /**
     * Возвращает карту типов
     * @return карта
     */
    public Map<TypeName,Type> typeMap(){
        return typeByName;
    }

    /**
     * Получение типа по его имени
     * @param typeName имя типа
     * @return тип
     */
    public Optional<Type> get(TypeName typeName){
        if( typeName==null ) throw new IllegalArgumentException("typeName==null");
        var type = typeByName.get(typeName);
        if( type!=null )return Optional.of(type);
        return Optional.empty();
    }

    /**
     * Добавляем тип в область видимости
     *
     * @param type тип
     */
    public void add(Type type) {
        if (type == null) throw new IllegalArgumentException("type==null");
        if( frozen )throw new TypeSysError.Frozen();
        types.add(type);

        if (type instanceof NamesOfType t) {
            var names = t.names();
            for (var name : names) {
                typeByName.put(name, type);
            }
        } else if (type instanceof NamedType t) {
            var typeName = t.name();
            typeByName.put(typeName, type);
        }
    }

    /**
     * Добавляет именованный тип
     *
     * @param typeName имя типа
     * @param type     тип
     */
    public void add(TypeName typeName, Type type) {
        if (typeName == null) throw new IllegalArgumentException("typeName==null");
        if (type == null) throw new IllegalArgumentException("type==null");
        if( frozen )throw new TypeSysError.Frozen();

        types.add(type);
        typeByName.put(typeName, type);
    }

    /**
     * Импортирует типы определенные в Unit файле
     * @param unit файл
     */
    public void add(PascalFileAst.Unit unit) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        if( frozen )throw new TypeSysError.Frozen();

        var typeSec = unit.api().declarations().filter( d -> d instanceof TypeSectionAst).map(d -> (TypeSectionAst)d );
        typeSec.forEach(typeSection -> {
            typeSection.types().forEach( tdecl -> {
                var ident = tdecl.typeIdent();
                var decl = tdecl.typeDecl();
                if( decl instanceof TypeDeclAst.Array a ) {}
                if( decl instanceof TypeDeclAst.Interface a ) {
                    add(unit, ident, a);
                }
                if( decl instanceof TypeDeclAst.Clazz a ) {}
                if( decl instanceof TypeDeclAst.MetaClass a ) {}
                if( decl instanceof TypeDeclAst.Simple a ) {}
                if( decl instanceof TypeDeclAst.StringType a ) {}
                if( decl instanceof TypeDeclAst.Structured a ) {}
                if( decl instanceof TypeDeclAst.TypeAlias a ) {}
                if( decl instanceof TypeDeclAst.Variant a ) {}
                if( decl instanceof TypeIdentAst a ) {}
            });
        });
    }

    protected void add(PascalFileAst.Unit unit, TypeIdentAst ident, TypeDeclAst.Interface astItf) {
        if( unit==null ) throw new IllegalArgumentException("unit==null");
        if( ident==null ) throw new IllegalArgumentException("ident==null");
        if( astItf==null ) throw new IllegalArgumentException("astItf==null");
        if( frozen )throw new TypeSysError.Frozen();

        var unitName = TypeName.of(unit.name());
        var itfName = TypeName.of(ident.name());
        var typeName = unitName.append(itfName);

        var itf = new InterfaceType.Named();
        itf.setName(typeName);
        itf.setComments(astItf.comments());

        var methods = astItf.body().map(itfItm -> {
            if( itfItm instanceof ClassMethodAst cm){
                if(cm instanceof ClassMethodAst.Procedure p){
                    return interfaceItemOf(unit,itf,ident,p).mapErr(e -> "procedure "+p.name()+" "+e);
                }else if(cm instanceof ClassMethodAst.Constructor c) {
                }else if(cm instanceof ClassMethodAst.Destructor d) {
                }else if(cm instanceof ClassMethodAst.Function f) {
                    return interfaceItemOf(unit,itf,ident,f).mapErr(e -> "function "+f.name()+" "+e);
                }else if(cm instanceof ClassMethodAst.Operator o) {
                }
            }else if( itfItm instanceof ClassPropertyAst cp){
                if(cp instanceof ClassPropertyAst.Property p){
                    return interfaceItemOf(unit,itf,ident,p).mapErr(e -> "property "+p.name()+" "+e);
                }
            }

            return Result.<InterfaceItem,String>error("can't map "+itfItm.getClass());
        });

        methods.forEach( m -> {
            itf.setBody( itf.getBody().append(m.unwrap(InterfaceItem.BrokenMethod::new)) );
        });

        ImList<Type,?> parents = astItf.parents().map( pAst -> {
            var tn = TypeName.of(pAst.name());
            return get(tn).orElse(new Type.UnitTypeRef(unit, TypeIdentAst.of(tn)));
        });
        itf.setParents(parents);

        add(itf);
    }

    protected Result<InterfaceItem,String> interfaceItemOf(PascalFileAst.Unit unit, InterfaceType.Named self, TypeIdentAst selfName, ClassMethodAst.Procedure proc ){
        var m = new Procedure();
        m.setName(proc.name());
        m.setVisibility(Visibility.Public);
        m.setDeclaration(Optional.of(proc.position()));
        m.setImplementation(Optional.empty());
        m.setDirectives(proc.directives().map(MethodDirective::of));
        m.setComments(proc.comments());
        var args = proc.arguments().foldRight(
            Result.ok(ImListLinked.<Argument>of(),String.class),
            (acc,it) -> acc.fmap(lst -> {
                var ma = new Argument();

                if( it.typeDecl().isEmpty() && it.defaultValue().isEmpty() ){
                    return Result.error("arg "+it.name()+" both typeDecl and defaultValue is empty - not implemented");
                }
                if( it.typeDecl().isEmpty() ){
                    return Result.error("arg "+it.name()+" both typeDecl");
                }

                var argType = it.typeDecl().get();
                if( selfName.equals(argType) ){
                    ma.setType(self);
                } else if( argType instanceof TypeIdentAst t ){
                    ma.setType(
                        get(TypeName.of(t.name())).orElse( new Type.UnitTypeRef(unit, argType) )
                    );
                }

                ma.setName(it.name());

                if( it.constraint().isEmpty() ){
                    ma.setDirection(ArgumentDirection.Input);
                }else{
                    ma.setDirection(switch (it.constraint().get()){
                        case Out -> ArgumentDirection.Output;
                        case Var -> ArgumentDirection.InputOutput;
                        case Const -> ArgumentDirection.ConstInput;
                    });
                }

                return Result.ok(lst.prepend(ma));
            })
        );
        return args.map( argz -> {
            m.setArguments(argz);
            return m;
        });
    }

    protected Result<InterfaceItem,String> interfaceItemOf(PascalFileAst.Unit unit, InterfaceType.Named self, TypeIdentAst selfName, ClassMethodAst.Function fun ){
        var m = new Function();
        m.setName(fun.name());
        m.setReturns(prepareTypeOf(unit,self,selfName,fun.result()));
        m.setVisibility(Visibility.Public);
        m.setDeclaration(Optional.of(fun.position()));
        m.setImplementation(Optional.empty());
        m.setDirectives(fun.directives().map(MethodDirective::of));
        m.setComments(fun.comments());

        var args = fun.arguments().foldRight(
            Result.ok(ImListLinked.<Argument>of(),String.class),
            (acc,it) -> acc.fmap(lst -> {
                var ma = new Argument();

                if( it.typeDecl().isEmpty() && it.defaultValue().isEmpty() ){
                    return Result.error("arg "+it.name()+" both typeDecl and defaultValue is empty - not implemented");
                }
                if( it.typeDecl().isEmpty() ){
                    return Result.error("arg "+it.name()+" both typeDecl");
                }

                ma.setType(prepareTypeOf(unit,self,selfName,it.typeDecl().get()));

                ma.setName(it.name());

                if( it.constraint().isEmpty() ){
                    ma.setDirection(ArgumentDirection.Input);
                }else{
                    ma.setDirection(switch (it.constraint().get()){
                        case Out -> ArgumentDirection.Output;
                        case Var -> ArgumentDirection.InputOutput;
                        case Const -> ArgumentDirection.ConstInput;
                    });
                }

                return Result.ok(lst.prepend(ma));
            })
        );

        return args.map( argz -> {
            m.setArguments(argz);
            return m;
        });
    }

    protected Result<InterfaceItem,String> interfaceItemOf(PascalFileAst.Unit unit, InterfaceType.Named self, TypeIdentAst selfName, ClassPropertyAst.Property prop) {
        var p = new Property();
        p.setName(prop.name());
        argsParse(unit,self,selfName,prop.propertyArray()).fmap( arrArgs -> {
            p.setArrayArguments(arrArgs);

            if( prop.type().isEmpty() )return Result.error("property type not defined");
            p.setType(prepareTypeOf(unit,self,selfName,prop.type().get()));

            p.setDeclaration(Optional.of(prop.position()));

            p.setComments(prop.comments());
            p.setStatik(prop.classFlag());

            ImList<PropertySpecifier,?> propSpec = prop.specifiers().map( s -> propertySpecifier(unit,self,selfName,s) );
            p.setSpecifiers( propSpec );

            return Result.ok(p);
        });

        return Result.error("not implemented");
    }

    protected Type prepareTypeOf(PascalFileAst.Unit unit, Type self, TypeIdentAst selfName, TypeDeclAst returns){
        if( selfName.equals(returns) ) {
            return self;
        } else if( returns instanceof TypeIdentAst t ) {
            return get(TypeName.of(t.name())).orElse(new Type.UnitTypeRef(unit, returns));
        } else if( returns instanceof TypeDeclAst.Variant ) {
            return get(TypeName.of("Variant")).orElse(new Type.UnitTypeRef(unit, returns));
        } else if( returns instanceof TypeDeclAst.StringType.StrIng s ){
            if( s.expression().isEmpty() ){
                return StringType.stringWithOutLengthType;
            }
            return new Type.UnitTypeRef(unit, returns);
        } else {
            return new Type.UnitTypeRef(unit, returns);
        }
    }

    protected Result<ImList<Argument,?>,String> argsParse(PascalFileAst.Unit unit, Type self, TypeIdentAst selfName, ImList<xyz.cofe.delphi.ast.Argument,?> arguments) {
        return arguments.foldRight(
            Result.ok(ImListLinked.<Argument>of(),String.class),
            (acc,it) -> acc.fmap(lst -> {
                var ma = new Argument();

                if( it.typeDecl().isEmpty() && it.defaultValue().isEmpty() ){
                    return Result.error("arg "+it.name()+" both typeDecl and defaultValue is empty - not implemented");
                }
                if( it.typeDecl().isEmpty() ){
                    return Result.error("arg "+it.name()+" typeDecl not implemented");
                }

                var argType = it.typeDecl().get();
                if( selfName.equals(argType) ){
                    ma.setType(self);
                } else if( argType instanceof TypeIdentAst t ){
                    ma.setType(
                        get(TypeName.of(t.name())).orElse( new Type.UnitTypeRef(unit, argType) )
                    );
                }

                ma.setName(it.name());

                if( it.constraint().isEmpty() ){
                    ma.setDirection(ArgumentDirection.Input);
                }else{
                    ma.setDirection(switch (it.constraint().get()){
                        case Out -> ArgumentDirection.Output;
                        case Var -> ArgumentDirection.InputOutput;
                        case Const -> ArgumentDirection.ConstInput;
                    });
                }

                return Result.ok(lst.prepend(ma));
            })
        );
    }

    protected PropertySpecifier propertySpecifier(PascalFileAst.Unit unit, Type self, TypeIdentAst selfName, ClassPropertyAst.Specifier spec) {
        if( spec instanceof ClassPropertyAst.Read r ){
            return new PropertySpecifier.Read(
                r.name(),
                r.expression().map(ExpressionAst::text)
            );
        }
        if( spec instanceof ClassPropertyAst.Write w ){
            return new PropertySpecifier.Write(
                w.name(),
                w.expression().map(ExpressionAst::text)
            );
        }
        if( spec instanceof ClassPropertyAst.ReadOnly r ){
            return new PropertySpecifier.ReadOnly();
        }
        if( spec instanceof ClassPropertyAst.WriteOnly r ){
            return new PropertySpecifier.WriteOnly();
        }
        if( spec instanceof ClassPropertyAst.DispID r ){
            return new PropertySpecifier.DispID(r.expression().text());
        }
        if( spec instanceof ClassPropertyAst.Stored s ){
            return new PropertySpecifier.Stored(s.expression().text());
        }
        if( spec instanceof ClassPropertyAst.DefaultExp d ){
            return new PropertySpecifier.DefaultExp(d.expression().text());
        }
        if( spec instanceof ClassPropertyAst.Default d ){
            return new PropertySpecifier.Default();
        }
        if( spec instanceof ClassPropertyAst.NoDefault n ){
            return new PropertySpecifier.NoDefault();
        }
        if( spec instanceof ClassPropertyAst.Implements i ){
            Type t = null;
            var tn = TypeName.of(i.typeId());
            var selfTn = TypeName.of(selfName.name());
            if( tn.equals(selfTn) ){
                t = self;
            }else{
                var t2 = get(tn);
                t = t2.orElseGet(() -> new Type.UnitTypeRef(unit, new TypeIdentAst(i.typeId(), ImListLinked.of())));
            }

            return new PropertySpecifier.Implements(t);
        }

        throw new RuntimeException("bug! unexpected");
    }
}
