package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Соглашения о вызовах
 */
public enum CallConvention {
    Cdecl,
    Pascal,
    Register,
    SafeCall,
    StdCall,
    Export,
    Far, // old
    Local, // old
    Near; // old

    public static CallConvention of(DelphiParser.CallConventionNoSemiContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        var txt = ctx.getText();
        if (txt.equalsIgnoreCase("cdecl")) return CallConvention.Cdecl;
        if (txt.equalsIgnoreCase("Pascal")) return CallConvention.Pascal;
        if (txt.equalsIgnoreCase("Register")) return CallConvention.Register;
        if (txt.equalsIgnoreCase("SafeCall")) return CallConvention.SafeCall;
        if (txt.equalsIgnoreCase("StdCall")) return CallConvention.StdCall;
        if (txt.equalsIgnoreCase("Export")) return CallConvention.Export;
        throw AstParseError.unExpected(ctx);
    }

    public static CallConvention of(DelphiParser.CallConventionContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        var txt = ctx.getText().toLowerCase();
        if (txt.startsWith("cdecl")) return CallConvention.Cdecl;
        if (txt.startsWith("pascal")) return CallConvention.Pascal;
        if (txt.startsWith("register")) return CallConvention.Register;
        if (txt.startsWith("safecall")) return CallConvention.SafeCall;
        if (txt.startsWith("stdcall")) return CallConvention.StdCall;
        if (txt.startsWith("export")) return CallConvention.Export;
        throw AstParseError.unExpected(ctx);
    }

    public static CallConvention of(DelphiParser.OldCallConventionDirectiveContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        var txt = ctx.getText().toLowerCase();
        if (txt.startsWith("far")) return CallConvention.Far;
        if (txt.startsWith("local")) return CallConvention.Local;
        if (txt.startsWith("near")) return CallConvention.Near;
        throw AstParseError.unExpected(ctx);
    }
}
