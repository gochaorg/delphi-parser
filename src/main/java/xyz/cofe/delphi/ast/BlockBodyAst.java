package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface BlockBodyAst permits StatementAst.Assembler,
                                             StatementAst.Compound {
    static BlockBodyAst of(DelphiParser.BlockBodyContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        if( ctx.compoundStatement()!=null && !ctx.compoundStatement().isEmpty() ){
            return StatementAst.Compound.of(ctx.compoundStatement());
        }
        if( ctx.assemblerStatement()!=null && !ctx.assemblerStatement().isEmpty() ){
            return StatementAst.Assembler.of(ctx.assemblerStatement());
        }
        throw AstParseError.unExpected(ctx);
    }
}
