# formal grammar
definition of the formal grammar of the lox programming language, updated as we go along

> program -> statement* EOF ;
>
> statement -> exprStmt | printStmt ;
> 
> exprStmt -> expression ";" ;
> printStmt -> "print" expression ";" ;
>
> expression -> equality ;
> equality -> comparison ( ( "!=" | "==" ) comparison )* ;
> comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
> term -> factor ( ( "-" | "+" ) factor )* ;
> factor -> unary ( ( "\" | "\*" ) unary )\* ; // (IGNORE \'s) ;
> unary -> ( "!" | "-" ) unary | primary ;
> primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;

