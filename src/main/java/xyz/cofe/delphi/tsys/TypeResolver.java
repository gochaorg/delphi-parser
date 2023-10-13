package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.tsys.tm.*;

/**
 * Разрешение типов
 */
public class TypeResolver {
    /**
     * Ссылка на тип который следует "решить"
     */
    public sealed interface TypeRefOwner {}

    /**
     * Тип аргумента метода класса/интерфейса
     * @param argType Тип аргумента который надо разрешить
     * @param arg Аргумент
     * @param fun Метод
     * @param classOrItfType Класс или интерфейс в котором определен метод
     * @param unit Модуль в котором определен тип
     */
    public record ArgOfMethod(Type.UnitTypeRef argType, Argument arg, Fun fun, Type classOrItfType, PascalUnit unit) implements TypeRefOwner {}

    /**
     * Тип результата метода класса/интерфейса
     * @param retType Тип результата метода
     * @param fun Метод
     * @param classOrItfType Класс или интерфейс в котором определен метод
     * @param unit Модуль в котором определен тип
     */
    public record ReturnsOfMethod(Type.UnitTypeRef retType, FunSetReturns fun, Type classOrItfType, PascalUnit unit) implements TypeRefOwner {}

    /**
     * Тип родительского объекта
     * @param parent Тип родителя класса/интерфейса
     * @param classOrItfType Класс или интерфейс в котором указан родитель
     * @param unit Модуль в котором определен тип
     */
    public record Parent(Type.UnitTypeRef parent, Type classOrItfType, PascalUnit unit) implements TypeRefOwner {}

    /**
     * Получение типов которые требуется разрешить
     * @param unit модуль
     * @return типы требующие разрешения
     */
    public static ImList<TypeRefOwner,?> typeRefs(PascalUnit unit) {
        if( unit==null ) throw new IllegalArgumentException("unit==null");
        //noinspection UnnecessaryLocalVariable
        ImList<TypeRefOwner,?> result = unit.getTypes().fmap( someType -> {
            if( someType instanceof ClassType ct ){
                return typeRefs(unit,ct);
            }else if( someType instanceof InterfaceType it ){
                return typeRefs(unit,it);
            }else{
                return ImListLinked.of();
            }
        });
        return result;
    }

    private static ImList<TypeRefOwner,?> typeRefs(PascalUnit unit, ClassType ct){
        ImList<TypeRefOwner,?> parents =
            ct.getParents().fmap(
                prntType -> prntType instanceof Type.UnitTypeRef ref
                    ? ImListLinked.of(new Parent(ref,ct,unit))
                    : ImListLinked.of() );

        ImList<TypeRefOwner,?> returns = ct.getBody().fmap( clsItm -> {
            if( clsItm instanceof FunSetReturns ret && ret.getReturns() instanceof Type.UnitTypeRef ref ){
                return ImListLinked.of(
                    new ReturnsOfMethod(ref,ret,ct,unit)
                );
            }else{
                return ImListLinked.of();
            }
        });

        ImList<TypeRefOwner,?> args = ct.getBody().fmap( clsItm -> {
            if( clsItm instanceof Fun f ){
                return ImListLinked.of( f );
            }else{
                return ImListLinked.of();
            }
        }).fmap( f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner,?> res = f.getArguments().fmap( arg -> {
                if(arg.getType() instanceof Type.UnitTypeRef ref){
                    return ImListLinked.of( new ArgOfMethod(ref,arg,f,ct,unit) );
                }else{
                    return ImListLinked.of();
                }
            });
            return res;
        });

        return parents.prepend(returns).prepend(args);
    }

    private static ImList<TypeRefOwner,?> typeRefs(PascalUnit unit, InterfaceType ct){
        ImList<TypeRefOwner,?> parents =
            ct.getParents().fmap(
                prntType -> prntType instanceof Type.UnitTypeRef ref
                    ? ImListLinked.of(new Parent(ref,ct,unit))
                    : ImListLinked.of() );

        ImList<TypeRefOwner,?> returns = ct.getBody().fmap( clsItm -> {
            if( clsItm instanceof FunSetReturns ret && ret.getReturns() instanceof Type.UnitTypeRef ref ){
                return ImListLinked.of(
                    new ReturnsOfMethod(ref,ret,ct,unit)
                );
            }else{
                return ImListLinked.of();
            }
        });

        ImList<TypeRefOwner,?> args = ct.getBody().fmap( clsItm -> {
            if( clsItm instanceof Fun f ){
                return ImListLinked.of( f );
            }else{
                return ImListLinked.of();
            }
        }).fmap( f -> {
            //noinspection UnnecessaryLocalVariable
            ImList<TypeRefOwner,?> res = f.getArguments().fmap( arg -> {
                if(arg.getType() instanceof Type.UnitTypeRef ref){
                    return ImListLinked.of( new ArgOfMethod(ref,arg,f,ct,unit) );
                }else{
                    return ImListLinked.of();
                }
            });
            return res;
        });

        return parents.prepend(returns).prepend(args);
    }


}
