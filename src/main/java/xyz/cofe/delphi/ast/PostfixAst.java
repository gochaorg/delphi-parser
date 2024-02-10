package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface PostfixAst {
    record ArrayIndex(
        AtomAst base,
        ImList<ExpressionAst> indexes,
        SourcePosition position
    ) implements PostfixAst, SrcPos {
        public static ArrayIndex build(DelphiParser.ArrayIndexAccessContext ctx, AtomAst base){
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");
            if( base==null ) throw new IllegalArgumentException("base==null");
            return new ArrayIndex(
                base,
                ImList.of(ctx.expression()).map(ExpressionAst::of),
                SourcePosition.of(ctx)
            );
        }
    }

    record Deref(AtomAst base, SourcePosition position)
    implements PostfixAst, SrcPos {
        public static Deref build(DelphiParser.DerefContext ctx, AtomAst base){
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");
            if( base==null ) throw new IllegalArgumentException("base==null");
            return new Deref(
                base,
                SourcePosition.of(ctx)
            );
        }
    }

    record Calling(ImList<Param> parameters) {
        public sealed interface Param {
            record NamedParam(String name, ExpressionAst expression) implements Param {}
            record PositionalParam(ExpressionAst expression) implements Param {}
        }
    }

}
