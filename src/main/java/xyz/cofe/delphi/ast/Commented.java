package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;

import java.util.Optional;

/**
 * Поддержка комментариев к узлу AST
 * @param <SELF> собственный тип
 */
public interface Commented<SELF> extends SrcPos {
    ImList<Comment> comments();

    SELF withComments(ImList<Comment> comments);
}
