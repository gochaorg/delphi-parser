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
public final class Procedure extends ProcBase implements Freeze, InterfaceItem, Fun, ClassItem {
    @Override
    public String toString(){
        return "procedure " + getName() + "\n" +
            indent("arguments:", getArguments()) +
            indent("directives:", getDirectives()) +
            indent("comments:", getComments().map(c -> c.text().replaceAll("[\\r\\n]", "")));
    }
}
