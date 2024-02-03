package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

import static xyz.cofe.delphi.ast.AstNode.upcast;


/**
 * Свойство класса/интерфейса
 */
public sealed interface ClassPropertyAst extends InterfaceItemAst, ClassItemAst, AstNode {
    @Override
    ClassPropertyAst astUpdate(AstUpdate.UpdateContext ctx);

    record Property(
        String name,
        ImList<ArgumentAst> propertyArray,
        Optional<TypeIdentAst> type,
        Optional<ExpressionAst> index,
        ImList<Specifier> specifiers,
        boolean classFlag,
        SourcePosition position,
        ImList<Comment> comments,
        ImList<CustomAttributeAst> attributes
    ) implements ClassPropertyAst, SrcPos, Commented<Property> {
        @Override
        public Property astUpdate(AstUpdate.UpdateContext ctx) {
            var cmts = this;

            if( ctx instanceof AstUpdate.CommentingContext cc )
                cmts = cc.commenting(cmts);

            var propArr = ctx.update(propertyArray);

            var typeChanged = new boolean[]{ false };
            var type = this.type.map( t -> {
                TypeIdentAst r = (TypeIdentAst) t.astUpdate(ctx);
                //noinspection ConstantValue
                typeChanged[0] = r!=t;
                return r;
            });

            var idxChanged = new boolean[]{ false };
            var idx = this.index.map( t -> {
                var r = t.astUpdate(ctx);
                idxChanged[0] = r!=t;
                return r;
            });

            Optional<ImList<Specifier>> spec = (Optional)ctx.updateUnsafe(specifiers);

            var attr = ctx.update(attributes);

            return new Property(
                name,
                propArr.orElse(propertyArray),
                type,
                idx,
                spec.orElse(specifiers),
                classFlag,
                position,
                comments,
                attr.orElse(attributes)
            );
        }

        @Override
        public Property withComments(ImList<Comment> comments) {
            return new Property(
                name,
                propertyArray,
                type,
                index,
                specifiers,
                classFlag,
                position,
                comments,
                attributes
            );
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(propertyArray)
                .append(upcast(type))
                .append(upcast(index))
                .append(upcast(specifiers))
                .append(upcast(attributes));
        }

        public Property withClassFlag(boolean value) {
            return new Property(
                name,
                propertyArray,
                type,
                index,
                specifiers,
                value,
                position,
                comments,
                attributes
            );
        }

        static ClassPropertyAst.Property of(DelphiParser.ClassPropertyContext ctx) {
            boolean classFlag =
                ctx.CLASS() != null
                    && ctx.CLASS().getText() != null
                    && ctx.CLASS().getText().length() > 0;

            String name = ctx.classPropertyName().getText();

            ImList<ArgumentAst> propArr = ImListLinked.of();
            if (ctx.classPropertyArray() != null
                && !ctx.classPropertyArray().isEmpty()
                && ctx.classPropertyArray().formalParameterList() != null
                && !ctx.classPropertyArray().formalParameterList().isEmpty()
            ) {
                propArr = ArgumentAst.of(ctx.classPropertyArray().formalParameterList());
            }

            Optional<TypeIdentAst> type = Optional.empty();
            if (ctx.genericTypeIdent() != null
                && !ctx.genericTypeIdent().isEmpty()
            ) {
                type = Optional.of(
                    TypeIdentAst.of(ctx.genericTypeIdent())
                );
            }

            Optional<ExpressionAst> exp = Optional.empty();
            if (ctx.index != null && !ctx.index.isEmpty()
            ) {
                exp = Optional.of(ExpressionAst.of(ctx.index));
            }

            ImList<Specifier> spec = ImListLinked.of();
            if (ctx.classPropSpec() != null && !ctx.classPropSpec().isEmpty()) {
                spec = spec.append(ImList.of(ctx.classPropSpec()).map(Specifier::of));
            }
            if (ctx.classPropDispSpec() != null && !ctx.classPropDispSpec().isEmpty()) {
                spec = spec.append(ImList.of(ctx.classPropDispSpec()).map(Specifier::of));
            }
            if (ctx.classPropPostfixSpec() != null && !ctx.classPropPostfixSpec().isEmpty()) {
                spec = spec.append(ImList.of(ctx.classPropPostfixSpec()).map(Specifier::of));
            }

            return new ClassPropertyAst.Property(
                name,
                propArr,
                type,
                exp,
                spec,
                classFlag,
                SourcePosition.of(ctx),
                ImList.of(),
                ctx.customAttribute()!=null && !ctx.customAttribute().isEmpty() ?
                ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) : ImList.of()
            );
        }
    }

    sealed interface Specifier extends AstNode, AstUpdate {
        @Override
        Specifier astUpdate(AstUpdate.UpdateContext ctx);

        static Specifier of(DelphiParser.ClassPropSpecContext ctx) {
            if (ctx.READ() != null && ctx.ident() != null && !ctx.ident().isEmpty()) {
                return new Read(ImList.of(ctx.ident()).map(RuleContext::getText));
            } else if (ctx.WRITE() != null && ctx.ident() != null && !ctx.ident().isEmpty()) {
                return new Write(ImList.of(ctx.ident()).map(RuleContext::getText));
            } else if( ctx.IMPLEMENTS()!=null && ctx.ident()!=null && !ctx.ident().isEmpty() ){
                return new Implements(ImList.of(ctx.ident()).map(RuleContext::getText));
            } else {
                throw AstParseError.unExpected(ctx);
            }
        }

        static Specifier of(DelphiParser.ClassPropPostfixSpecContext ctx) {
            if (ctx.DEFAULT() != null) {
                if (ctx.expression() != null) {
                    return new Default(
                        Optional.of(ExpressionAst.of(ctx.expression()))
                    );
                }
                return new Default(Optional.empty());
            } else if (ctx.NODEFAULT() != null) {
                return new NoDefault();
            } else if (ctx.STORED() != null && ctx.expression() != null) {
                return new Stored(ExpressionAst.of(ctx.expression()));
            } else {
                throw AstParseError.unExpected(ctx);
            }
        }

        static Specifier of(DelphiParser.ClassPropDispSpecContext ctx) {
            if (ctx.READONLY() != null) return new ReadOnly();
            if (ctx.WRITEONLY() != null) return new WriteOnly();
            if (ctx.DISPID() != null && ctx.expression() != null) return new DispID(ExpressionAst.of(ctx.expression()));
            throw AstParseError.unExpected(ctx);
        }
    }

    record Read(
        ImList<String> name
    ) implements Specifier {
        @Override
        public Read astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    record Write(
        ImList<String> name
    ) implements Specifier {
        @Override
        public Write astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    record ReadOnly() implements Specifier {
        @Override
        public ReadOnly astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    record WriteOnly() implements Specifier {
        @Override
        public WriteOnly astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    record DispID(ExpressionAst expression) implements Specifier {
        @Override
        public DispID astUpdate(AstUpdate.UpdateContext ctx) {
            var exp = expression.astUpdate(ctx);
            if( exp==expression )return this;
            return new DispID(expression);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record Stored(ExpressionAst expression) implements Specifier {
        @Override
        public Stored astUpdate(AstUpdate.UpdateContext ctx) {
            var exp = expression.astUpdate(ctx);
            if( exp==expression )return this;
            return new Stored(exp);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record Default(Optional<ExpressionAst> expression) implements Specifier {
        @Override
        public Default astUpdate(AstUpdate.UpdateContext ctx) {
            var exp = ctx.updateUnsafe(expression);
            if( exp.isEmpty() )return this;
            return new Default(exp.orElse(expression));
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record NoDefault() implements Specifier {
        @Override
        public NoDefault astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    record Implements(ImList<String> typeId) implements Specifier {
        @Override
        public Implements astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }
}