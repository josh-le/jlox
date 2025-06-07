# jlox
a tree-walk interpreter for the Lox language written in Java
## table of contents
- [running the interpreter](#running-the-interpreter)
- [material covered](#material-covered)
- [formal grammar](#formal-grammar)
- [todo](#todo)
- [resources](#resources)
## running the interpreter
first, ensure you have java installed
```bash
git clone https://github.com/josh-leblanc/jlox
cd jlox

# running a file
make FILE=main.lox

# running the REPL
make repl
> print 2 + 3;
5
> print "a" + "b"
ab

# running the test file
make test # compiles the interpreter and runs the file test.lox
```
this will run a lox REPL, support for running files is coming soon
## material covered
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
- functions
    - native functions
    - function declarations
    - function objects
    - return statements
    - local functions and closures
- resolver
    - resolver class to do a pass over the tree before the interpreter to perform semantic analysis
        - current functionality is to store which nested scope variables are stored in in relation to the current scope, but could be expanded a lot

__topics covered:__
- formal grammar
- design patterns:
    - visitor
    - sentinel classes
- associativity and precedence
- recursive descent
- evaluating and interpreting the syntax tree
- control flow
- scopes, environments and closures
- semantic analysis
## formal grammar
definition of the formal grammar of the lox programming language, updated as we go along
> program -> declaration* EOF ;
>
> declaration -> funDecl | varDecl | statement ;
>
> funDecl -> "fun" function ; <br>
> function -> IDENTIFIER "(" parameters? ")" block ; <br>
> varDecl -> "var" IDENTIFIER ( "=" expression )? ";" ; <br>
> parameters -> IDENTIFIER ( "," IDENTIFIER )* ; <br>
>
> statement -> exprStmt | printStmt | block | ifStmt | returnStmt | whileStmt | forStmt ;
>
> exprStmt -> expression ";" ; <br>
> printStmt -> "print" expression ";" ; <br>
> block -> "{" declaration* "}" ; <br>
> ifStmt -> "if" "(" expression ")" statement ( "else" statement )? ; <br>
> returnStmt -> "return" expression? ";" ; <br>
> whileStmt -> "while" "(" expression ")" statement ; <br>
> forStmt -> "for" "(" ( varDecl | exprStmt | ";" ) expression? ";" expression? ")" statement ;
>
> expression -> assignment ; <br>
> assignment -> IDENTIFIER "=" assignment | logic_or ; <br>
> logic_or -> logic_and ( "or" logic_and )* ; <br>
> logic_and -> equality ( "and" equality )* ; <br>
> equality -> comparison ( ( "!=" | "==" ) comparison )* ; <br>
> comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ; <br>
> term -> factor ( ( "-" | "+" ) factor )* ; <br>
> factor -> unary ( ( "\" | "\*" ) unary )\* ; // (IGNORE \'s) ; <br>
> unary -> ( "!" | "-" ) unary | call ; <br>
> call -> primary ( "(" arguments? ")" )* ; <br>
> arguments -> expression ( "," expression )* ; <br>
> primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER ;
## todo
main functionality of the interpreter is finished, but there are still things that can be added:
- ch 12 and 13 to implement classes
- challenges for pretty much all the chapters
## resources
following the book <a href="https://craftinginterpreters.com/">Crafting Interpreters by Robert Nystrom</a>
