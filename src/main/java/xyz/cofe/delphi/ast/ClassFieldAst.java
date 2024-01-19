package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * Поле класса/интерфейса
 * @param name имя поля
 * @param type тип
 * @param position позиция в исходнике
 */
public record ClassFieldAst(String name, TypeDeclAst type, SourcePosition position, ImList<Comment> comments)
implements ClassItemAst, AstNode, SrcPos, Commented<ClassFieldAst>
{
    @Override
    public ClassFieldAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    @Override
    public ClassFieldAst withComments(ImList<Comment> comments) {
        return this;
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return ImListLinked.of(type);
    }

    static ImList<ClassFieldAst> of(DelphiParser.ClassFieldContext ctx){
        ImList<ClassFieldAst> result = ImListLinked.of();
        Optional<TypeDeclAst> type = Optional.empty();

        if(ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty()){
            type = Optional.of( TypeDeclAst.of(ctx.typeDecl()) );
        }

        if( ctx.identList()!=null
            && ctx.identList().ident()!=null
            && type.isPresent()
        ){
            var type_f = type.get();
            result = ImListLinked.of(ctx.identList().ident())
                .map(RuleContext::getText)
                .map(i -> new ClassFieldAst(i,type_f,SourcePosition.of(ctx),ImListLinked.of()));
        }
        return result;
    }
}
