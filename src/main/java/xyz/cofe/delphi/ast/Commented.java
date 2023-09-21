package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;

import java.util.Optional;

public interface Commented<SELF> extends SrcPos {
    ImList<Comment,?> comments();

    SELF withComments(ImList<Comment,?> comments);
}
