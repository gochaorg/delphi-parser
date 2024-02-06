package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.*;
import xyz.cofe.delphi.ast.ClazzTypeAst;
import xyz.cofe.delphi.tsys.tm.*;

import java.util.*;

/**
 * Область видимости типов
 * <p>
 *
 * Задача <p>
 *
 * <pre>
 *   Импортировать типы определенные в Unit
 *      Определенные типы - добавить как определение
 *         Для типов на которые ссылки - добавить как ссылки заглушки
 *   По завершению импорта Unit-ов
 *      Рекурсивно обойти все ссылки и
 *          заменить на найденные
 *          заменить на нен найденые
 * </pre>
 */
public class TypeScope implements Freeze {
    private volatile boolean frozen;

    private final Set<Type> types = new HashSet<>();
    private final Map<TypeName, List<Type>> typeByName = new HashMap<>();
    private final Map<TypeName, List<PascalUnit>> units = new HashMap<>();

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
        typeByName.values().forEach( l -> {
            l.forEach( t -> {
                if (t instanceof Freeze f) f.freeze();
            });
        });
        units.values().forEach( l -> l.forEach(PascalUnit::freeze));
        this.frozen = true;
    }
    //endregion

    //region typeMap() / getType()
    private void initSimpleTypes() {
        SimpleType.typeMap().forEach(this::add);
    }

    /**
     * Возвращает карту типов
     * @return карта
     */
    @SuppressWarnings("unused")
    public Map<TypeName,List<Type>> typeMap(){
        return typeByName;
    }

    /**
     * Получение типа по его имени
     * @param typeName имя типа
     * @return тип
     */
    public Optional<Type> getType(TypeName typeName){
        if( typeName==null ) throw new IllegalArgumentException("typeName==null");
        var lst = typeByName.get(typeName);
        if( lst!=null && !lst.isEmpty() )return Optional.of(lst.get(0));
        return Optional.empty();
    }
    //endregion

    /**
     * Возвращает карту модулей
     * @return карта
     */
    public Map<TypeName,List<PascalUnit>> unitMap() {
        return units;
    }

    //region add / import type
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
                typeByName.computeIfAbsent(name, n -> new ArrayList<>()).add(type);
            }
        } else if (type instanceof NamedType t) {
            var typeName = t.name();
            typeByName.computeIfAbsent(typeName, n -> new ArrayList<>()).add(type);
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
        typeByName.computeIfAbsent(typeName, n -> new ArrayList<>()).add(type);
    }

    private final TypeImporter typeImporter = new TypeImporter(this::getType);

    /**
     * Импортирует типы определенные в Unit файле
     * @param unit файл
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public PascalUnit add(PascalFileAst.Unit unit) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        if( frozen )throw new TypeSysError.Frozen();

        var pasUnit = new PascalUnit();
        pasUnit.setName(TypeName.of(unit.name()));
        pasUnit.setComments(unit.comments());
        pasUnit.setDeclaration(Optional.of(unit.position()));
        pasUnit.setUses(unit.api().uses().map(ns -> TypeName.of(ns.name())));

        List<Type> types = new ArrayList<>();

        var typeSec = unit.api().declarations().filter( d -> d instanceof TypeSectionAst).map(d -> (TypeSectionAst)d );
        typeSec.each(typeSection -> typeSection.types().each(tdecl -> {
            var ident = tdecl.typeIdent();
            var decl = tdecl.typeDecl();
            if( decl instanceof ArrayTypeAst) {}
            if( decl instanceof InterfaceTypeAst a ) {
                var itf = typeImporter.interfaceTypeOf(unit, ident, a);
                types.add(itf);
                add(itf);
            }
            if( decl instanceof ClazzTypeAst a ) {
                var cls =  typeImporter.classTypeOf(unit, ident, a);
                types.add(cls);
                add((Type) cls);
            }
            if( decl instanceof SimpleTypeAst) {}
            if( decl instanceof StringTypeAst) {}
            if( decl instanceof StructuredTypeAst) {}
            if( decl instanceof TypeAliasAst) {}
            if( decl instanceof VariantTypeAst) {}
            if( decl instanceof TypeIdentAst ) {}
            if( decl instanceof ProcedureTypeAst ) {}
        }));

        var procedures = new ArrayList<Procedure>();
        var functions = new ArrayList<Function>();
        var notImported = new ArrayList<>();
        unit.api().declarations().fmap(ProcDeclAst.class).each( procDec -> {
            typeImporter.procedureOrFunctionOf(unit, procDec).fold( proc -> {
                if( proc instanceof Procedure p ){
                    procedures.add(p);
                }else if( proc instanceof Function f ){
                    functions.add(f);
                }
                return null;
            }, msg -> {
                System.err.println("not imported: "+msg);
                System.err.println("  "+procDec);
                return null;
            });
        });

        pasUnit.setTypes(ImListLinked.of(types));
        pasUnit.setFunctions(ImList.of(functions));
        pasUnit.setProcedures(ImList.of(procedures));

        units.computeIfAbsent(pasUnit.getName(), u -> new ArrayList<>()).add(pasUnit);

        return pasUnit;
    }
    //endregion
}
