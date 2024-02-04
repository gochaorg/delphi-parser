package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.ast.FDirective;

/**
 * Директивы метода
 */
public sealed interface MethodDirective {
    record Reintroduce() implements MethodDirective {
        public String toString() { return "Reintroduce"; }
    }
    record Overload() implements MethodDirective {
        public String toString() { return "Overload"; }
    }
    record Message(String expression) implements MethodDirective {
        public String toString() { return "Message("+expression+")"; }
    }
    record Static() implements MethodDirective {
        public String toString() { return "Static"; }
    }
    record Dynamic() implements MethodDirective {
        public String toString() { return "Dynamic"; }
    }
    record Override() implements MethodDirective {
        public String toString() { return "Override"; }
    }
    record Virtual() implements MethodDirective {
        public String toString() { return "Virtual"; }
    }
    record Abstract() implements MethodDirective {
        public String toString() { return "Abstract"; }
    }
    record Final() implements MethodDirective {
        public String toString() { return "Final"; }
    }
    record Inline() implements MethodDirective {
        public String toString() { return "Inline"; }
    }
    record Assembler() implements MethodDirective {
        public String toString() { return "Assembler"; }
    }
    record Cdecl() implements MethodDirective {
        public String toString() { return "Cdecl"; }
    }
    record Pascal() implements MethodDirective {
        public String toString() { return "Pascal"; }
    }
    record Register() implements MethodDirective {
        public String toString() { return "Register"; }
    }
    record SafeCall() implements MethodDirective {
        public String toString() { return "SafeCall"; }
    }
    record StdCall() implements MethodDirective {
        public String toString() { return "StdCall"; }
    }
    record Export() implements MethodDirective {
        public String toString() { return "Export"; }
    }
    record Far() implements MethodDirective {
        public String toString() { return "Far"; }
    }
    record Local() implements MethodDirective {
        public String toString() { return "Local"; }
    }
    record Near() implements MethodDirective {
        public String toString() { return "Near"; }
    }
    record DispID(String expression) implements MethodDirective {
        public String toString() { return "DispID("+expression+")"; }
    }

    public static MethodDirective of(FDirective.MethodDirective dir) {
        if( dir instanceof FDirective.Reintroduce )return new Reintroduce();
        if( dir instanceof FDirective.Overload )return new Overload();
        if( dir instanceof FDirective.Message m )return new Message(m.expression().text());
        if( dir instanceof FDirective.Static )return new Static();
        if( dir instanceof FDirective.Dynamic )return new Dynamic();
        if( dir instanceof FDirective.Override )return new Override();
        if( dir instanceof FDirective.Virtual )return new Virtual();
        if( dir instanceof FDirective.Abstract )return new Abstract();
        if( dir instanceof FDirective.Final )return new Final();
        if( dir instanceof FDirective.Inline )return new Inline();
        if( dir instanceof FDirective.Assembler )return new Assembler();
        if( dir instanceof FDirective.Cdecl )return new Cdecl();
        if( dir instanceof FDirective.Pascal )return new Pascal();
        if( dir instanceof FDirective.Register )return new Register();
        if( dir instanceof FDirective.SafeCall )return new SafeCall();
        if( dir instanceof FDirective.StdCall )return new StdCall();
        if( dir instanceof FDirective.Export )return new Export();
        if( dir instanceof FDirective.Far )return new Far();
        if( dir instanceof FDirective.Local )return new Local();
        if( dir instanceof FDirective.Near )return new Near();
        if( dir instanceof FDirective.DispID d )return new DispID(d.expression().text());
        return null;
    }
}
