package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;
import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;

import java.util.Optional;

import static xyz.cofe.delphi.impl.Indent.indent;

public final class Field implements Freeze, ClassItem {
    //region freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze() {
        this.frozen = true;
    }

    //endregion
    //region name : String - имя поля
    private String name = "?";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("name==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.name = name;
    }

    //endregion
    //region fieldType : Type - тип поля
    private Type fieldType = Type.NotAssigned.instance;

    /**
     * Возвращает тип поля
     *
     * @return Тип поля
     */
    public Type getFieldType() {
        return fieldType;
    }

    /**
     * Указывает тип поля
     *
     * @param fieldType Тип поля
     */
    public void setFieldType(Type fieldType) {
        if (fieldType == null) throw new IllegalArgumentException("fieldType==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.fieldType = fieldType;
    }

    //endregion
    //region visibility : Visibility - видимость
    private Visibility visibility = Visibility.Private;

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        if (visibility == null) throw new IllegalArgumentException("visibility==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.visibility = visibility;
    }

    //endregion
    //region comments : ImList<Comment> - Комментарии
    private ImList<Comment> comments = ImListLinked.of();

    public ImList<Comment> getComments() {
        return comments;
    }

    public void setComments(ImList<Comment> comments) {
        if (comments == null) throw new IllegalArgumentException("comments==null");
        if (frozen) throw new TypeSysError.Frozen();
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
        if (declaration == null) throw new IllegalArgumentException("declaration==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.declaration = declaration;
    }
    //endregion

    public String toString() {
        return "field " + name + "\n" +
            "  visibility: " + visibility + "\n" +
            "  type: " + fieldType + "\n" +
            indent("comments:", getComments().map(c -> c.text().replaceAll("[\\r\\n]", "")));
    }
}
