package xyz.cofe.delphi.lexer;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Result;

import java.util.function.Predicate;

/**
 * Вычисление условной компиляции
 */
public class ComputeDefault implements EvalCondition.Compute {
    private final PreProcState preProcState;
    private final Predicate<String> declared;

    public ComputeDefault(PreProcState preProcState) {
        if (preProcState == null) throw new IllegalArgumentException("preProcState==null");
        this.preProcState = preProcState;
        this.declared = str -> false;
    }

    public ComputeDefault(PreProcState preProcState, Predicate<String> declared) {
        if (preProcState == null) throw new IllegalArgumentException("preProcState==null");
        if (declared == null) throw new IllegalArgumentException("declared==null");
        this.preProcState = preProcState;
        this.declared = declared;
    }

    private Result<Double,EvalError> toNum(EvalCondition.Value value){
        if( value instanceof EvalCondition.Value.Numeric num ){
            return Result.ok(num.value());
        }else if( value instanceof EvalCondition.Value.Variable ident){
            var num = preProcState.get(ident.name());
            if( num.isEmpty() ){
                return Result.error(new EvalError("variable "+ident.name()+" not defined"));
            }
            return Result.ok(num.get());
        }else{
            return Result.error(new EvalError("can't cast "+value.getClass().getSimpleName()+" as number"));
        }
    }

    private Result<Boolean,EvalError> toBool(EvalCondition.Value value){
        if( value instanceof EvalCondition.Value.Bool bo){
            return Result.ok(bo.value());
        }else if( value instanceof EvalCondition.Value.Variable ident){
            var num = preProcState.isDefined(ident.name());
            return Result.ok(num);
        }else{
            return Result.error(new EvalError("can't cast "+value.getClass().getSimpleName()+" as boolean"));
        }
    }

    @Override
    public Result<EvalCondition.Value, EvalError> or(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toBool(left).fmap( leftBool -> toBool(right).map( rightBool -> new EvalCondition.Value.Bool(leftBool || rightBool)));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> and(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toBool(left).fmap( leftBool -> toBool(right).map( rightBool -> new EvalCondition.Value.Bool(leftBool && rightBool)));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> lessOrEquals(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNum(left).fmap( leftNum -> toNum(right).map( rightNum -> new EvalCondition.Value.Bool(leftNum.compareTo(rightNum)<=0 ) ));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> moreOrEquals(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNum(left).fmap( leftNum -> toNum(right).map( rightNum -> new EvalCondition.Value.Bool(leftNum.compareTo(rightNum)>=0 ) ));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> less(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNum(left).fmap( leftNum -> toNum(right).map( rightNum -> new EvalCondition.Value.Bool(leftNum.compareTo(rightNum)<0 ) ));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> more(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNum(left).fmap( leftNum -> toNum(right).map( rightNum -> new EvalCondition.Value.Bool(leftNum.compareTo(rightNum)>0 ) ));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> equals(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNum(left).fmap( leftNum -> toNum(right).map( rightNum -> new EvalCondition.Value.Bool(leftNum.compareTo(rightNum)==0 ) ));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> notEquals(EvalCondition.Value left, EvalCondition.Value right) {
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNum(left).fmap( leftNum -> toNum(right).map( rightNum -> new EvalCondition.Value.Bool(leftNum.compareTo(rightNum)!=0 ) ));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> invert(EvalCondition.Value value) {
        if( value==null )throw new IllegalArgumentException("value==null");
        if( !(value instanceof EvalCondition.Value.Bool bo) ){
            return Result.error(new EvalError("expect boolean at invert(not)"));
        }

        return Result.ok(new EvalCondition.Value.Bool(!bo.value()));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> plus(EvalCondition.Value value) {
        if( value==null )throw new IllegalArgumentException("value==null");
        if( !(value instanceof EvalCondition.Value.Numeric num) ){
            return Result.error(new EvalError("expect numeric at unary plus"));
        }

        return Result.ok(new EvalCondition.Value.Numeric(num.value()));
    }

    @Override
    public Result<EvalCondition.Value, EvalError> minus(EvalCondition.Value value) {
        if( value==null )throw new IllegalArgumentException("value==null");
        if( !(value instanceof EvalCondition.Value.Numeric num) ){
            return Result.error(new EvalError("expect numeric at unary minus"));
        }

        return Result.ok(new EvalCondition.Value.Numeric(- num.value()));
    }

    // defined
    // declared
    @Override
    public Result<EvalCondition.Value, EvalError> call(EvalCondition.Value base, ImList<EvalCondition.Value> args) {
        if( base==null )throw new IllegalArgumentException("base==null");
        if( args==null )throw new IllegalArgumentException("args==null");

        if (!(base instanceof EvalCondition.Value.Variable baseVar)) {
            return Result.error(new EvalError("can't call " + base.getClass().getSimpleName()));
        }

        if (baseVar.name().equalsIgnoreCase("defined")) {
            if (args.size() != 1) {
                return Result.error(new EvalError("can't call defined with args: " + args + ", expect one arg"));
            }

            //noinspection OptionalGetWithoutIsPresent
            var arg = args.get(0).get();

            if (arg instanceof EvalCondition.Value.Variable ident) {
                var r = preProcState.isDefined(ident.name());
                return Result.ok(new EvalCondition.Value.Bool(r));
            } else {
                return Result.error(new EvalError("expect ident (EvalCondition.Value.Variable) at arg"));
            }
        }

        if (baseVar.name().equalsIgnoreCase("declared")) {
            if (args.size() != 1) {
                return Result.error(new EvalError("can't call defined with args: " + args + ", expect one arg"));
            }

            //noinspection OptionalGetWithoutIsPresent
            var arg = args.get(0).get();

            if (arg instanceof EvalCondition.Value.Variable ident) {
                var r = declared.test(ident.name());
                return Result.ok(new EvalCondition.Value.Bool(r));
            } else {
                return Result.error(new EvalError("expect ident (EvalCondition.Value.Variable) at arg"));
            }
        }

        return Result.error(new EvalError("can't call undefined function: "+baseVar.name()));
    }
}
