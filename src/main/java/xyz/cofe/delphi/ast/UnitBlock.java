package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public record UnitBlock(
    ImList<StatementAst> actions,
    ImList<StatementAst> initActions,
    ImList<StatementAst> finalActions,
    SourcePosition position,
    ImList<Comment> comments
) implements WithComments<UnitBlock>
{
    public static UnitBlock of(DelphiParser.UnitBlockContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        if( ctx.unitInitialization()!=null && !ctx.unitInitialization().isEmpty() ){
            if( ctx.unitInitialization().unitFinalization()!=null && !ctx.unitInitialization().unitFinalization().isEmpty() ){
                return new UnitBlock(
                    ImList.of(),
                    ImList.of(ctx.unitInitialization().statementList().statement()).map(StatementAst::of),
                    ImList.of(ctx.unitInitialization().unitFinalization().statementList().statement()).map(StatementAst::of),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }
            return new UnitBlock(
                ImList.of(),
                ImList.of(ctx.unitInitialization().statementList().statement()).map(StatementAst::of),
                ImList.of(),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
        if( ctx.compoundStatement()!=null && !ctx.compoundStatement().isEmpty() ){
            return new UnitBlock(
                ctx.compoundStatement()!=null &&
                    ctx.compoundStatement().statementList()!=null &&
                    ctx.compoundStatement().statementList().statement()!=null
                ? ImList.of(ctx.compoundStatement().statementList().statement()).map(StatementAst::of)
                    : ImList.of(),
                ImList.of(),
                ImList.of(),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
        return new UnitBlock(
            ImList.of(),
            ImList.of(),
            ImList.of(),
            SourcePosition.of(ctx),
            ImList.of()
        );
    }
}
