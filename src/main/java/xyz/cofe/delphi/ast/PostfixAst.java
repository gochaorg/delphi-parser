package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface PostfixAst extends AtomAst {
    record ArrayIndex(
        AtomAst base,
        ImList<ExpressionAst> indexes,
        SourcePosition position
    ) implements PostfixAst,
                 SrcPos {
        public static ArrayIndex build(DelphiParser.ArrayIndexAccessContext ctx, AtomAst base) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (base == null) throw new IllegalArgumentException("base==null");
            return new ArrayIndex(
                base,
                ImList.of(ctx.expression()).map(ExpressionAst::of),
                SourcePosition.of(ctx)
            );
        }

        @Override
        public String text() {
            return "???";
        }
    }

    record Deref(AtomAst base, SourcePosition position)
        implements PostfixAst,
                   SrcPos {
        public static Deref build(DelphiParser.DerefContext ctx, AtomAst base) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (base == null) throw new IllegalArgumentException("base==null");
            return new Deref(
                base,
                SourcePosition.of(ctx)
            );
        }

        @Override
        public String text() {
            return "???";
        }
    }

    record Calling(AtomAst base, ImList<Param> parameters, SourcePosition position)
        implements PostfixAst,
                   SrcPos {
        public sealed interface Param extends SrcPos {
            record NamedParam(String name, ExpressionAst expression, SourcePosition position) implements Param {
            }

            record PositionalParam(ExpressionAst expression, SourcePosition position) implements Param {
            }

            public static Param of(DelphiParser.CallParamContext ctx) {
                if (ctx == null) throw new IllegalArgumentException("ctx==null");
                if (ctx.namedPassParam() != null && !ctx.namedPassParam().isEmpty()) {
                    return new NamedParam(
                        ctx.namedPassParam().paramName().getText(),
                        ExpressionAst.of(ctx.namedPassParam().expression()),
                        SourcePosition.of(ctx)
                    );
                }
                if (ctx.unnamedPassParam() != null && !ctx.unnamedPassParam().isEmpty()) {
                    return new PositionalParam(
                        ExpressionAst.of(ctx.unnamedPassParam().expression()),
                        SourcePosition.of(ctx)
                    );
                }
                throw AstParseError.unExpected(ctx);
            }
        }

        public static Calling build(DelphiParser.CallingContext ctx, AtomAst base) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (base == null) throw new IllegalArgumentException("base==null");
            return new Calling(
                base,
                ImList.of(ctx.callParam()).map(Param::of),
                SourcePosition.of(ctx)
            );
        }

        @Override
        public String text() {
            return "???";
        }
    }

    record FieldAccess(AtomAst base, String fieldName, SourcePosition position)
        implements PostfixAst,
                   SrcPos {
        public static FieldAccess build(DelphiParser.FieldAccessContext ctx, AtomAst base) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (base == null) throw new IllegalArgumentException("base==null");
            return new FieldAccess(
                base,
                ctx.fieldName.getText(),
                SourcePosition.of(ctx)
            );
        }

        @Override
        public String text() {
            return "???";
        }
    }

    record GenericCallParams(AtomAst base, ImList<TypeIdentAst> genericParameters, SourcePosition position)
        implements PostfixAst,
                   SrcPos {
        public static GenericCallParams build(DelphiParser.GenericCallTypeParamsContext ctx, AtomAst base) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (base == null) throw new IllegalArgumentException("base==null");
            return new GenericCallParams(
                base,
                ImList.of(ctx.genericTypeIdent()).map(TypeIdentAst::of),
                SourcePosition.of(ctx)
            );
        }

        @Override
        public String text() {
            return "???";
        }
    }

    public static PostfixAst build(DelphiParser.PostfixContext ctx, AtomAst base) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (base == null) throw new IllegalArgumentException("base==null");

        if (ctx.arrayIndexAccess() != null && !ctx.arrayIndexAccess().isEmpty())
            return ArrayIndex.build(ctx.arrayIndexAccess(), base);

        if (ctx.deref() != null && !ctx.deref().isEmpty())
            return Deref.build(ctx.deref(), base);

        if (ctx.calling() != null && !ctx.calling().isEmpty())
            return Calling.build(ctx.calling(), base);

        if (ctx.fieldAccess() != null && !ctx.fieldAccess().isEmpty())
            return FieldAccess.build(ctx.fieldAccess(), base);

        if (ctx.genericCallTypeParams() != null && !ctx.genericCallTypeParams().isEmpty())
            return GenericCallParams.build(ctx.genericCallTypeParams(), base);

        throw AstParseError.unExpected(ctx);
    }
}
