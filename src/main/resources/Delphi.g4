grammar Delphi;

/*
 * Sonar Delphi Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02

 * Updated from above original ANTLR3 to ANTLR4 by Jens Gotthardsen
 * https://github.com/gotthardsen/Delphi-ANTRL4-Grammar
 */

@lexer::namespace{DelphiGrammar}
@parser::namespace{DelphiGrammar}
options { caseInsensitive = true; }
//****************************
//section start
//****************************
file                         : program | library | unit | packageE
                             ;
//****************************
//section fileDefinition
//****************************

program                      : (programHead)? (usesFileClause)? block '.'
                             ;
programHead                  : 'program' namespaceName (programParmSeq)? ';'
                             ;
programParmSeq               : '(' (ident (',' ident)* )? ')'
                             ;
library                      : libraryHead (usesFileClause)? block '.'
                             ;
libraryHead                  : 'library' namespaceName (hintingDirective)* ';' 
                             ;
packageE                     : packageHead requiresClause (containsClause)? 'end' '.'
                             ;
packageHead                  : 'package' namespaceName ';'
                             ;

// implementation
unit                         : unitHead unitInterface unitImplementation? unitBlock '.'
                             ;

// implementation
unitHead                     : 'unit' namespaceName (hintingDirective)* ';'
                             ;

// implementation
unitInterface                : 'interface' (usesClause)? (interfaceDecl)* 
                             ;

// implementation
unitImplementation           : 'implementation' (usesClause)? (declSection)* 
                             ;

// implementation
unitBlock                    : unitInitialization 'end'
                             | compoundStatement
                             | 'end'
                             ;

// implementation
unitInitialization           : 'initialization' statementList (unitFinalization)?
                             ;

// implementation
unitFinalization             : 'finalization' statementList
                             ;
//****************************
//section fileUsage
//****************************
containsClause               : 'contains' namespaceFileNameList
                             ;
requiresClause               : 'requires' namespaceNameList
                             ;
usesClause                   : 'uses' namespaceNameList 
                             ;
usesFileClause               : 'uses' namespaceFileNameList 
                             ;
namespaceFileNameList        : namespaceFileName (',' namespaceFileName)* ';' 
                             ;
namespaceFileName            : namespaceName ('in' QuotedString)? 
                             ;
namespaceNameList            : namespaceName (',' namespaceName)* ';' 
                             ;
//****************************
//section declaration
//****************************

// implemented BodyAst
block                        : (declSection)* (blockBody)
                             ;

// implemented BlockBodyAst
blockBody                    : compoundStatement
                             | assemblerStatement
                             ;

// implemented DeclSectionAst
declSection                  : labelDeclSection
                             | constSection
                             | typeSection
                             | varSection
                             | exportedProcHeading
                             | methodDecl
                             | procDecl
                             | exportsSection
                             ;

// implemented InterfaceDeclAst
interfaceDecl                : procDecl
                             | methodDecl
                             | typeSection
                             | varSection
                             | exportedProcHeading
                             | exportsSection
                             | constSection
                             ;

// implemented LabelDeclSectionAst
labelDeclSection             : 'label' label (',' label)* ';'
                             ;

// implemented ConstSectionAst
constSection                 : constKey (constDeclaration)*  //CHANGED, erased one constDeclaration, for: "const {$include versioninfo.inc }"
                             ;

// implemented
constKey                     : 'const'
                             | 'resourcestring'
                             ;

// implemented
constDeclaration             : (customAttribute)* ident (':' typeDecl)? '=' constExpression (hintingDirective)* ';'
                             ;

// implemented
typeSection                  : 'type' typeDeclaration (typeDeclaration)* 
                             ;

// implemented
typeDeclaration              : (customAttribute)* genericTypeIdent '=' typeDecl (hintingDirective)* ';'
                             ;

// implemented
varSection                   : varKey varDeclaration*
                             ;
// implemented
varKey                       : 'var'
                             | 'threadvar'
                             ;
// implemented
// threadvar geen initializations alleen globaal
varDeclaration               : (customAttribute)* identListFlat ':' typeDecl (varValueSpec)? (hintingDirective)* ';'
                             ;
// implemented
varValueSpec                 : 'absolute' ident
                             | 'absolute' constExpression

                             | '=' ( '(' ')' | constExpression )
                             // рабочий пример unit OleCtrls;
                             // ....
                             //  var  // init to zero, never written to
                             //    DispParams: TDispParams = ();

                             ;

// implemented
exportsSection               : 'exports' ident exportItem (',' ident exportItem)* ';'
                             ;

// implemented
exportItem                   : ('(' (formalParameterList)? ')')? (INDEX index1=expression)? (NAME name1=expression)? ('resident')?
                             ;
//****************************
//section type
//****************************

// implemented
typeDecl                     : strucType
                             | pointerType
                             | stringType
                             | procedureType 
                             | variantType
                             | ('type')? typeId (genericPostfix)?
                             | simpleType
                             ;

