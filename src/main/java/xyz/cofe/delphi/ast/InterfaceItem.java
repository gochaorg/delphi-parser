package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;


public sealed interface InterfaceItem
extends AstNode
permits ClassProperty, ClassMethod
{
    static InterfaceItem of(DelphiParser.InterfaceItemContext ctx){
        if( ctx.classMethod()!=null && !ctx.classMethod().isEmpty() ){
            return ClassMethod.of(ctx.classMethod());
        }

        if( ctx.classProperty()!=null && !ctx.classProperty().isEmpty() ){
            var classFlag = ctx.CLASS()!=null
                && ctx.CLASS().getText()!=null
                && ctx.CLASS().getText().length()>0;

            var prop = ClassProperty.Property.of(ctx.classProperty());
            if( classFlag ){
                prop = prop.withClassFlag(true);
            }

            return prop;
        }

        throw AstParseError.unExpected(ctx);
    }
}
