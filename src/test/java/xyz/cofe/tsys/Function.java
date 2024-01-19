package xyz.cofe.tsys;

import xyz.cofe.coll.im.ImList;

public record Function(ImList<Argument> arguments, Type result) {}
