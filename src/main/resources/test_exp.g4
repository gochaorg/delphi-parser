grammar test_exp;
options { caseInsensitive = true; }

exp: relOp
   ;

relOp   : sumOp ( ( '=' | '<>' | '<' | '>' | '<=' | '>=' | 'in' | 'is' ) exp )? ;

sumOp   : mulOp ( ( '+' | '-' | 'or' | 'xor' ) exp )? ;

mulOp   : unaryOp ( ( '*' | '/' | 'div' | 'mod' | 'and' | 'shl' | 'shr' | 'as' ) exp ) ? ;

unaryOp : ('@' | 'not' | '+' | '-' ) exp
        | atom
        ;

atom    : ( NUM
          | IDENT
          | '(' exp ')'
          ) postfix?
        ;

postfix : arrayIndex
        | deref
        | calling
        | fieldAccess
        ;

deref : '^' postfix?;

calling     : '(' ( callParam ( ',' callParam )* )? ')' postfix?;
callParam   : ( paramName=IDENT ':=' )? exp;

arrayIndex  : '[' exp ']' postfix?;

fieldAccess : '.' fieldName=IDENT;

fragment DIGIT: '0'..'9';
NUM: DIGIT DIGIT*;

fragment LOW_CASE_LETTER: 'a'..'z';
fragment HI_CASE_LETTER : 'A'..'Z';
fragment IDENT_FIRST: LOW_CASE_LETTER | HI_CASE_LETTER | '_' ;
fragment IDENT_NEXT : LOW_CASE_LETTER | HI_CASE_LETTER | '_' | DIGIT ;
IDENT   : IDENT_FIRST IDENT_NEXT*;

WS: ' ' -> skip;