// about packed:
//   https://docwiki.embarcadero.com/RADStudio/Alexandria/en/Structured_Types_(Delphi)
//   specify byte alignment by including the reserved word packed when you declare a structured type
//   The packed word specifies compressed data storage
//
//  https://docwiki.embarcadero.com/RADStudio/Alexandria/en/Internal_Data_Formats_(Delphi)#Implicit_Packing_of_Fields_with_a_Common_Type_Specification
//    Earlier versions of the Delphi compiler, such as Delphi 7 and earlier,
//    implicitly applied packed alignment to fields that were declared together, that is,
//    fields that have a common type specification. Newer compilers can reproduce the behavior
//    if you specify the directive {$OLDTYPELAYOUT ON}.
//
//    This directive byte-aligns (packs) the fields that have a common type specification, even
//    if the declaration does not include the packed modifier and the record type is not declared in the {$A-} state.
// implemented
strucType                    : ('packed')? strucTypePart 
                             ;

// implemented
strucTypePart                : arrayType
                             | setType
                             | fileType
                             | classDecl
                             ;

// implemented
arrayType                    :  'array' ('[' (arrayIndex)? (',' (arrayIndex)?)* ']')? 'of' arraySubType 
                                     //CHANGED we only need type info
                             ;

// implemented
arrayIndex                   : typeId
                             | expression '..' expression
                             ;

// implemented
arraySubType                 : 'const'
                             | typeDecl
                             ;

// implemented
setType                      : 'set' 'of' typeDecl          //CHANGED we only need type info
                             ;
// set type alleen ordinal of subrange type
// implemented
fileType                     : 'file' ('of' typeDecl)?
                             ;

// implemented
pointerType                  : '^' typeDecl
                             | 'pointer'
                             ;

// implemented
stringType                   : 'string' ('[' expression ']')?
                             | ('type')? ANSISTRING (codePageNumber)?
                             ;

// implemented
codePageNumber               : '(' intNum ')'
                             ;

// implemented
procedureType                : methodType
                             | simpleProcedureType
                             | procedureReference
                             ;

// implemented
methodType                   : procedureTypeHeading 'of' 'object'
                             ;

// implemented
simpleProcedureType          : procedureTypeHeading ( (';')? callConventionNoSemi)?
                             ;

// implemented
procedureReference           : 'reference' 'to' procedureTypeHeading
                             ;

procedureTypeHeading         : 'function' (formalParameterSection)? ':' (customAttribute)* typeDecl
                             | 'procedure' (formalParameterSection)?
                             ;
variantType                  : 'variant' // SzJ TODO TEMP
                             ;
simpleType                   : ident // не понятный случай
                             | subRangeType // реаилизовано
                             | enumType // реализовано, через ж.. subRangeType, см комментарии
                             ;

// implemented
subRangeType                 : constExpression ('..' constExpression)?
                             ;

enumType                     : '(' ident ('=' expression)? (',' ident ('=' expression)? )* ')' // TODO не работает, вместо этого захват - subRangeType
                             ;
typeId                       : namespacedQualifiedIdent
                             ;
//****************************
//section generics
//****************************
genericTypeIdent             : qualifiedIdent (genericDefinition)?     //CHANGED we don't need <Type> data, it produced empty nodes
                             ;

// todo ???
genericDefinition            : simpleGenericDefinition
                             | constrainedGenericDefinition
                             ;
simpleGenericDefinition      : '<' ident (',' ident)* '>'
                             ;
constrainedGenericDefinition : '<' constrainedGeneric (';' constrainedGeneric)* '>'
                             ;
constrainedGeneric           : ident (':' genericConstraint (',' genericConstraint)*)?
                             ;
genericConstraint            : ident
                             | ( 'record' | 'class' | 'constructor' )
                             ;
genericPostfix               : '<' typeDecl (',' typeDecl)* '>'
                             ;
//****************************
//section class
//****************************
classDecl                    : classTypeTypeDecl
                             | classTypeDecl 
                             | classHelperDecl 
                             | interfaceTypeDecl 
                             | objectDecl 
                             | recordDecl 
                             | recordHelperDecl 
                             ;

// implemented MetaClassTypeAst
classTypeTypeDecl            : 'class' 'of' typeId
                             ;

// implemented
classTypeDecl                : 'class' (classState)? (classParent)? (classItem)* 'end' 
                             | 'class' classParent?
                             ;

// implemented
classState                   : 'sealed'
                             | 'abstract'
                             ;

// implemented
classParent                  : '(' genericTypeIdent (',' genericTypeIdent)* ')'    //CHANGEd from typeId to classParentId
                             ;

// implemented
classItem                    : visibility
                             | classMethod
                             | classField
                             | classProperty
                             | constSection
                             | typeSection
                             | ('class')? varSection
                             ;
classHelperDecl              : 'class' 'helper' (classParent)? 'for' typeId (classHelperItem)* 'end' //CHANGED, we only need "for" class name
                             ;
classHelperItem              : visibility
                             | classMethod
                             | classProperty
                             | ('class')? varSection
                             ;
interfaceTypeDecl            : interfaceKey (classParent)? (interfaceGuid)? (interfaceItem)* 'end' 
                             | interfaceKey (classParent)? 
                             ;
interfaceKey                 : 'interface'
                             | 'dispinterface'
                             ;
interfaceGuid                : '[' QuotedString ']' 
                             ;
interfaceItem                : classMethod
                             | ('class')? classProperty
                             ;
objectDecl                   : 'object' (classParent)? (objectItem)* 'end' 
                             ;
