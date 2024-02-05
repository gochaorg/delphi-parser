package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * Атомарное значение
 */
sealed public interface AtomAst {
    /**
     * Целое число
     *
     * @param value    декодированное значение
     * @param position позиция в исходниках
     */
    record IntNumber(
        long value,
        SourcePosition position
    ) implements AtomAst,
                 SrcPos {
    }

    /**
     * Плавающее число
     *
     * @param value    декодированное значение
     * @param position позиция в исходниках
     */
    record FloatNumber(
        double value,
        SourcePosition position
    ) implements AtomAst,
                 SrcPos {
    }

    /**
     * Текстовая строка
     *
     * @param value    декодированное значение
     * @param position позиция в исходниках
     */
    record StringValue(
        String value,
        SourcePosition position
    ) implements AtomAst,
                 SrcPos {

        private static final String hex = "0123456789abcdefABCDEF";
        private static final String dec = "0123456789";

        private static boolean isDigit(String src,int off,int base){
            if( off>=src.length() )return false;
            if( off<0 )return false;
            char c = src.charAt(off);
            if( base==16 ){
                return hex.indexOf(c) >= 0;
            }
            return dec.indexOf(c) >= 0;
        }
        private static boolean isQuote(String src,int off){
            if( off>=src.length() )return false;
            if( off<0 )return false;
            char c = src.charAt(off);
            return c == '\'';
        }

        public static Optional<String> decode(String src){
            if( src==null ) throw new IllegalArgumentException("src==null");

            StringBuilder sb = new StringBuilder();
            StringBuilder digits = new StringBuilder();

            String state = "";

            for( var i=0;i<src.length();i++ ){
                char chr = src.charAt(i);
                switch (state) {
                    case "" -> {
                        switch (chr) {
                            case '#' -> {
                                state = "#";
                                digits.setLength(0);
                            }
                            case '\'' -> {
                                state = "str";
                            }
                            case ' ' | '\n' | '\r' | '\t' -> {}
                            default -> state = "err";
                        }
                    }
                    case "#" -> {
                        switch (chr) {
                            case '$' -> {
                                state = "#$";
                            }
                            case '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' -> {
                                state = "#d";
                                digits.append(chr);
                            }
                        }
                    }
                    case "#$" -> {
                        if( isDigit(src,i,16)) {
                            digits.append(chr);
                            if( !isDigit(src,i+1,16)){
                                state = "";
                                int num = Integer.parseInt(digits.toString(),16);
                                sb.append((char) num);
                            }
                        }else {
                            state = "err";
                        }
                    }
                    case "#d" -> {
                        if( isDigit(src,i,10)){
                            digits.append(chr);
                            if( !isDigit(src,i+1,10)){
                                state = "";
                                int num = Integer.parseInt(digits.toString(),10);
                                sb.append((char) num);
                            }
                        }else{
                            state = "err";
                        }
                    }
                    case "str" -> {
                        switch (chr) {
                            case '\'' -> {
                                if( isQuote(src,i+1) ) {
                                    state = "qstr";
                                }else{
                                    state = "";
                                }
                            }
                            default -> {
                                sb.append(chr);
                            }
                        }
                    }
                    case "qstr" -> {
                        sb.append("'");
                        state = "str";
                    }
                    case "err" -> {}
                }
            }

            return
                state == "err" ?
                    Optional.empty() :
                Optional.of(sb.toString());
        }

        public static StringValue of(DelphiParser.StringFactorContext ctx){
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");

            var str = decode(ctx.getText());
            throw AstParseError.notImplemented(ctx);
        }
    }

    /**
     * Значение унаследованное, определяется контекстом
     *
     * @param position позиция в исходниках
     */
    record InheritedContext(SourcePosition position) implements AtomAst,
                                                                SrcPos {
    }

    /**
     * @param id
     * @param position
     */
    record InheritedIdent(String id, SourcePosition position) implements AtomAst,
                                                                         SrcPos {
    }

    /**
     * Ссылка на идентификатор/переменную/метод
     *
     * @param id
     * @param position
     */
    record IdentRef(String id, SourcePosition position) implements AtomAst,
                                                                   SrcPos {
    }

    /**
     * NULL ссылка
     *
     * @param position позиция в исходниках
     */
    record NilValue(SourcePosition position) implements AtomAst,
                                                        SrcPos {
    }

    /**
     * множество
     */
    sealed interface DelphiSet extends SrcPos,
                                       AtomAst {
        record One(ExpressionAst expression, SourcePosition position) implements DelphiSet {
        }

        record FromTo(ExpressionAst from, ExpressionAst to, SourcePosition position) implements DelphiSet {
        }

        record Sequence(ImList<DelphiSet> items, SourcePosition position) implements DelphiSet {
        }
    }

}
