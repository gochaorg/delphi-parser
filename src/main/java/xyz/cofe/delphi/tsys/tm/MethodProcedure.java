package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.tsys.Freeze;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Метод интерфейса/класса
 */
public final class MethodProcedure extends MethodBase implements Freeze, InterfaceItem, Proc, ClassItem {
    @Override
    public String toString(){
        return "procedure " + getName() + "\n" +
            "  visibility: "+getVisibility()+"\n"+
            indent("arguments:", getArguments()) +
            indent("directives:", getDirectives()) +
            indent("comments:", getComments().map(c -> c.text().replaceAll("[\\r\\n]", "")));
    }
}