objectItem                   : visibility
                             | classMethod
                             | classField
                             ;

// implemented
recordDecl                   : simpleRecord
                             | variantRecord
                             ;

// implemented
simpleRecord                 : 'record' (recordField)* (recordItem)* 'end' 
                             ;

// implemented
variantRecord                : 'record' (recordField)* recordVariantSection 'end'
                             ;

// implemented
recordItem                   : visibility     //ADDED
                             | classMethod
                             | classProperty
                             | constSection
                             | typeSection
                             | recordField
                             | ('class')? varSection
                             ;

// implemented
recordField                  : identList ':' typeDecl (hintingDirective)* (';')?  //CHANGED not needed ; at the end
                             ;
recordVariantField           : identList ':' typeDecl (hintingDirective)* (';') ?
                             ;
recordVariantSection         : 'case' (ident ':')? typeDecl 'of' (recordVariant | ';') (recordVariant | ';')*
                             ;
recordVariant                : constExpression (',' constExpression)* ':' '(' (recordVariantField)* ')'   //CHANGED to recordVariantField from recordField
                             ;
recordHelperDecl             : 'record' 'helper' 'for' typeId (recordHelperItem)* 'end'
                             ;
recordHelperItem             : visibility
                             | classMethod
                             | classProperty
                             ;

// implemented
classMethod                  : (customAttribute)* ('class')? methodKey mname=ident (genericDefinition)? (formalParameterSection)? ';'? ( methodDirective)*
                             | (customAttribute)* ('class')? 'function' mname=ident (genericDefinition)? (formalParameterSection)? ':' (retAttr=customAttribute)* typeDecl ';'? (methodDirective)*
                             | (customAttribute)* ('class')? 'operator' mname=ident (genericDefinition)? (formalParameterSection)? ':' (retAttr=customAttribute)* typeDecl ';'
                             | oleClassMethodAlias
                             ;

// implemented
oleClassMethodAlias          : ( 'function' | 'procedure' ) comItfName=ident '.' comItfMethod=ident '=' implMethod=ident ';' // очень странная форма для реализации конкретного метода Ole/Com/ActiveX
                             ;

// implemented
classField                   : (customAttribute)* identList ':' typeDecl (hintingDirective)* ';' (hintingDirective)*
                             ;

//classProperty                : (customAttribute)? ('class')? 'property' classPropertyName (classPropertyArray)? (':' genericTypeIdent)? (classPropertyIndex)? (classPropertySpecifier)* ';' (classPropertyEndSpecifier)*
//                              // CHANGED added (classPropertySpecifier)* at end for "default;"
//                              // CHANGEDD to genericTypeIdent for "property QueryBuilder : IQueryBuilder<GenericRecord>"
//                             ;

// implemented
// TODO есть еще spec implements
classProperty   : (customAttribute)* 'class' ?
                  'property' classPropertyName
                        classPropertyArray?
                   ( ':' genericTypeIdent ) ?
                   ('index' index=expression)? ( classPropSpec* | classPropDispSpec* ) ';'? ( classPropPostfixSpec+ ';')?
                ;

// implemented
classPropSpec   : 'read' ident ('.' ident)*
                | 'write' ident ('.' ident)*
                | 'implements' ident (',' ident)*
                ;

// implemented
classPropPostfixSpec    : 'default' expression ?
                        | 'nodefault'
                        | 'stored' expression
                        ;

// implemented
classPropDispSpec : 'readonly'
                  | 'writeonly'
                  | 'dispid' expression
                  ;

// implemented
classPropertyName            : PROTECTED
                             | RESIDENT
                             | ident; //TODO Возможно надо добавить не только RESIDENT

// implemented
classPropertyArray           : '[' formalParameterList ']'
                             ;
// implemented
visibility                   : (STRICT)? 'protected'
                             | (STRICT)? 'private'
                             | ( 'public'
                             | 'published' 
                             | 'automated' )     // win32 deprecated
                             ;
//****************************
//section procedure
//****************************

//implemented
//TODO Как так procedure имеет возвращаемый тип, а function нет ???
//TODO Проверить на счет перестановки procedure <-> function
exportedProcHeading          : 'procedure' ident (formalParameterSection)? ':' (customAttribute)* typeDecl ';' (functionDirective)*
                             | 'function' ident (formalParameterSection)? ';' (functionDirective)*
                             ;

// implemented
methodDecl                   : methodDeclHeading ';' (methodDirective)* (methodBody)?
                             ;

// implemented
methodDeclHeading            : (customAttribute)* ('class')?  methodKey methodName (formalParameterSection)?
                             | (customAttribute)* ('class')? 'function' methodName (formalParameterSection)? (':' (customAttribute)* typeDecl)?
                             | (customAttribute)* 'class' 'operator' methodName (formalParameterSection)? (':' (customAttribute)* typeDecl)?
                             ;

// impl
methodKey                    : 'procedure'
                             | 'constructor'
                             | 'destructor'
                             ;

// impl
methodName                   : className=ident (classTArgs=genericDefinition)?
                               ('.' nestedName=ident (nestedTArgs=genericDefinition)?)?
                               '.' methName=ident (methTArgs=genericDefinition)?
                             ;

