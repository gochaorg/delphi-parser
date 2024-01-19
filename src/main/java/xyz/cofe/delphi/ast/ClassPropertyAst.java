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
        ImList<Comment> comments
    ) implements ClassPropertyAst, SrcPos, Commented<ClassPropertyAst> {
        @Override
        public Property astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ClassPropertyAst withComments(ImList<Comment> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(propertyArray).append(upcast(type)).append(upcast(index)).append(upcast(specifiers));
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
                ImListLinked.of()
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
            if (ctx.classPropertyIndex() != null
                && !ctx.classPropertyIndex().isEmpty()
                && ctx.classPropertyIndex().expression() != null
                && !ctx.classPropertyIndex().expression().isEmpty()
            ) {
                exp = Optional.of(ExpressionAst.of(ctx.classPropertyIndex().expression()));
            }

            ImList<Specifier> spec = ImListLinked.of();
            if (ctx.classPropertySpecifier() != null) {
                spec = ImListLinked.of(ctx.classPropertySpecifier())
                    .map(Specifier::of);
            }

            return new ClassPropertyAst.Property(
                name,
                propArr,
                type,
                exp,
                spec,
                classFlag,
                SourcePosition.of(ctx),
                ImListLinked.of()
            );
        }
    }

    sealed interface Specifier extends AstNode {
        @Override
        Specifier astUpdate(AstUpdate.UpdateContext ctx);

        static Specifier of(DelphiParser.ClassPropertyEndSpecifierContext ctx) {
            if (ctx.STORED() != null
                && ctx.STORED().getText() != null
                && ctx.STORED().getText().length() > 0
                && ctx.expression() != null
                && !ctx.expression().isEmpty()
            ) {
                return new Stored(ExpressionAst.of(ctx.expression()));
            }

            if (ctx.DEFAULT() != null
                && ctx.DEFAULT().getText() != null
                && ctx.DEFAULT().getText().length() > 0
            ) {
                if (ctx.expression() != null && !ctx.expression().isEmpty()) {
                    return new DefaultExp(ExpressionAst.of(ctx.expression()));
                }

                return new Default();
            }

            if (ctx.NODEFAULT() != null
                && ctx.NODEFAULT().getText() != null
                && ctx.NODEFAULT().getText().length() > 0) {
                return new NoDefault();
            }

            throw AstParseError.unExpected(ctx);
        }

        static Specifier of(DelphiParser.ClassPropertySpecifierContext ctx) {
            if (ctx.classPropertyReadWrite() != null && !ctx.classPropertyReadWrite().isEmpty())
                return of(ctx.classPropertyReadWrite());

            if (ctx.classPropertyDispInterface() != null && !ctx.classPropertyDispInterface().isEmpty())
                return of(ctx.classPropertyDispInterface());

            if (ctx.STORED() != null
                && ctx.STORED().getText() != null
                && ctx.STORED().getText().length() > 0
                && ctx.expression() != null
            ) {
                return new Stored(ExpressionAst.of(ctx.expression()));
            }

            if (ctx.DEFAULT() != null
                && ctx.DEFAULT().getText() != null
                && ctx.DEFAULT().getText().length() > 0
            ) {
                if (ctx.expression() != null && !ctx.expression().isEmpty()) {
                    return new DefaultExp(ExpressionAst.of(ctx.expression()));
                }

                return new Default();
            }

            if (ctx.NODEFAULT() != null
                && ctx.NODEFAULT().getText() != null
                && ctx.NODEFAULT().getText().length() > 0
            ) {
                return new NoDefault();
            }

            if (ctx.IMPLEMENTS() != null
                && ctx.IMPLEMENTS().getText() != null
                && ctx.IMPLEMENTS().getText().length() > 0
                && ctx.typeId() != null
                && !ctx.typeId().isEmpty()
            ) {
                var nq = ctx.typeId().namespacedQualifiedIdent();

                ImListLinked<String> namespace = ImListLinked.of();
                if (!ctx.typeId().namespacedQualifiedIdent().isEmpty()
                    && nq.namespaceName() != null
                ) {
                    namespace =
                        ImListLinked.of(
                            nq.namespaceName().ident().stream().map(
                                RuleContext::getText
                            ).toList());
                }

                ImListLinked<String> ident = ImListLinked.of();
                if (!ctx.typeId().namespacedQualifiedIdent().qualifiedIdent().isEmpty()) {
                    ident = ImListLinked.of(
                        ctx.typeId().namespacedQualifiedIdent().qualifiedIdent().ident()
                            .stream().map(RuleContext::getText).toList());
                }

                return new Implements(
                    namespace.prepend(ident)
                );
            }

            throw AstParseError.notImplemented(ctx);
        }

        static Specifier of(DelphiParser.ClassPropertyReadWriteContext ctx) {
            var name = ImListLinked.of(
                ctx.qualifiedIdent().ident().stream().map(RuleContext::getText).toList()
            );

            Optional<ExpressionAst> exp =
                ctx.expression() != null && !ctx.expression().isEmpty() ?
                    Optional.of(ExpressionAst.of(ctx.expression())) :
                    Optional.empty();

            if (ctx.READ() != null && ctx.READ().getText().toLowerCase().startsWith("read")) {
                return new Read(name, exp);
            } else if (ctx.WRITE() != null && ctx.WRITE().getText().toLowerCase().startsWith("write")) {
                return new Write(name, exp);
            }

            throw AstParseError.unExpected();
        }

        static Specifier of(DelphiParser.ClassPropertyDispInterfaceContext ctx) {
            if (ctx.getText().toLowerCase().startsWith("readonl")) {
                return new ReadOnly();
            } else if (ctx.getText().toLowerCase().startsWith("writeonly")) {
                return new WriteOnly();
            } else if (
                ctx.dispIDDirective() != null &&
                    ctx.dispIDDirective().expression() != null &&
                    ctx.getText().toLowerCase().startsWith("dispid")
            ) {
                return new DispID(ExpressionAst.of(ctx.dispIDDirective().expression()));
            }

            throw AstParseError.notImplemented();
        }
    }

    record Read(
        ImList<String> name,
        Optional<ExpressionAst> expression
    ) implements Specifier {
        @Override
        public Read astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record Write(
        ImList<String> name,
        Optional<ExpressionAst> expression
    ) implements Specifier {
        @Override
        public Write astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
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
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record Stored(ExpressionAst expression) implements Specifier {
        @Override
        public Stored astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record DefaultExp(ExpressionAst expression) implements Specifier {
        @Override
        public DefaultExp astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record Default() implements Specifier {
        @Override
        public Default astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
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