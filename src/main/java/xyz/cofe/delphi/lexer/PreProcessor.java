package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.Token;
import xyz.cofe.coll.im.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PreProcessor {
    private final LexAstParser parser = new LexAstParser();
    private final PreProcState state;
    private final Function<PreProcState,EvalCondition> evalConditionBuilder;

    public PreProcessor() {
        this(new PreProcState());
    }

    public PreProcessor(PreProcState initialState) {
        this(new PreProcState(), state->new EvalCondition(new ComputeDefault(state)));
    }

    public PreProcessor(PreProcState initialState, Function<PreProcState,EvalCondition> evalConditionBuilder) {
        if( initialState==null )throw new IllegalArgumentException("initialState==null");
        if( evalConditionBuilder==null )throw new IllegalArgumentException("evalConditionBuilder==null");
        state = initialState;
        this.evalConditionBuilder = evalConditionBuilder;
    }

    public TokenizedFile process(TokenizedFile file) {
        if (file == null) throw new IllegalArgumentException("file==null");
        var lexStruct = parser.parse(file);

        var state = new ProcessState(this.state);
        lexStruct.each(state::eval);

        return new TokenizedFile(
            file.source(),
            file.tokens().filter(t -> !state.excludeSet.contains(t))
        );
    }

    private class ProcessState {
        public ProcessState(PreProcState state){
            if( state==null )throw new IllegalArgumentException("state==null");
            this.state = state.clone();
            this.excludeSet = new HashSet<>();
            this.evalCondition = evalConditionBuilder.apply(this.state);
        }

        public final PreProcState state;
        public final Set<Token> excludeSet;
        public final EvalCondition evalCondition;

        public void eval( LexAst lexAst ){
            if( lexAst==null )throw new IllegalArgumentException("lexAst==null");

            if( lexAst instanceof LexAst.Define def ){
                state.define(def.name());
            }else if( lexAst instanceof LexAst.UnDef unDef ){
                state.undef(unDef.name());
            }else if( lexAst instanceof LexAst.ConditionalCompile cc ){
                Consumer<Boolean> then = evalResult -> {
                    if( evalResult ){
                        cc.negative().nestedTokens().forEach(excludeSet::add);
                        eval(cc.positive());
                    }else{
                        cc.positive().nestedTokens().forEach(excludeSet::add);
                        eval(cc.negative());
                    }
                };
                if( cc.condition() instanceof LexAst.Condition.If _if ){
                    then.accept(eval(_if));
                }else if( cc.condition() instanceof LexAst.Condition.IfOpt ifOpt ){
                    then.accept(eval(ifOpt));
                }else if( cc.condition() instanceof LexAst.Condition.IfDef ifDef ){
                    then.accept(eval(ifDef));
                }else if( cc.condition() instanceof LexAst.Condition.IfNotDef ifNotDef ){
                    then.accept(eval(ifNotDef));
                }
            }else if( lexAst instanceof LexAst.Lexs lexs){
                lexs.nodes().forEach(this::eval);
            }
        }

        private boolean eval(LexAst.Condition.If _if){
            var expr = EvalCondition.parse(_if.code(), _if.code());
            if( expr.getError().isPresent() ){
                var err = expr.getError().get();
                throw new LexError(err);
            }

            //noinspection OptionalGetWithoutIsPresent
            var res = evalCondition.eval(expr.getOk().get());
            if( res.getError().isPresent() ){
                throw new LexError(res.getError().get());
            }

            //noinspection OptionalGetWithoutIsPresent
            var r = res.getOk().get();
            if( r instanceof EvalCondition.Value.Bool bo ){
                return bo.value();
            }else{
                throw new LexError("can't cast "+r.getClass().getSimpleName()+" to boolean");
            }
        }

        private boolean eval(LexAst.Condition.IfOpt ifOpt){
            //TODO impl
            System.err.println("not implemented if opt "+ifOpt.name());
            return false;
        }

        private boolean eval(LexAst.Condition.IfDef ifDef){
            return state.isDefined(ifDef.name());
        }

        private boolean eval(LexAst.Condition.IfNotDef ifNotDef){
            return !state.isDefined(ifNotDef.name());
        }
    }
}