//procDecl                     : procDeclHeading (functionDirective)* ';' (procBody)?     //CHANGED
procDecl                     : procDeclHeading ( ';'? funcDirective (';' funcDirective)* )? ';'? procBody? //TODO блять, точка с запятой - вообще по случайной логике раставляются
                             ;


// impl
procDeclHeading              : (customAttribute)* 'procedure' ident (formalParameterSection)?             //CHANGED
                             | (customAttribute)* 'function' ident (formalParameterSection)? ':' typeDecl
                             ;

// impl
formalParameterSection       : '(' (formalParameterList)? ')'
                             ;

// impl
formalParameterList          : formalParameter (';' formalParameter)*
                             ;

// impl
formalParameter              : //(customAttribute)?
                               (parmType)? identListFlat (':' typeDecl)? ('=' expression)? 
               //expressions was cut out, beacause we dont have to know default variable values; they were causing troubles with DelphiCodeAnalyser
                             ;

// impl
parmType                     : 'const'
                             | 'var'
                             | 'out'
                             ;

// implemented
methodBody                   : block ';'
                             ;

// implemented
procBody                     : 'forward' ';' (functionDirective)*   // CHECKEN ; en directive plaats!
                             | 'external' ('name' expression | 'index' expression)* (functionDirective)* // CHECKEN directive plaats
                             | block ';'
                             ;
//****************************
//section customAttributes
//****************************
customAttribute              : customAttributeDecl //'abekat' //customAttributeList
                             ;

customAttributeDecl          : '[' customAttributeNamedCall (',' customAttributeNamedCall)* ']'
                             ;

customAttributeNamedCall     : ( namespacedQualifiedIdent | paramName ) ('(' (expressionList)? ')')?
                             ;


//****************************
//section expression
//****************************

// Базовое выражение
// implemented ExpressionAst
expression                   : anonymousExpression
                             | simpleExpression //Просто для примера как не надо: (relOp simpleExpression)? ('=' expression)?   //CHANGED, added expression for: "if( functionCall(x, 7+66) = true ) then" syntax
                             ;

// типа лямбды
// implemented LambdaAst
anonymousExpression          : 'procedure' (formalParameterSection)? block
                             | 'function' (formalParameterSection)? ':' typeDecl block
                             ;

// простые выражения
// implemented
simpleExpression             : relOp
                             ;

// менее приоритетные
// implemented
relOp   : left=sumOp ( op=( '=' | '<>' | '<' | '>' | '<=' | '>=' | 'in' | 'is' ) right=expression )? ;

// выше приоритет
// implemented
sumOp   : left=mulOp ( op=( '+' | '-' | 'or' | 'xor' ) right=expression )? ;

// еще выше приоритет
// implemented
mulOp   : left=unaryOp ( op=( '*' | '/' | 'div' | 'mod' | 'and' | 'shl' | 'shr' | 'as' ) right=expression )? ;

// макс приоритет операторов
// implemented
unaryOp : op=('@' | 'not' | '+' | '-' | '^' ) exp=expression
        | atom
        ;

// атом
// implemented
atom    : ( intNum
          | realNum
          | stringFactor
          | preDefinedValues
          | ('inherited')? identInAtom
          | 'inherited' // TODO внезапно это то же валидный идентификатор!! блять!!
          | setExpression
          | '(' expression ')' // скобочное выражение
          ) postfix*
        ;

// TODO внезапно ключевое слово RESIDENT - валидный идентификатор: что блять происходит!
// TODO проверить что является переменной, параметром, а что ключевым словом

// implemented
identInAtom : RESIDENT
            | DISPID
            | NODEFAULT
            | HELPER
            | FORWARD
            | ident
            ;

// implemented
setExpression   : '[' (head=expression (delim=(','|'..') tail=expression)*)? ']' ;

// цепь вызовов
// implemented
postfix : arrayIndexAccess
        | deref
        | calling
        | fieldAccess
        | genericCallTypeParams
        ;

// получение значение по адресу
// implemented
deref : '^' ;

// вызов функции/процедуры/метода
// implemented
calling     : '(' ( callParam ( ',' callParam )* )? ')' ;

// способ передачи параметра
// implemented
callParam   : namedPassParam | unnamedPassParam;

// передача параметров по имени - работает для Ole | IDispatch
// implemented
namedPassParam : paramName ':=' expression;
paramName   : TYPE | ABSOLUTE | ABSTRACT | ADD | AND | ANSISTRING | ARRAY | AS | ASM
            | ASSEMBLER | ASSEMBLY | AT | AUTOMATED | BEGIN | BREAK | CASE | CDECL | CLASS
            | CONST | CONSTRUCTOR | CONTAINS | CONTINUE | DEFAULT | DEPRECATED | DESTRUCTOR
            | DISPID | DISPINTERFACE | DIV | DO | DOWNTO | DQ | DW | DYNAMIC
            | ELSE | END | EXCEPT | EXPERIMENTAL | EXPORT | EXPORTS | EXTERNAL
            | FAR | FILE | FINAL | FOR | FORWARD | FUNCTION | GOTO | HELPER
            | IF | IMPLEMENTATION | IMPLEMENTS | IN | INDEX | INHERITED | INLINE | INTERFACE | IS
            | LABEL | LIBRARY | LOCAL | MESSAGE | MOD
            | NAME | NEAR | NIL | NODEFAULT | NOT
            | OBJECT | OF | ON | OPERATOR | OR | OVERLOAD | OVERRIDE
            | PACKAGE | PACKED | PASCAL | PLATFORM | POINTER | PRIVATE | PROCEDURE | PROGRAM
            | PROPERTY | PROTECTED | PUBLIC | PUBLISHED
            | RAISE | READ | READONLY | RECORD | REFERENCE | REGISTER | REINTRODUCE
            | REMOVE | REPEAT | REQUIRES | RESIDENT | RESOURCESTRING
            | SAFECALL | SEALED | SET | SHL | SHR | STATIC | STDCALL | STORED
            | STRICT | STRING
            | THEN | THREADVAR | TO | UNIT | USES
            | VAR | VARARGS | VARIANT | VIRTUAL
            | WHILE | WITH | WRITE | WRITEONLY | XOR
            | FALSE | TRUE
            | TkClass | TkNewType
            | DELAYED
            | ident
            ;

