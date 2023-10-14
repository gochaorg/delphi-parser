package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;
import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;

import java.util.Optional;

import static xyz.cofe.delphi.tsys.tm.json.jakson.JsonAttr.JSON_TYPE_FIELD;

/**
 * Интерфейс
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "DefaultAnnotationParam"})
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = JSON_TYPE_FIELD
)
public sealed class InterfaceType implements Type, Freeze, PascalUnitItem, ParentsProperty {
    //region Конструкторы
    public InterfaceType() {
    }

    public InterfaceType(InterfaceType sample) {
        if (sample == null) throw new IllegalArgumentException("sample==null");
        this.parents = sample.parents;
        this.body = sample.body;
        this.guid = sample.guid;
        this.declaration = sample.declaration;
        this.implementation = sample.implementation;
        this.comments = sample.comments;
        this.frozen = false;
    }
    //endregion

    //region Freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze() {
        this.frozen = true;
    }
    //endregion

    //ImList<Generic,?> params,

    //region parents : ImList<Type>
    private ImList<Type> parents;

    public ImList<Type> getParents() {
        return parents;
    }

    public void setParents(ImList<Type> parents) {
        if (parents == null) throw new IllegalArgumentException("parents==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.parents = parents;
    }

    //endregion
    //region body : ImList<InterfaceItem>
    private ImList<InterfaceItem> body = ImListLinked.of();

    public ImList<InterfaceItem> getBody() {
        return body;
    }

    public void setBody(ImList<InterfaceItem> body) {
        if (body == null) throw new IllegalArgumentException("body==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.body = body;
    }

    //endregion
    //region guid : Optional<String>
    private Optional<String> guid = Optional.empty();

    public Optional<String> getGuid() {
        return guid;
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public void setGuid(Optional<String> guid) {
        if (guid == null) throw new IllegalArgumentException("guid==null");
        if (frozen) throw new TypeSysError.Frozen();
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
        if (declaration == null) throw new IllegalArgumentException("declaration==null");
        if (frozen) throw new TypeSysError.Frozen();
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
        if (implementation == null) throw new IllegalArgumentException("implementation==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.implementation = implementation;
    }

    //endregion
    //region comments : ImList<Comment,?> - Комментарии
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

    public static final class Named extends InterfaceType implements NamedType {
        //region NamedInterfaceType
        public Named() {
        }

        public Named(InterfaceType sample) {
            super(sample);
        }

        //endregion
        //region name : TypeName
        private TypeName name = TypeName.of();

        public TypeName getName() {
            return name;
        }

        public void setName(TypeName name) {
            if (name == null) throw new IllegalArgumentException("name==null");
            if (isFrozen()) throw new TypeSysError.Frozen();
            this.name = name;
        }

        @Override
        public TypeName name() {
            return name;
        }
        //endregion

        @Override
        public String toString() {
            return "interface " + name;
        }
    }
}