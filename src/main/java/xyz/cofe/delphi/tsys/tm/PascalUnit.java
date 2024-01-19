package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;
import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;

import java.util.Optional;

/**
 * Некий модуль компиляции
 */
@SuppressWarnings("unused")
public class PascalUnit implements Freeze {
    //region frozen
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze() {
        types.each(t -> {
            if (t instanceof Freeze f) f.freeze();
        });
        this.frozen = true;
    }
    //endregion

    //region declaration : Optional<SourcePosition> - где определение модуля
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

    //region name : TypeName - имя модуля
    private TypeName name = TypeName.DEFAULT;

    public TypeName getName() {
        return name;
    }

    public void setName(TypeName name) {
        if (name == null) throw new IllegalArgumentException("name==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.name = name;
    }
    //endregion

    //region uses : ImList<TypeName> - используемые модули
    private ImList<TypeName> uses = ImListLinked.of();

    public ImList<TypeName> getUses() {
        return uses;
    }

    public void setUses(ImList<TypeName> uses) {
        if (uses == null) throw new IllegalArgumentException("uses==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.uses = uses;
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

    //region types : ImList<Type> - типы
    private ImList<Type> types = ImListLinked.of();

    public ImList<Type> getTypes() {
        return types;
    }

    public void setTypes(ImList<Type> types) {
        if (types == null) throw new IllegalArgumentException("types==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.types = types;
    }
    //endregion

    //region procedures : ImList<Procedure>
    private ImList<Procedure> procedures = ImList.of();
    public ImList<Procedure> getProcedures(){
        return procedures;
    }
    public void setProcedures(ImList<Procedure> procs){
        if( procs==null ) throw new IllegalArgumentException("procs==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.procedures = procs;
    }
    //endregion

    //region functions : ImList<Function>
    private ImList<Function> functions = ImList.of();
    public ImList<Function> getFunctions(){
        return functions;
    }
    public void setFunctions(ImList<Function> functions){
        if( functions==null ) throw new IllegalArgumentException("functions==null");
        if (frozen) throw new TypeSysError.Frozen();
        this.functions = functions;
    }
    //endregion

    //private

    public PascalUnit() {
    }

    public PascalUnit(PascalUnit sample) {
        if (sample == null) throw new IllegalArgumentException("sample==null");
        this.declaration = sample.declaration;
        this.name = sample.name;
        this.uses = sample.uses;
        this.comments = sample.comments;
        this.types = sample.types;
    }
}
