package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;

/**
 * Идентификатор типа.
 * <p>
 * <br/>
 * Флаг typeFlag - объявляет новый тип, а не синоним типа.
 *
 * <a href="https://docwiki.embarcadero.com/RADStudio/Alexandria/en/Type_Compatibility_and_Identity_(Delphi)">
 * Подробности тут
 * </a>
 * <p>
 * <br/>
 * <b>Type Identity</b>
 * <br/>
 * <p>
 * When one type identifier is declared using another type identifier,
 * without qualification, they denote the same type. Thus, given the declarations:
 *
 * <pre>
 * type
 *   T1 = Integer;
 *   T2 = T1;
 *   T3 = Integer;
 *   T4 = T2;
 * </pre>
 * <p>
 * T1, T2, T3, T4, and Integer all denote the same type.
 * To create distinct types, repeat the word type in the declaration. For example:
 *
 * <pre>
 *     type TMyInteger = type Integer;
 * </pre>
 *
 * @param name          имя типа
 * @param genericParams параметры типа
 */
public record NewTypeIdAst(
    ImList<String> name,
    ImList<TypeDeclAst> genericParams,
    SourcePosition position,
    ImList<Comment> comments
) implements TypeDeclAst, SrcPos, Commented<NewTypeIdAst> {
    @Override
    public NewTypeIdAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    @Override
    public NewTypeIdAst withComments(ImList<Comment> comments) {
        return this;
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return genericParams;
    }
}
