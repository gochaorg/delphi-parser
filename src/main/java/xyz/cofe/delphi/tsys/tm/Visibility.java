package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.ast.VisibilityAst;

/**
 * Видимость члена класса
 */
public enum Visibility {
    StrictProtected,
    Protected,
    StrictPrivate,
    Private,
    Public,
    Published,
    Automated;

    public static Visibility of(VisibilityAst visibilityAst){
        if( visibilityAst==null ) throw new IllegalArgumentException("visibilityAst==null");
        return switch(visibilityAst){
            case Public -> Public;
            case Private -> Private;
            case Automated -> Automated;
            case Protected -> Protected;
            case Published -> Published;
            case StrictPrivate -> Private;
            case StrictProtected -> StrictProtected;
        };
    }
}