// передача параметров позиционно
// implemented
unnamedPassParam : expression;

// implemented
arrayIndexAccess  : '[' expression (',' expression)* ']';

// implemented
fieldAccess : '.' fieldName=paramName;

// implemented
genericCallTypeParams : '<' genericTypeIdent (',' genericTypeIdent)* '>';

// implemented
preDefinedValues    : 'nil' | 'true' | 'false';

// implemented
stringFactor                 : ControlString (QuotedString ControlString)* (QuotedString)?
                             | QuotedString (ControlString QuotedString)* (ControlString)?
                             ;

// implemented
setSection                   : '[' (expression ((',' | '..') expression)*)? ']'
                             ;

// Оставил для примера вызова Generic proc/method...
//designatorItem               : '^'
//                             | ('.' | '@') ident              //CHANGED added '@'
//                             | ('<' genericTypeIdent (',' genericTypeIdent)* '>')       //ADDED for proc<sth, sth>.foo;
//                             | '[' expressionList ']'
//                             | '(' ( callParam (',' callParam)* )? ')'
//                             ;

// implemented
expressionList               : expression (',' expression)*;

//****************************
//section statement
//****************************

// implemented StatementAst.java
statement                    : ifStatement
                             | caseStatement
                             | repeatStatement
                             | whileStatement
                             | forStatement
                             | withStatement
                             | tryStatement
                             | raiseStatement
                             | assemblerStatement
                             | 'inherited' simpleExpression?
                             | compoundStatement
                             | gotoStatement
                             | label ':' statement
                             | simpleStatement
                             ;

// implemented StatementAst.java
ifStatement                  : 'if' expression 'then' positive=statement ('else' negative=statement)?
                             | 'if' expression 'then' positive=statement 'else' // TODO Delphi7 реально такое компилирует !! блИат
                             | 'if' expression 'then' 'else' negative=statement // TODO Delphi7 реально такое компилирует !!
                             | 'if' expression 'then' // TODO Delphi7 реально такое компилирует !!
                             ;

// implemented StatementAst.java
caseStatement                : 'case' expression 'of' (caseItem)* ('else' statementList (';')?)? 'end'
                             ;

// implemented StatementAst.java
//caseItem                     : caseLabel (',' caseLabel)* ':' statement (';')? // checken of ; sep of scheider is //TODO <- херня какая-то
caseItem                     : caseLabel (',' caseLabel)* ':' ( statement (';')? | ';' )
                             ;

// implemented StatementAst.java
caseLabel                    : expression ('..' expression)?
                             ;

// implemented StatementAst.java
repeatStatement              : 'repeat' statementList 'until' cond=expression
                             ;

// implemented StatementAst.java
whileStatement               : 'while' cond=expression 'do' ( ';' | statement ) ?
                             ;

// implemented StatementAst.java
forStatement                 : 'for' iterValue=atom ':=' start=expression 'to' end=expression 'do' statement
                             | 'for' iterValue=atom ':=' startBig=expression 'downto' endSmall=expression 'do' statement
                             | 'for' iterValue=atom 'in' source=expression 'do' statement
                             ;

// implemented StatementAst.java
withStatement                : 'with' withItem 'do' statement
                             ;

// implemented StatementAst.java
withItem                     : expression 'as' ident       //ADDED
                             | expression (',' expression)*
                             ;

// implemented StatementAst.java
compoundStatement            : 'begin' statementList 'end'
                             ;

// implemented StatementAst.java
statementList                : (statement)? (';' (statement)?)*
                             ;

// implemented StatementAst.java
simpleStatement              : simpleExpression (':=' ('inherited')? assignSource=expression) ?
                             ;

// implemented StatementAst.java
gotoStatement                : 'goto' label
                             | 'exit' ('(' expression ')')?   
                             | ( 'break'                          
                             | 'continue' )
                             ;
//****************************
//section constExpression
//****************************

// implemented ConstSectionAst
constExpression              : '(' recordConstExpression (';' recordConstExpression)* ';'? ')' //CHANGED reversed order
                             | '(' constExpression (',' constExpression)* ')'
                             | expression
                             ;

// implemented ConstSectionAst
recordConstExpression        : ident ':' constExpression
                             ;
//****************************
//section exceptionStatement
//****************************

