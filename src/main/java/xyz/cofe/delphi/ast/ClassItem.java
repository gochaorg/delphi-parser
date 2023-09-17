package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Элемент класса
 */
public sealed interface ClassItem
    permits ClassField, ClassItem.ClassVarSection, ClassMethod, ClassProperty, ConstSection.Constants, TypeSection, Visibility
{
    /**
     * Секция переменных
     * @param classFlag некий флаг 'class' - хз зачкем, наверно static // TODO выяснить
     * @param variables список переменных
     */
    record ClassVarSection(boolean classFlag, VarSection.Variables variables)
    implements ClassItem {
    }

    static ImList<ClassItem,?> of(DelphiParser.ClassItemContext ctx){
        if( ctx.visibility()!=null && !ctx.visibility().isEmpty() )
            return ImListLinked.of(Visibility.of(ctx.visibility()));

        if(ctx.classMethod()!=null && !ctx.classMethod().isEmpty())
            return ImListLinked.of(ClassMethod.of(ctx.classMethod()));

        if(ctx.classField()!=null && !ctx.classField().isEmpty())
            return ClassField.of(ctx.classField()).map(i -> (ClassItem) i);

        if(ctx.classProperty()!=null && !ctx.classProperty().isEmpty())
            return ImListLinked.of(ClassProperty.Property.of(ctx.classProperty()));

        if(ctx.constSection()!=null && !ctx.constSection().isEmpty())
            return ImListLinked.of(ConstSection.Constants.of(ctx.constSection()));

        if(ctx.typeSection()!=null && !ctx.typeSection().isEmpty()){
            return ImListLinked.of(TypeSection.of(ctx.typeSection()));
        }

        if(ctx.varSection()!=null && !ctx.varSection().isEmpty()){
            var classFlag = ctx.CLASS() != null;

            return ImListLinked.of(
                new ClassVarSection(classFlag, VarSection.Variables.of(ctx.varSection()))
            );
        }

        throw AstParseError.unExpected(ctx);
    }
}
