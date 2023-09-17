package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Уровень видимости
 */
public enum Visibility implements ClassItem {
    StrictProtected,
    Protected,
    StrictPrivate,
    Private,
    Public,
    Published,
    Automated;

    static Visibility of(DelphiParser.VisibilityContext ctx){
        if(ctx.STRICT()!=null ){
            if( ctx.PROTECTED()!=null ) return Visibility.StrictProtected;
            if( ctx.PRIVATE()!=null ) return Visibility.StrictPrivate;

            throw AstParseError.unExpected(ctx);
        }else{
            if( ctx.PROTECTED()!=null ) return Visibility.Protected;
            if( ctx.PRIVATE()!=null ) return Visibility.Private;
            if( ctx.PUBLIC()!=null ) return Visibility.Public;
            if( ctx.PUBLIC()!=null ) return Visibility.Public;
            if( ctx.PUBLISHED()!=null ) return Visibility.Published;
            if( ctx.AUTOMATED()!=null ) return Visibility.Automated;

            throw AstParseError.unExpected(ctx);
        }
    }
}
