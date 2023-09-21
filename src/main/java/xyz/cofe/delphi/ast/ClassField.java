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
public record ClassField(String name, TypeDecl type,SourcePosition position,ImList<Comment,?> comments)
implements ClassItem, AstNode, SrcPos, Commented<ClassField>
{
    @Override
    public ClassField astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    @Override
    public ClassField withComments(ImList<Comment, ?> comments) {
        return this;
    }

    @Override
    public ImList<? extends AstNode, ?> nestedAstNodes() {
        return ImListLinked.of(type);
    }

    static ImList<ClassField,?> of(DelphiParser.ClassFieldContext ctx){
        ImList<ClassField,?> result = ImListLinked.of();
        Optional<TypeDecl> type = Optional.empty();

        if(ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty()){
            type = Optional.of( TypeDecl.of(ctx.typeDecl()) );
        }

        if( ctx.identList()!=null
            && ctx.identList().ident()!=null
            && type.isPresent()
        ){
            var type_f = type.get();
            result = ImListLinked.of(ctx.identList().ident())
                .map(RuleContext::getText)
                .map(i -> new ClassField(i,type_f,SourcePosition.of(ctx),ImListLinked.of()));
        }
        return result;
    }
}
