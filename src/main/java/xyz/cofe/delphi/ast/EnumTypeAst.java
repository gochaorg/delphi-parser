package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Tuple2;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Перечисляемый тип
 * <p>
 *
 * Пример: <code>type TAccess = <b>(Deny, View, All, None)</b>;</code>
 *
 * @param entries элементы перечисления
 * @param position позиция в исходниках
 */
public record EnumTypeAst(ImList<EnumEntry> entries, SourcePosition position) implements SrcPos, TypeDeclAst {
    @Override
    public TypeDeclAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    public sealed interface EnumEntry {
        /**
         * Именная константа, без указанного значения <code>type TAccess = (<b>Deny</b>, View, All, None)</code>
         *
         * @param name Просто именованный элемент
         */
        record Named( String name ) implements EnumEntry {
            public static Optional<Named> parse(DelphiParser.ConstExpressionContext cexp){
                if( cexp==null )throw new IllegalArgumentException("cexp==null");
                return identOf(cexp).map(Named::new);
            }
        }

        /**
         * Именная константа, с указанием значения <code>type TAccess = (<b>Deny=1</b>, View='abc', All, None)</code>
         *
         * @param name Имя константы
         * @param number число ассоциированное с константой
         */
        record Numeric( String name, int number ) implements EnumEntry {
            public static Optional<Numeric> parse(DelphiParser.ConstExpressionContext cexp){
                if( cexp==null )throw new IllegalArgumentException("cexp==null");
                if( cexp.expression()==null )return Optional.empty();
                return compareEqualsExp(cexp.expression())
                    .flatMap( leftRight -> identOf(leftRight._1())
                        .flatMap(ident -> intValueOf(leftRight._2())
                            .map(num -> new Numeric(ident,num))));
            }
        }

        public static Optional<? extends EnumEntry> parse(DelphiParser.ConstExpressionContext cexp){
            if( cexp==null )throw new IllegalArgumentException("cexp==null");

            Optional<? extends EnumEntry> named = Named.parse(cexp);
            if(named.isPresent())return named;

            return Numeric.parse(cexp);
        }
    }

    public static Optional<EnumTypeAst> parse( DelphiParser.SubRangeTypeContext subRangeType ){
        if( subRangeType==null )throw new IllegalArgumentException("subRangeType==null");
        if( subRangeType.constExpression()==null )return Optional.empty();
        if( subRangeType.constExpression().size()!=1 )return Optional.empty();

        var constExpLst = subRangeType.constExpression().get(0);
        if( constExpLst.constExpression()==null )return Optional.empty();

        var lst = new ArrayList<EnumEntry>();
        for( var cexp : constExpLst.constExpression() ){
            var enOpt = EnumEntry.parse(cexp);
            if( enOpt.isEmpty() )return Optional.empty();

            lst.add(enOpt.get());
        }

        return Optional.of(new EnumTypeAst(ImList.of(lst), SourcePosition.of(subRangeType) ));
    }

    //TODO Будет вынесено в отдельный класс/файл
    //#region частные случаи выражений (expression)
    private static Optional<Integer> intValueOf(DelphiParser.ExpressionContext exp){
        if( exp==null )throw new IllegalArgumentException("exp==null");
        if( exp.simpleExpression()==null )return Optional.empty();
        if( exp.simpleExpression().relOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp().unaryOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp().unaryOp().atom()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp().unaryOp().atom().intNum()==null )return Optional.empty();
        var intNum = exp.simpleExpression().relOp().sumOp().mulOp().unaryOp().atom().intNum();
        if( intNum.TkIntNum()==null && intNum.TkHexNum()==null )return Optional.empty();
        if( intNum.TkIntNum()!=null ){
            return Optional.of(Integer.parseInt(intNum.TkIntNum().getText()));
        }
        if( intNum.TkHexNum()!=null ){
            return Optional.of(Integer.parseInt(intNum.TkHexNum().getText().replace("$",""),16));
        }
        return Optional.empty();
    }

    private static Optional<String> strValueOf(DelphiParser.ExpressionContext exp){
        if( exp==null )throw new IllegalArgumentException("exp==null");
        if( exp.simpleExpression()==null )return Optional.empty();
        if( exp.simpleExpression().relOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp().unaryOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp().unaryOp().atom()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().sumOp().mulOp().unaryOp().atom().stringFactor()==null )return Optional.empty();
        var str = exp.simpleExpression().relOp().sumOp().mulOp().unaryOp().atom().stringFactor();
        return Optional.of(str.getText());
    }

    private static Optional<Tuple2<DelphiParser.SumOpContext,DelphiParser.ExpressionContext>> compareEqualsExp(DelphiParser.ExpressionContext exp){
        if( exp==null )throw new IllegalArgumentException("exp==null");
        if( exp.simpleExpression()==null )return Optional.empty();
        if( exp.simpleExpression().relOp()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().op==null )return Optional.empty();
        if( exp.simpleExpression().relOp().op.getText()==null )return Optional.empty();
        if( exp.simpleExpression().relOp().op.getText().equals("=") )return Optional.empty();
        if( exp.simpleExpression().relOp().left==null )return Optional.empty();
        if( exp.simpleExpression().relOp().right==null )return Optional.empty();
        return Optional.of(
            Tuple2.of(
                exp.simpleExpression().relOp().left,
                exp.simpleExpression().relOp().right
            )
        );
    }

    private static Optional<String> identOf(DelphiParser.SumOpContext sumOp){
        if( sumOp==null )throw new IllegalArgumentException("sumOp==null");
        if( sumOp.mulOp()==null )return Optional.empty();
        if( sumOp.mulOp().left==null )return Optional.empty();
        if( sumOp.mulOp().left.atom()==null )return Optional.empty();
        if( sumOp.mulOp().left.atom().identInAtom()==null )return Optional.empty();
        if( sumOp.mulOp().left.atom().identInAtom().getText()==null )return Optional.empty();
        return Optional.of(sumOp.mulOp().left.atom().identInAtom().getText());
    }

    private static Optional<String> identOf(DelphiParser.ConstExpressionContext cexp){
        if( cexp==null )throw new IllegalArgumentException("cexp==null");
        if( cexp.expression()==null )return Optional.empty();
        if( cexp.expression().simpleExpression()==null )return Optional.empty();
        if( cexp.expression().simpleExpression().relOp()==null )return Optional.empty();
        if( cexp.expression().simpleExpression().relOp().sumOp()==null )return Optional.empty();
        return identOf(cexp.expression().simpleExpression().relOp().sumOp());
    }
    //#endregion
}

