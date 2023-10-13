package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.ast.ClassMethodAst;

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

    public static MethodDirective of(ClassMethodAst.MethodDirective dir) {
        if( dir instanceof ClassMethodAst.MethodDirective.Reintroduce )return new Reintroduce();
        if( dir instanceof ClassMethodAst.MethodDirective.Overload )return new Overload();
        if( dir instanceof ClassMethodAst.MethodDirective.Binding.Message m )return new Message(m.expression().text());
        if( dir instanceof ClassMethodAst.MethodDirective.Binding.Static )return new Static();
        if( dir instanceof ClassMethodAst.MethodDirective.Binding.Dynamic )return new Dynamic();
        if( dir instanceof ClassMethodAst.MethodDirective.Binding.Override )return new Override();
        if( dir instanceof ClassMethodAst.MethodDirective.Binding.Virtual )return new Virtual();
        if( dir instanceof ClassMethodAst.MethodDirective.Abstract )return new Abstract();
        if( dir instanceof ClassMethodAst.MethodDirective.Final )return new Final();
        if( dir instanceof ClassMethodAst.MethodDirective.Inline )return new Inline();
        if( dir instanceof ClassMethodAst.MethodDirective.Assembler )return new Assembler();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.Cdecl )return new Cdecl();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.Pascal )return new Pascal();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.Register )return new Register();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.SafeCall )return new SafeCall();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.StdCall )return new StdCall();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.Export )return new Export();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.Far )return new Far();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.Local )return new Local();
        if( dir instanceof ClassMethodAst.MethodDirective.CallConvention.Near )return new Near();
        if( dir instanceof ClassMethodAst.MethodDirective.DispID d )return new DispID(d.expression().text());
        return null;
    }
}
