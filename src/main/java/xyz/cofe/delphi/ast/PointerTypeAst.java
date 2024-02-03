package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface PointerTypeAst extends TypeDeclAst
{
    static PointerTypeAst of(DelphiParser.PointerTypeContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        if( ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty() ){
            return new Pointer(TypeDeclAst.of(ctx.typeDecl()), SourcePosition.of(ctx), ImList.of());
        }
        if( ctx.POINTER()!=null || ctx.POINTER2()!=null ){
            return new UntypedPointer(SourcePosition.of(ctx),ImList.of());
        }
        throw AstParseError.unExpected(ctx);
    }

    record UntypedPointer(SourcePosition position, ImList<Comment> comments)
        implements PointerTypeAst, AstUpdate<UntypedPointer>, Commented<UntypedPointer>
    {
        @Override
        public UntypedPointer astUpdate(AstUpdate.UpdateContext ctx) {
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");
            return ctx instanceof AstUpdate.CommentingContext cc ?
                cc.commenting(this) : this;
        }

        @Override
        public UntypedPointer withComments(ImList<Comment> comments) {
            if( comments==null ) throw new IllegalArgumentException("comments==null");
            return new UntypedPointer(position,comments);
        }
    }

    record Pointer(TypeDeclAst type, SourcePosition position, ImList<Comment> comments)
        implements PointerTypeAst, AstUpdate<Pointer>, Commented<Pointer>
    {
        @Override
        public Pointer astUpdate(AstUpdate.UpdateContext ctx) {
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");
            var t = type.astUpdate(ctx);
            var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
                cc.commenting(this) : this;
            if( t==type && cmts==this )return this;
            return new Pointer(
                type,
                position,
                cmts.comments
            );
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return ImList.of(type);
        }

        @Override
        public Pointer withComments(ImList<Comment> comments) {
            if( comments==null ) throw new IllegalArgumentException("comments==null");
            return new Pointer(type,position,comments);
        }
    }
}
