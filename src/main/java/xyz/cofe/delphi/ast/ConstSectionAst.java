package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;

import java.util.Optional;

/**
 * Секция констант
 */
public sealed interface ConstSectionAst extends InterfaceDecl {
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
    ) implements ConstSectionAst, ClassItemAst, RecordItemAst, SrcPos, Commented<Constants>
    {
        @Override
        public Constants withComments(ImList<Comment> comments) {
            return new Constants(constants,key,position,comments);
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
    ) implements Commented<Const>
    {
        @Override
        public Const withComments(ImList<Comment> comments) {
            if( comments==null )throw new IllegalArgumentException("comments==null");
            return new Const(
                name, type, expression, attributes, comments, position
            );
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
    sealed interface ConstExpression {
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
    }
    record NamedExp( String name, ConstExpression expression ) implements ConstExpression {
        public static NamedExp of(DelphiParser.RecordConstExpressionContext ctx){
            return new NamedExp(
                ctx.ident().getText(),
                ConstExpression.of(ctx.constExpression())
            );
        }
    }
    record Sequence( ImList<ConstExpression> seq ) implements ConstExpression {
    }
    record SeqNamed( ImList<NamedExp> seq ) implements ConstExpression {
    }
}
