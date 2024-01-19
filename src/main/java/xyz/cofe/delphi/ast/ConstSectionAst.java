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
    record Constants(ImList<Const> constants, ConstKey key) implements ConstSectionAst, ClassItemAst, AstNode {
        @Override
        public Constants astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
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

            if(ctx.constDeclaration()!=null && !ctx.constDeclaration().isEmpty())
                return new Constants(
                    ImListLinked.of(ctx.constDeclaration())
                        .map(Const::of),
                    key
                );

            return new Constants(ImListLinked.of(), key);
        }
    }

    enum ConstKey implements AstNode {
        Const,
        ResourceString
    }

    record Const(String name, Optional<TypeDeclAst> type, ConstExpression expression ) implements AstNode // TODO comment!
    {
        @Override
        public Const astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(type).append(expression);
        }

        static Const of(DelphiParser.ConstDeclarationContext ctx){
            return new Const(
                ctx.ident().getText(),
                ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty() ?
                    Optional.of(TypeDeclAst.of(ctx.typeDecl())) :
                    Optional.empty(),
                ConstExpression.of(ctx.constExpression())
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
