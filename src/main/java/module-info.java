open module delphi.parser {
    exports xyz.cofe.delphi.parser;
    exports xyz.cofe.coll.im;

    requires java.base;
    requires java.logging;
    requires org.antlr.antlr4.runtime;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
}