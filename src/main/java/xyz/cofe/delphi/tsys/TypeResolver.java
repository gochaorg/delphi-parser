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
public class TypeResolver {
    /**
     * Ссылка на тип который следует "решить"
     */
    public sealed interface TypeRefOwner {
    }

    /**
     * Тип аргумента метода класса/интерфейса
     */
    public sealed interface ArgOfMethod extends TypeRefOwner {
        /**
         * Тип аргумента который надо разрешить
         *
         * @return Тип аргумента
         */
        Type.UnitTypeRef argType();

        /**
         * Аргумент
         *
         * @return Аргумент
         */
        Argument arg();

        /**
         * Метод
         *
         * @return Метод
         */
        Fun fun();

        /**
         * Класс или интерфейс в котором определен метод
         *
         * @return Класс или интерфейс
         */
        Type objType();

        /**
         * Модуль в котором определен тип
         *
         * @return Модуль в котором определен тип
         */
        PascalUnit unit();
    }

    public record ArgOfConstructor(Type.UnitTypeRef argType, Argument arg, Constructor fun, ClassType objType, PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfConstructorNamed(Type.UnitTypeRef argType, Argument arg, Constructor fun, ClassType.Named objType, PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfDestructor(Type.UnitTypeRef argType, Argument arg, Destructor fun, ClassType objType, PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfDestructorNamed(Type.UnitTypeRef argType, Argument arg, Destructor fun, ClassType.Named objType, PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfFunction(Type.UnitTypeRef argType, Argument arg, Function fun, Type objType,PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfFunctionNamed(Type.UnitTypeRef argType, Argument arg, Function fun, NamedType objType,PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfProcedure(Type.UnitTypeRef argType, Argument arg, Procedure fun, Type objType,PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfProcedureNamed(Type.UnitTypeRef argType, Argument arg, Procedure fun, NamedType objType,PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfOperator(Type.UnitTypeRef argType, Argument arg, Operator fun, ClassType objType,PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfOperatorNamed(Type.UnitTypeRef argType, Argument arg, Operator fun, ClassType.Named objType,PascalUnit unit) implements ArgOfMethod {}

    /**
     * Тип результата метода класса/интерфейса
     *
     * @param retType Тип результата метода
     * @param fun     Метод
     * @param objType Класс или интерфейс в котором определен метод
     * @param unit    Модуль в котором определен тип
     */
    public record ReturnsOfMethod(Type.UnitTypeRef retType, FunSetReturns fun, Type objType,
                                  PascalUnit unit) implements TypeRefOwner {
    }

    /**
     * Тип родительского объекта
     *
     * @param parent  Тип родителя класса/интерфейса
     * @param objType Класс или интерфейс в котором указан родитель
     * @param unit    Модуль в котором определен тип
     */
    public record Parent(Type.UnitTypeRef parent, Type objType, PascalUnit unit) implements TypeRefOwner {
    }

    //region typeRefs() : ImList<TypeRefOwner,?> - Получение типов которые требуется разрешить

    /**
     * Получение типов которые требуется разрешить
     *
     * @param unit модуль
     * @return типы требующие разрешения
     */
    public static ImList<TypeRefOwner> typeRefs(PascalUnit unit) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        //noinspection UnnecessaryLocalVariable
        ImList<TypeRefOwner> result = unit.getTypes().fmap(someType -> {
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

    private static ImList<TypeRefOwner> typeRefs(PascalUnit unit, ClassType ct) {
        ImList<TypeRefOwner> parents =
            ct.getParents().fmap(
                prntType -> prntType instanceof Type.UnitTypeRef ref
                    ? ImListLinked.of(new Parent(ref, ct, unit))
                    : ImListLinked.of());

        ImList<TypeRefOwner> returns = ct.getBody().fmap(clsItm -> {
            if (clsItm instanceof FunSetReturns ret && ret.getReturns() instanceof Type.UnitTypeRef ref) {
                return ImListLinked.of(
                    new ReturnsOfMethod(ref, ret, ct, unit)
                );
            } else {
                return ImListLinked.of();
            }
        });

        ImList<TypeRefOwner> args = ct.getBody().fmap(Fun.class).fmap(f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner> res = f.getArguments().fmap(arg -> {
                if (arg.getType() instanceof Type.UnitTypeRef ref) {
                    if (f instanceof Constructor c) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new ArgOfConstructorNamed(ref, arg, c, nct, unit)) :
                            ImListLinked.of(new ArgOfConstructor(ref, arg, c, ct, unit));
                    } else if (f instanceof Destructor d) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new ArgOfDestructorNamed(ref, arg, d, nct, unit)) :
                            ImListLinked.of(new ArgOfDestructor(ref, arg, d, ct, unit));
                    } else if (f instanceof Function ff) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new ArgOfFunctionNamed(ref, arg, ff, nct, unit)) :
                            ImListLinked.of(new ArgOfFunction(ref, arg, ff, ct, unit));
                    } else if (f instanceof Procedure p) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new ArgOfProcedureNamed(ref, arg, p, nct, unit)) :
                            ImListLinked.of(new ArgOfProcedure(ref, arg, p, ct, unit));
                    } else if (f instanceof Operator op) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new ArgOfOperatorNamed(ref, arg, op, nct, unit)) :
                            ImListLinked.of(new ArgOfOperator(ref, arg, op, ct, unit));
                    } else {
                        throw new RuntimeException("bug!");
                    }
                } else {
                    return ImListLinked.of();
                }
            });
            return res;
        });

        return parents.prepend(returns).prepend(args);
    }

    private static ImList<TypeRefOwner> typeRefs(PascalUnit unit, InterfaceType ct) {
        ImList<TypeRefOwner> parents =
            ct.getParents().fmap(
                prntType -> prntType instanceof Type.UnitTypeRef ref
                    ? ImListLinked.of(new Parent(ref, ct, unit))
                    : ImListLinked.of());

        ImList<TypeRefOwner> returns = ct.getBody().fmap(clsItm -> {
            if (clsItm instanceof FunSetReturns ret && ret.getReturns() instanceof Type.UnitTypeRef ref) {
                return ImListLinked.of(
                    new ReturnsOfMethod(ref, ret, ct, unit)
                );
            } else {
                return ImListLinked.of();
            }
        });

        ImList<TypeRefOwner> args = ct.getBody().fmap(Fun.class).fmap(f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner> res = f.getArguments().fmap(arg -> {
                if (arg.getType() instanceof Type.UnitTypeRef ref) {
                    if (f instanceof Function ff) {
                        return ct instanceof InterfaceType.Named nct ?
                            ImListLinked.of(new ArgOfFunctionNamed(ref, arg, ff, nct, unit)) :
                            ImListLinked.of(new ArgOfFunction(ref, arg, ff, ct, unit));
                    } else if (f instanceof Procedure pp) {
                        return ct instanceof InterfaceType.Named nct ?
                            ImListLinked.of(new ArgOfProcedureNamed(ref, arg, pp, nct, unit)) :
                            ImListLinked.of(new ArgOfProcedure(ref, arg, pp, ct, unit));
                    } else {
                        throw new RuntimeException("bug");
                    }
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
        if (prnt.parent().type() instanceof TypeIdentAst id) {
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
