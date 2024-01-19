package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;
import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;

import java.util.Optional;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Метод интерфейса/класса
 */
public final class Operator implements Freeze, Proc, ClassItem, FunSetReturns {
    //region freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(){
        arguments.each(Argument::freeze);
        if( returns instanceof Freeze f )f.freeze();
        this.frozen = true;
    }
    //endregion

    //region name : String - имя метода
    private String name = "?";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if( name==null ) throw new IllegalArgumentException("name==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.name = name;
    }
    //endregion
    //region arguments : ImList<MethodParam> - параметры метода
    private ImList<Argument> arguments = ImListLinked.of();

    /**
     * Возвращает аргументы функции
     * @return аргументы функции
     */
    public ImList<Argument> getArguments() {
        return arguments;
    }

    /**
     * Указывает аргументы функции
     * @param arguments аргументы функции
     */
    public void setArguments(ImList<Argument> arguments) {
        if( arguments==null ) throw new IllegalArgumentException("arguments==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.arguments = arguments;
    }
    //endregion
    //region returns : Type - тип результата
    private Type returns = Type.VoidType.instance;

    /**
     * Возвращает тип результата
     * @return Тип результата
     */
    public Type getReturns() {
        return returns;
    }

    /**
     * Указывает тип результата
     * @param returns Тип результата
     */
    public void setReturns(Type returns) {
        if( returns==null ) throw new IllegalArgumentException("returns==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.returns = returns;
    }
    //endregion
    //region visibility : Visibility - видимость
    private Visibility visibility = Visibility.Private;

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        if( visibility==null ) throw new IllegalArgumentException("visibility==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.visibility = visibility;
    }
    //endregion
    //region comments : ImList<Comment> - Комментарии
    private ImList<Comment> comments = ImListLinked.of();

    public ImList<Comment> getComments() {
        return comments;
    }

    public void setComments(ImList<Comment> comments) {
        if( comments==null ) throw new IllegalArgumentException("comments==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.comments = comments;
    }
    //endregion
    //region declaration : Optional<SourcePosition>
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<SourcePosition> declaration = Optional.empty();

    public Optional<SourcePosition> getDeclaration() {
        return declaration;
    }

    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
    public void setDeclaration(Optional<SourcePosition> declaration) {
        if( declaration==null ) throw new IllegalArgumentException("declaration==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.declaration = declaration;
    }
    //endregion
    //region implementation : Optional<SourcePosition>
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<SourcePosition> implementation = Optional.empty();

    public Optional<SourcePosition> getImplementation() {
        return implementation;
    }

    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
    public void setImplementation(Optional<SourcePosition> implementation) {
        if( implementation==null ) throw new IllegalArgumentException("implementation==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.implementation = implementation;
    }
    //endregion

    @Override
    public String toString(){
        return "operator " + getName() + "\n" +
            "  visibility: "+getVisibility()+"\n"+
            indent("arguments:", getArguments()) +
            "  returns:\n" +
            indent("    ", getReturns().toString()) + "\n" +
            indent("comments:", getComments().map(c -> c.text().replaceAll("[\\r\\n]", "")));
    }
}