// implemented StatementAst.java
tryStatement                 : 'try' actions=statementList 'except' except=handlerList 'end'
                             | 'try' actions=statementList 'finally' final=statementList 'end'
                             ;

// implemented StatementAst.java
handlerList                  : (handler)* ('else' statementList)?
                             | statementList
                             ;

// implemented StatementAst.java
handler                      : 'on' (handlerIdent)? typeId 'do' handlerStatement  //CHANGED - ; is not required ; handlerIdent not required, example:  "on einvalidoperation do;"
                             ;

// implemented StatementAst.java
handlerIdent                 : Alpha ':'
                             | ident ':'
                             ;

// implemented StatementAst.java
handlerStatement             : statement (';')?
                             | ';'
                             ;

// implemented StatementAst.java
raiseStatement               : 'raise' (base=expression)? (AT atExp=expression)? // CHECKEN!
                             ;           
//****************************
//section AssemblerStatement
//****************************

// implemented StatementAst.java
assemblerStatement           : 'asm' ~('end')* 'end'    //ADDED we don't realy care about assembler statements, since they don't contribute to
                             ;                //any measure, just skip, allow all
//****************************
//section directive
//****************************
// implemented FDirective
methodDirective              : reintroduceDirective         // 1
                             | overloadDirective            // 2
                             | bindingDirective             // 3
                             | abstractDirective            // 3 virtual;
                             | inlineDirective              // 4 niet virtual or dynamic
                             | callConvention               // 4
                             | hintingDirective ';'       // 4 (niet abstract)
                             | oldCallConventionDirective   // 1
                             | dispIDDirective
                             ;

// implemented FDirective
//TODO Кандидат на удаление
functionDirective            : overloadDirective          // 1
                             | inlineDirective            // 1
                             | callConvention             // 1
                             | oldCallConventionDirective // 1
                             | hintingDirective ';'      // 1
                             | (callConventionNoSemi)? externalDirective          // 1
                             | 'unsafe' ';'              // 1 .net?
                             | 'delayed' ';'
                             ;

// implemented FDirective
funcDirective   : 'overload'
                | 'inline' | 'assembler'
                // соглашение о вызовах, возможен один вариант из
                    | 'cdecl' | 'pascal' | 'register' | 'safecall' | 'stdcall' | 'export'
                    | 'far' | 'local' | 'near'
                | 'deprecated' stringFactor?
                // хз что, возможно допустим один вариант из
                    | 'experimental' | 'platform' | 'library'
                // укакие-то external
                    | 'varargs' | 'external' constExpression externalSpecifier* | 'external'
                    | 'unsafe'
                    | 'delayed'
                ;


// implemented FDirective
reintroduceDirective         : 'reintroduce' ';'
                             ;

// implemented FDirective
overloadDirective            : 'overload' (';')?    //CHANGE ; not needed
                             ;

// implemented FDirective
bindingDirective             : 'message' expression ';'
                             | 'static' ';'
                             | 'dynamic' ';'
                             | 'override' ';'
                             | 'virtual' ';'
                             ;

// implemented FDirective
abstractDirective            : 'abstract' ';'
                             | 'final' ';'
                             ;

// implemented FDirective
inlineDirective              : 'inline' ';'
                             | 'assembler' ';' // deprecated
                             ;

// implemented FDirective
callConvention               : 'cdecl' ';'    //
                             | 'pascal' ';'   //
                             | 'register' ';' //
                             | 'safecall' ';' //
                             | 'stdcall' ';'  //
                             | 'export' ';'   // deprecated
                             ;

// implemented FDirective
callConventionNoSemi         : 'cdecl'    //    //ADDED for procedureType error fixing, without ';' at the end
                             | 'pascal'   //
                             | 'register' //
                             | 'safecall' //
                             | 'stdcall'  //
                             | 'export'   // deprecated
                             ;

// implemented FDirective
oldCallConventionDirective   : 'far' ';'      // deprecated
                             | 'local' ';'    // niet in windows maakt functie niet exporteerbaar
                             | 'near' ';'     // deprecated
                             ;

// implemented FDirective
hintingDirective             : 'deprecated' (stringFactor)?
                             | ( 'experimental'  // added 2006
                             | 'platform'
                             | 'library' )
                             ;

// implemented FDirective
externalDirective            : 'varargs' ';'   // alleen bij external cdecl
                             | 'external' ';'
                             | 'external' constExpression (externalSpecifier)* ';' // expression : dll name
                             ;

// implemented FDirective
externalSpecifier            : 'name' constExpression
                             | 'index' constExpression   // specific to a platform
                             ;

// implemented FDirective
dispIDDirective              : 'dispid' expression ';'
                             ;
//****************************
////section general
//****************************

// implemented
ident                        : TkIdentifier
                             | AMBER TkIdentifier
                             | usedKeywordsAsNames
                             ;                 
usedKeywordsAsNames          : (NAME | READONLY | ADD | AT | MESSAGE | POINTER | INDEX | DEFAULT | STRING | CONTINUE)
                             | (READ | WRITE | REGISTER | VARIANT | OPERATOR | REMOVE | LOCAL | REFERENCE | CONTAINS | FINAL)
                             | (BREAK | EXIT | STRICT | OUT | OBJECT | EXPORT | ANSISTRING | IMPLEMENTS | STORED | DELAYED)
                             ;                           
