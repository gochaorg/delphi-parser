package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.tree.ParseTree;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;
import xyz.cofe.text.Text;

import java.util.Optional;

public sealed interface StatementAst permits StatementAst.Assembler,
                                             StatementAst.Assignment,
                                             StatementAst.Break,
                                             StatementAst.Case,
                                             StatementAst.Compound,
                                             StatementAst.Continue,
                                             StatementAst.Exit,
                                             StatementAst.Expr,
                                             StatementAst.For,
                                             StatementAst.Goto,
                                             StatementAst.If,
                                             StatementAst.Label,
                                             StatementAst.Raise,
                                             StatementAst.Repeat,
                                             StatementAst.TryExcept,
                                             StatementAst.TryFinally,
                                             StatementAst.While,
                                             StatementAst.With {

    static StatementAst of(DelphiParser.StatementContext ctx) {
        if (ctx == null) throw new IllegalArgumentException(" ctx==null");

        if (ctx.ifStatement() != null && !ctx.ifStatement().isEmpty())
            return If.of(ctx.ifStatement());

        if (ctx.caseStatement() != null && !ctx.caseStatement().isEmpty())
            return Case.of(ctx.caseStatement());

        if (ctx.repeatStatement() != null && !ctx.repeatStatement().isEmpty())
            return Repeat.of(ctx.repeatStatement());

        if (ctx.whileStatement() != null && !ctx.whileStatement().isEmpty())
            return While.of(ctx.whileStatement());

        if (ctx.forStatement() != null && !ctx.forStatement().isEmpty())
            return For.of(ctx.forStatement());

        if (ctx.withStatement() != null && !ctx.withStatement().isEmpty())
            return With.of(ctx.withStatement());

        if (ctx.tryStatement() != null && !ctx.tryStatement().isEmpty()) {
            var ts = ctx.tryStatement();
            if (ts.final_ != null && ts.FINALLY() != null) {
                return new TryFinally(
                    ts.actions != null ? ImList.of(ts.actions.statement()).map(StatementAst::of) : ImList.of(),
                    ImList.of(ts.final_.statement()).map(StatementAst::of),
                    SourcePosition.of(ctx.tryStatement()),
                    ImList.of()
                );
            }

            if (ts.EXCEPT() != null && ts.except != null && ts.handlerList() != null) {
                var hl = ts.handlerList();
                var handlers = ImList.of(hl.handler()).map(TryExcept.Handler::of);
                var defActions = ImList.<StatementAst>of();
                if (hl.statementList() != null && !hl.statementList().isEmpty()) {
                    defActions = ImList.of(hl.statementList().statement()).map(StatementAst::of);
                }

                var acts = ts.actions != null ?
                    ImList.of(ts.actions.statement()).map(StatementAst::of) :
                    ImList.<StatementAst>of();

                return new TryExcept(
                    acts,
                    handlers,
                    defActions,
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            throw AstParseError.unExpected(ctx);
        }

        if (ctx.raiseStatement() != null && !ctx.raiseStatement().isEmpty()) {
            return Raise.of(ctx.raiseStatement());
        }

        if (ctx.assemblerStatement() != null && !ctx.assemblerStatement().isEmpty()) {
            return Assembler.of(ctx.assemblerStatement());
        }

        if (ctx.INHERITED() != null && !ctx.INHERITED().getText().isEmpty()) {
            if (ctx.simpleExpression() != null) {
                return new Expr.InheritedSimple(
                    SimpleExpressionAst.of(ctx.simpleExpression()),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            return new Expr.Inherited(
                SourcePosition.of(ctx),
                ImList.of()
            );
        }

        if (ctx.compoundStatement() != null && !ctx.compoundStatement().isEmpty()) {
            return Compound.of(ctx.compoundStatement());
        }

        if (ctx.gotoStatement() != null && !ctx.gotoStatement().isEmpty()) {
            var st = ctx.gotoStatement();

            if (st.GOTO() != null && st.label() != null) {
                return new Goto(st.label().getText(), SourcePosition.of(ctx), ImList.of());
            }

            if (st.EXIT() != null) {
                if (st.expression() != null && !st.expression().isEmpty()) {
                    return new Exit(
                        Optional.of(ExpressionAst.of(st.expression())),
                        SourcePosition.of(ctx),
                        ImList.of()
                    );
                }

                return new Exit(
                    Optional.empty(),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            if (st.BREAK() != null) {
                return new Break(SourcePosition.of(ctx), ImList.of());
            }

            if(st.CONTINUE()!=null){
                return new Continue(SourcePosition.of(ctx), ImList.of());
            }

            throw AstParseError.unExpected(ctx);
        }

        if( ctx.label()!=null && !ctx.label().isEmpty()
            && ctx.statement()!=null && !ctx.statement().isEmpty()
        ){
            return new Label(
                ctx.label().getText(),
                StatementAst.of(ctx.statement()),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }

        if( ctx.simpleStatement()!=null ){
            var st = ctx.simpleStatement();
            if( st.assignSource!=null ){
                if( st.INHERITED()!=null ){
                    return new Assignment(
                        SimpleExpressionAst.of(st.simpleExpression()),
                        new Expr.InheritedSimple(
                            ExpressionAst.of(st.assignSource),
                            SourcePosition.of(st.assignSource),
                            ImList.of()
                        ),
                        SourcePosition.of(ctx),
                        ImList.of()
                    );
                }

                return new Assignment(
                    SimpleExpressionAst.of(st.simpleExpression()),
                    ExpressionAst.of(st.assignSource),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            return new Expr.Simple(
                SimpleExpressionAst.of(st.simpleExpression()),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }

        throw AstParseError.unExpected(ctx);
    }

    //#region if
    record If(
        ExpressionAst predicate,
        Optional<StatementAst> positive,
        Optional<StatementAst> negative,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<If> {

        @SuppressWarnings("ConstantValue")
        public static If of(DelphiParser.IfStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.expression() == null || ctx.expression().isEmpty()) throw AstParseError.unExpected(ctx);
            if (ctx.positive != null && ctx.negative != null) {
                return new If(
                    ExpressionAst.of(ctx.expression()),
                    Optional.of(StatementAst.of(ctx.positive)),
                    Optional.of(StatementAst.of(ctx.negative)),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }
            if (ctx.positive != null && ctx.negative == null) {
                return new If(
                    ExpressionAst.of(ctx.expression()),
                    Optional.of(StatementAst.of(ctx.positive)),
                    Optional.empty(),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }
            if (ctx.positive == null && ctx.negative != null) {
                return new If(
                    ExpressionAst.of(ctx.expression()),
                    Optional.empty(),
                    Optional.of(StatementAst.of(ctx.negative)),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }
            return new If(
                ExpressionAst.of(ctx.expression()),
                Optional.empty(),
                Optional.empty(),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //#endregion
    //region case
    record Case(
        ExpressionAst what,
        ImList<Chance> chances,
        ImList<StatementAst> defaultAction,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Case> {
        //region key
        public sealed interface Key {
            sealed interface SeqPart extends Key {}

            record One(
                ExpressionAst expression,
                SourcePosition position,
                ImList<Comment> comments
            ) implements Key,
                         SeqPart,
                         WithComments<One> {}

            record Range(
                ExpressionAst first,
                ExpressionAst last,
                SourcePosition position,
                ImList<Comment> comments
            ) implements Key,
                         SeqPart,
                         WithComments<Range> {}

            record Sequence(
                ImList<SeqPart> elements,
                SourcePosition position,
                ImList<Comment> comments
            ) implements Key,
                         WithComments<Sequence> {}

            public static Key of(DelphiParser.CaseItemContext ctx) {
                if (ctx == null) throw new IllegalArgumentException("ctx==null");
                if (ctx.caseLabel() == null) throw AstParseError.unExpected(ctx);
                Key key = null;
                for (var lbl : ctx.caseLabel()) {
                    if (lbl.expression() == null
                        || lbl.expression().isEmpty()
                    ) throw AstParseError.unExpected(lbl);

                    SeqPart k2 = null;
                    if (lbl.expression().size() == 1) {
                        k2 = new One(
                            ExpressionAst.of(lbl.expression().get(0)),
                            SourcePosition.of(lbl),
                            ImList.of()
                        );
                    } else if (lbl.expression().size() == 2) {
                        k2 = new Range(
                            ExpressionAst.of(lbl.expression().get(0)),
                            ExpressionAst.of(lbl.expression().get(1)),
                            SourcePosition.of(lbl),
                            ImList.of()
                        );
                    } else {
                        throw AstParseError.unExpected(lbl);
                    }

                    if (key == null) {
                        key = k2;
                    } else if (key instanceof Sequence seq) {
                        key = new Sequence(
                            seq.elements().append(k2),
                            SourcePosition.of(ctx),
                            ImList.of()
                        );
                    } else //noinspection ConstantValue
                        if (key instanceof SeqPart sp) {
                            key = new Sequence(
                                ImList.of(sp, k2),
                                SourcePosition.of(ctx),
                                ImList.of()
                            );
                        }
                }
                return key;
            }
        }
        //endregion
        //region chance
        public record Chance(
            Key key,
            Optional<StatementAst> when,
            SourcePosition position,
            ImList<Comment> comments
        ) implements WithComments<Chance> {
            public static Chance of(DelphiParser.CaseItemContext ctx) {
                if (ctx == null) throw new IllegalArgumentException("ctx==null");
                var key = Key.of(ctx);

                if (ctx.statement() == null || ctx.statement().isEmpty()) {
                    return new Chance(
                        key,
                        Optional.empty(),
                        SourcePosition.of(ctx),
                        ImList.of()
                    );
                }

                return new Chance(
                    key,
                    Optional.of(StatementAst.of(ctx.statement())),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }
        }
        //endregion

        public static Case of(DelphiParser.CaseStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.expression() == null || ctx.expression().isEmpty()) throw AstParseError.unExpected(ctx);

            ImList<StatementAst> defActions = ImList.of();
            if (ctx.statementList() != null
                && !ctx.statementList().isEmpty()
                && ctx.statementList().statement() != null
            ) {
                defActions = ImList
                    .of(ctx.statementList().statement())
                    .map(StatementAst::of);
            }

            var chances = ImList.of(ctx.caseItem()).map(Chance::of);

            return new Case(
                ExpressionAst.of(ctx.expression()),
                chances,
                defActions,
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //endregion
    //region repeat
    record Repeat(
        ImList<StatementAst> actions,
        ExpressionAst predicate,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Repeat> {
        public static Repeat of(DelphiParser.RepeatStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.cond == null || ctx.cond.isEmpty()) throw AstParseError.unExpected(ctx);
            return new Repeat(
                ImList.of(ctx.statementList().statement()).map(StatementAst::of),
                ExpressionAst.of(ctx.cond),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //endregion
    //region while
    record While(
        ExpressionAst predicate,
        Optional<StatementAst> action,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<While> {
        public static While of(DelphiParser.WhileStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.cond == null || ctx.cond.isEmpty()) throw AstParseError.unExpected(ctx);
            if (ctx.statement() != null && !ctx.statement().isEmpty()) {
                return new While(
                    ExpressionAst.of(ctx.cond),
                    Optional.of(StatementAst.of(ctx.statement())),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }
            return new While(
                ExpressionAst.of(ctx.cond),
                Optional.empty(),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //endregion
    //region for
    sealed interface For extends StatementAst {
        record ForAscending(
            AtomAst variable,
            ExpressionAst from,
            ExpressionAst to,
            StatementAst action,
            SourcePosition position,
            ImList<Comment> comments
        ) implements For,
                     WithComments<ForAscending> {}

        record ForDescending(
            AtomAst variable,
            ExpressionAst from,
            ExpressionAst to,
            StatementAst action,
            SourcePosition position,
            ImList<Comment> comments
        ) implements For,
                     WithComments<ForDescending> {}

        record ForSource(
            AtomAst variable,
            ExpressionAst source,
            StatementAst action,
            SourcePosition position,
            ImList<Comment> comments
        ) implements For,
                     WithComments<ForSource> {}

        static For of(DelphiParser.ForStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.iterValue == null || ctx.iterValue.isEmpty()) throw AstParseError.unExpected(ctx);
            if (ctx.statement() == null || ctx.statement().isEmpty()) throw AstParseError.unExpected(ctx);

            if (ctx.start != null && !ctx.start.isEmpty()) {
                if (ctx.end == null || ctx.end.isEmpty()) throw AstParseError.unExpected(ctx);
                return new ForAscending(
                    AtomAst.of(ctx.iterValue),
                    ExpressionAst.of(ctx.start),
                    ExpressionAst.of(ctx.end),
                    StatementAst.of(ctx.statement()),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            if (ctx.startBig != null && !ctx.startBig.isEmpty()) {
                if (ctx.endSmall == null || ctx.endSmall.isEmpty()) throw AstParseError.unExpected(ctx);
                return new ForDescending(
                    AtomAst.of(ctx.iterValue),
                    ExpressionAst.of(ctx.startBig),
                    ExpressionAst.of(ctx.endSmall),
                    StatementAst.of(ctx.statement()),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            if (ctx.source != null && !ctx.source.isEmpty()) {
                return new ForSource(
                    AtomAst.of(ctx.iterValue),
                    ExpressionAst.of(ctx.source),
                    StatementAst.of(ctx.statement()),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            throw AstParseError.unExpected(ctx);
        }
    }
    //endregion
    //region with
    record With(
        Value value,
        StatementAst action,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<With> {
        public sealed interface Value {
            record ValueAlias(
                ExpressionAst expression,
                String ident,
                SourcePosition position
            ) implements Value {}
            record Values(
                ImList<ExpressionAst> expressions,
                SourcePosition position
            ) implements Value {}

            static Value of(DelphiParser.WithItemContext ctx) {
                if (ctx == null) throw new IllegalArgumentException("ctx==null");
                if (ctx.ident() != null && !ctx.ident().isEmpty() && ctx.expression().size() == 1) {
                    return new ValueAlias(
                        ExpressionAst.of(ctx.expression().get(0)),
                        ctx.ident().getText(),
                        SourcePosition.of(ctx)
                    );
                }
                if (!ctx.expression().isEmpty()) {
                    return new Values(
                        ImList.of(ctx.expression()).map(ExpressionAst::of),
                        SourcePosition.of(ctx)
                    );
                }
                throw AstParseError.unExpected(ctx);
            }
        }

        public static With of(DelphiParser.WithStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.withItem() == null) throw AstParseError.unExpected(ctx);
            if (ctx.statement() == null) throw AstParseError.unExpected(ctx);
            return new With(
                Value.of(ctx.withItem()),
                StatementAst.of(ctx.statement()),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //endregion
    //region try except
    record TryExcept(
        ImList<StatementAst> actions,
        ImList<Handler> handlers,
        ImList<StatementAst> defaultAction,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<TryExcept> {
        public record Handler(
            Optional<String> variable,
            TypeIdentAst type,
            Optional<StatementAst> action,
            SourcePosition position,
            ImList<Comment> comments
        ) implements WithComments<Handler> {
            public static Handler of(DelphiParser.HandlerContext ctx) {
                if (ctx == null) throw new IllegalArgumentException("ctx==null");
                if (ctx.typeId() == null || ctx.typeId().isEmpty()) throw AstParseError.unExpected(ctx);
                if (ctx.handlerStatement() == null || ctx.handlerStatement().isEmpty())
                    throw AstParseError.unExpected(ctx);

                var varName = Optional.<String>empty();
                if (ctx.handlerIdent() != null && !ctx.handlerIdent().isEmpty()) {
                    var name = ctx.handlerIdent().getText();
                    name = Text.trimEnd(name, ":");
                    varName = Optional.of(name);
                }

                var st = Optional.<StatementAst>empty();
                if (ctx.handlerStatement() != null
                    && ctx.handlerStatement().statement() != null
                    && !ctx.handlerStatement().statement().isEmpty()
                ) {
                    st = Optional.of(
                        StatementAst.of(ctx.handlerStatement().statement())
                    );
                }

                return new Handler(
                    varName,
                    TypeIdentAst.of(ctx.typeId()),
                    st,
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }
        }
    }

    record TryFinally(
        ImList<StatementAst> actions,
        ImList<StatementAst> finalAction,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<TryFinally> {
    }
    //endregion
    //region raise
    record Raise(
        Optional<ExpressionAst> expression,
        Optional<ExpressionAst> atExpression,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Raise> {
        public static Raise of(DelphiParser.RaiseStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            return new Raise(
                ctx.base != null ? Optional.of(ExpressionAst.of(ctx.base)) : Optional.empty(),
                ctx.atExp != null ? Optional.of(ExpressionAst.of(ctx.atExp)) : Optional.empty(),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //endregion
    //region assembler
    record Assembler(
        ImList<String> instructions,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 BlockBodyAst,
                 WithComments<Assembler> {
        public static Assembler of(DelphiParser.AssemblerStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            return new Assembler(
                ImList.of(ctx.END()).map(ParseTree::getText).filter(t -> !t.equalsIgnoreCase("end")),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //endregion

    sealed interface Expr extends StatementAst, ExpressionAst {
        record Simple(
            ExpressionAst expression,
            SourcePosition position,
            ImList<Comment> comments
        ) implements Expr {}

        record InheritedSimple(
            ExpressionAst expression,
            SourcePosition position,
            ImList<Comment> comments
        ) implements Expr {}

        record Inherited(
            SourcePosition position,
            ImList<Comment> comments
        ) implements Expr {}
    }

    //region Compound
    record Compound(
        ImList<StatementAst> actions,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 BlockBodyAst,
                 WithComments<Compound> {
        public static Compound of(DelphiParser.CompoundStatementContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            return new Compound(
                ImList.of(ctx.statementList().statement()).map(StatementAst::of),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
    }
    //endregion
    //region Goto
    record Goto(
        String label,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Goto> {}
    //endregion
    //region Exit
    record Exit(
        Optional<ExpressionAst> expression,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Exit> {}
    //endregion
    //region Break
    record Break(
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Break> {}
    //endregion
    //region Continue
    record Continue(
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Continue> {}
    //endregion
    //region Label
    record Label(
        String label,
        StatementAst statement,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Label> {}
    //endregion
    //region Assignment
    record Assignment(
        ExpressionAst target,
        ExpressionAst expression,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StatementAst,
                 WithComments<Assignment> {}
    //endregion
}
