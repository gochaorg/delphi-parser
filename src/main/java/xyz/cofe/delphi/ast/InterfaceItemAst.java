package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;


public sealed interface InterfaceItemAst
permits ClassPropertyAst, ClassMethodAst
{
    static InterfaceItemAst of(DelphiParser.InterfaceItemContext ctx){
        if( ctx.classMethod()!=null && !ctx.classMethod().isEmpty() ){
            return ClassMethodAst.of(ctx.classMethod());
        }

        if( ctx.classProperty()!=null && !ctx.classProperty().isEmpty() ){
            var classFlag = ctx.CLASS()!=null
                && ctx.CLASS().getText()!=null
                && ctx.CLASS().getText().length()>0;

            var prop = ClassPropertyAst.Property.of(ctx.classProperty());
            if( classFlag ){
                prop = prop.withClassFlag(true);
            }

            return prop;
        }

        throw AstParseError.unExpected(ctx);
    }
}