identList                    : paramName (',' paramName)*
                             ;

identListFlat                : paramName (',' paramName)*    //ADDED used in formalParemeter
                             ;

label                        : ( TkIdentifier | TkIntNum | TkHexNum ) | usedKeywordsAsNames
                             ;

// implemented
intNum                       : TkIntNum
                             | TkHexNum
                             ;

// implemented
realNum                      : TkRealNum
                             ;                             
namespacedQualifiedIdent     : (namespaceName '.')? qualifiedIdent
                             ;
namespaceName                : ident ('.' ident)*
                             ;
qualifiedIdent               :  (ident '.')*  ident   //must stay the way it is, with '.' for proper class method identyfication
                             ;
                                   
// KEYWORDS
ABSOLUTE          : 'absolute'       ;
ABSTRACT          : 'abstract'       ;
ADD               : 'add'            ;
AND               : 'and'            ;
ANSISTRING        : 'ansistring'     ;
ARRAY             : 'array'          ;
AS                : 'as'             ;
ASM               : 'asm'            ;
ASSEMBLER         : 'assembler'      ;
ASSEMBLY          : 'assembly'       ;
AT                : 'at'             ;
AUTOMATED         : 'automated'      ;
BEGIN             : 'begin'          ;
BREAK             : 'break'          ;
CASE              : 'case'           ;
CDECL             : 'cdecl'          ;
CLASS             : 'class'          ;
CONST             : 'const'          ;
CONSTRUCTOR       : 'constructor'    ;
CONTAINS          : 'contains'       ;
CONTINUE          : 'continue'       ;
DEFAULT           : 'default'        ;
DEPRECATED        : 'deprecated'     ;
DESTRUCTOR        : 'destructor'     ;
DELAYED           : 'delayed'        ;
DISPID            : 'dispid'         ;
DISPINTERFACE     : 'dispinterface'  ;
DIV               : 'div'            ;
DO                : 'do'             ;
DOWNTO            : 'downto'         ;
DQ                : 'dq'             ;
DW                : 'dw'             ;
DYNAMIC           : 'dynamic'        ;
ELSE              : 'else'           ;
END               : 'end'            ;
EXCEPT            : 'except'         ;
EXIT              : 'exit'           ;
EXPERIMENTAL      : 'experimental'   ;
EXPORT            : 'export'         ;
EXPORTS           : 'exports'        ;
EXTERNAL          : 'external'       ;
FAR               : 'far'            ;
FILE              : 'file'           ;
FINAL             : 'final'          ;
FINALIZATION      : 'finalization'   ;
FINALLY           : 'finally'        ;
FOR               : 'for'            ;
FORWARD           : 'forward'        ;
FUNCTION          : 'function'       ;
GOTO              : 'goto'           ;
HELPER            : 'helper'         ;
IF                : 'if'             ;
IMPLEMENTATION    : 'implementation' ;
IMPLEMENTS        : 'implements'     ;
IN                : 'in'             ;
INDEX             : 'index'          ;
INHERITED         : 'inherited'      ;
INITIALIZATION    : 'initialization' ;
INLINE            : 'inline'         ;
INTERFACE         : 'interface'      ;
IS                : 'is'             ;
LABEL             : 'label'          ;
LIBRARY           : 'library'        ;
LOCAL             : 'local'          ;
MESSAGE           : 'message'        ;
MOD               : 'mod'            ;
NAME              : 'name'           ;
NEAR              : 'near'           ;
NIL               : 'nil'            ;
NODEFAULT         : 'nodefault'      ;
NOT               : 'not'            ;
OBJECT            : 'object'         ;
OF                : 'of'             ;
ON                : 'on'             ;
OPERATOR          : 'operator'       ;
OR                : 'or'             ;
OUT               : 'out'            ;
OVERLOAD          : 'overload'       ;
OVERRIDE          : 'override'       ;
PACKAGE           : 'package'        ;
PACKED            : 'packed'         ;
PASCAL            : 'pascal'         ;
PLATFORM          : 'platform'       ;
POINTER           : 'pointer'        ;
PRIVATE           : 'private'        ;
PROCEDURE         : 'procedure'      ;
PROGRAM           : 'program'        ;
PROPERTY          : 'property'       ;
PROTECTED         : 'protected'      ;
PUBLIC            : 'public'         ;
PUBLISHED         : 'published'      ;
RAISE             : 'raise'          ;
READ              : 'read'           ;
READONLY          : 'readonly'       ;
RECORD            : 'record'         ;
REFERENCE         : 'reference'      ;
REGISTER          : 'register'       ;
REINTRODUCE       : 'reintroduce'    ;
REMOVE            : 'remove'         ;
REPEAT            : 'repeat'         ;
REQUIRES          : 'requires'       ;
RESIDENT          : 'resident'       ;
RESOURCESTRING    : 'resourcestring' ;
SAFECALL          : 'safecall'       ;
SEALED            : 'sealed'         ;
SET               : 'set'            ;
SHL               : 'shl'            ;
SHR               : 'shr'            ;
STATIC            : 'static'         ;
STDCALL           : 'stdcall'        ;
STORED            : 'stored'         ;
STRICT            : 'strict'         ;
STRING            : 'string'         ;
THEN              : 'then'           ;
THREADVAR         : 'threadvar'      ;
TO                : 'to'             ;
TRY               : 'try'            ;
TYPE              : 'type'           ;
UNIT              : 'unit'           ;
UNSAFE            : 'unsafe'         ;
UNTIL             : 'until'          ;
USES              : 'uses'           ;
VAR               : 'var'            ;
VARARGS           : 'varargs'        ;
VARIANT           : 'variant'        ;
VIRTUAL           : 'virtual'        ;
WHILE             : 'while'          ;
WITH              : 'with'           ;
WRITE             : 'write'          ;
WRITEONLY         : 'writeonly'      ;
XOR               : 'xor'            ;
FALSE             : 'false'          ;
TRUE              : 'true'           ;

