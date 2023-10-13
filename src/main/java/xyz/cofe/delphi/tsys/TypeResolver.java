package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.TypeIdentAst;
import xyz.cofe.delphi.tsys.tm.*;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Разрешение типов
 */
public class TypeResolver {
    /**
     * Ссылка на тип который следует "решить"
     */
    public sealed interface TypeRefOwner {
    }

    /**
     * Тип аргумента метода класса/интерфейса
     *
     * @param argType        Тип аргумента который надо разрешить
     * @param arg            Аргумент
     * @param fun            Метод
     * @param classOrItfType Класс или интерфейс в котором определен метод
     * @param unit           Модуль в котором определен тип
     */
    public record ArgOfMethod(Type.UnitTypeRef argType, Argument arg, Fun fun, Type classOrItfType,
                              PascalUnit unit) implements TypeRefOwner {
    }

    /**
     * Тип результата метода класса/интерфейса
     *
     * @param retType        Тип результата метода
     * @param fun            Метод
     * @param classOrItfType Класс или интерфейс в котором определен метод
     * @param unit           Модуль в котором определен тип
     */
    public record ReturnsOfMethod(Type.UnitTypeRef retType, FunSetReturns fun, Type classOrItfType,
                                  PascalUnit unit) implements TypeRefOwner {
    }

    /**
     * Тип родительского объекта
     *
     * @param parent         Тип родителя класса/интерфейса
     * @param classOrItfType Класс или интерфейс в котором указан родитель
     * @param unit           Модуль в котором определен тип
     */
    public record Parent(Type.UnitTypeRef parent, Type classOrItfType, PascalUnit unit) implements TypeRefOwner {
    }

    //region typeRefs() : ImList<TypeRefOwner,?> - Получение типов которые требуется разрешить

    /**
     * Получение типов которые требуется разрешить
     *
     * @param unit модуль
     * @return типы требующие разрешения
     */
    public static ImList<TypeRefOwner, ?> typeRefs(PascalUnit unit) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        //noinspection UnnecessaryLocalVariable
        ImList<TypeRefOwner, ?> result = unit.getTypes().fmap(someType -> {
            if (someType instanceof ClassType ct) {
                return typeRefs(unit, ct);
            } else if (someType instanceof InterfaceType it) {
                return typeRefs(unit, it);
            } else {
                return ImListLinked.of();
            }
        });
        return result;
    }

    private static ImList<TypeRefOwner, ?> typeRefs(PascalUnit unit, ClassType ct) {
        ImList<TypeRefOwner, ?> parents =
            ct.getParents().fmap(
                prntType -> prntType instanceof Type.UnitTypeRef ref
                    ? ImListLinked.of(new Parent(ref, ct, unit))
                    : ImListLinked.of());

        ImList<TypeRefOwner, ?> returns = ct.getBody().fmap(clsItm -> {
            if (clsItm instanceof FunSetReturns ret && ret.getReturns() instanceof Type.UnitTypeRef ref) {
                return ImListLinked.of(
                    new ReturnsOfMethod(ref, ret, ct, unit)
                );
            } else {
                return ImListLinked.of();
            }
        });

        ImList<TypeRefOwner, ?> args = ct.getBody().fmap(clsItm -> {
            if (clsItm instanceof Fun f) {
                return ImListLinked.of(f);
            } else {
                return ImListLinked.of();
            }
        }).fmap(f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner, ?> res = f.getArguments().fmap(arg -> {
                if (arg.getType() instanceof Type.UnitTypeRef ref) {
                    return ImListLinked.of(new ArgOfMethod(ref, arg, f, ct, unit));
                } else {
                    return ImListLinked.of();
                }
            });
            return res;
        });

        return parents.prepend(returns).prepend(args);
    }

    private static ImList<TypeRefOwner, ?> typeRefs(PascalUnit unit, InterfaceType ct) {
        ImList<TypeRefOwner, ?> parents =
            ct.getParents().fmap(
                prntType -> prntType instanceof Type.UnitTypeRef ref
                    ? ImListLinked.of(new Parent(ref, ct, unit))
                    : ImListLinked.of());

        ImList<TypeRefOwner, ?> returns = ct.getBody().fmap(clsItm -> {
            if (clsItm instanceof FunSetReturns ret && ret.getReturns() instanceof Type.UnitTypeRef ref) {
                return ImListLinked.of(
                    new ReturnsOfMethod(ref, ret, ct, unit)
                );
            } else {
                return ImListLinked.of();
            }
        });

        ImList<TypeRefOwner, ?> args = ct.getBody().fmap(clsItm -> {
            if (clsItm instanceof Fun f) {
                return ImListLinked.of(f);
            } else {
                return ImListLinked.of();
            }
        }).fmap(f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner, ?> res = f.getArguments().fmap(arg -> {
                if (arg.getType() instanceof Type.UnitTypeRef ref) {
                    return ImListLinked.of(new ArgOfMethod(ref, arg, f, ct, unit));
                } else {
                    return ImListLinked.of();
                }
            });
            return res;
        });

        return parents.prepend(returns).prepend(args);
    }
    //endregion

    /**
     * Разрешение типа
     *
     * @param typeRef   ссылка на тип
     * @param typeScope область определения типов
     * @return тип
     */
    public static Optional<Type> resolve(TypeRefOwner typeRef, TypeScope typeScope) {
        if (typeRef == null) throw new IllegalArgumentException("typeRef==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");

        if (typeRef instanceof ArgOfMethod a) return resolve(a, typeScope);
        if (typeRef instanceof ReturnsOfMethod a) return resolve(a, typeScope);
        if (typeRef instanceof Parent a) return resolve(a, typeScope);

        return Optional.empty();
    }

    /**
     * Разрешение типа
     *
     * @param arg       Аргумент
     * @param typeScope Область типов
     * @return тип
     */
    public static Optional<Type> resolve(ArgOfMethod arg, TypeScope typeScope) {
        if (arg == null) throw new IllegalArgumentException("arg==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");
        if (arg.argType().type() instanceof TypeIdentAst id) {
            return resolve(id, arg.unit(), typeScope);
        }
        return Optional.empty();
    }

    public static Optional<Type> resolve(ReturnsOfMethod ret, TypeScope typeScope) {
        if (ret == null) throw new IllegalArgumentException("ret==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");
        if (ret.retType().type() instanceof TypeIdentAst id) {
            return resolve(id, ret.unit(), typeScope);
        }
        return Optional.empty();
    }

    public static Optional<Type> resolve(Parent prnt, TypeScope typeScope) {
        if (prnt == null) throw new IllegalArgumentException("prnt==null");
        if (typeScope == null) throw new IllegalArgumentException("typeScope==null");
        if( prnt.parent().type() instanceof TypeIdentAst id ){
            return resolve(id, prnt.unit(), typeScope);
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
