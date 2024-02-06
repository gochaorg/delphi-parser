package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;

import java.util.Optional;

/**
 * Секция констант
 */
public sealed interface ConstSectionAst extends InterfaceDecl, AstNode {
    /**
     * Перечень констант
     * @param constants константы
     * @param key тип констант // TODO выяснить что за Resource string
     */
    record Constants(
        ImList<Const> constants,
        ConstKey key,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ConstSectionAst, ClassItemAst, AstNode, RecordItemAst, SrcPos, Commented<Constants>
    {
        @Override
        public Constants astUpdate(AstUpdate.UpdateContext ctx) {
            var cns = ctx.update(constants);
            var k = (ConstKey) key.astUpdate(ctx);
            var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
                cc.commenting(this) : this;
            if( cmts==this  && k==key && cns.isEmpty() )return this;
            return new Constants(cns.orElse(constants), k, position, cmts.comments);
        }

        @Override
        public Constants withComments(ImList<Comment> comments) {
            return new Constants(constants,key,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(constants).append(upcast(key));
        }

        static Constants of(DelphiParser.ConstSectionContext ctx){
            var key = ConstKey.Const;

            if( ctx.constKey()!=null
                && !ctx.constKey().isEmpty()
                && ctx.constKey().getText()!=null
                && ctx.constKey().getText().toLowerCase().startsWith("res") ){
                key = ConstKey.ResourceString;
            }

            return new Constants(
                ctx.constDeclaration()!=null && !ctx.constDeclaration().isEmpty() ?
                    ImList.of(ctx.constDeclaration()).map(Const::of) : ImList.of(),
                key,
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }

    enum ConstKey implements AstNode {
        Const,
        ResourceString
    }

    record Const(
        String name,
        Optional<TypeDeclAst> type,
        ConstExpression expression,
        ImList<CustomAttributeAst> attributes,
        ImList<Comment> comments,
        SourcePosition position
    ) implements AstNode, AstUpdate<Const>, Commented<Const>
    {
        @Override
        public Const astUpdate(AstUpdate.UpdateContext ctx) {
            if( ctx==null )throw new IllegalArgumentException("ctx==null");
            var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
                cc.commenting(this) : this;
            var type1 = ctx.updateUnsafe(type);
            var expr = expression.astUpdate(ctx);
            var attr = ctx.update(attributes);
            if( cmts==this && type1.isEmpty() && expr==expression && attr.isEmpty() )return this;
            return new Const(
                name,
                type1.orElse(type),
                expr,
                attr.orElse(attributes),
                cmts.comments,
                position
            );
        }

        @Override
        public Const withComments(ImList<Comment> comments) {
            if( comments==null )throw new IllegalArgumentException("comments==null");
            return new Const(
                name, type, expression, attributes, comments, position
            );
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(type).append(expression).append(upcast(attributes));
        }

        static Const of(DelphiParser.ConstDeclarationContext ctx){
            if( ctx==null )throw new IllegalArgumentException("ctx==null");
            return new Const(
                ctx.ident().getText(),
                ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty() ?
                    Optional.of(TypeDeclAst.of(ctx.typeDecl())) :
                    Optional.empty(),
                ConstExpression.of(ctx.constExpression()),
                ctx.customAttribute()!=null && !ctx.customAttribute().isEmpty() ?
                    ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) : ImList.of(),
                ImList.of(),
                SourcePosition.of(ctx)
            );
        }
    }
    sealed interface ConstExpression extends AstNode {
        @Override
        default ConstExpression astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        static ConstExpression of(DelphiParser.ConstExpressionContext ctx ) {
            if( ctx.recordConstExpression()!=null && !ctx.recordConstExpression().isEmpty() ){
                return new SeqNamed(
                    ImListLinked.of(ctx.recordConstExpression())
                    .map(NamedExp::of));
            }

            if( ctx.constExpression()!=null
            && !ctx.constExpression().isEmpty()
            ) {
                return new Sequence(
                    ImListLinked.of(ctx.constExpression()).map(ConstExpression::of)
                );
            }

            if( ctx.expression()!=null && !ctx.expression().isEmpty() ){
                return new ConstExp(ExpressionAst.of(ctx.expression()));
            }

            throw AstParseError.unExpected(ctx);
        }
    }

    record ConstExp( ExpressionAst expression ) implements ConstExpression {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }
    record NamedExp( String name, ConstExpression expression ) implements ConstExpression {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }

        public static NamedExp of(DelphiParser.RecordConstExpressionContext ctx){
            return new NamedExp(
                ctx.ident().getText(),
                ConstExpression.of(ctx.constExpression())
            );
        }
    }
    record Sequence( ImList<ConstExpression> seq ) implements ConstExpression {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(seq);
        }
    }
    record SeqNamed( ImList<NamedExp> seq ) implements ConstExpression {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(seq);
        }
    }
}
