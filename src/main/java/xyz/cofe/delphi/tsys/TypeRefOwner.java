package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.tsys.tm.*;

/**
 * Ссылка на тип который следует "решить"
 */
public sealed interface TypeRefOwner {
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
    //region typeRefs() : ImList<TypeRefOwner,?> - Получение типов которые требуется разрешить
    //endregion


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
        Proc fun();

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
    public record ArgOfFunction(Type.UnitTypeRef argType, Argument arg, MethodFunction fun, Type objType, PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfFunctionNamed(Type.UnitTypeRef argType, Argument arg, MethodFunction fun, NamedType objType, PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfProcedure(Type.UnitTypeRef argType, Argument arg, MethodProcedure fun, Type objType, PascalUnit unit) implements ArgOfMethod {}
    public record ArgOfProcedureNamed(Type.UnitTypeRef argType, Argument arg, MethodProcedure fun, NamedType objType, PascalUnit unit) implements ArgOfMethod {}
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
    public record Parent(Type.UnitTypeRef parent, ParentsProperty objType, PascalUnit unit) implements TypeRefOwner {
    }

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
                    ? ImListLinked.of(new TypeRefOwner.Parent(ref, ct, unit))
                    : ImListLinked.of());

        ImList<TypeRefOwner> returns = ct.getBody().fmap(clsItm -> {
            if (clsItm instanceof FunSetReturns ret && ret.getReturns() instanceof Type.UnitTypeRef ref) {
                return ImListLinked.of(
                    new TypeRefOwner.ReturnsOfMethod(ref, ret, ct, unit)
                );
            } else {
                return ImListLinked.of();
            }
        });

        ImList<TypeRefOwner> args = ct.getBody().fmap(Proc.class).fmap(f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner> res = f.getArguments().fmap(arg -> {
                if (arg.getType() instanceof Type.UnitTypeRef ref) {
                    if (f instanceof Constructor c) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new TypeRefOwner.ArgOfConstructorNamed(ref, arg, c, nct, unit)) :
                            ImListLinked.of(new TypeRefOwner.ArgOfConstructor(ref, arg, c, ct, unit));
                    } else if (f instanceof Destructor d) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new TypeRefOwner.ArgOfDestructorNamed(ref, arg, d, nct, unit)) :
                            ImListLinked.of(new TypeRefOwner.ArgOfDestructor(ref, arg, d, ct, unit));
                    } else if (f instanceof MethodFunction ff) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new TypeRefOwner.ArgOfFunctionNamed(ref, arg, ff, nct, unit)) :
                            ImListLinked.of(new TypeRefOwner.ArgOfFunction(ref, arg, ff, ct, unit));
                    } else if (f instanceof MethodProcedure p) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new TypeRefOwner.ArgOfProcedureNamed(ref, arg, p, nct, unit)) :
                            ImListLinked.of(new TypeRefOwner.ArgOfProcedure(ref, arg, p, ct, unit));
                    } else if (f instanceof Operator op) {
                        return ct instanceof ClassType.Named nct ?
                            ImListLinked.of(new TypeRefOwner.ArgOfOperatorNamed(ref, arg, op, nct, unit)) :
                            ImListLinked.of(new TypeRefOwner.ArgOfOperator(ref, arg, op, ct, unit));
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

        ImList<TypeRefOwner> args = ct.getBody().fmap(Proc.class).fmap(f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner> res = f.getArguments().fmap(arg -> {
                if (arg.getType() instanceof Type.UnitTypeRef ref) {
                    if (f instanceof MethodFunction ff) {
                        return ct instanceof InterfaceType.Named nct ?
                            ImListLinked.of(new ArgOfFunctionNamed(ref, arg, ff, nct, unit)) :
                            ImListLinked.of(new ArgOfFunction(ref, arg, ff, ct, unit));
                    } else if (f instanceof MethodProcedure pp) {
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
}
