package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Объявление типа
 */
public sealed interface TypeDeclAst
    extends AstNode
    permits ArrayTypeAst,
            ClazzTypeAst,
            EnumTypeAst,
            FileTypeAst,
            InterfaceTypeAst,
            MetaClassAst,
            NewTypeIdAst,
            PointerTypeAst,
            ProcedureTypeAst,
            SetTypeAst,
            SimpleTypeAst,
            StringTypeAst,
            StructuredTypeAst,
            TypeAliasAst,
            TypeIdentAst,
            VariantTypeAst {
    @Override
    TypeDeclAst astUpdate(AstUpdate.UpdateContext ctx);

    public static TypeDeclAst of(DelphiParser.TypeDeclContext ctx) {
        if (ctx.strucType() != null
            && !ctx.strucType().isEmpty()
        ) return StructuredTypeAst.of(ctx.strucType());

        if (ctx.typeId() != null && !ctx.typeId().isEmpty()) {
            boolean typeFlag = ctx.TYPE() != null
                && ctx.TYPE().getText() != null
                && !ctx.TYPE().getText().isEmpty();

            ImList<String> namespace = ImListLinked.of();
            ImList<String> ident = ImListLinked.of();

            if (ctx.typeId().namespacedQualifiedIdent() != null
                && !ctx.typeId().namespacedQualifiedIdent().isEmpty()
                && ctx.typeId().namespacedQualifiedIdent().namespaceName() != null
                && !ctx.typeId().namespacedQualifiedIdent().namespaceName().isEmpty()) {
                namespace = ImListLinked.of(
                    ctx.typeId().namespacedQualifiedIdent().namespaceName().ident()
                ).map(RuleContext::getText);
            }

            if (ctx.typeId().namespacedQualifiedIdent() != null
                && ctx.typeId().namespacedQualifiedIdent().qualifiedIdent() != null
                && !ctx.typeId().namespacedQualifiedIdent().qualifiedIdent().isEmpty()
            ) {
                ident = ImListLinked.of(
                    ctx.typeId().namespacedQualifiedIdent().qualifiedIdent().ident()
                ).map(RuleContext::getText);
            }

            var name = ident.prepend(namespace);

            ImList<TypeDeclAst> genericValues = ImListLinked.of();
            if (ctx.genericPostfix() != null
                && !ctx.genericPostfix().isEmpty()
            ) {
                genericValues = ImListLinked
                    .of(ctx.genericPostfix().typeDecl())
                    .map(TypeDeclAst::of);
            }

            if (typeFlag) {
                return new NewTypeIdAst(
                    name,
                    genericValues,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
                );
            } else {
                if (genericValues.size() == 0) {
                    return new TypeIdentAst(
                        name,
                        ImListLinked.of()
                    );
                }

                return new TypeAliasAst(
                    name,
                    genericValues,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
                );
            }
        }

        if (ctx.variantType() != null && !ctx.variantType().isEmpty()) {
            return new VariantTypeAst();
        }

        if (ctx.stringType() != null && !ctx.stringType().isEmpty()) {
            return StringTypeAst.of(ctx.stringType());
        }

        if (ctx.procedureType() != null && !ctx.procedureType().isEmpty()) {
            return ProcedureTypeAst.of(ctx.procedureType());
        }

        if (ctx.simpleType() != null && ctx.simpleType().subRangeType() != null) {
            var enumTypeOpt = EnumTypeAst.parse(ctx.simpleType().subRangeType());
            if (enumTypeOpt.isPresent()) {
                return enumTypeOpt.get();
            }
        }

        if (ctx.pointerType() != null) {
            return PointerTypeAst.of(ctx.pointerType());
        }

        throw AstParseError.notImplemented(ctx);
    }
}
