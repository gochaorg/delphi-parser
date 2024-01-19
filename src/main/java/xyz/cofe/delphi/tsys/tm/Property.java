package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;
import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class Property implements Freeze, InterfaceItem, ClassItem {
    //region freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(){
        this.frozen = true;
        if( type instanceof Freeze f )f.freeze();
        if( arrayArguments!=null )arrayArguments.each(Argument::freeze);
    }
    //endregion

    public Property(){}
    public Property(Property sample){
        if( sample==null ) throw new IllegalArgumentException("sample==null");
        this.frozen = false;
        this.name = sample.name;
        this.type = sample.type;
        this.arrayArguments = sample.arrayArguments;
        this.expression = sample.expression;
        this.specifiers = sample.specifiers;
    }

    //region name : String
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
    //region type : Type
    private Type type = Type.NotAssigned.instance;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if( type==null ) throw new IllegalArgumentException("type==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.type = type;
    }
    //endregion
    //region statik : boolean
    private boolean statik = false;

    public boolean isStatik() {
        return statik;
    }

    public void setStatik(boolean statik) {
        if( frozen )throw new TypeSysError.Frozen();
        this.statik = statik;
    }
    //endregion
    //region arrayArguments : ImList<Argument>
    private ImList<Argument> arrayArguments = ImListLinked.of();

    public ImList<Argument> getArrayArguments() {
        return arrayArguments;
    }

    public void setArrayArguments(ImList<Argument> arrayArguments) {
        if( arrayArguments==null ) throw new IllegalArgumentException("arrayArguments==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.arrayArguments = arrayArguments;
    }
    //endregion
    //region expression : Optional<String>
    private Optional<String> expression = Optional.empty();

    public Optional<String> getExpression() {
        return expression;
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public void setExpression(Optional<String> expression) {
        if( expression==null ) throw new IllegalArgumentException("expression==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.expression = expression;
    }
    //endregion
    //region specifiers : ImList<PropertySpecifier>
    private ImList<PropertySpecifier> specifiers = ImListLinked.of();

    public ImList<PropertySpecifier> getSpecifiers() {
        return specifiers;
    }

    public void setSpecifiers(ImList<PropertySpecifier> specifiers) {
        if( specifiers==null ) throw new IllegalArgumentException("specifiers==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.specifiers = specifiers;
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
}
