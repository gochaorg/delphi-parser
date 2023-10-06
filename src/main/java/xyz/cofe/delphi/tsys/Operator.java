package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Метод интерфейса/класса
 */
public final class Operator extends FunBase implements Freeze, Fun {
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("operator ").append(getName()).append("\n");
        sb.append(indent("arguments:",getArguments()));

        sb.append("  returns:\n");
        sb.append(indent("    ",getReturns().toString())).append("\n");

        sb.append(indent("directives:",getDirectives()));

        ImList<String,?> cmnt = getComments().map(c -> c.text().replaceAll("[\\r\\n]",""));
        sb.append(indent("comments:",cmnt));

        return sb.toString();
    }
}
