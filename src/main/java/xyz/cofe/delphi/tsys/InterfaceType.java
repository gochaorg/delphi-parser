package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.SourcePosition;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class InterfaceType implements Freeze {
    //region Freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(){
        this.frozen = true;
    }
    //endregion

    //ImList<Generic,?> params,

    //region parents : ImList<Type, ?>
    private ImList<Type, ?> parents;

    public ImList<Type, ?> getParents() {
        return parents;
    }

    public void setParents(ImList<Type, ?> parents) {
        if( parents==null ) throw new IllegalArgumentException("parents==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.parents = parents;
    }
    //endregion
    //region methods : ImList<Method, ?>
    private ImList<Method, ?> methods = ImListLinked.of();

    public ImList<Method, ?> getMethods() {
        return methods;
    }

    public void setMethods(ImList<Method, ?> methods) {
        if( methods==null ) throw new IllegalArgumentException("methods==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.methods = methods;
    }
    //endregion
    //region guid : Optional<String>
    private Optional<String> guid = Optional.empty();

    public Optional<String> getGuid() {
        return guid;
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public void setGuid(Optional<String> guid) {
        if( guid==null ) throw new IllegalArgumentException("guid==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.guid = guid;
    }
    //endregion
    //region declaration : Optional<SourcePosition>
    private Optional<SourcePosition> declaration;

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
    private Optional<SourcePosition> implementation;

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