package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.tsys.Freeze;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Метод интерфейса/класса
 */
public final class MethodFunction extends MethodFunBase implements Freeze, InterfaceItem, ClassItem, Proc {
    @Override
    public String toString(){
        return "function " + getName() + "\n" +
            "  visibility: "+getVisibility()+"\n"+
            indent("arguments:", getArguments()) +
            "  returns:\n" +
            indent("    ", getReturns().toString()) + "\n" +
            indent("directives:", getDirectives()) +
            indent("comments:", getComments().map(c -> c.text().replaceAll("[\\r\\n]", "")));
    }
}
