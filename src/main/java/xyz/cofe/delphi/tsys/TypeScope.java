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
                }else if(cm instanceof ClassMethodAst.Constructor c) {
                }else if(cm instanceof ClassMethodAst.Destructor d) {
                }else if(cm instanceof ClassMethodAst.Function f) {
                    return interfaceItemOf(unit,itf,ident,f).mapErr(e -> "function "+f.name()+" "+e);
                }else if(cm instanceof ClassMethodAst.Operator o) {
                }
            }else if( itfItm instanceof ClassPropertyAst cp){
                if(cp instanceof ClassPropertyAst.Property p){
                }
            }

            return Result.<InterfaceItem,String>error("can't map "+itfItm.getClass());
        });

        methods.forEach( m -> {
            itf.setBody( itf.getBody().append(m.unwrap(InterfaceItem.BrokenMethod::new)) );
        });

        add(itf);
    }

    protected Result<InterfaceItem,String> interfaceItemOf(PascalFileAst.Unit unit, InterfaceType.Named self, TypeIdentAst selfName, ClassMethodAst.Procedure proc ){
        var m = new Method();
        m.setName(proc.name());
        m.setReturns(Optional.empty());
        m.setVisibility(Visibility.Public);
        m.setDeclaration(Optional.of(proc.position()));
        m.setImplementation(Optional.empty());
        m.setDirectives(proc.directives().map(MethodDirective::of));
        m.setComments(proc.comments());
        var args = proc.arguments().foldRight(
            Result.ok(ImListLinked.<MethodArgument>of(),String.class),
            (acc,it) -> acc.fmap(lst -> {
                var ma = new MethodArgument();

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
        var m = new Method();
        m.setName(fun.name());
        m.setReturns(Optional.of(returnTypeOf(unit,self,selfName,fun.result())));
        m.setVisibility(Visibility.Public);
        m.setDeclaration(Optional.of(fun.position()));
        m.setImplementation(Optional.empty());
        m.setDirectives(fun.directives().map(MethodDirective::of));
        m.setComments(fun.comments());

//        m.setArguments(
//            fun.arguments().map( astArg -> {
//                var ma = new MethodArgument();
//                return ma; // idea bug! ma - по факту нормально компилируется, но idea думает что тип не совпадает
//            })
//        );

        var args = fun.arguments().foldRight(
            Result.ok(ImListLinked.<MethodArgument>of(),String.class),
            (acc,it) -> acc.fmap(lst -> {
                var ma = new MethodArgument();

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

    protected Type returnTypeOf(PascalFileAst.Unit unit, Type self, TypeIdentAst selfName, TypeDeclAst returns){
        if( selfName.equals(returns) ) {
            return self;
        } else if( returns instanceof TypeIdentAst t ){
            return get(TypeName.of(t.name())).orElse( new Type.UnitTypeRef(unit,returns) );
        } else {
            return new Type.UnitTypeRef(unit, returns);
        }
    }

    protected Result<ImList<MethodArgument,?>,String> argsParse(PascalFileAst.Unit unit, Type self, TypeIdentAst selfName, ImList<Argument,?> arguments) {
        return arguments.foldRight(
            Result.ok(ImListLinked.<MethodArgument>of(),String.class),
            (acc,it) -> acc.fmap(lst -> {
                var ma = new MethodArgument();

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
}
