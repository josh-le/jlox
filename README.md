# jlox
Work In Progress

interpreter for the Lox language written in Java
# running the interpreter
```bash
git clone https://github.com/josh-leblanc/jlox
cd jlox
make
# example:
> print 2 + 3;
5
> print "a" + "b"
ab
```
this will run a lox REPL, support for running files is coming soon
# material covered
__built:__
- scanner
- script for generating AST classes
- (not so) pretty printer for AST
- parser
- interpreter
    - evaluating expressions
    - statements
        - print statement
        - expression statement
        - variable assignment
    - declarations
        - global variable declaration
    - scope
- flow control
    - if statement
    - while loop
    - for loop

__building:__
- functions


__topics covered:__
- formal grammar
- design patterns:
    - visitor
    - sentinel classes
- associativity and precedence
- recursive descent
# formal grammar
definition of the formal grammar of the lox programming language, updated as we go along
> program -> declaration* EOF ;
>
> declaration -> funDecl | varDecl | statement ;
>
> funDecl -> "fun" function ;
> function -> IDENTIFIER "(" parameters? ")" block;
> varDecl -> "var" IDENTIFIER ( "=" expression )? ";";
> parameters -> IDENTIFIER ( "," IDENTIFIER )* ;
>
> statement -> exprStmt | printStmt | block | ifStmt | returnStmt | whileStmt | forStmt ;
>
> exprStmt -> expression ";" ;
> printStmt -> "print" expression ";" ;
> block -> "{" declaration* "}" ;
> ifStmt -> "if" "(" expression ")" statement ( "else" statement )? ;
> returnStmt -> "return" expression? ";" ;
> whileStmt -> "while" "(" expression ")" statement ;
> forStmt -> "for" "(" ( varDecl | exprStmt | ";" ) expression? ";" expression? ")" statement ;
>
> expression -> assignment ;
> assignment -> IDENTIFIER "=" assignment | logic_or ;
> logic_or -> logic_and ( "or" logic_and )* ;
> logic_and -> equality ( "and" equality )* ;
> equality -> comparison ( ( "!=" | "==" ) comparison )* ;
> comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
> term -> factor ( ( "-" | "+" ) factor )* ;
> factor -> unary ( ( "\" | "\*" ) unary )\* ; // (IGNORE \'s) ;
> unary -> ( "!" | "-" ) unary | call ;
> call -> primary ( "(" arguments? ")" )* ;
> arguments -> expression ( "," expression )* ;
> primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER ;
# resources
following the book <a href="https://craftinginterpreters.com/">Crafting Interpreters by Robert Nystrom</a>
