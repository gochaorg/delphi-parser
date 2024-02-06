package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Уровень видимости
 */
public enum VisibilityAst implements ClassItemAst, RecordItemAst {
    StrictProtected,
    Protected,
    StrictPrivate,
    Private,
    Public,
    Published,
    Automated;

    static VisibilityAst of(DelphiParser.VisibilityContext ctx){
        if(ctx.STRICT()!=null ){
            if( ctx.PROTECTED()!=null ) return VisibilityAst.StrictProtected;
            if( ctx.PRIVATE()!=null ) return VisibilityAst.StrictPrivate;

            throw AstParseError.unExpected(ctx);
        }else{
            if( ctx.PROTECTED()!=null ) return VisibilityAst.Protected;
            if( ctx.PRIVATE()!=null ) return VisibilityAst.Private;
            if( ctx.PUBLIC()!=null ) return VisibilityAst.Public;
            if( ctx.PUBLIC()!=null ) return VisibilityAst.Public;
            if( ctx.PUBLISHED()!=null ) return VisibilityAst.Published;
            if( ctx.AUTOMATED()!=null ) return VisibilityAst.Automated;

            throw AstParseError.unExpected(ctx);
        }
    }

    @Override
    public VisibilityAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
