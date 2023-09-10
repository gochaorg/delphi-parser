package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public record ClassField(String name, TypeDecl type)
implements ClassItem
{
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
                .map(i -> new ClassField(i,type_f));
        }
        return result;
    }
}
