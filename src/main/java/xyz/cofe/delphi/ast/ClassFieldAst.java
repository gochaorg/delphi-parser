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
public record ClassFieldAst(String name, TypeDeclAst type, SourcePosition position, ImList<Comment> comments,
                            ImList<CustomAttributeAst> attributes)
implements ClassItemAst, AstNode, SrcPos, Commented<ClassFieldAst>
{
    @Override
    public ClassFieldAst astUpdate(AstUpdate.UpdateContext ctx) {
        var cmts = this;
        if( ctx instanceof AstUpdate.CommentingContext cc ){
            cmts = cc.commenting(cmts);
        }

        var type = this.type.astUpdate(ctx);
        var attrs = ctx.update(attributes);

        if( cmts.comments==comments && type==this.type && attrs.isEmpty() )
            return this;

        return new ClassFieldAst(name, type, position, cmts.comments, attrs.orElse(attributes));
    }

    @Override
    public ClassFieldAst withComments(ImList<Comment> comments) {
        return new ClassFieldAst(
            name,
            type,
            position,
            comments,
            attributes
        );
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return ImList.<AstNode>of(type).append(attributes);
    }

    static ImList<ClassFieldAst> of(DelphiParser.ClassFieldContext ctx){
        ImList<ClassFieldAst> result = ImListLinked.of();
        Optional<TypeDeclAst> type = Optional.empty();

        if(ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty()){
            type = Optional.of( TypeDeclAst.of(ctx.typeDecl()) );
        }

        if( ctx.identList()!=null
            && ctx.identList().paramName()!=null
            && type.isPresent()
        ){
            var type_f = type.get();
            result = ImListLinked.of(ctx.identList().paramName())
                .map(RuleContext::getText)
                .map(i -> new ClassFieldAst(
                    i,type_f,SourcePosition.of(ctx),ImListLinked.of(),
                    ctx.customAttribute()!=null && !ctx.customAttribute().isEmpty() ?
                        ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) :
                        ImList.of()
                ));
        }
        return result;
    }
}
