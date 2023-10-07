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
public final class Function extends FunBase implements Freeze, InterfaceItem, ClassItem, Fun {
    @Override
    public String toString(){
        return "function " + getName() + "\n" +
            indent("arguments:", getArguments()) +
            "  returns:\n" +
            indent("    ", getReturns().toString()) + "\n" +
            indent("directives:", getDirectives()) +
            indent("comments:", getComments().map(c -> c.text().replaceAll("[\\r\\n]", "")));
    }
}
