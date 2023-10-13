package xyz.cofe.delphi.impl;

import xyz.cofe.coll.im.ImList;

public class Indent {
    /**
     * Форматирует исходную строку, добавляет отступы в начале каждой новой строки
     * @param indent отступ
     * @param source исходная строка
     * @return отформатированная строка
     */
    public static String indent(String indent,String source){
        if(indent==null)throw new IllegalArgumentException("indent==null");
        if(source==null)throw new IllegalArgumentException("source==null");
        var lines = source.split("\r?\n");
        var sb = new StringBuilder();
        var i = -1;
        for( var line : lines ){
            i++;
            sb.append(indent).append(line);
            if( i<(lines.length)-1 )sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Форматирует исходные данные:
     * <ul>
     *   <li> добавляет заголовок в начале
     *   <li> добавляет отступы в начале каждой новой строки
     * </ul>
     * Если исходные данные пусты - то возвращает пустую строку
     * @param title заголовок
     * @param source данные
     * @return отформатированное представление
     */
    public static String indent(String title, ImList<?> source){
        if( title==null ) throw new IllegalArgumentException("title==null");
        if( source==null ) throw new IllegalArgumentException("source==null");
        if( source.size()==0 )return "";

        StringBuilder sb = new StringBuilder();
//        sb.append("\n");
        sb.append("  ").append(title.trim()).append("\n");

        var i = new int[] { -1 };
        var cnt = source.size();

        source.each(s -> {
            i[0] = i[0] + 1;
            var ii = i[0];

            sb.append(indent("    ",s.toString()));
            //if( ii<(cnt-1) )
                sb.append("\n");
        });

        return sb.toString();
    }
}
