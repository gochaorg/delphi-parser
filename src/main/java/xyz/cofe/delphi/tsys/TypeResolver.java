package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.TypeIdentAst;
import xyz.cofe.delphi.tsys.tm.*;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Разрешение типов
 */
public interface TypeResolver {
    /**
     * Возвращает тип, который был найден
     * @return тип
     */
    Type resolved();

    /**
     * Применение
     */
    void apply();

    private static TypeResolver create(Type type, Runnable applyCode){
        if( type==null ) throw new IllegalArgumentException("type==null");
        if( applyCode==null ) throw new IllegalArgumentException("applyCode==null");
        return new TypeResolver() {
            @Override
            public Type resolved() {
                return type;
            }

            @Override
            public void apply() {
                applyCode.run();
            }
        };
    }

    /**
     * Разрешение типа
     *
     * @param typeRef   ссылка на тип
     * @param typeScope область определения типов
     * @return тип
     */
    public static Optional<TypeResolver> resolve(TypeRefOwner typeRef, TypeScope typeScope) {
        if (typeRef == null) throw new IllegalArgumentException("typeRef==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");

        if (typeRef instanceof TypeRefOwner.ArgOfMethod a) return resolve(a, typeScope);
        if (typeRef instanceof TypeRefOwner.ReturnsOfMethod a) return resolve(a, typeScope);
        if (typeRef instanceof TypeRefOwner.Parent a) return resolve(a, typeScope);

        return Optional.empty();
    }

    /**
     * Разрешение типа
     *
     * @param arg       Аргумент
     * @param typeScope Область типов
     * @return тип
     */
    public static Optional<TypeResolver> resolve(TypeRefOwner.ArgOfMethod arg, TypeScope typeScope) {
        if (arg == null) throw new IllegalArgumentException("arg==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");
        if (arg.argType().type() instanceof TypeIdentAst id) {
            return resolve(id, arg.unit(), typeScope).map( type -> create(type, ()-> arg.arg().setType(type)));
        }
        return Optional.empty();
    }

    public static Optional<TypeResolver> resolve(TypeRefOwner.ReturnsOfMethod ret, TypeScope typeScope) {
        if (ret == null) throw new IllegalArgumentException("ret==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");
        if (ret.retType().type() instanceof TypeIdentAst id) {
            return resolve(id, ret.unit(), typeScope).map(type -> create(type, ()-> ret.fun().setReturns(type)));
        }
        return Optional.empty();
    }

    public static Optional<TypeResolver> resolve(TypeRefOwner.Parent prnt, TypeScope typeScope) {
        if (prnt == null) throw new IllegalArgumentException("prnt==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");
        if (prnt.parent().type() instanceof TypeIdentAst id) {
            return resolve(id, prnt.unit(), typeScope).map(type -> create(type,()->{
                prnt.objType().setParents(
                    prnt.objType().getParents().filter( t -> t != prnt.parent() ).prepend( type )
                );
            }));
        }
        return Optional.empty();
    }

    /**
     * Попытка разрешить ссылку через поиск в модуле или секции uses
     *
     * @param typeIdRef ссылка
     * @param unitRef   модуль содержащий ссылку
     * @param typeScope область типов
     * @return тип
     */
    private static Optional<Type> resolve(TypeIdentAst typeIdRef, PascalUnit unitRef, TypeScope typeScope) {
        return resolveNear(typeIdRef, unitRef, typeScope).or(() -> resolveUses(typeIdRef, unitRef, typeScope));
    }

    /**
     * Поиск типа там же, где указана ссылка
     *
     * @param typeIdRef ссылка
     * @param unitRef   модуль содержащий ссылку
     * @param typeScope область типов
     * @return тип
     */
    private static Optional<Type> resolveNear(TypeIdentAst typeIdRef, PascalUnit unitRef, TypeScope typeScope) {
        if (typeIdRef.name().size() == 1) {
            return unitRef.getTypes().fmap(NamedType.class).find(
                namedType -> namedType.name().last().flatMap(
                    na -> typeIdRef.name().get(0).map(nb -> nb.equalsIgnoreCase(na))
                ).orElse(false)
            ).map(n -> n);
        }
        return Optional.empty();
    }

    /**
     * Поиск в области типов, относительно секции uses модуля содержащего ссылку
     *
     * @param typeIdRef ссылка
     * @param unitRef   модуль содержащий ссылку
     * @param typeScope область типов
     * @return тип
     */
    private static Optional<Type> resolveUses(TypeIdentAst typeIdRef, PascalUnit unitRef, TypeScope typeScope) {
        if (typeIdRef.name().size() == 1) {
            return unitRef.getUses().stream().flatMap(tn ->
                    typeScope.unitMap().containsKey(tn)
                        ? typeScope.unitMap().get(tn).stream() : Stream.empty()
                ).flatMap(unit ->
                    unit.getTypes().stream().flatMap(t -> t instanceof NamedType nt ? Stream.of(nt) : Stream.empty())
                )
                .flatMap(namedType -> namedType.name().last().flatMap(na ->
                        {
                            var t = typeIdRef.name().get(0).map(nb -> nb.equalsIgnoreCase(na)).orElse(false);
                            return t ? Optional.of(namedType) : Optional.empty();
                        }
                    ).stream()
                ).findFirst().map(t -> t);
        }
        return Optional.empty();
    }
}
