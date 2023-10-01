package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;

import java.util.Optional;

/**
 * Метод интерфейса/класса
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class Method implements Freeze, InterfaceItem {
    //region freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(){
        arguments.forEach(MethodArgument::freeze);
        returns.ifPresent(p -> {
            if (p instanceof Freeze f) f.freeze();
        } );
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
    //region params : ImList<MethodParam,?> - параметры метода
    private ImList<MethodArgument,?> arguments = ImListLinked.of();

    public ImList<MethodArgument, ?> getArguments() {
        return arguments;
    }

    public void setArguments(ImList<MethodArgument, ?> arguments) {
        if( arguments==null ) throw new IllegalArgumentException("arguments==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.arguments = arguments;
    }
    //endregion
    //region returns : Optional<Type> - тип результата
    private Optional<Type> returns = Optional.empty();

    public Optional<Type> getReturns() {
        return returns;
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public void setReturns(Optional<Type> returns) {
        if( returns==null ) throw new IllegalArgumentException("returns==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.returns = returns;
    }
    //endregion
    //region directives : ImList<MethodDirective,?> - опции/директивы
    private ImList<MethodDirective,?> directives = ImListLinked.of();

    public ImList<MethodDirective, ?> getDirectives() {
        return directives;
    }

    public void setDirectives(ImList<MethodDirective, ?> directives) {
        if( directives==null ) throw new IllegalArgumentException("directives==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.directives = directives;
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
    //region comments : ImList<Comment,?> - Комментарии
    private ImList<Comment,?> comments = ImListLinked.of();

    public ImList<Comment, ?> getComments() {
        return comments;
    }

    public void setComments(ImList<Comment, ?> comments) {
        if( comments==null ) throw new IllegalArgumentException("comments==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.comments = comments;
    }
    //endregion
    //region declaration : Optional<SourcePosition>
    private Optional<SourcePosition> declaration = Optional.empty();

    public Optional<SourcePosition> getDeclaration() {
        return declaration;
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public void setDeclaration(Optional<SourcePosition> declaration) {
        if( declaration==null ) throw new IllegalArgumentException("declaration==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.declaration = declaration;
    }
    //endregion
    //region implementation : Optional<SourcePosition>
    private Optional<SourcePosition> implementation = Optional.empty();

    public Optional<SourcePosition> getImplementation() {
        return implementation;
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public void setImplementation(Optional<SourcePosition> implementation) {
        if( implementation==null ) throw new IllegalArgumentException("implementation==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.implementation = implementation;
    }
    //endregion
}
