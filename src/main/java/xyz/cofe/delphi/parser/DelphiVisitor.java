// Generated from /home/user/code/delphi/delphi-parser/src/main/resources/Delphi.g4 by ANTLR 4.13.1
package xyz.cofe.delphi.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DelphiParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DelphiVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DelphiParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(DelphiParser.FileContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#programHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramHead(DelphiParser.ProgramHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#programParmSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramParmSeq(DelphiParser.ProgramParmSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#library}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibrary(DelphiParser.LibraryContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#libraryHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryHead(DelphiParser.LibraryHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#packageE}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageE(DelphiParser.PackageEContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#packageHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageHead(DelphiParser.PackageHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit(DelphiParser.UnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unitHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitHead(DelphiParser.UnitHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unitInterface}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitInterface(DelphiParser.UnitInterfaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unitImplementation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitImplementation(DelphiParser.UnitImplementationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unitBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitBlock(DelphiParser.UnitBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unitInitialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitInitialization(DelphiParser.UnitInitializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unitFinalization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitFinalization(DelphiParser.UnitFinalizationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#containsClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainsClause(DelphiParser.ContainsClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#requiresClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequiresClause(DelphiParser.RequiresClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#usesClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsesClause(DelphiParser.UsesClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#usesFileClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsesFileClause(DelphiParser.UsesFileClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#namespaceFileNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceFileNameList(DelphiParser.NamespaceFileNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#namespaceFileName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceFileName(DelphiParser.NamespaceFileNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#namespaceNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceNameList(DelphiParser.NamespaceNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(DelphiParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#blockBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockBody(DelphiParser.BlockBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#declSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclSection(DelphiParser.DeclSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDecl(DelphiParser.InterfaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#labelDeclSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelDeclSection(DelphiParser.LabelDeclSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#constSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstSection(DelphiParser.ConstSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#constKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstKey(DelphiParser.ConstKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#constDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDeclaration(DelphiParser.ConstDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#typeSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSection(DelphiParser.TypeSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(DelphiParser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#varSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSection(DelphiParser.VarSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#varKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarKey(DelphiParser.VarKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(DelphiParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#varValueSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarValueSpec(DelphiParser.VarValueSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#exportsSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportsSection(DelphiParser.ExportsSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#exportItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportItem(DelphiParser.ExportItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#typeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDecl(DelphiParser.TypeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#strucType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrucType(DelphiParser.StrucTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#strucTypePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrucTypePart(DelphiParser.StrucTypePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(DelphiParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#arrayIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndex(DelphiParser.ArrayIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#arraySubType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArraySubType(DelphiParser.ArraySubTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#setType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetType(DelphiParser.SetTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#fileType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileType(DelphiParser.FileTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#pointerType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerType(DelphiParser.PointerTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#stringType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringType(DelphiParser.StringTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#codePageNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCodePageNumber(DelphiParser.CodePageNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procedureType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureType(DelphiParser.ProcedureTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodType(DelphiParser.MethodTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#simpleProcedureType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleProcedureType(DelphiParser.SimpleProcedureTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procedureReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureReference(DelphiParser.ProcedureReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procedureTypeHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureTypeHeading(DelphiParser.ProcedureTypeHeadingContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#variantType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantType(DelphiParser.VariantTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#simpleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleType(DelphiParser.SimpleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#subRangeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubRangeType(DelphiParser.SubRangeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#enumType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumType(DelphiParser.EnumTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#typeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeId(DelphiParser.TypeIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#genericTypeIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericTypeIdent(DelphiParser.GenericTypeIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#genericDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericDefinition(DelphiParser.GenericDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#simpleGenericDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleGenericDefinition(DelphiParser.SimpleGenericDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#constrainedGenericDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstrainedGenericDefinition(DelphiParser.ConstrainedGenericDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#constrainedGeneric}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstrainedGeneric(DelphiParser.ConstrainedGenericContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#genericConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericConstraint(DelphiParser.GenericConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#genericPostfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericPostfix(DelphiParser.GenericPostfixContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDecl(DelphiParser.ClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classTypeTypeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassTypeTypeDecl(DelphiParser.ClassTypeTypeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classTypeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassTypeDecl(DelphiParser.ClassTypeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classState}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassState(DelphiParser.ClassStateContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classParent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassParent(DelphiParser.ClassParentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassItem(DelphiParser.ClassItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classHelperDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassHelperDecl(DelphiParser.ClassHelperDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classHelperItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassHelperItem(DelphiParser.ClassHelperItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceTypeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceTypeDecl(DelphiParser.InterfaceTypeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceKey(DelphiParser.InterfaceKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceGuid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceGuid(DelphiParser.InterfaceGuidContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#interfaceItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceItem(DelphiParser.InterfaceItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#objectDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectDecl(DelphiParser.ObjectDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#objectItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectItem(DelphiParser.ObjectItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordDecl(DelphiParser.RecordDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#simpleRecord}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleRecord(DelphiParser.SimpleRecordContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#variantRecord}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantRecord(DelphiParser.VariantRecordContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordItem(DelphiParser.RecordItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordField(DelphiParser.RecordFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordVariantField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordVariantField(DelphiParser.RecordVariantFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordVariantSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordVariantSection(DelphiParser.RecordVariantSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordVariant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordVariant(DelphiParser.RecordVariantContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordHelperDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordHelperDecl(DelphiParser.RecordHelperDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordHelperItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordHelperItem(DelphiParser.RecordHelperItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classMethod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassMethod(DelphiParser.ClassMethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#oleClassMethodAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOleClassMethodAlias(DelphiParser.OleClassMethodAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassField(DelphiParser.ClassFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassProperty(DelphiParser.ClassPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classPropSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPropSpec(DelphiParser.ClassPropSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classPropPostfixSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPropPostfixSpec(DelphiParser.ClassPropPostfixSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classPropDispSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPropDispSpec(DelphiParser.ClassPropDispSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classPropertyName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPropertyName(DelphiParser.ClassPropertyNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#classPropertyArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassPropertyArray(DelphiParser.ClassPropertyArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibility(DelphiParser.VisibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#exportedProcHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportedProcHeading(DelphiParser.ExportedProcHeadingContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDecl(DelphiParser.MethodDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodDeclHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclHeading(DelphiParser.MethodDeclHeadingContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodKey(DelphiParser.MethodKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodName(DelphiParser.MethodNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcDecl(DelphiParser.ProcDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procDeclHeading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcDeclHeading(DelphiParser.ProcDeclHeadingContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#formalParameterSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterSection(DelphiParser.FormalParameterSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#formalParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterList(DelphiParser.FormalParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#formalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameter(DelphiParser.FormalParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#parmType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParmType(DelphiParser.ParmTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodBody(DelphiParser.MethodBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#procBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcBody(DelphiParser.ProcBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#customAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomAttribute(DelphiParser.CustomAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#customAttributeDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomAttributeDecl(DelphiParser.CustomAttributeDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#customAttributeNamedCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomAttributeNamedCall(DelphiParser.CustomAttributeNamedCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#anonymousExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnonymousExpression(DelphiParser.AnonymousExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#simpleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpression(DelphiParser.SimpleExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#relOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelOp(DelphiParser.RelOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#sumOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSumOp(DelphiParser.SumOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#mulOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulOp(DelphiParser.MulOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(DelphiParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(DelphiParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#identInAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentInAtom(DelphiParser.IdentInAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#setExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetExpression(DelphiParser.SetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#postfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfix(DelphiParser.PostfixContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#deref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeref(DelphiParser.DerefContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#calling}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalling(DelphiParser.CallingContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#callParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallParam(DelphiParser.CallParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#namedPassParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedPassParam(DelphiParser.NamedPassParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#paramName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamName(DelphiParser.ParamNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#unnamedPassParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnnamedPassParam(DelphiParser.UnnamedPassParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#arrayIndexAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndexAccess(DelphiParser.ArrayIndexAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#fieldAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldAccess(DelphiParser.FieldAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#genericCallTypeParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericCallTypeParams(DelphiParser.GenericCallTypeParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#preDefinedValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreDefinedValues(DelphiParser.PreDefinedValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#stringFactor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringFactor(DelphiParser.StringFactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#setSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetSection(DelphiParser.SetSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(DelphiParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(DelphiParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(DelphiParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#caseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseStatement(DelphiParser.CaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#caseItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseItem(DelphiParser.CaseItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#caseLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseLabel(DelphiParser.CaseLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#repeatStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatStatement(DelphiParser.RepeatStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(DelphiParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(DelphiParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#withStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithStatement(DelphiParser.WithStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#withItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithItem(DelphiParser.WithItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(DelphiParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(DelphiParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStatement(DelphiParser.SimpleStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#gotoStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGotoStatement(DelphiParser.GotoStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#constExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExpression(DelphiParser.ConstExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#recordConstExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordConstExpression(DelphiParser.RecordConstExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#tryStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryStatement(DelphiParser.TryStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#handlerList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerList(DelphiParser.HandlerListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#handler}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandler(DelphiParser.HandlerContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#handlerIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerIdent(DelphiParser.HandlerIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#handlerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerStatement(DelphiParser.HandlerStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#raiseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaiseStatement(DelphiParser.RaiseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#assemblerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssemblerStatement(DelphiParser.AssemblerStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#methodDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDirective(DelphiParser.MethodDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#functionDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDirective(DelphiParser.FunctionDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#funcDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDirective(DelphiParser.FuncDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#reintroduceDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReintroduceDirective(DelphiParser.ReintroduceDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#overloadDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverloadDirective(DelphiParser.OverloadDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#bindingDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBindingDirective(DelphiParser.BindingDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#abstractDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstractDirective(DelphiParser.AbstractDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#inlineDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineDirective(DelphiParser.InlineDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#callConvention}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallConvention(DelphiParser.CallConventionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#callConventionNoSemi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallConventionNoSemi(DelphiParser.CallConventionNoSemiContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#oldCallConventionDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOldCallConventionDirective(DelphiParser.OldCallConventionDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#hintingDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHintingDirective(DelphiParser.HintingDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#externalDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalDirective(DelphiParser.ExternalDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#externalSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalSpecifier(DelphiParser.ExternalSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#dispIDDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDispIDDirective(DelphiParser.DispIDDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(DelphiParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#usedKeywordsAsNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsedKeywordsAsNames(DelphiParser.UsedKeywordsAsNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#identList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentList(DelphiParser.IdentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#identListFlat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentListFlat(DelphiParser.IdentListFlatContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(DelphiParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#intNum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntNum(DelphiParser.IntNumContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#realNum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealNum(DelphiParser.RealNumContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#namespacedQualifiedIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespacedQualifiedIdent(DelphiParser.NamespacedQualifiedIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#namespaceName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceName(DelphiParser.NamespaceNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link DelphiParser#qualifiedIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdent(DelphiParser.QualifiedIdentContext ctx);
}