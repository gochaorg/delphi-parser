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
    public void add(PascalFile.Unit unit) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        var typeSec = unit.api().declarations().filter( d -> d instanceof TypeSection ).map( d -> (TypeSection)d );
        typeSec.forEach(typeSection -> {
            typeSection.types().forEach( tdecl -> {
                var ident = tdecl.typeIdent();
                var decl = tdecl.typeDecl();
                if( decl instanceof TypeDecl.Array a ) {}
                if( decl instanceof TypeDecl.Interface a ) {
                    add(unit, ident, a);
                }
                if( decl instanceof TypeDecl.MetaClass a ) {}
                if( decl instanceof TypeDecl.Simple a ) {}
                if( decl instanceof TypeDecl.StringType a ) {}
                if( decl instanceof TypeDecl.Structured a ) {}
                if( decl instanceof TypeDecl.TypeAlias a ) {}
                if( decl instanceof TypeDecl.Variant a ) {}
                if( decl instanceof TypeIdent a ) {}
            });
        });
    }

    protected void add(PascalFile.Unit unit, TypeIdent ident, TypeDecl.Interface itf) {

        System.out.println();
    }
}
