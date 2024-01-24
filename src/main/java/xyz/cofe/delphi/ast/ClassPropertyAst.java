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
            if (ctx.index != null && !ctx.index.isEmpty()
            ) {
                exp = Optional.of(ExpressionAst.of(ctx.index));
            }

            ImList<Specifier> spec = ImListLinked.of();
            if (ctx.classPropSpec() != null && !ctx.classPropSpec().isEmpty()) {
                spec = spec.append(ImList.of(ctx.classPropSpec()).map(Specifier::of));
            }
            if( ctx.classPropDispSpec()!=null && !ctx.classPropDispSpec().isEmpty()){
                spec = spec.append(ImList.of(ctx.classPropDispSpec()).map(Specifier::of));
            }
            if( ctx.classPropPostfixSpec()!=null && !ctx.classPropPostfixSpec().isEmpty()){
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
                ImListLinked.of()
            );
        }
    }

    sealed interface Specifier extends AstNode {
        @Override
        Specifier astUpdate(AstUpdate.UpdateContext ctx);

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

        static Specifier of(DelphiParser.ClassPropSpecContext ctx){
            if( ctx.READ()!=null && ctx.ident()!=null ){
                return new Read(ctx.ident().getText());
            }else if( ctx.WRITE()!=null && ctx.ident()!=null ){
                return new Write(ctx.ident().getText());
            }else{
                throw AstParseError.unExpected(ctx);
            }
        }

        static Specifier of(DelphiParser.ClassPropPostfixSpecContext ctx){
            if( ctx.DEFAULT()!=null ){
                if( ctx.expression()!=null ){
                    return new Default(
                        Optional.of(ExpressionAst.of(ctx.expression()))
                    );
                }
                return new Default(Optional.empty());
            }else if( ctx.NODEFAULT()!=null ){
                return new NoDefault();
            }else if( ctx.STORED()!=null && ctx.expression()!=null ){
                return new Stored(ExpressionAst.of(ctx.expression()));
            }else{
                throw AstParseError.unExpected(ctx);
            }
        }

        static Specifier of(DelphiParser.ClassPropDispSpecContext ctx){
            if( ctx.READONLY()!=null )return new ReadOnly();
            if( ctx.WRITEONLY()!=null )return new WriteOnly();
            if( ctx.DISPID()!=null && ctx.expression()!=null )return new DispID(ExpressionAst.of(ctx.expression()));
            throw AstParseError.unExpected(ctx);
        }
    }

    record Read(
        String name
    ) implements Specifier {
        @Override
        public Read astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    record Write(
        String name
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

    record Default(Optional<ExpressionAst> expression) implements Specifier {
        @Override
        public Default astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
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

    // TODO not used!
    record Implements(ImList<String> typeId) implements Specifier {
        @Override
        public Implements astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }
}