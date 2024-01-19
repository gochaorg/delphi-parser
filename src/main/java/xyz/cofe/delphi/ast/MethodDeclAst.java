package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.List;
import java.util.Optional;

import static xyz.cofe.delphi.ast.AstNode.upcast;

/**
 * Декларация метода класса
 */
@SuppressWarnings("rawtypes")
public sealed interface MethodDeclAst extends InterfaceDecl,
                                              AstNode,
                                              SrcPos,
                                              AstUpdate
{
    @Override
    default MethodDeclAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    /**
     * Имя метода
     */
    sealed interface Name extends AstNode, SrcPos {
        default Name astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        /**
         * Имя метода класса
         *
         * @param className           имя класса
         * @param classGenericParams  параметры класса
         * @param methodName          имя метода
         * @param methodGenericParams параметры метода
         */
        record DirectName(
            String className,
            ImList<GenericAst.Param> classGenericParams,
            String methodName,
            ImList<GenericAst.Param> methodGenericParams,
            SourcePosition position,
            ImList<Comment> comments
        ) implements Name, AstUpdate<DirectName>, Commented<DirectName> {
            @Override
            public DirectName withComments(ImList<Comment> comments) {
                return new DirectName(className,classGenericParams,methodName,methodGenericParams,position,comments);
            }

            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return upcast(methodGenericParams).prepend(upcast(classGenericParams));
            }

            @Override
            public DirectName astUpdate(AstUpdate.UpdateContext ctx) {
                if( ctx==null ) throw new IllegalArgumentException("ctx==null");
                var res = this;

                if( ctx instanceof AstUpdate.CommentingContext cc ){
                    res = cc.commenting(this);
                }

                var clsGen = ctx.update(classGenericParams);
                var mthGen = ctx.update(methodGenericParams);

                if( clsGen.isEmpty() && mthGen.isEmpty() && comments==res.comments )return this;

                return new DirectName(
                    className,
                    clsGen.orElse(classGenericParams),
                    methodName,
                    mthGen.orElse(methodGenericParams),
                    position,
                    res.comments
                );
            }
        }

        /**
         * Имя метода вложенного класса
         *
         * @param className           имя класса
         * @param classGenericParams  параметры класса
         * @param nestedName          имя вложенного класса
         * @param nestedGenericParams параметры вложенного класса
         * @param methodName          имя метода
         * @param methodGenericParams параметры метода
         */
        record NestedName(
            String className,
            ImList<GenericAst.Param> classGenericParams,
            String nestedName,
            ImList<GenericAst.Param> nestedGenericParams,
            String methodName,
            ImList<GenericAst.Param> methodGenericParams,
            SourcePosition position,
            ImList<Comment> comments
        ) implements Name, Commented<NestedName>, AstUpdate<NestedName> {
            @Override
            public NestedName withComments(ImList<Comment> comments) {
                return new NestedName(className, classGenericParams, nestedName, nestedGenericParams, methodName, methodGenericParams, position, comments);
            }

            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return upcast(classGenericParams).append(upcast(nestedGenericParams)).append(upcast(methodGenericParams));
            }

            @Override
            public NestedName astUpdate(AstUpdate.UpdateContext ctx) {
                if( ctx==null ) throw new IllegalArgumentException("ctx==null");
                var res = this;

                if( ctx instanceof AstUpdate.CommentingContext cc ){
                    res = cc.commenting(this);
                }

                var clsGen = ctx.update(classGenericParams);
                var mthGen = ctx.update(methodGenericParams);
                var nstGen = ctx.update(nestedGenericParams);

                if( clsGen.isEmpty() && mthGen.isEmpty() && nstGen.isEmpty() && comments==res.comments )return this;

                return new NestedName(
                    className,
                    clsGen.orElse(classGenericParams),
                    nestedName,
                    nstGen.orElse(nestedGenericParams),
                    methodName,
                    mthGen.orElse(methodGenericParams),
                    position,
                    res.comments
                );
            }
        }

        static Name of(DelphiParser.MethodNameContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");

            ImList<GenericAst.Param> classTArgs = ctx.classTArgs != null && !ctx.classTArgs.isEmpty()
                ? GenericAst.of(ctx.classTArgs)
                : ImListLinked.of();
            ImList<GenericAst.Param> nestedTArgs = ctx.nestedTArgs != null && !ctx.nestedTArgs.isEmpty()
                ? GenericAst.of(ctx.nestedTArgs)
                : ImListLinked.of();
            ImList<GenericAst.Param> methTArgs = ctx.methTArgs != null && !ctx.methTArgs.isEmpty()
                ? GenericAst.of(ctx.methTArgs)
                : ImListLinked.of();

            var clsName = ctx.className != null ? ctx.className.getText() : "";
            var nestedName = ctx.nestedName != null ? ctx.nestedName.getText() : "";
            var methName = ctx.methName != null ? ctx.methName.getText() : "";

            if (nestedName.isEmpty()) {
                return new DirectName(
                    clsName,
                    classTArgs,
                    methName,
                    methTArgs,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
                );
            }

            return new NestedName(
                clsName,
                classTArgs,
                nestedName,
                nestedTArgs,
                methName,
                methTArgs,
                SourcePosition.of(ctx),
                ImListLinked.of()
            );
        }
    }

    //region Constructor

    /**
     * Конструктор класса
     * @param statik флаг статичного конструктора - он по определению должен быть всегда true (по сути)
     * @param name Имя конструктора
     * @param arguments Аргументы
     * @param directives Директивы
     * @param position Позиция в исходниках
     * @param comments Комментарии
     */
    record Constructor(
        boolean statik,
        Name name,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements MethodDeclAst, Commented<Constructor> {
        @Override
        public Constructor withComments(ImList<Comment> comments) {
            return new Constructor(statik,name,arguments,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments).append(upcast(directives));
        }

        @Override
        public Constructor astUpdate(AstUpdate.UpdateContext ctx) {
            var args = ctx.update(arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var cmnt = ctx instanceof AstUpdate.CommentingContext cc ? cc.commenting(this) : this;
            if( cmnt==this && args.isEmpty() && directives.isEmpty() )return this;
            //noinspection unchecked,rawtypes
            return new Constructor(
                statik,
                name,
                args.orElse(arguments),
                (ImList) directives.orElse((ImList) this.directives),
                position,
                cmnt.comments);
        }
    }
    //endregion

    //region Destructor
    /**
     * Деструктор класса
     * @param statik флаг статичного конструктора - он по определению должен быть всегда false (по сути)
     * @param name Имя деструктора
     * @param arguments Аргументы
     * @param directives Директивы
     * @param position Позиция в исходниках
     * @param comments Комментарии
     */
    record Destructor(
        boolean statik,
        Name name,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements MethodDeclAst, Commented<Destructor> {
        @Override
        public Destructor withComments(ImList<Comment> comments) {
            return new Destructor(statik,name,arguments,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments).append(upcast(directives));
        }

        @Override
        public Destructor astUpdate(AstUpdate.UpdateContext ctx) {
            var args = ctx.update(arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var cmnt = ctx instanceof AstUpdate.CommentingContext cc ? cc.commenting(this) : this;
            if( cmnt==this && args.isEmpty() && directives.isEmpty() )return this;
            //noinspection unchecked,rawtypes
            return new Destructor(
                statik,
                name,
                args.orElse(arguments),
                (ImList) directives.orElse((ImList) this.directives),
                position,
                cmnt.comments
            );
        }
    }
    //endregion

    //region Procedure
    record Procedure(
        boolean statik,
        Name name,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements MethodDeclAst, Commented<Procedure> {
        @Override
        public Procedure withComments(ImList<Comment> comments) {
            return new Procedure(statik,name,arguments,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments).append(upcast(directives));
        }

        @Override
        public Procedure astUpdate(AstUpdate.UpdateContext ctx) {
            var args = ctx.update(arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var cmnt = ctx instanceof AstUpdate.CommentingContext cc ? cc.commenting(this) : this;
            if( cmnt==this && args.isEmpty() && directives.isEmpty() )return this;
            //noinspection unchecked,rawtypes
            return new Procedure(
                statik,
                name,
                args.orElse(arguments),
                (ImList) directives.orElse((ImList) this.directives),
                position,
                cmnt.comments);
        }
    }
    //endregion

    //region Function
    record Function(
        boolean statik,
        Name name,
        ImList<ArgumentAst> arguments,
        TypeDeclAst returns,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements MethodDeclAst, Commented<Function> {
        @Override
        public Function withComments(ImList<Comment> comments) {
            return new Function(statik,name,arguments,returns,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments).append(returns).append(upcast(directives));
        }

        @Override
        public Function astUpdate(AstUpdate.UpdateContext ctx) {
            var args = ctx.update(arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var cmnt = ctx instanceof AstUpdate.CommentingContext cc ? cc.commenting(this) : this;
            var rets = returns.astUpdate(ctx);
            if( cmnt==this && args.isEmpty() && rets==returns && directives.isEmpty() )return this;
            //noinspection unchecked,rawtypes
            return new Function(
                statik,
                name,
                args.orElse(arguments),
                rets,
                (ImList) directives.orElse((ImList) this.directives),
                position,
                cmnt.comments);
        }
    }
    //endregion

    //region Operator
    record Operator(
        Name name,
        ImList<ArgumentAst> arguments,
        TypeDeclAst returns,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements MethodDeclAst, Commented<Operator> {
        @Override
        public Operator withComments(ImList<Comment> comments) {
            return new Operator(name,arguments,returns,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments).append(returns).append(upcast(directives));
        }

        @Override
        public Operator astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }
    //endregion

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static MethodDeclAst of(DelphiParser.MethodDeclContext ctx) {
        var name = Name.of(ctx.methodDeclHeading().methodName());

        boolean statik =
            ctx.methodDeclHeading().CLASS()!=null;

        var args = ctx.methodDeclHeading().formalParameterSection()!=null &&
            !ctx.methodDeclHeading().formalParameterSection().isEmpty() &&
            ctx.methodDeclHeading().formalParameterSection().formalParameterList()!=null &&
            !ctx.methodDeclHeading().formalParameterSection().formalParameterList().isEmpty()
            ? ArgumentAst.of(ctx.methodDeclHeading().formalParameterSection().formalParameterList())
            : ArgumentAst.of(List.of());

        var returns = ctx.methodDeclHeading().typeDecl()!=null &&
            !ctx.methodDeclHeading().typeDecl().isEmpty()
            ? Optional.of(TypeDeclAst.of(ctx.methodDeclHeading().typeDecl()))
            : Optional.<TypeDeclAst>empty();

        var directives =
            ctx.methodDirective()!=null && !ctx.methodDirective().isEmpty()
            ? ImListLinked.of(ctx.methodDirective()).map(MethodDirectiveAst::of)
            : ImListLinked.<MethodDirectiveAst>of();

        if (ctx.methodDeclHeading().methodKey() != null && !ctx.methodDeclHeading().methodKey().isEmpty()) {
            if( ctx.methodDeclHeading().methodKey().PROCEDURE()!=null ){
                return new Procedure(
                    statik,
                    name,
                    args,
                    directives,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
                );
            }else if( ctx.methodDeclHeading().methodKey().CONSTRUCTOR()!=null ){
                return new Constructor(
                    statik,
                    name,
                    args,
                    directives,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
                );
            }else if( ctx.methodDeclHeading().methodKey().DESTRUCTOR()!=null ){
                return new Destructor(
                    statik,
                    name,
                    args,
                    directives,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
                );
            }else{
                throw AstParseError.unExpected(ctx.methodDeclHeading().methodKey());
            }
        } else if (ctx.methodDeclHeading().FUNCTION() != null) {
            return new Function(
                statik,
                name,
                args,
                returns.get(),
                directives,
                SourcePosition.of(ctx),
                ImListLinked.of()
            );
        } else if (ctx.methodDeclHeading().OPERATOR() != null) {
            return new Operator(
                name,
                args,
                returns.get(),
                directives,
                SourcePosition.of(ctx),
                ImListLinked.of()
            );
        }else {
            throw AstParseError.unExpected(ctx);
        }
    }
}
