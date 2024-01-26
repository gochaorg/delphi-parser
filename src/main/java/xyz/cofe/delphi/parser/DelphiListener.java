// Generated from C:/code/other/delphi-parser/src/main/resources/Delphi.g4 by ANTLR 4.13.1
package xyz.cofe.delphi.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DelphiParser}.
 */
public interface DelphiListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DelphiParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(DelphiParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(DelphiParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(DelphiParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#programHead}.
	 * @param ctx the parse tree
	 */
	void enterProgramHead(DelphiParser.ProgramHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#programHead}.
	 * @param ctx the parse tree
	 */
	void exitProgramHead(DelphiParser.ProgramHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#programParmSeq}.
	 * @param ctx the parse tree
	 */
	void enterProgramParmSeq(DelphiParser.ProgramParmSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#programParmSeq}.
	 * @param ctx the parse tree
	 */
	void exitProgramParmSeq(DelphiParser.ProgramParmSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#library}.
	 * @param ctx the parse tree
	 */
	void enterLibrary(DelphiParser.LibraryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#library}.
	 * @param ctx the parse tree
	 */
	void exitLibrary(DelphiParser.LibraryContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#libraryHead}.
	 * @param ctx the parse tree
	 */
	void enterLibraryHead(DelphiParser.LibraryHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#libraryHead}.
	 * @param ctx the parse tree
	 */
	void exitLibraryHead(DelphiParser.LibraryHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#packageE}.
	 * @param ctx the parse tree
	 */
	void enterPackageE(DelphiParser.PackageEContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#packageE}.
	 * @param ctx the parse tree
	 */
	void exitPackageE(DelphiParser.PackageEContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#packageHead}.
	 * @param ctx the parse tree
	 */
	void enterPackageHead(DelphiParser.PackageHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#packageHead}.
	 * @param ctx the parse tree
	 */
	void exitPackageHead(DelphiParser.PackageHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(DelphiParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(DelphiParser.UnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unitHead}.
	 * @param ctx the parse tree
	 */
	void enterUnitHead(DelphiParser.UnitHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unitHead}.
	 * @param ctx the parse tree
	 */
	void exitUnitHead(DelphiParser.UnitHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unitInterface}.
	 * @param ctx the parse tree
	 */
	void enterUnitInterface(DelphiParser.UnitInterfaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unitInterface}.
	 * @param ctx the parse tree
	 */
	void exitUnitInterface(DelphiParser.UnitInterfaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unitImplementation}.
	 * @param ctx the parse tree
	 */
	void enterUnitImplementation(DelphiParser.UnitImplementationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unitImplementation}.
	 * @param ctx the parse tree
	 */
	void exitUnitImplementation(DelphiParser.UnitImplementationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unitBlock}.
	 * @param ctx the parse tree
	 */
	void enterUnitBlock(DelphiParser.UnitBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unitBlock}.
	 * @param ctx the parse tree
	 */
	void exitUnitBlock(DelphiParser.UnitBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unitInitialization}.
	 * @param ctx the parse tree
	 */
	void enterUnitInitialization(DelphiParser.UnitInitializationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unitInitialization}.
	 * @param ctx the parse tree
	 */
	void exitUnitInitialization(DelphiParser.UnitInitializationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unitFinalization}.
	 * @param ctx the parse tree
	 */
	void enterUnitFinalization(DelphiParser.UnitFinalizationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unitFinalization}.
	 * @param ctx the parse tree
	 */
	void exitUnitFinalization(DelphiParser.UnitFinalizationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#containsClause}.
	 * @param ctx the parse tree
	 */
	void enterContainsClause(DelphiParser.ContainsClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#containsClause}.
	 * @param ctx the parse tree
	 */
	void exitContainsClause(DelphiParser.ContainsClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#requiresClause}.
	 * @param ctx the parse tree
	 */
	void enterRequiresClause(DelphiParser.RequiresClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#requiresClause}.
	 * @param ctx the parse tree
	 */
	void exitRequiresClause(DelphiParser.RequiresClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#usesClause}.
	 * @param ctx the parse tree
	 */
	void enterUsesClause(DelphiParser.UsesClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#usesClause}.
	 * @param ctx the parse tree
	 */
	void exitUsesClause(DelphiParser.UsesClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#usesFileClause}.
	 * @param ctx the parse tree
	 */
	void enterUsesFileClause(DelphiParser.UsesFileClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#usesFileClause}.
	 * @param ctx the parse tree
	 */
	void exitUsesFileClause(DelphiParser.UsesFileClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#namespaceFileNameList}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceFileNameList(DelphiParser.NamespaceFileNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#namespaceFileNameList}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceFileNameList(DelphiParser.NamespaceFileNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#namespaceFileName}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceFileName(DelphiParser.NamespaceFileNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#namespaceFileName}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceFileName(DelphiParser.NamespaceFileNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#namespaceNameList}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceNameList(DelphiParser.NamespaceNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#namespaceNameList}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceNameList(DelphiParser.NamespaceNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(DelphiParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(DelphiParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#blockBody}.
	 * @param ctx the parse tree
	 */
	void enterBlockBody(DelphiParser.BlockBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#blockBody}.
	 * @param ctx the parse tree
	 */
	void exitBlockBody(DelphiParser.BlockBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#declSection}.
	 * @param ctx the parse tree
	 */
	void enterDeclSection(DelphiParser.DeclSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#declSection}.
	 * @param ctx the parse tree
	 */
	void exitDeclSection(DelphiParser.DeclSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDecl(DelphiParser.InterfaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDecl(DelphiParser.InterfaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#labelDeclSection}.
	 * @param ctx the parse tree
	 */
	void enterLabelDeclSection(DelphiParser.LabelDeclSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#labelDeclSection}.
	 * @param ctx the parse tree
	 */
	void exitLabelDeclSection(DelphiParser.LabelDeclSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#constSection}.
	 * @param ctx the parse tree
	 */
	void enterConstSection(DelphiParser.ConstSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#constSection}.
	 * @param ctx the parse tree
	 */
	void exitConstSection(DelphiParser.ConstSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#constKey}.
	 * @param ctx the parse tree
	 */
	void enterConstKey(DelphiParser.ConstKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#constKey}.
	 * @param ctx the parse tree
	 */
	void exitConstKey(DelphiParser.ConstKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstDeclaration(DelphiParser.ConstDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstDeclaration(DelphiParser.ConstDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#typeSection}.
	 * @param ctx the parse tree
	 */
	void enterTypeSection(DelphiParser.TypeSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#typeSection}.
	 * @param ctx the parse tree
	 */
	void exitTypeSection(DelphiParser.TypeSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(DelphiParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(DelphiParser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#varSection}.
	 * @param ctx the parse tree
	 */
	void enterVarSection(DelphiParser.VarSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#varSection}.
	 * @param ctx the parse tree
	 */
	void exitVarSection(DelphiParser.VarSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#varKey}.
	 * @param ctx the parse tree
	 */
	void enterVarKey(DelphiParser.VarKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#varKey}.
	 * @param ctx the parse tree
	 */
	void exitVarKey(DelphiParser.VarKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(DelphiParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(DelphiParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#varValueSpec}.
	 * @param ctx the parse tree
	 */
	void enterVarValueSpec(DelphiParser.VarValueSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#varValueSpec}.
	 * @param ctx the parse tree
	 */
	void exitVarValueSpec(DelphiParser.VarValueSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#exportsSection}.
	 * @param ctx the parse tree
	 */
	void enterExportsSection(DelphiParser.ExportsSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#exportsSection}.
	 * @param ctx the parse tree
	 */
	void exitExportsSection(DelphiParser.ExportsSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#exportItem}.
	 * @param ctx the parse tree
	 */
	void enterExportItem(DelphiParser.ExportItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#exportItem}.
	 * @param ctx the parse tree
	 */
	void exitExportItem(DelphiParser.ExportItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#typeDecl}.
	 * @param ctx the parse tree
	 */
	void enterTypeDecl(DelphiParser.TypeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#typeDecl}.
	 * @param ctx the parse tree
	 */
	void exitTypeDecl(DelphiParser.TypeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#strucType}.
	 * @param ctx the parse tree
	 */
	void enterStrucType(DelphiParser.StrucTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#strucType}.
	 * @param ctx the parse tree
	 */
	void exitStrucType(DelphiParser.StrucTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#strucTypePart}.
	 * @param ctx the parse tree
	 */
	void enterStrucTypePart(DelphiParser.StrucTypePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#strucTypePart}.
	 * @param ctx the parse tree
	 */
	void exitStrucTypePart(DelphiParser.StrucTypePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(DelphiParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(DelphiParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndex(DelphiParser.ArrayIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndex(DelphiParser.ArrayIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#arraySubType}.
	 * @param ctx the parse tree
	 */
	void enterArraySubType(DelphiParser.ArraySubTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#arraySubType}.
	 * @param ctx the parse tree
	 */
	void exitArraySubType(DelphiParser.ArraySubTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#setType}.
	 * @param ctx the parse tree
	 */
	void enterSetType(DelphiParser.SetTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#setType}.
	 * @param ctx the parse tree
	 */
	void exitSetType(DelphiParser.SetTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#fileType}.
	 * @param ctx the parse tree
	 */
	void enterFileType(DelphiParser.FileTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#fileType}.
	 * @param ctx the parse tree
	 */
	void exitFileType(DelphiParser.FileTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#pointerType}.
	 * @param ctx the parse tree
	 */
	void enterPointerType(DelphiParser.PointerTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#pointerType}.
	 * @param ctx the parse tree
	 */
	void exitPointerType(DelphiParser.PointerTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#stringType}.
	 * @param ctx the parse tree
	 */
	void enterStringType(DelphiParser.StringTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#stringType}.
	 * @param ctx the parse tree
	 */
	void exitStringType(DelphiParser.StringTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#codePageNumber}.
	 * @param ctx the parse tree
	 */
	void enterCodePageNumber(DelphiParser.CodePageNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#codePageNumber}.
	 * @param ctx the parse tree
	 */
	void exitCodePageNumber(DelphiParser.CodePageNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#procedureType}.
	 * @param ctx the parse tree
	 */
	void enterProcedureType(DelphiParser.ProcedureTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#procedureType}.
	 * @param ctx the parse tree
	 */
	void exitProcedureType(DelphiParser.ProcedureTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodType}.
	 * @param ctx the parse tree
	 */
	void enterMethodType(DelphiParser.MethodTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodType}.
	 * @param ctx the parse tree
	 */
	void exitMethodType(DelphiParser.MethodTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#simpleProcedureType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleProcedureType(DelphiParser.SimpleProcedureTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#simpleProcedureType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleProcedureType(DelphiParser.SimpleProcedureTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#procedureReference}.
	 * @param ctx the parse tree
	 */
	void enterProcedureReference(DelphiParser.ProcedureReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#procedureReference}.
	 * @param ctx the parse tree
	 */
	void exitProcedureReference(DelphiParser.ProcedureReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#procedureTypeHeading}.
	 * @param ctx the parse tree
	 */
	void enterProcedureTypeHeading(DelphiParser.ProcedureTypeHeadingContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#procedureTypeHeading}.
	 * @param ctx the parse tree
	 */
	void exitProcedureTypeHeading(DelphiParser.ProcedureTypeHeadingContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#variantType}.
	 * @param ctx the parse tree
	 */
	void enterVariantType(DelphiParser.VariantTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#variantType}.
	 * @param ctx the parse tree
	 */
	void exitVariantType(DelphiParser.VariantTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#simpleType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleType(DelphiParser.SimpleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#simpleType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleType(DelphiParser.SimpleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#subRangeType}.
	 * @param ctx the parse tree
	 */
	void enterSubRangeType(DelphiParser.SubRangeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#subRangeType}.
	 * @param ctx the parse tree
	 */
	void exitSubRangeType(DelphiParser.SubRangeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#enumType}.
	 * @param ctx the parse tree
	 */
	void enterEnumType(DelphiParser.EnumTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#enumType}.
	 * @param ctx the parse tree
	 */
	void exitEnumType(DelphiParser.EnumTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#typeId}.
	 * @param ctx the parse tree
	 */
	void enterTypeId(DelphiParser.TypeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#typeId}.
	 * @param ctx the parse tree
	 */
	void exitTypeId(DelphiParser.TypeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#genericTypeIdent}.
	 * @param ctx the parse tree
	 */
	void enterGenericTypeIdent(DelphiParser.GenericTypeIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#genericTypeIdent}.
	 * @param ctx the parse tree
	 */
	void exitGenericTypeIdent(DelphiParser.GenericTypeIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#genericDefinition}.
	 * @param ctx the parse tree
	 */
	void enterGenericDefinition(DelphiParser.GenericDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#genericDefinition}.
	 * @param ctx the parse tree
	 */
	void exitGenericDefinition(DelphiParser.GenericDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#simpleGenericDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSimpleGenericDefinition(DelphiParser.SimpleGenericDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#simpleGenericDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSimpleGenericDefinition(DelphiParser.SimpleGenericDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#constrainedGenericDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstrainedGenericDefinition(DelphiParser.ConstrainedGenericDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#constrainedGenericDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstrainedGenericDefinition(DelphiParser.ConstrainedGenericDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#constrainedGeneric}.
	 * @param ctx the parse tree
	 */
	void enterConstrainedGeneric(DelphiParser.ConstrainedGenericContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#constrainedGeneric}.
	 * @param ctx the parse tree
	 */
	void exitConstrainedGeneric(DelphiParser.ConstrainedGenericContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#genericConstraint}.
	 * @param ctx the parse tree
	 */
	void enterGenericConstraint(DelphiParser.GenericConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#genericConstraint}.
	 * @param ctx the parse tree
	 */
	void exitGenericConstraint(DelphiParser.GenericConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#genericPostfix}.
	 * @param ctx the parse tree
	 */
	void enterGenericPostfix(DelphiParser.GenericPostfixContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#genericPostfix}.
	 * @param ctx the parse tree
	 */
	void exitGenericPostfix(DelphiParser.GenericPostfixContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void enterClassDecl(DelphiParser.ClassDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void exitClassDecl(DelphiParser.ClassDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classTypeTypeDecl}.
	 * @param ctx the parse tree
	 */
	void enterClassTypeTypeDecl(DelphiParser.ClassTypeTypeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classTypeTypeDecl}.
	 * @param ctx the parse tree
	 */
	void exitClassTypeTypeDecl(DelphiParser.ClassTypeTypeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classTypeDecl}.
	 * @param ctx the parse tree
	 */
	void enterClassTypeDecl(DelphiParser.ClassTypeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classTypeDecl}.
	 * @param ctx the parse tree
	 */
	void exitClassTypeDecl(DelphiParser.ClassTypeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classState}.
	 * @param ctx the parse tree
	 */
	void enterClassState(DelphiParser.ClassStateContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classState}.
	 * @param ctx the parse tree
	 */
	void exitClassState(DelphiParser.ClassStateContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classParent}.
	 * @param ctx the parse tree
	 */
	void enterClassParent(DelphiParser.ClassParentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classParent}.
	 * @param ctx the parse tree
	 */
	void exitClassParent(DelphiParser.ClassParentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classItem}.
	 * @param ctx the parse tree
	 */
	void enterClassItem(DelphiParser.ClassItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classItem}.
	 * @param ctx the parse tree
	 */
	void exitClassItem(DelphiParser.ClassItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classHelperDecl}.
	 * @param ctx the parse tree
	 */
	void enterClassHelperDecl(DelphiParser.ClassHelperDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classHelperDecl}.
	 * @param ctx the parse tree
	 */
	void exitClassHelperDecl(DelphiParser.ClassHelperDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classHelperItem}.
	 * @param ctx the parse tree
	 */
	void enterClassHelperItem(DelphiParser.ClassHelperItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classHelperItem}.
	 * @param ctx the parse tree
	 */
	void exitClassHelperItem(DelphiParser.ClassHelperItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceTypeDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceTypeDecl(DelphiParser.InterfaceTypeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceTypeDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceTypeDecl(DelphiParser.InterfaceTypeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceKey}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceKey(DelphiParser.InterfaceKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceKey}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceKey(DelphiParser.InterfaceKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceGuid}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceGuid(DelphiParser.InterfaceGuidContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceGuid}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceGuid(DelphiParser.InterfaceGuidContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#interfaceItem}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceItem(DelphiParser.InterfaceItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#interfaceItem}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceItem(DelphiParser.InterfaceItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#objectDecl}.
	 * @param ctx the parse tree
	 */
	void enterObjectDecl(DelphiParser.ObjectDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#objectDecl}.
	 * @param ctx the parse tree
	 */
	void exitObjectDecl(DelphiParser.ObjectDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#objectItem}.
	 * @param ctx the parse tree
	 */
	void enterObjectItem(DelphiParser.ObjectItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#objectItem}.
	 * @param ctx the parse tree
	 */
	void exitObjectItem(DelphiParser.ObjectItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordDecl}.
	 * @param ctx the parse tree
	 */
	void enterRecordDecl(DelphiParser.RecordDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordDecl}.
	 * @param ctx the parse tree
	 */
	void exitRecordDecl(DelphiParser.RecordDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#simpleRecord}.
	 * @param ctx the parse tree
	 */
	void enterSimpleRecord(DelphiParser.SimpleRecordContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#simpleRecord}.
	 * @param ctx the parse tree
	 */
	void exitSimpleRecord(DelphiParser.SimpleRecordContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#variantRecord}.
	 * @param ctx the parse tree
	 */
	void enterVariantRecord(DelphiParser.VariantRecordContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#variantRecord}.
	 * @param ctx the parse tree
	 */
	void exitVariantRecord(DelphiParser.VariantRecordContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordItem}.
	 * @param ctx the parse tree
	 */
	void enterRecordItem(DelphiParser.RecordItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordItem}.
	 * @param ctx the parse tree
	 */
	void exitRecordItem(DelphiParser.RecordItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordField}.
	 * @param ctx the parse tree
	 */
	void enterRecordField(DelphiParser.RecordFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordField}.
	 * @param ctx the parse tree
	 */
	void exitRecordField(DelphiParser.RecordFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordVariantField}.
	 * @param ctx the parse tree
	 */
	void enterRecordVariantField(DelphiParser.RecordVariantFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordVariantField}.
	 * @param ctx the parse tree
	 */
	void exitRecordVariantField(DelphiParser.RecordVariantFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordVariantSection}.
	 * @param ctx the parse tree
	 */
	void enterRecordVariantSection(DelphiParser.RecordVariantSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordVariantSection}.
	 * @param ctx the parse tree
	 */
	void exitRecordVariantSection(DelphiParser.RecordVariantSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordVariant}.
	 * @param ctx the parse tree
	 */
	void enterRecordVariant(DelphiParser.RecordVariantContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordVariant}.
	 * @param ctx the parse tree
	 */
	void exitRecordVariant(DelphiParser.RecordVariantContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordHelperDecl}.
	 * @param ctx the parse tree
	 */
	void enterRecordHelperDecl(DelphiParser.RecordHelperDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordHelperDecl}.
	 * @param ctx the parse tree
	 */
	void exitRecordHelperDecl(DelphiParser.RecordHelperDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordHelperItem}.
	 * @param ctx the parse tree
	 */
	void enterRecordHelperItem(DelphiParser.RecordHelperItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordHelperItem}.
	 * @param ctx the parse tree
	 */
	void exitRecordHelperItem(DelphiParser.RecordHelperItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classMethod}.
	 * @param ctx the parse tree
	 */
	void enterClassMethod(DelphiParser.ClassMethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classMethod}.
	 * @param ctx the parse tree
	 */
	void exitClassMethod(DelphiParser.ClassMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#oleClassMethodAlias}.
	 * @param ctx the parse tree
	 */
	void enterOleClassMethodAlias(DelphiParser.OleClassMethodAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#oleClassMethodAlias}.
	 * @param ctx the parse tree
	 */
	void exitOleClassMethodAlias(DelphiParser.OleClassMethodAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classField}.
	 * @param ctx the parse tree
	 */
	void enterClassField(DelphiParser.ClassFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classField}.
	 * @param ctx the parse tree
	 */
	void exitClassField(DelphiParser.ClassFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classProperty}.
	 * @param ctx the parse tree
	 */
	void enterClassProperty(DelphiParser.ClassPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classProperty}.
	 * @param ctx the parse tree
	 */
	void exitClassProperty(DelphiParser.ClassPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropSpec}.
	 * @param ctx the parse tree
	 */
	void enterClassPropSpec(DelphiParser.ClassPropSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropSpec}.
	 * @param ctx the parse tree
	 */
	void exitClassPropSpec(DelphiParser.ClassPropSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropPostfixSpec}.
	 * @param ctx the parse tree
	 */
	void enterClassPropPostfixSpec(DelphiParser.ClassPropPostfixSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropPostfixSpec}.
	 * @param ctx the parse tree
	 */
	void exitClassPropPostfixSpec(DelphiParser.ClassPropPostfixSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropDispSpec}.
	 * @param ctx the parse tree
	 */
	void enterClassPropDispSpec(DelphiParser.ClassPropDispSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropDispSpec}.
	 * @param ctx the parse tree
	 */
	void exitClassPropDispSpec(DelphiParser.ClassPropDispSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropertyName}.
	 * @param ctx the parse tree
	 */
	void enterClassPropertyName(DelphiParser.ClassPropertyNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropertyName}.
	 * @param ctx the parse tree
	 */
	void exitClassPropertyName(DelphiParser.ClassPropertyNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropertyArray}.
	 * @param ctx the parse tree
	 */
	void enterClassPropertyArray(DelphiParser.ClassPropertyArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropertyArray}.
	 * @param ctx the parse tree
	 */
	void exitClassPropertyArray(DelphiParser.ClassPropertyArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropertyIndex}.
	 * @param ctx the parse tree
	 */
	void enterClassPropertyIndex(DelphiParser.ClassPropertyIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropertyIndex}.
	 * @param ctx the parse tree
	 */
	void exitClassPropertyIndex(DelphiParser.ClassPropertyIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropertySpecifier}.
	 * @param ctx the parse tree
	 */
	void enterClassPropertySpecifier(DelphiParser.ClassPropertySpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropertySpecifier}.
	 * @param ctx the parse tree
	 */
	void exitClassPropertySpecifier(DelphiParser.ClassPropertySpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropertyEndSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterClassPropertyEndSpecifier(DelphiParser.ClassPropertyEndSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropertyEndSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitClassPropertyEndSpecifier(DelphiParser.ClassPropertyEndSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropertyReadWrite}.
	 * @param ctx the parse tree
	 */
	void enterClassPropertyReadWrite(DelphiParser.ClassPropertyReadWriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropertyReadWrite}.
	 * @param ctx the parse tree
	 */
	void exitClassPropertyReadWrite(DelphiParser.ClassPropertyReadWriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#classPropertyDispInterface}.
	 * @param ctx the parse tree
	 */
	void enterClassPropertyDispInterface(DelphiParser.ClassPropertyDispInterfaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#classPropertyDispInterface}.
	 * @param ctx the parse tree
	 */
	void exitClassPropertyDispInterface(DelphiParser.ClassPropertyDispInterfaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#visibility}.
	 * @param ctx the parse tree
	 */
	void enterVisibility(DelphiParser.VisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#visibility}.
	 * @param ctx the parse tree
	 */
	void exitVisibility(DelphiParser.VisibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#exportedProcHeading}.
	 * @param ctx the parse tree
	 */
	void enterExportedProcHeading(DelphiParser.ExportedProcHeadingContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#exportedProcHeading}.
	 * @param ctx the parse tree
	 */
	void exitExportedProcHeading(DelphiParser.ExportedProcHeadingContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void enterMethodDecl(DelphiParser.MethodDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void exitMethodDecl(DelphiParser.MethodDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodDeclHeading}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclHeading(DelphiParser.MethodDeclHeadingContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodDeclHeading}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclHeading(DelphiParser.MethodDeclHeadingContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodKey}.
	 * @param ctx the parse tree
	 */
	void enterMethodKey(DelphiParser.MethodKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodKey}.
	 * @param ctx the parse tree
	 */
	void exitMethodKey(DelphiParser.MethodKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodName}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(DelphiParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodName}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(DelphiParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#procDecl}.
	 * @param ctx the parse tree
	 */
	void enterProcDecl(DelphiParser.ProcDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#procDecl}.
	 * @param ctx the parse tree
	 */
	void exitProcDecl(DelphiParser.ProcDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#procDeclHeading}.
	 * @param ctx the parse tree
	 */
	void enterProcDeclHeading(DelphiParser.ProcDeclHeadingContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#procDeclHeading}.
	 * @param ctx the parse tree
	 */
	void exitProcDeclHeading(DelphiParser.ProcDeclHeadingContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#formalParameterSection}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterSection(DelphiParser.FormalParameterSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#formalParameterSection}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterSection(DelphiParser.FormalParameterSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(DelphiParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(DelphiParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(DelphiParser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(DelphiParser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#parmType}.
	 * @param ctx the parse tree
	 */
	void enterParmType(DelphiParser.ParmTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#parmType}.
	 * @param ctx the parse tree
	 */
	void exitParmType(DelphiParser.ParmTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(DelphiParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(DelphiParser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#procBody}.
	 * @param ctx the parse tree
	 */
	void enterProcBody(DelphiParser.ProcBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#procBody}.
	 * @param ctx the parse tree
	 */
	void exitProcBody(DelphiParser.ProcBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#customAttribute}.
	 * @param ctx the parse tree
	 */
	void enterCustomAttribute(DelphiParser.CustomAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#customAttribute}.
	 * @param ctx the parse tree
	 */
	void exitCustomAttribute(DelphiParser.CustomAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#customAttributeList}.
	 * @param ctx the parse tree
	 */
	void enterCustomAttributeList(DelphiParser.CustomAttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#customAttributeList}.
	 * @param ctx the parse tree
	 */
	void exitCustomAttributeList(DelphiParser.CustomAttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#customAttributeDecl}.
	 * @param ctx the parse tree
	 */
	void enterCustomAttributeDecl(DelphiParser.CustomAttributeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#customAttributeDecl}.
	 * @param ctx the parse tree
	 */
	void exitCustomAttributeDecl(DelphiParser.CustomAttributeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(DelphiParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#anonymousExpression}.
	 * @param ctx the parse tree
	 */
	void enterAnonymousExpression(DelphiParser.AnonymousExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#anonymousExpression}.
	 * @param ctx the parse tree
	 */
	void exitAnonymousExpression(DelphiParser.AnonymousExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpression(DelphiParser.SimpleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpression(DelphiParser.SimpleExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#relOp}.
	 * @param ctx the parse tree
	 */
	void enterRelOp(DelphiParser.RelOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#relOp}.
	 * @param ctx the parse tree
	 */
	void exitRelOp(DelphiParser.RelOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#sumOp}.
	 * @param ctx the parse tree
	 */
	void enterSumOp(DelphiParser.SumOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#sumOp}.
	 * @param ctx the parse tree
	 */
	void exitSumOp(DelphiParser.SumOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#mulOp}.
	 * @param ctx the parse tree
	 */
	void enterMulOp(DelphiParser.MulOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#mulOp}.
	 * @param ctx the parse tree
	 */
	void exitMulOp(DelphiParser.MulOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(DelphiParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(DelphiParser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(DelphiParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(DelphiParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#identInAtom}.
	 * @param ctx the parse tree
	 */
	void enterIdentInAtom(DelphiParser.IdentInAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#identInAtom}.
	 * @param ctx the parse tree
	 */
	void exitIdentInAtom(DelphiParser.IdentInAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#setExpression}.
	 * @param ctx the parse tree
	 */
	void enterSetExpression(DelphiParser.SetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#setExpression}.
	 * @param ctx the parse tree
	 */
	void exitSetExpression(DelphiParser.SetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#postfix}.
	 * @param ctx the parse tree
	 */
	void enterPostfix(DelphiParser.PostfixContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#postfix}.
	 * @param ctx the parse tree
	 */
	void exitPostfix(DelphiParser.PostfixContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#deref}.
	 * @param ctx the parse tree
	 */
	void enterDeref(DelphiParser.DerefContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#deref}.
	 * @param ctx the parse tree
	 */
	void exitDeref(DelphiParser.DerefContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#calling}.
	 * @param ctx the parse tree
	 */
	void enterCalling(DelphiParser.CallingContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#calling}.
	 * @param ctx the parse tree
	 */
	void exitCalling(DelphiParser.CallingContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#callParam}.
	 * @param ctx the parse tree
	 */
	void enterCallParam(DelphiParser.CallParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#callParam}.
	 * @param ctx the parse tree
	 */
	void exitCallParam(DelphiParser.CallParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#namedPassParam}.
	 * @param ctx the parse tree
	 */
	void enterNamedPassParam(DelphiParser.NamedPassParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#namedPassParam}.
	 * @param ctx the parse tree
	 */
	void exitNamedPassParam(DelphiParser.NamedPassParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#paramName}.
	 * @param ctx the parse tree
	 */
	void enterParamName(DelphiParser.ParamNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#paramName}.
	 * @param ctx the parse tree
	 */
	void exitParamName(DelphiParser.ParamNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#unnamedPassParam}.
	 * @param ctx the parse tree
	 */
	void enterUnnamedPassParam(DelphiParser.UnnamedPassParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#unnamedPassParam}.
	 * @param ctx the parse tree
	 */
	void exitUnnamedPassParam(DelphiParser.UnnamedPassParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#arrayIndexAccess}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndexAccess(DelphiParser.ArrayIndexAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#arrayIndexAccess}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndexAccess(DelphiParser.ArrayIndexAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess(DelphiParser.FieldAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#fieldAccess}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess(DelphiParser.FieldAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#genericCallTypeParams}.
	 * @param ctx the parse tree
	 */
	void enterGenericCallTypeParams(DelphiParser.GenericCallTypeParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#genericCallTypeParams}.
	 * @param ctx the parse tree
	 */
	void exitGenericCallTypeParams(DelphiParser.GenericCallTypeParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#preDefinedValues}.
	 * @param ctx the parse tree
	 */
	void enterPreDefinedValues(DelphiParser.PreDefinedValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#preDefinedValues}.
	 * @param ctx the parse tree
	 */
	void exitPreDefinedValues(DelphiParser.PreDefinedValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#stringFactor}.
	 * @param ctx the parse tree
	 */
	void enterStringFactor(DelphiParser.StringFactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#stringFactor}.
	 * @param ctx the parse tree
	 */
	void exitStringFactor(DelphiParser.StringFactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#setSection}.
	 * @param ctx the parse tree
	 */
	void enterSetSection(DelphiParser.SetSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#setSection}.
	 * @param ctx the parse tree
	 */
	void exitSetSection(DelphiParser.SetSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(DelphiParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(DelphiParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(DelphiParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(DelphiParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(DelphiParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(DelphiParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(DelphiParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(DelphiParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#caseItem}.
	 * @param ctx the parse tree
	 */
	void enterCaseItem(DelphiParser.CaseItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#caseItem}.
	 * @param ctx the parse tree
	 */
	void exitCaseItem(DelphiParser.CaseItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#caseLabel}.
	 * @param ctx the parse tree
	 */
	void enterCaseLabel(DelphiParser.CaseLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#caseLabel}.
	 * @param ctx the parse tree
	 */
	void exitCaseLabel(DelphiParser.CaseLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(DelphiParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(DelphiParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(DelphiParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(DelphiParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(DelphiParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(DelphiParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#withStatement}.
	 * @param ctx the parse tree
	 */
	void enterWithStatement(DelphiParser.WithStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#withStatement}.
	 * @param ctx the parse tree
	 */
	void exitWithStatement(DelphiParser.WithStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#withItem}.
	 * @param ctx the parse tree
	 */
	void enterWithItem(DelphiParser.WithItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#withItem}.
	 * @param ctx the parse tree
	 */
	void exitWithItem(DelphiParser.WithItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(DelphiParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(DelphiParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(DelphiParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(DelphiParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStatement(DelphiParser.SimpleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStatement(DelphiParser.SimpleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#gotoStatement}.
	 * @param ctx the parse tree
	 */
	void enterGotoStatement(DelphiParser.GotoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#gotoStatement}.
	 * @param ctx the parse tree
	 */
	void exitGotoStatement(DelphiParser.GotoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#constExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstExpression(DelphiParser.ConstExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#constExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstExpression(DelphiParser.ConstExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#recordConstExpression}.
	 * @param ctx the parse tree
	 */
	void enterRecordConstExpression(DelphiParser.RecordConstExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#recordConstExpression}.
	 * @param ctx the parse tree
	 */
	void exitRecordConstExpression(DelphiParser.RecordConstExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#tryStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryStatement(DelphiParser.TryStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#tryStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryStatement(DelphiParser.TryStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#handlerList}.
	 * @param ctx the parse tree
	 */
	void enterHandlerList(DelphiParser.HandlerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#handlerList}.
	 * @param ctx the parse tree
	 */
	void exitHandlerList(DelphiParser.HandlerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#handler}.
	 * @param ctx the parse tree
	 */
	void enterHandler(DelphiParser.HandlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#handler}.
	 * @param ctx the parse tree
	 */
	void exitHandler(DelphiParser.HandlerContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#handlerIdent}.
	 * @param ctx the parse tree
	 */
	void enterHandlerIdent(DelphiParser.HandlerIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#handlerIdent}.
	 * @param ctx the parse tree
	 */
	void exitHandlerIdent(DelphiParser.HandlerIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerStatement(DelphiParser.HandlerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerStatement(DelphiParser.HandlerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#raiseStatement}.
	 * @param ctx the parse tree
	 */
	void enterRaiseStatement(DelphiParser.RaiseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#raiseStatement}.
	 * @param ctx the parse tree
	 */
	void exitRaiseStatement(DelphiParser.RaiseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#assemblerStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssemblerStatement(DelphiParser.AssemblerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#assemblerStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssemblerStatement(DelphiParser.AssemblerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#methodDirective}.
	 * @param ctx the parse tree
	 */
	void enterMethodDirective(DelphiParser.MethodDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#methodDirective}.
	 * @param ctx the parse tree
	 */
	void exitMethodDirective(DelphiParser.MethodDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#functionDirective}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDirective(DelphiParser.FunctionDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#functionDirective}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDirective(DelphiParser.FunctionDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#funcDirective}.
	 * @param ctx the parse tree
	 */
	void enterFuncDirective(DelphiParser.FuncDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#funcDirective}.
	 * @param ctx the parse tree
	 */
	void exitFuncDirective(DelphiParser.FuncDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#reintroduceDirective}.
	 * @param ctx the parse tree
	 */
	void enterReintroduceDirective(DelphiParser.ReintroduceDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#reintroduceDirective}.
	 * @param ctx the parse tree
	 */
	void exitReintroduceDirective(DelphiParser.ReintroduceDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#overloadDirective}.
	 * @param ctx the parse tree
	 */
	void enterOverloadDirective(DelphiParser.OverloadDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#overloadDirective}.
	 * @param ctx the parse tree
	 */
	void exitOverloadDirective(DelphiParser.OverloadDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#bindingDirective}.
	 * @param ctx the parse tree
	 */
	void enterBindingDirective(DelphiParser.BindingDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#bindingDirective}.
	 * @param ctx the parse tree
	 */
	void exitBindingDirective(DelphiParser.BindingDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#abstractDirective}.
	 * @param ctx the parse tree
	 */
	void enterAbstractDirective(DelphiParser.AbstractDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#abstractDirective}.
	 * @param ctx the parse tree
	 */
	void exitAbstractDirective(DelphiParser.AbstractDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#inlineDirective}.
	 * @param ctx the parse tree
	 */
	void enterInlineDirective(DelphiParser.InlineDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#inlineDirective}.
	 * @param ctx the parse tree
	 */
	void exitInlineDirective(DelphiParser.InlineDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#callConvention}.
	 * @param ctx the parse tree
	 */
	void enterCallConvention(DelphiParser.CallConventionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#callConvention}.
	 * @param ctx the parse tree
	 */
	void exitCallConvention(DelphiParser.CallConventionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#callConventionNoSemi}.
	 * @param ctx the parse tree
	 */
	void enterCallConventionNoSemi(DelphiParser.CallConventionNoSemiContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#callConventionNoSemi}.
	 * @param ctx the parse tree
	 */
	void exitCallConventionNoSemi(DelphiParser.CallConventionNoSemiContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#oldCallConventionDirective}.
	 * @param ctx the parse tree
	 */
	void enterOldCallConventionDirective(DelphiParser.OldCallConventionDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#oldCallConventionDirective}.
	 * @param ctx the parse tree
	 */
	void exitOldCallConventionDirective(DelphiParser.OldCallConventionDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#hintingDirective}.
	 * @param ctx the parse tree
	 */
	void enterHintingDirective(DelphiParser.HintingDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#hintingDirective}.
	 * @param ctx the parse tree
	 */
	void exitHintingDirective(DelphiParser.HintingDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#externalDirective}.
	 * @param ctx the parse tree
	 */
	void enterExternalDirective(DelphiParser.ExternalDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#externalDirective}.
	 * @param ctx the parse tree
	 */
	void exitExternalDirective(DelphiParser.ExternalDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#externalSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterExternalSpecifier(DelphiParser.ExternalSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#externalSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitExternalSpecifier(DelphiParser.ExternalSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#dispIDDirective}.
	 * @param ctx the parse tree
	 */
	void enterDispIDDirective(DelphiParser.DispIDDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#dispIDDirective}.
	 * @param ctx the parse tree
	 */
	void exitDispIDDirective(DelphiParser.DispIDDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(DelphiParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(DelphiParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#usedKeywordsAsNames}.
	 * @param ctx the parse tree
	 */
	void enterUsedKeywordsAsNames(DelphiParser.UsedKeywordsAsNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#usedKeywordsAsNames}.
	 * @param ctx the parse tree
	 */
	void exitUsedKeywordsAsNames(DelphiParser.UsedKeywordsAsNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#identList}.
	 * @param ctx the parse tree
	 */
	void enterIdentList(DelphiParser.IdentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#identList}.
	 * @param ctx the parse tree
	 */
	void exitIdentList(DelphiParser.IdentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#identListFlat}.
	 * @param ctx the parse tree
	 */
	void enterIdentListFlat(DelphiParser.IdentListFlatContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#identListFlat}.
	 * @param ctx the parse tree
	 */
	void exitIdentListFlat(DelphiParser.IdentListFlatContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(DelphiParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(DelphiParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#intNum}.
	 * @param ctx the parse tree
	 */
	void enterIntNum(DelphiParser.IntNumContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#intNum}.
	 * @param ctx the parse tree
	 */
	void exitIntNum(DelphiParser.IntNumContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#realNum}.
	 * @param ctx the parse tree
	 */
	void enterRealNum(DelphiParser.RealNumContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#realNum}.
	 * @param ctx the parse tree
	 */
	void exitRealNum(DelphiParser.RealNumContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#namespacedQualifiedIdent}.
	 * @param ctx the parse tree
	 */
	void enterNamespacedQualifiedIdent(DelphiParser.NamespacedQualifiedIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#namespacedQualifiedIdent}.
	 * @param ctx the parse tree
	 */
	void exitNamespacedQualifiedIdent(DelphiParser.NamespacedQualifiedIdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#namespaceName}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceName(DelphiParser.NamespaceNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#namespaceName}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceName(DelphiParser.NamespaceNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link DelphiParser#qualifiedIdent}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedIdent(DelphiParser.QualifiedIdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DelphiParser#qualifiedIdent}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedIdent(DelphiParser.QualifiedIdentContext ctx);
}