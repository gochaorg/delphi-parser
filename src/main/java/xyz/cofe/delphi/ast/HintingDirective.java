package xyz.cofe.delphi.ast;

public sealed interface HintingDirective {
    record Deprecated() implements HintingDirective {}
    record Experimental() implements HintingDirective {}
    record Platform() implements HintingDirective {}
    record Library() implements HintingDirective {}
}
