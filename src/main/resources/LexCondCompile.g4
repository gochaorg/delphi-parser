grammar LexCondCompile;
@lexer::namespace{LexCondCompile}
@parser::namespace{LexCondCompile}
options { caseInsensitive = true; }

expr: logicOp;

logicOp : left=andOp ( op='or' right=expr)? ;

andOp : left=relOp ( op='and' right=expr )?;

relOp: left=unaryOp ( op=('<=' | '>=' | '<>' | '!=' | '<' | '=' | '>') right=expr )?;

unaryOp: op=('!' | 'not' | '-' | '+' )? value=atom;

atom:   ( IDENT
        | NUM
        | '(' expr ')'
        ) postfix*
    ;

postfix : call
        ;

call: '(' (expr (',' expr)*)? ')';

IDENT: ('a'..'z' | '_' ) ('a'..'z' | '_' | '0'..'9' )*;

fragment DIGIT: '0'..'9';

NUM: DIGIT DIGIT* ('.' DIGIT DIGIT*)?;

WS: ' ' -> skip;
