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

__building:__

__topics covered:__
- formal grammar
- design patterns:
    - visitor
    - sentinel classes
- associativity and precedence
- recursive descent

# resources
following the book <a href="https://craftinginterpreters.com/">Crafting Interpreters by Robert Nystrom</a>
