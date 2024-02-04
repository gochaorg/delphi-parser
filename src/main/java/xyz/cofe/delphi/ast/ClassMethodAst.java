package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.text.Text.indent;

/**
 * Методы класса/интерфейса/...
 */
public sealed interface ClassMethodAst extends InterfaceItemAst,
                                               ClassItemAst,
                                               SrcPos,
                                               Commented<ClassMethodAst> {
    /**
     * аргументы метода
     *
     * @return аргументы
     */
    ImList<ArgumentAst> arguments();

    @Override
    ClassMethodAst astUpdate(AstUpdate.UpdateContext ctx);

    /**
     * Процедура
     *
     * @param name          имя
     * @param genericParams generic параметры
     * @param arguments     аргументы
     * @param directives    директивы компилятора
     * @param position      Позиция в исходниках
     * @param comments      Комментарии
     * @param attributes    Аттрибуты
     */
    record Procedure(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments,
        ImList<CustomAttributeAst> attributes
    ) implements ClassMethodAst,
                 SrcPos {
        @Override
        public Procedure astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams); // AstUpdate.astUpdates(this.genericParams, ctx);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var attrs = ctx.update(attributes);

            var res = this;
            if (ctx instanceof AstUpdate.CommentingContext cc) {
                res = (Procedure) cc.commenting(res);
            }

            if (directives.isEmpty()
                && genericParams.isEmpty()
                && arguments.isEmpty()
                && res.comments == this.comments
                && attrs.isEmpty()
            ) return this;

            //noinspection unchecked,rawtypes
            res = new Procedure(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                (ImList) directives.orElse((ImList) this.directives), //directives.orElse(this.directives),
                position,
                res.comments,
                attrs.orElse(attributes)
            );

            return res;
        }

        @Override
        public Procedure withComments(ImList<Comment> comments) {
            if (comments == null) throw new IllegalArgumentException("comments==null");
            return new Procedure(
                name, genericParams, arguments, directives, position, comments,
                attributes
            );
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams)
                .append(upcast(arguments))
                .append(upcast(directives))
                .append(upcast(attributes));
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(Function.class.getSimpleName()).append(" ").append(name).append("\n");
            if (genericParams.size() > 0) {
                sb.append("  genericParams:\n");
                genericParams.each(p -> sb.append(indent("    ", p.toString())).append("\n"));
            }
            if (arguments.size() > 0) {
                sb.append("  arguments:\n");
                arguments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (directives.size() > 0) {
                sb.append("  directives:\n");
                directives.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (attributes.size() > 0) {
                sb.append("  attributes:\n");
                attributes.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (comments.size() > 0) {
                sb.append("  comments:\n");
                comments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            sb.append("  position: ").append(position).append("\n");
            return sb.toString();
        }
    }

    /**
     * Конструктор
     *
     * @param name          имя
     * @param genericParams generic параметры
     * @param arguments     аргументы
     * @param directives    директивы компилятора
     * @param attributes    Атрибуты
     * @param comments      Комментарии
     * @param position      Позиция в исходниках
     */
    record Constructor(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments,
        ImList<CustomAttributeAst> attributes
    ) implements ClassMethodAst,
                 SrcPos {
        @Override
        public Constructor withComments(ImList<Comment> comments) {
            return new Constructor(name, genericParams, arguments, directives, position, comments, attributes);
        }

        public Constructor astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var attrs = ctx.update(attributes);

            var res = this;
            if (ctx instanceof AstUpdate.CommentingContext cc) {
                res = (Constructor) cc.commenting(res);
            }

            if (directives.isEmpty()
                && genericParams.isEmpty()
                && arguments.isEmpty()
                && res.comments == this.comments
                && attrs.isEmpty()
            ) return this;

            //noinspection unchecked,rawtypes
            res = new Constructor(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                (ImList) directives.orElse((ImList) this.directives),
                position,
                res.comments,
                attrs.orElse(attributes)
            );

            return res;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams)
                .append(upcast(arguments))
                .append(upcast(directives))
                .append(upcast(attributes));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(Constructor.class.getSimpleName()).append(" ").append(name).append("\n");
            if (genericParams.size() > 0) {
                sb.append("  genericParams:\n");
                genericParams.each(p -> sb.append(indent("    ", p.toString())).append("\n"));
            }
            if (arguments.size() > 0) {
                sb.append("  arguments:\n");
                arguments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (directives.size() > 0) {
                sb.append("  directives:\n");
                directives.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (attributes.size() > 0) {
                sb.append("  attributes:\n");
                attributes.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (comments.size() > 0) {
                sb.append("  comments:\n");
                comments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            sb.append("  position: " + position + "\n");
            return sb.toString();
        }
    }

    /**
     * Деструктор
     *
     * @param name          имя
     * @param genericParams generic параметры
     * @param arguments     аргументы
     * @param directives    директивы компилятора
     * @param position      Позиция в исходниках
     * @param comments      Комментарии
     * @param attributes    Атрибуты
     */
    record Destructor(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments,
        ImList<CustomAttributeAst> attributes
    ) implements ClassMethodAst,
                 SrcPos {
        public Destructor astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var attrs = ctx.update(attributes);

            var res = this;
            if (ctx instanceof AstUpdate.CommentingContext cc) {
                res = (Destructor) cc.commenting(res);
            }

            if (directives.isEmpty()
                && genericParams.isEmpty()
                && arguments.isEmpty()
                && res.comments == this.comments
                && attrs.isEmpty()
            ) return this;

            //noinspection unchecked,rawtypes
            res = new Destructor(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                (ImList) directives.orElse((ImList) this.directives),
                position,
                res.comments,
                attrs.orElse(attributes)
            );

            return res;
        }

        @Override
        public Destructor withComments(ImList<Comment> comments) {
            return new Destructor(name, genericParams, arguments, directives, position, comments, attributes);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return
                upcast(genericParams)
                    .append(upcast(arguments))
                    .append(upcast(directives))
                    .append(upcast(attributes));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(Destructor.class.getSimpleName() + " " + name).append("\n");
            if (genericParams.size() > 0) {
                sb.append("  genericParams:\n");
                genericParams.each(p -> sb.append(indent("    ", p.toString())).append("\n"));
            }
            if (arguments.size() > 0) {
                sb.append("  arguments:\n");
                arguments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (directives.size() > 0) {
                sb.append("  directives:\n");
                directives.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (attributes.size() > 0) {
                sb.append("  attributes:\n");
                attributes.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (comments.size() > 0) {
                sb.append("  comments:\n");
                comments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            sb.append("  position: " + position + "\n");
            return sb.toString();
        }
    }

    /**
     * Функция
     *
     * @param name             имя
     * @param genericParams    generic параметры
     * @param arguments        аргументы
     * @param result           результат вызова
     * @param directives       директивы компилятора
     * @param position         Позиция в исходниках
     * @param attributes       Аттрибуты
     * @param comments         Комментарии
     * @param resultAttributes Атрибуты результата
     */
    record Function(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        TypeDeclAst result,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments,
        ImList<CustomAttributeAst> attributes,
        ImList<CustomAttributeAst> resultAttributes
    ) implements ClassMethodAst,
                 SrcPos {
        @Override
        public Function astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams); // AstUpdate.astUpdates(this.genericParams, ctx);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var result = this.result.astUpdate(ctx);
            var attrs = ctx.update(attributes);
            var rattrs = ctx.update(resultAttributes);

            var res = this;

            if (ctx instanceof AstUpdate.CommentingContext cc) {
                res = (Function) cc.commenting(res);
            }

            if (directives.isEmpty()
                && genericParams.isEmpty()
                && arguments.isEmpty()
                && result == this.result
                && res.comments == this.comments
                && attrs.isEmpty()
                && rattrs.isEmpty()
            ) return this;

            //noinspection unchecked,rawtypes
            res = new Function(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                result,
                (ImList) directives.orElse((ImList) this.directives),
                position,
                res.comments,
                attrs.orElse(attributes),
                rattrs.orElse(resultAttributes)
            );

            return res;
        }

        @Override
        public Function withComments(ImList<Comment> comments) {
            if (comments == null) throw new IllegalArgumentException("comments==null");
            return new Function(
                name,
                genericParams,
                arguments,
                result,
                directives,
                position,
                comments,
                attributes,
                resultAttributes);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams)
                .append(upcast(arguments))
                .append(upcast(directives))
                .append(result)
                .append(upcast(attributes))
                .append(resultAttributes);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(Function.class.getSimpleName()).append(" ").append(name).append("\n");
            if (genericParams.size() > 0) {
                sb.append("  genericParams:\n");
                genericParams.each(p -> sb.append(indent("    ", p.toString())).append("\n"));
            }
            if (arguments.size() > 0) {
                sb.append("  arguments:\n");
                arguments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            sb.append("  result:\n");
            sb.append(indent("    ", result.toString()));
            if (directives.size() > 0) {
                sb.append("  directives:\n");
                directives.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (attributes.size() > 0) {
                sb.append("  attributes:\n");
                attributes.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (resultAttributes.size() > 0) {
                sb.append("  resultAttributes:\n");
                resultAttributes.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (comments.size() > 0) {
                sb.append("  comments:\n");
                comments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            sb.append("  position: ").append(position).append("\n");
            return sb.toString();
        }
    }

    /**
     * Оператор
     *
     * @param name             имя
     * @param genericParams    generic параметры
     * @param arguments        аргументы
     * @param result           результат вызова
     * @param position         Позиция в исходниках
     * @param comments         Комментарии
     * @param attributes       Атрибуты
     * @param resultAttributes Атрибуты связанные с результатом
     */
    record Operator(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        TypeDeclAst result,
        SourcePosition position,
        ImList<Comment> comments,
        ImList<CustomAttributeAst> attributes,
        ImList<CustomAttributeAst> resultAttributes
    ) implements ClassMethodAst,
                 SrcPos {
        public Operator astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var result = this.result.astUpdate(ctx);
            var attrs = ctx.update(attributes);
            var rattrs = ctx.update(resultAttributes);

            var res = this;
            if (ctx instanceof AstUpdate.CommentingContext cc) {
                res = (Operator) cc.commenting(res);
            }

            if (genericParams.isEmpty()
                && arguments.isEmpty()
                && res.comments == this.comments
                && result == this.result
                && attrs.isEmpty()
                && rattrs.isEmpty()
            ) return this;

            res = new Operator(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                result,
                position,
                res.comments,
                attrs.orElse(attributes),
                rattrs.orElse(resultAttributes)
            );

            return res;
        }

        @Override
        public Operator withComments(ImList<Comment> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams).append(upcast(arguments)).append(result);
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(Function.class.getSimpleName()).append(" ").append(name).append("\n");
            if (genericParams.size() > 0) {
                sb.append("  genericParams:\n");
                genericParams.each(p -> sb.append(indent("    ", p.toString())).append("\n"));
            }
            if (arguments.size() > 0) {
                sb.append("  arguments:\n");
                arguments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            sb.append("  result:\n");
            sb.append(indent("    ", result.toString()));
            if (attributes.size() > 0) {
                sb.append("  attributes:\n");
                attributes.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (resultAttributes.size() > 0) {
                sb.append("  resultAttributes:\n");
                resultAttributes.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            if (comments.size() > 0) {
                sb.append("  comments:\n");
                comments.each(a -> sb.append(indent("    ", a.toString())).append("\n"));
            }
            sb.append("  position: ").append(position).append("\n");
            return sb.toString();
        }
    }

    record MethodAlias(ImList<String> sourceName, String implName, SourcePosition position,
                       ImList<Comment> comments) implements ClassMethodAst,
                                                            SrcPos {
        @Override
        public ImList<ArgumentAst> arguments() {
            return ImList.of();
        }

        @Override
        public ClassMethodAst astUpdate(AstUpdate.UpdateContext ctx) {
            var withCmnts = this;
            if (ctx instanceof AstUpdate.CommentingContext cc) {
                withCmnts = (MethodAlias) cc.commenting(withCmnts);
            }

            if (withCmnts.comments == comments) return this;

            return withCmnts;
        }

        @Override
        public ClassMethodAst withComments(ImList<Comment> comments) {
            return new MethodAlias(sourceName, implName, position, comments);
        }

        public static MethodAlias of(DelphiParser.OleClassMethodAliasContext ctx) {
            if (ctx.comItfMethod == null) throw AstParseError.unExpected(ctx);
            if (ctx.comItfName == null) throw AstParseError.unExpected(ctx);
            if (ctx.implMethod == null) throw AstParseError.unExpected(ctx);

            return new MethodAlias(
                ImList.of(ctx.comItfName.getText(), ctx.comItfMethod.getText()),
                ctx.implMethod.getText(),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }

    static ClassMethodAst of(DelphiParser.ClassMethodContext ctx) {
        if (ctx.oleClassMethodAlias() != null && !ctx.oleClassMethodAlias().isEmpty())
            return MethodAlias.of(ctx.oleClassMethodAlias());

        if (ctx.mname == null) throw AstParseError.unExpected();
        var name = ctx.mname.getText();

        ImListLinked<GenericAst.Param> generic_params =
            ctx.genericDefinition() != null && !ctx.genericDefinition().isEmpty()
                ? GenericAst.of(ctx.genericDefinition())
                : ImListLinked.of();

        ImList<ArgumentAst> params =
            ctx.formalParameterSection() != null && !ctx.formalParameterSection().isEmpty() && ctx.formalParameterSection().formalParameterList() != null ?
                ArgumentAst.of(ctx.formalParameterSection().formalParameterList()) :
                ImListLinked.of();

        ImList<MethodDirectiveAst> meth_dir =
            ctx.methodDirective() != null ?
                ImListLinked.of(ctx.methodDirective()).map(MethodDirectiveAst::of) :
                ImListLinked.of();

        Optional<TypeDeclAst> result =
            (ctx.typeDecl() != null && !ctx.typeDecl().isEmpty()) ?
                Optional.of(TypeDeclAst.of(ctx.typeDecl())) :
                Optional.empty();

        var methKey =
            ctx.methodKey() == null ||
                ctx.methodKey().isEmpty() ||
                ctx.methodKey().getText() == null ?
                "" :
                ctx.methodKey().getText();

        //noinspection ConstantConditions
        if (ctx.OPERATOR() != null
            && ctx.OPERATOR().getText().toLowerCase().startsWith("opera")
            && result.isPresent()
        ) {
            return new Operator(
                name, generic_params, params, result.get(), SourcePosition.of(ctx), ImList.of(),
                ctx.customAttribute() != null && !ctx.customAttribute().isEmpty() ? ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) : ImList.of(),
                ctx.retAttr != null && !ctx.retAttr.isEmpty() ? ImList.of(CustomAttributeAst.of(ctx.retAttr)) : ImList.of()
            );
        }

        if (methKey.startsWith("pro")) {
            return new Procedure(
                name, generic_params, params, meth_dir, SourcePosition.of(ctx), ImListLinked.of(),
                ctx.customAttribute() != null && !ctx.customAttribute().isEmpty() ? ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) : ImList.of()
            );
        }

        if (methKey.startsWith("cons")) {
            return new Constructor(
                name, generic_params, params, meth_dir, SourcePosition.of(ctx), ImListLinked.of(),
                ctx.customAttribute() != null && !ctx.customAttribute().isEmpty() ? ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) : ImList.of()
            );
        }

        if (methKey.startsWith("des")) {
            return new Destructor(name, generic_params, params, meth_dir, SourcePosition.of(ctx), ImListLinked.of(),
                ctx.customAttribute() != null && !ctx.customAttribute().isEmpty() ? ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) : ImList.of()
            );
        }

        if (ctx.FUNCTION() != null
            && ctx.FUNCTION().getText() != null
            && !ctx.FUNCTION().getText().isEmpty()
            && result != null
            && result.isPresent()) {
            return new Function(
                name, generic_params, params, result.get(), meth_dir, SourcePosition.of(ctx), ImListLinked.of(),
                ctx.customAttribute() != null && !ctx.customAttribute().isEmpty() ? ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) : ImList.of(),
                ctx.retAttr != null && !ctx.retAttr.isEmpty() ? ImList.of(CustomAttributeAst.of(ctx.retAttr)) : ImList.of()
            );
        }

        throw AstParseError.notImplemented(ctx);
    }
}
