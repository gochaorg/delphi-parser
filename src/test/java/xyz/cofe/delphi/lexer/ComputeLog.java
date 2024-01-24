package xyz.cofe.delphi.lexer;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Result;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ComputeLog implements EvalCondition.Compute {
    private final EvalCondition.Compute target;
    private final Consumer<String> log;

    public ComputeLog(EvalCondition.Compute target, Consumer<String> log) {
        if( target==null )throw new IllegalArgumentException("target==null");
        if( log==null )throw new IllegalArgumentException("log==null");
        this.target = target;
        this.log = log;
    }

    @Override
    public Result<EvalCondition.Value, EvalError> or(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("or: left="+left+" right="+right);
        return target.or(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> and(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("and: left="+left+" right="+right);
        return target.and(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> lessOrEquals(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("lessOrEquals: left="+left+" right="+right);
        return target.lessOrEquals(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> moreOrEquals(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("moreOrEquals: left="+left+" right="+right);
        return target.moreOrEquals(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> less(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("less: left="+left+" right="+right);
        return target.less(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> more(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("more: left="+left+" right="+right);
        return target.more(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> equals(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("equals: left="+left+" right="+right);
        return target.equals(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> notEquals(EvalCondition.Value left, EvalCondition.Value right) {
        log.accept("notEquals: left="+left+" right="+right);
        return target.notEquals(left, right);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> invert(EvalCondition.Value value) {
        log.accept("invert: value="+value);
        return target.invert(value);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> plus(EvalCondition.Value value) {
        log.accept("plus: value="+value);
        return target.plus(value);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> minus(EvalCondition.Value value) {
        log.accept("minus: value="+value);
        return target.minus(value);
    }

    @Override
    public Result<EvalCondition.Value, EvalError> call(EvalCondition.Value base, ImList<EvalCondition.Value> args) {
        log.accept("call: base="+base+" args="+args);
        return target.call(base, args);
    }
}
