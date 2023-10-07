package xyz.cofe.delphi.tsys;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Метод интерфейса/класса
 */
public final class Destructor extends ProcBase implements Freeze, Fun, ClassItem {
    @Override
    public String toString(){
        return "destructor " + getName() + "\n" +
            indent("arguments:", getArguments()) +
            indent("directives:", getDirectives()) +
            indent("comments:", getComments().map(c -> c.text().replaceAll("[\\r\\n]", "")));
    }
}
