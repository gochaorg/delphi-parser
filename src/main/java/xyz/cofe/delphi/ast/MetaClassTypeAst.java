package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import static xyz.cofe.delphi.ast.impl.Ident.identifier;

/**
 * Метакласс: <code>class of <i>class_name</i></code>
 *
 * @param type ссылка на тип
 */
public record MetaClassTypeAst(ImList<String> type, SourcePosition position) implements TypeDeclAst, SrcPos {
    @Override
    public TypeDeclAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    public static MetaClassTypeAst of(DelphiParser.ClassTypeTypeDeclContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        if( ctx.typeId()==null || ctx.typeId().isEmpty() )throw AstParseError.unExpected(ctx);
        return new MetaClassTypeAst(
            identifier(ctx.typeId()),
            SourcePosition.of(ctx)
        );
    }
}
