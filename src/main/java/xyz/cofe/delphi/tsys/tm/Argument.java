package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.tsys.Freeze;
import xyz.cofe.delphi.tsys.TypeSysError;

import java.util.Optional;

/**
 * Аргумент метода
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Argument implements Freeze {
    //region freeze
    private volatile boolean frozen;

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(){
        if( type instanceof Freeze f )f.freeze();
        this.frozen = true;
    }
    //endregion

    //region name : String - имя параметра
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
    //region type : Type - тип
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
    //region expression : Optional<String> - значение по умолчанию
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
    //region direction : ParamDirection - направление
    private ArgumentDirection direction = ArgumentDirection.Input;

    public ArgumentDirection getDirection() {
        return direction;
    }

    public void setDirection(ArgumentDirection direction) {
        if( direction==null ) throw new IllegalArgumentException("direction==null");
        if( frozen )throw new TypeSysError.Frozen();
        this.direction = direction;
    }
    //endregion

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("  type: ").append(type).append("\n");
        expression.ifPresent(e -> {
            sb.append("  expression: ").append(e).append("\n");
        });
        sb.append("  direction: ").append(direction).append("\n");
        return sb.toString();
    }
}
