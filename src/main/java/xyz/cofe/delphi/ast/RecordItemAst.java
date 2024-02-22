package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface RecordItemAst permits ClassMethodAst,
                                              ClassPropertyAst,
                                              ConstSectionAst.Constants,
                                              RecordFieldAst,
                                              RecordItemAst.Vars,
                                              RecordVariantSectionAst,
                                              TypeSectionAst,
                                              VisibilityAst {
    record Vars(boolean klass, VarSectionAst varSection, SourcePosition position, ImList<Comment> comments)
        implements RecordItemAst, Commented<Vars>, SrcPos
    {
        @Override
        public Vars withComments(ImList<Comment> comments) {
            if( comments==null )throw new IllegalArgumentException("comments==null");
            return new Vars(klass,varSection,position,comments);
        }
    }

    static ImList<RecordItemAst> of(DelphiParser.RecordItemContext ctx){
        if( ctx==null )throw new IllegalArgumentException("ctx==null");

        if( ctx.visibility()!=null && !ctx.visibility().isEmpty() )
            return ImList.of(VisibilityAst.of(ctx.visibility()));

        if( ctx.classMethod()!=null && !ctx.classMethod().isEmpty() )
            return ImList.of(ClassMethodAst.of(ctx.classMethod()));

        if( ctx.classProperty()!=null && !ctx.classProperty().isEmpty() )
            return ImList.of(ClassPropertyAst.Property.of(ctx.classProperty()));

        if( ctx.constSection()!=null && !ctx.constSection().isEmpty() )
            return ImList.of(ConstSectionAst.Constants.of(ctx.constSection()));

        if( ctx.typeSection()!=null && !ctx.typeSection().isEmpty() )
            return ImList.of(TypeSectionAst.of(ctx.typeSection()));

        if( ctx.recordField()!=null && !ctx.recordField().isEmpty() )
            return RecordFieldAst.of(ctx.recordField());

        if( ctx.varSection()!=null && !ctx.varSection().isEmpty() ){
            var klass = ctx.CLASS()!=null && !ctx.CLASS().getText().isEmpty();
            return ImList.of(
                new Vars(
                    klass,
                    VarSectionAst.Variables.of(ctx.varSection()),
                    SourcePosition.of(ctx),
                    ImList.of()
                )
            );
        }

        throw AstParseError.unExpected(ctx);
    }
}
