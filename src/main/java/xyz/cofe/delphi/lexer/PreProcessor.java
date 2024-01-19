package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.Token;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PreProcessor {
    private final LexAstParser parser = new LexAstParser();
    private final PreProcState state;

    public PreProcessor() {
        state = new PreProcState();
    }

    public PreProcessor(PreProcState initialState) {
        if( initialState==null )throw new IllegalArgumentException("initialState==null");
        state = initialState;
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

    private static class ProcessState {
        public ProcessState(PreProcState state){
            if( state==null )throw new IllegalArgumentException("state==null");
            this.state = state.clone();
            this.excludeSet = new HashSet<>();
        }

        public final PreProcState state;
        public final Set<Token> excludeSet;

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
            throw new RuntimeException("not impl eval LexAst.Condition.If");
        }

        private boolean eval(LexAst.Condition.IfOpt ifOpt){
            throw new RuntimeException("not impl eval LexAst.Condition.IfOpt");
        }

        private boolean eval(LexAst.Condition.IfDef ifDef){
            return state.isDefined(ifDef.name());
        }

        private boolean eval(LexAst.Condition.IfNotDef ifNotDef){
            return !state.isDefined(ifNotDef.name());
        }
    }
}
