package xyz.cofe.delphi.lexer;

import java.util.function.Consumer;

public class LexAstVisitor {
    public static void visit(LexAst ast, Consumer<LexAst> enter, Consumer<LexAst> exit){
        if( ast==null )throw new IllegalArgumentException("ast==null");
        if( enter==null )throw new IllegalArgumentException("enter==null");
        if( exit==null )throw new IllegalArgumentException("exit==null");
        if( ast instanceof LexAst.Lex lex ){
            enter.accept(lex);
            exit.accept(lex);
        }else if( ast instanceof LexAst.Lexs lexs ){
            enter.accept(lexs);
            for( var la : lexs.nodes() ){
                visit(la,enter,exit);
            }
            exit.accept(lexs);
        }else if( ast instanceof LexAst.Define def ){
            enter.accept(def);
            exit.accept(def);
        }else if( ast instanceof LexAst.UnDef udef ){
            enter.accept(udef);
            exit.accept(udef);
        }else if( ast instanceof LexAst.ConditionalCompile cc ){
            enter.accept(cc);
            visit(cc.positive(),enter,exit);
            visit(cc.negative(),enter,exit);
            exit.accept(cc);
        }
    }
}