//----------------------------------------------------------------------------
// OPERATORS
//----------------------------------------------------------------------------
PLUS              : '+'   ;
MINUS             : '-'   ;
STAR              : '*'   ;
SLASH             : '/'   ;
ASSIGN            : ':='  ;
COMMA             : ','   ;
SEMI              : ';'   ;
COLON             : ':'   ;
EQUAL             : '='   ;
NOT_EQUAL         : '<>'  ;
LT                : '<'   ;
LE                : '<='  ;
GE                : '>='  ;
GT                : '>'   ;
LPAREN            : '('   ;
RPAREN            : ')'   ;
LBRACK            : '['   ; // line_tab[line]
LBRACK2           : '(.'  ; // line_tab(.line.)
RBRACK            : ']'   ;
RBRACK2           : '.)'  ;
POINTER2          : '^'   ;
AT2               : '@'   ;
DOT               : '.'   ;// ('.' {$setType(DOTDOT);})?  ;
DOTDOT            : '..'  ;
LCURLY            : '{'   ;
RCURLY            : '}'   ;     

AMBER             : '&'   ;
//DOUBLEAT          : '@@'  ;

//****************************
//section token
//****************************
TkGlobalFunction        : 'FUNCTION_GLOBAL'
                        ;
TkFunctionName          : 'FUNCTION_NAME'
                        ;
TkFunctionArgs          : 'FUNCTION_ARGS'
                        ;
TkFunctionBody          : 'FUNCTION_BODY'
                        ;
TkFunctionReturn        : 'FUNCTION_RETURN'
                        ;
TkCustomAttribute       : 'CUSTOM_ATTRIBUTE'
                        ;
TkCustomAttributeArgs   : 'CUSTOM_ATTRIBUTE_ARGS'
                        ;
TkNewType               : 'NEW_TYPE'
                        ;
TkClass                 : 'CLASS'
                        ;
TkRecord                : 'RECORD_TYPE'
                        ;
TkRecordHelper          : 'RECORD_HELPER'
                        ;
TkInterface             : 'INTERFACE_TYPE'
                        ;
TkObject                : 'OBJECT_TYPE'
                        ;
TkClassOfType           : 'CLASS_OF_TYPE'
                        ;
TkVariableType          : 'VARIABLE_TYPE'
                        ;
TkVariableIdents        : 'VARIABLE_IDENTS'
                        ;
TkVariableParam         : 'VARIABLE_PARAM'
                        ;
TkGuid                  : 'INTERFACE_GUID'
                        ;
TkClassParents          : 'CLASS_PARENTS'
                        ;
TkClassField            : 'CLASS_FIELD'
                        ;
TkAnonymousExpression   : 'ANONYMOUS_EXPRESSION'
                        ;
TkIdentifier            : (Alpha | '_') (Alpha | Digit | '_')*
                        ;  
TkIntNum                : Digitseq
                        ;
TkRealNum               : Digitseq ('.' Digitseq)? (('e'|'E') ('+'|'-')? Digitseq)?  //CHANGED
                        ;
TkHexNum                : '$' Hexdigitseq
                        ;
TkAsmHexNum             : Hexdigitseq ('h'|'H')
                        ;
fragment TkAsmHexLabel  : Hexdigitseq ':' // no usage
                        ;
QuotedString            : '\'' ('\'\'' | ~('\''))* '\''   //taken from PASCAL grammar
                        ;
ControlString           : Controlchar (Controlchar)*
                        ;
                        
fragment                
Controlchar             : '#' Digitseq
                        | '#' '$' Hexdigitseq
                        ;
fragment                
Alpha                   : 'a'..'z'
                        | 'A'..'Z'
                        | '\u0080'..'\uFFFE' ~('\uFEFF') //ADDED unicode support
                        ;
fragment                
Digit                   : '0'..'9'
                        ;
fragment                
Digitseq                : Digit (Digit)*
                        ;
fragment                
Hexdigit                : Digit | 'a'..'f' | 'A'..'F'
                        ;
Hexdigitseq             : Hexdigit (Hexdigit)*
                        ;
COMMENT                 :
                        (
                            '//' ~('\n'|'\r')* '\r'? '\n'
                        |  '(*' .*? '*)'  
                        |  '{' .*? '}'
                        )
                        // -> skip
                        -> channel(HIDDEN)
                        ;
WS                      : (' '|'\t'|'\r'|'\n'|'\f')+ -> skip
                        ;
UnicodeBOM              : '\uFEFF' -> skip 
                        ;                             
