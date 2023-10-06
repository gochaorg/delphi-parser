package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;

import java.util.Optional;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Метод интерфейса/класса
 */
public final class Function extends FunBase implements Freeze, InterfaceItem, Fun {
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("function ").append(getName()).append("\n");
        sb.append(indent("arguments:",getArguments()));

        sb.append("  returns:\n");
        sb.append(indent("    ",getReturns().toString())).append("\n");

        sb.append(indent("directives:",getDirectives()));

        ImList<String,?> cmnt = getComments().map(c -> c.text().replaceAll("[\\r\\n]",""));
        sb.append(indent("comments:",cmnt));

        return sb.toString();
    }
}
