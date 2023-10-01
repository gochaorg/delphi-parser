package xyz.cofe.delphi.tsys;

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
public class TypeScope {
    private final Set<Type> types = new HashSet<>();
    private final Map<TypeName, Type> typeByName = new HashMap<>();

    public TypeScope() {
        initSimpleTypes();
    }

    private void initSimpleTypes() {
        SimpleType.typeMap().forEach(this::add);
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

        types.add(type);
        typeByName.put(typeName, type);
    }

    /**
     * Импортирует типы определенные в Unit файле
     * @param unit файл
     */
    public void add(PascalFileAst.Unit unit) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        var typeSec = unit.api().declarations().filter( d -> d instanceof TypeSectionAst).map(d -> (TypeSectionAst)d );
        typeSec.forEach(typeSection -> {
            typeSection.types().forEach( tdecl -> {
                var ident = tdecl.typeIdent();
                var decl = tdecl.typeDecl();
                if( decl instanceof TypeDeclAst.Array a ) {}
                if( decl instanceof TypeDeclAst.Interface a ) {
                    add(unit, ident, a);
                }
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

        var unitName = TypeName.of(unit.name());
        var itfName = TypeName.of(ident.name());
        var typeName = unitName.append(itfName);

        var itf = new InterfaceType.Named();
        itf.setName(typeName);

//        astItf.body().map(itfItm -> {
//            if( itfItm instanceof ClassMethod cm){
//                if(cm instanceof ClassMethod.Procedure p){
//                }else if(cm instanceof ClassMethod.Constructor c) {
//                }else if(cm instanceof ClassMethod.Destructor c) {
//                }else if(cm instanceof ClassMethod.Function c) {
//                }else if(cm instanceof ClassMethod.Operator c) {
//                }
//            }else if( itfItm instanceof ClassProperty cp){
//                if(cp instanceof ClassProperty.Property p){
//                }
//            }
//        });
    }

//    protected
}
