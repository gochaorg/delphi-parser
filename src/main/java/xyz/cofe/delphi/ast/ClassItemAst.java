package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Элемент класса
 */
public sealed interface ClassItemAst
    permits
    ClassFieldAst,
    ClassItemAst.ClassVarSection,
    ClassMethodAst,
    ClassPropertyAst,
    ConstSectionAst.Constants,
    TypeSectionAst,
    VisibilityAst {
    /**
     * Секция переменных
     *
     * @param classFlag некий флаг 'class' - хз зачкем, наверно static // TODO выяснить
     * @param variables список переменных
     */
    record ClassVarSection(boolean classFlag, VarSectionAst.Variables variables)
        implements ClassItemAst {
    }

    static ImList<ClassItemAst> of(DelphiParser.ClassItemContext ctx) {
        if (ctx.visibility() != null && !ctx.visibility().isEmpty())
            return ImListLinked.of(VisibilityAst.of(ctx.visibility()));

        if (ctx.classMethod() != null && !ctx.classMethod().isEmpty())
            return ImListLinked.of(ClassMethodAst.of(ctx.classMethod()));

        if (ctx.classField() != null && !ctx.classField().isEmpty())
            return ClassFieldAst.of(ctx.classField()).map(i -> i);

        if (ctx.classProperty() != null && !ctx.classProperty().isEmpty())
            return ImListLinked.of(ClassPropertyAst.Property.of(ctx.classProperty()));

        if (ctx.constSection() != null && !ctx.constSection().isEmpty())
            return ImListLinked.of(ConstSectionAst.Constants.of(ctx.constSection()));

        if (ctx.typeSection() != null && !ctx.typeSection().isEmpty()) {
            return ImListLinked.of(TypeSectionAst.of(ctx.typeSection()));
        }

        if (ctx.varSection() != null && !ctx.varSection().isEmpty()) {
            var classFlag = ctx.CLASS() != null;

            return ImListLinked.of(
                new ClassVarSection(classFlag, VarSectionAst.Variables.of(ctx.varSection()))
            );
        }

        throw AstParseError.unExpected(ctx);
    }
}
