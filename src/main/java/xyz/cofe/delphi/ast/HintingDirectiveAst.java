package xyz.cofe.delphi.ast;

public sealed interface HintingDirectiveAst {
    record Deprecated() implements HintingDirectiveAst {}
    record Experimental() implements HintingDirectiveAst {}
    record Platform() implements HintingDirectiveAst {}
    record Library() implements HintingDirectiveAst {}
}
