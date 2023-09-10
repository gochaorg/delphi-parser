package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public sealed interface ClassProperty extends InterfaceItem, ClassItem {
    record Property(
        String name,
        ImList<Argument, ?> propertyArray,
        Optional<TypeIdent> type,
        Optional<Expression> index,
        ImList<Specifier,?> specifiers,
        boolean classFlag
        ) implements ClassProperty {
        public Property withClassFlag(boolean value){
            return new Property(
                name,
                propertyArray,
                type,
                index,
                specifiers,
                value
            );
        }

        static ClassProperty.Property of( DelphiParser.ClassPropertyContext ctx ){
            boolean classFlag =
                ctx.CLASS()!=null
                && ctx.CLASS().getText()!=null
                && ctx.CLASS().getText().length()>0;

            String name = ctx.ident().getText();

            ImList<Argument,?> propArr = ImListLinked.of();
            if( ctx.classPropertyArray()!=null
                && !ctx.classPropertyArray().isEmpty()
                && ctx.classPropertyArray().formalParameterList()!=null
                && !ctx.classPropertyArray().formalParameterList().isEmpty()
            ){
                propArr = Argument.of(ctx.classPropertyArray().formalParameterList());
            }

            Optional<TypeIdent> type = Optional.empty();
            if( ctx.genericTypeIdent()!=null
            && !ctx.genericTypeIdent().isEmpty()
            ) {
                type = Optional.of(
                    TypeIdent.of(ctx.genericTypeIdent())
                );
            }

            Optional<Expression> exp = Optional.empty();
            if( ctx.classPropertyIndex()!=null
                && !ctx.classPropertyIndex().isEmpty()
                && ctx.classPropertyIndex().expression()!=null
                && !ctx.classPropertyIndex().expression().isEmpty()
            ){
                exp = Optional.of(Expression.of(ctx.classPropertyIndex().expression()));
            }

            ImList<Specifier,?> spec = ImListLinked.of();
            if( ctx.classPropertySpecifier()!=null ){
                spec = ImListLinked.of(ctx.classPropertySpecifier())
                    .map(Specifier::of);
            }

            return new ClassProperty.Property(
                name,
                propArr,
                type,
                exp,
                spec,
                classFlag
            );
        }
    }

    sealed interface Specifier {
        static Specifier of(DelphiParser.ClassPropertyEndSpecifierContext ctx){
            if( ctx.STORED()!=null
                && ctx.STORED().getText()!=null
                && ctx.STORED().getText().length()>0
                && ctx.expression()!=null
                && !ctx.expression().isEmpty()
            ) {
                return new Stored(Expression.of(ctx.expression()));
            }

            if( ctx.DEFAULT()!=null
                && ctx.DEFAULT().getText()!=null
                && ctx.DEFAULT().getText().length()>0
            ) {
                if( ctx.expression()!=null && !ctx.expression().isEmpty() ){
                    return new DefaultExp(Expression.of(ctx.expression()));
                }

                return new Default();
            }

            if( ctx.NODEFAULT()!=null
                && ctx.NODEFAULT().getText()!=null
                && ctx.NODEFAULT().getText().length()>0 ){
                return new NoDefault();
            }

            throw AstParseError.unExpected(ctx);
        }

        static Specifier of(DelphiParser.ClassPropertySpecifierContext ctx){
            if( ctx.classPropertyReadWrite()!=null && !ctx.classPropertyReadWrite().isEmpty() )
                return of(ctx.classPropertyReadWrite());

            if( ctx.classPropertyDispInterface()!=null && !ctx.classPropertyDispInterface().isEmpty() )
                return of(ctx.classPropertyDispInterface());

            if( ctx.STORED()!=null
                && ctx.STORED().getText()!=null
                && ctx.STORED().getText().length()>0
                && ctx.expression()!=null
            ){
                return new Stored(Expression.of(ctx.expression()));
            }

            if( ctx.DEFAULT()!=null
                && ctx.DEFAULT().getText()!=null
                && ctx.DEFAULT().getText().length()>0
            ){
                if( ctx.expression()!=null && !ctx.expression().isEmpty() ){
                    return new DefaultExp(Expression.of(ctx.expression()));
                }

                return new Default();
            }

            if( ctx.NODEFAULT()!=null
                && ctx.NODEFAULT().getText()!=null
                && ctx.NODEFAULT().getText().length()>0
            ){
                return new NoDefault();
            }

            if( ctx.IMPLEMENTS()!=null
                && ctx.IMPLEMENTS().getText()!=null
                && ctx.IMPLEMENTS().getText().length()>0
                && ctx.typeId()!=null
                && !ctx.typeId().isEmpty()
            ){
                var nq = ctx.typeId().namespacedQualifiedIdent();

                ImListLinked<String> namespace = ImListLinked.of();
                if( !ctx.typeId().namespacedQualifiedIdent().isEmpty()
                && nq.namespaceName()!=null
                ){
                    namespace =
                    ImListLinked.of(
                    nq.namespaceName().ident().stream().map(
                        RuleContext::getText
                    ).toList());
                }

                ImListLinked<String> ident = ImListLinked.of();
                if( !ctx.typeId().namespacedQualifiedIdent().qualifiedIdent().isEmpty() ){
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

        static Specifier of(DelphiParser.ClassPropertyReadWriteContext ctx){
            var name = ImListLinked.of(
                ctx.qualifiedIdent().ident().stream().map(RuleContext::getText).toList()
            );

            Optional<Expression> exp =
                ctx.expression()!=null && !ctx.expression().isEmpty() ?
                Optional.of(Expression.of(ctx.expression())) :
                Optional.empty();

            if( ctx.READ()!=null && ctx.READ().getText().toLowerCase().startsWith("read") ){
                return new Read(name, exp);
            } else if (ctx.WRITE()!=null && ctx.WRITE().getText().toLowerCase().startsWith("write")) {
                return new Write(name, exp);
            }

            throw AstParseError.unExpected();
        }

        static Specifier of(DelphiParser.ClassPropertyDispInterfaceContext ctx){
            if(ctx.getText().toLowerCase().startsWith("readonl")){
                return new ReadOnly();
            }else if(ctx.getText().toLowerCase().startsWith("writeonly")){
                return new WriteOnly();
            }else if(
                ctx.dispIDDirective()!=null &&
                ctx.dispIDDirective().expression()!=null &&
                ctx.getText().toLowerCase().startsWith("dispid")
            ) {
                return new DispID(Expression.of(ctx.dispIDDirective().expression()));
            }

            throw AstParseError.notImplemented();
        }
    }
    record Read(
        ImList<String,?> name,
        Optional<Expression> expression
    ) implements Specifier {}
    record Write(
        ImList<String,?> name,
        Optional<Expression> expression
    ) implements Specifier {}
    record ReadOnly() implements Specifier {}
    record WriteOnly() implements Specifier {}
    record DispID(Expression expression) implements Specifier {}
    record Stored(Expression expression) implements Specifier {}
    record DefaultExp(Expression expression) implements Specifier {}
    record Default() implements Specifier {}
    record NoDefault() implements Specifier {}
    record Implements(ImList<String,?> typeId) implements Specifier {}
}