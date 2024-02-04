package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.FDirective;
import xyz.cofe.delphi.ast.SourcePosition;
import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;

import java.util.Optional;

/**
 * Базовый класс для имплементации функции/процедуры
 */
public sealed abstract class ProcBase implements Freeze permits Function, Procedure {
    public ProcBase() {
    }

    //region freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze() {
        arguments.each(Argument::freeze);
        if (returns instanceof Freeze f) f.freeze();
        this.frozen = true;
    }
    //endregion

    //region name : String - имя функции/процедуры
    private String name = "?";

    /**
     * Имя функции/процедуры
     *
     * @return Имя функции/процедуры
     */
    public String getName() {
        return name;
    }

    /**
     * Имя функции/процедуры
     *
     * @param name Имя функции/процедуры
     */
    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("name==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.name = name;
    }

    //endregion
    //region arguments : ImList<MethodParam> - параметры метода
    private ImList<Argument> arguments = ImListLinked.of();

    /**
     * Возвращает аргументы функции
     *
     * @return аргументы функции
     */
    public ImList<Argument> getArguments() {
        return arguments;
    }

    /**
     * Указывает аргументы функции
     *
     * @param arguments аргументы функции
     */
    public void setArguments(ImList<Argument> arguments) {
        if (arguments == null) throw new IllegalArgumentException("arguments==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.arguments = arguments;
    }

    //endregion
    //region returns : Type - тип результата
    private Type returns = Type.VoidType.instance;

    /**
     * Возвращает тип результата
     *
     * @return Тип результата
     */
    public Type getReturns() {
        return returns;
    }

    /**
     * Указывает тип результата
     *
     * @param returns Тип результата
     */
    protected void setReturns(Type returns) {
        if (returns == null) throw new IllegalArgumentException("returns==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.returns = returns;
    }

    //endregion
    //region directives : ImList<FunctionDirectiveAst> - опции/директивы
    private ImList<FDirective.FunctionDirective> directives = ImListLinked.of();

    /**
     * Директивы функции/процедуры
     * @return Директивы функции/процедуры
     */
    public ImList<FDirective.FunctionDirective> getDirectives() {
        return directives;
    }

    /**
     * Директивы функции/процедуры
     * @param directives Директивы функции/процедуры
     */
    public void setDirectives(ImList<FDirective.FunctionDirective> directives) {
        if (directives == null) throw new IllegalArgumentException("directives==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.directives = directives;
    }

    //endregion
    //region comments : ImList<Comment> - Комментарии
    private ImList<Comment> comments = ImListLinked.of();

    /**
     * Комментарии
     * @return Комментарии
     */
    public ImList<Comment> getComments() {
        return comments;
    }

    /**
     * Комментарии
     * @param comments Комментарии
     */
    public void setComments(ImList<Comment> comments) {
        if (comments == null) throw new IllegalArgumentException("comments==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.comments = comments;
    }

    //endregion
    //region declaration : Optional<SourcePosition>
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<SourcePosition> declaration = Optional.empty();

    /**
     * Позиция в исходниках где объявлено
     * @return Позиция в исходниках где объявлено
     */
    public Optional<SourcePosition> getDeclaration() {
        return declaration;
    }

    /**
     * Позиция в исходниках где объявлено
     * @param declaration Позиция в исходниках где объявлено
     */
    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
    public void setDeclaration(Optional<SourcePosition> declaration) {
        if (declaration == null) throw new IllegalArgumentException("declaration==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.declaration = declaration;
    }

    //endregion
    //region implementation : Optional<SourcePosition>
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<SourcePosition> implementation = Optional.empty();

    /**
     * Позиция в исходниках где реализовано
     * @return Позиция в исходниках где реализовано
     */
    public Optional<SourcePosition> getImplementation() {
        return implementation;
    }

    /**
     * Позиция в исходниках где реализовано
     * @param implementation Позиция в исходниках где реализовано
     */
    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
    public void setImplementation(Optional<SourcePosition> implementation) {
        if (implementation == null) throw new IllegalArgumentException("implementation==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.implementation = implementation;
    }
    //endregion
}
