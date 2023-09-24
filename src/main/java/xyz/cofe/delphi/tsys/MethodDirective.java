package xyz.cofe.delphi.tsys;

import xyz.cofe.delphi.ast.ClassMethod;
import xyz.cofe.delphi.ast.Expression;

public sealed interface MethodDirective {
    record Reintroduce() implements MethodDirective {}
    record Overload() implements MethodDirective {}
    record Message(String expression) implements MethodDirective {}
    record Static() implements MethodDirective {}
    record Dynamic() implements MethodDirective {}
    record Override() implements MethodDirective {}
    record Virtual() implements MethodDirective {}
    record Abstract() implements MethodDirective {}
    record Final() implements MethodDirective {}
    record Inline() implements MethodDirective {}
    record Assembler() implements MethodDirective {}
    record Cdecl() implements MethodDirective {}
    record Pascal() implements MethodDirective {}
    record Register() implements MethodDirective {}
    record SafeCall() implements MethodDirective {}
    record StdCall() implements MethodDirective {}
    record Export() implements MethodDirective {}
    record Far() implements MethodDirective {}
    record Local() implements MethodDirective {}
    record Near() implements MethodDirective {}
    record DispID(String expression) implements MethodDirective {}

    public static MethodDirective of(ClassMethod.MethodDirective dir) {
        if( dir instanceof ClassMethod.MethodDirective.Reintroduce )return new Reintroduce();
        if( dir instanceof ClassMethod.MethodDirective.Overload )return new Overload();
        if( dir instanceof ClassMethod.MethodDirective.Binding.Message m )return new Message(m.expression().text());
        if( dir instanceof ClassMethod.MethodDirective.Binding.Static )return new Static();
        if( dir instanceof ClassMethod.MethodDirective.Binding.Dynamic )return new Dynamic();
        if( dir instanceof ClassMethod.MethodDirective.Binding.Override )return new Override();
        if( dir instanceof ClassMethod.MethodDirective.Binding.Virtual )return new Virtual();
        if( dir instanceof ClassMethod.MethodDirective.Abstract )return new Abstract();
        if( dir instanceof ClassMethod.MethodDirective.Final )return new Final();
        if( dir instanceof ClassMethod.MethodDirective.Inline )return new Inline();
        if( dir instanceof ClassMethod.MethodDirective.Assembler )return new Assembler();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.Cdecl )return new Cdecl();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.Pascal )return new Pascal();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.Register )return new Register();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.SafeCall )return new SafeCall();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.StdCall )return new StdCall();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.Export )return new Export();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.Far )return new Far();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.Local )return new Local();
        if( dir instanceof ClassMethod.MethodDirective.CallConvention.Near )return new Near();
        if( dir instanceof ClassMethod.MethodDirective.DispID d )return new DispID(d.expression().text());
        return null;
    }
}
