package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;
import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;
import xyz.cofe.delphi.tsys.tm.json.jakson.ClassTypeSer;

import java.util.Optional;

/**
 * Класс
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@JsonSerialize(using = ClassTypeSer.class)
public sealed class ClassType implements PascalUnitItem, Type, Freeze, ParentsProperty
{
    public ClassType(){}
    public ClassType(ClassType sample){
        if( sample==null ) throw new IllegalArgumentException("sample==null");
        this.frozen = false;
        this.parents = sample.parents;
        this.body = sample.body;
        this.specifier = sample.specifier;
        this.comments = sample.comments;
        this.declaration = sample.declaration;
        this.implementation = sample.implementation;
    }

    //region Freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(){
        this.frozen = true;
    }
    //endregion
    //region parents : ImList<Type>
    private ImList<Type> parents;

    public ImList<Type> getParents() {
        return parents;
    }

    public void setParents(ImList<Type> parents) {
        if( parents==null ) throw new IllegalArgumentException("parents==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.parents = parents;
    }
    //endregion
    //region body : ImList<InterfaceItem> - Содержание класса
    private ImList<ClassItem> body = ImListLinked.of();
    public ImList<ClassItem> getBody() {
        return body;
    }
    public void setBody(ImList<ClassItem> body) {
        if( body==null ) throw new IllegalArgumentException("body==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.body = body;
    }
    //endregion
    //region specifier : Specifier = Sealed | Abstract | Default
    public static enum Specifier {
        Sealed,
        Abstract,
        Default
    }

    private Specifier specifier = Specifier.Default;

    public Specifier getSpecifier() {
        return specifier;
    }

    public void setSpecifier(Specifier specifier) {
        if( specifier==null ) throw new IllegalArgumentException("specifier==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.specifier = specifier;
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

    @JsonSerialize(using = ClassTypeSer.class)
    public static final class Named extends ClassType implements NamedType {
        public Named(){
        }
        public Named(ClassType sample){
            super(sample);
        }
        public Named(Named sample){
            super(sample);
            this.name = sample.name;
        }

        //region name : TypeName
        private TypeName name = TypeName.of();

        public TypeName getName() {
            return name;
        }

        public void setName(TypeName name) {
            if( name==null ) throw new IllegalArgumentException("name==null");
            if( isFrozen() )throw new TypeSysError.Frozen();
            this.name = name;
        }

        @Override
        public TypeName name() {
            return name;
        }
        //endregion

        @Override
        public String toString() {
            return "class "+name;
        }
    }
}
