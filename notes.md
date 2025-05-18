---
id: Crafting Interpreters - Robert Nystrom
aliases: []
tags:
  - books
  - in-progress
---
[[Build Your Own Interpreter - Code Crafters]] we're reading the book instead
# 1. introduction
## why learn this stuff
you encounter little different languages everywhere

it's good practice

this guy likes languages.
## how the book is orgnized
intro, compiler in java, compiler in C

ok it says it just does the same thing twice in a row so I think I'm just going to start with the C one.

there are **challenges**, and some of them require to modify your code. make a new branch for that, because the future chapters rely on *unchallenged* code

ok i might go back and do the Java one.
### challenges
#### hello world in Java
file HelloWorld.java:
```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```
makefile:
```makefile
compile:
    javac HelloWorld.java
run: compile
    java HelloWorld
debug: compile
    jdb HelloWorld
clean:
    rm -f *.class
```
debugging:
  stop in HelloWorld.main: Set a breakpoint at the main method.
  run: Start the program.
  next: Step to the next line.
  step: Step into the next method call.
  print variableName: Print the value of a variable.
  cont: Continue execution until the next breakpoint.
  exit: Exit the debugger.
# 2. a map of the territory
## parts of a language
### scanning
also known as lexing, lexical analysis. it's just chunking the text in the file into tokens.
### parsing
a parser takes the tokens and builds an **Abstract Syntax Tree** that holds the structure of the grammar. the parser also reports **syntax errors**
### static analysis
first bit is called **binding or resolution**. each **identifier** is matched to a definition and scope

this is where we type check if the language is statically typed. if a and b are can't be added together, it throws a type error.

this information is stored in:
  - the AST
  - a symbol table (lookup table for variables)
  - a more powerful data structure we will learn about later

everything so far is part of the **front-end**. there is also **middle-end and back-end**
### intermediate representations
front-end of the compiler is generally handing the language specific things and the back-end of the compiler is generally handling the architecture-specific things.

between these stages, the code is in an **intermediate representation** that can be generalized to different languages and architectures. (GIMPLE and RTL).
### optimization
once we understand what the program means, we can replace things with the same semantics to **optimize** the code.

"If you can’t resist poking your foot into that hole, some keywords to get you started are “constant propagation”, “common subexpression elimination”, “loop invariant code motion”, “global value numbering”, “strength reduction”, “scalar replacement of aggregates”, “dead code elimination”, and “loop unrolling”."
### code generation
we are now in the **back-end**.

**bytecode** is made for a hypothetical idealized machine to not be so tied to the particular architecture of one computer.
### virtual machine
no chips speak bytecode so you have to translate it to the native code of the architecture of what chip you're on

an alternative is a **(language/process) virtual machine** that emulates a chip that would support the bytecode architecture, but this is slow. this is separate from a **system virtual machine** which actually emulates the hardware of another machine.
### runtime
garbage collection, keeping track of types during execution

in go, each compiled application  has its own copy of Go's runtime directly embedded in it.

if the language is run in an interpreter then the runtime is in there.
## shortcuts and alternate routes
above covers everything we might implement in the compiler, but you necessarily need to do all of it.
### single pass compilers
the parser directly outputs code, and does not create an AST or any other IRs. once code is parsed once the compiler needs to know how to correctly compile it. Pascal and C were implemented with this type of compilation in mind, and it creates a lot of the limitations on those languages
### tree-walk interpreters
some languages begin executing code right after parsing it to an AST, with maybe a little static analysis involved.

the interpreter traverses the tree and executes the code as it goes.
### transpilers
transpilers turn source code of one language into source code of another language. when UNIX became popular a lot of transpilers targeted C, now it's mostly JavaScript to run things in the browser.
### just-in-time compilation
this is quite hard, and it compiles the code while the program is running. usually to adapt to a certain architecture.
## compilers and interpreters
compilers and interpreters are not mutually exclusive

**compiling** involves translating a source language to anohter form. usually it's to lower level code but not necessarily. 

the main idea of an **interpreter** is that it executes code immediately, whereas a compiler just generates a file that needs to be executed
## our journey
that was a lot of info, not supposed to understand it all now.
# 3. the lox language
## hello lox
```lox
// HelloWorld.lox
print "Hello, world!";
```
## a high level language
lox is a ^
### dynamic typing
variables can store values of any type, and a single variable can store different types at different times. type errors are reported at runtime.

dynamic typing is actually easier to implement in the interpreter than static typing
### automatic memory management
two main techniques: **reference counting** and **tracing grabage collection**. reference counting is easier but has limitations so most good languages use some sort of garbage collector

we are going to build a garbage collector :)
## data types
booleans: true, false

one kind of number: double precision floating point

strings enclosed in souble quotes

nil is null.
## expressions
arithmetic - normal stuff as well as concatenating strings with + and negating numbers when there is a plus sign in front of them. errors thrown if you try to use an operator on two different types

equalities allow different types to be compared but it's always false, comparisons are present as well.

logical operators: !, and, or

and and or return the left operand if it's true and the right operand if it's false. that's weird?
## statements
where an expression's main job is to produce a value, the statement's main job is to produce an effect. 

an expression turns into a statement when you put a semicolon after it.

you can use curly brackets to create a new scope
## variables
declared with the `var` variable. defaults to nil if not initialized. 
## control flow
lifting if, for and while right from C
## functions
there is no difference between the declaration of a function and the definition of a function, they happen in the same line

calling a function has the same format as C

we will use the fun keyword for definintion of a function

an **argument** is something that is passed into a function that is being called. a **parameter** is the variable that holds the value of a parameter inside the body of the function
### closures
functions in Lox are *first-class*, which means they are actual values that you can get references to, store in variables, and pass around. this works:
```lox
// first class demonstration
fun addPair(a, b) {
    return a + b;
}
 
fun identity(a) {
    return a;
  }

print identity(addPair)(1, 2); // Prints "3".

// local functions
fun outerFunction() {
  fun localFunction() {
    print "I'm local!";
  }
  localFunction();
}

// closures
fun returnFunction() {
  var outside = "outside";
  fun inner() {
    print outside;
  }
  return inner;
}
var fn = returnFunction();
fn(); // prints "outside"
```
this also means that we can create higher level functions and return them from other functions

since the function references a value in the other function, the other function is considered a **closure**, and it has to keep the values of the function present in memory as the code continues to execute
## classes
Lox is going to be object oriented
### classes or prototypes
in class-based languages there are classes that hold the inheritance chain and methods, and instances that hold the state and a reference to the class of the object

in prototypal languages there are only object that inherit from one another, not sure I exactly understand this completely. they are simpler than classed languages but people usually just end up making classes with the protocols, so Lox will have classes
### classes in Lox
declare like so:
```lox
class Breakfast {
  cook() {
    print "Eggs a-fryin'!";
  }
  serve(who) {
    print "Enjoy your breakfast, " + who + ".";
  }
} 
var someVariable = Breakfast();
```
wow ok properties can just be dynamically assigned, you don't need to define the fields in the class definition.
```lox
breakfast.meat = "sausage";
```
the equivalent of python's self is `this` in the class method definitions

all classes can have an `init` method that is automatically called when an instance is created.
### inheritance
```lox
class Brunch < Breakfast {
  init(meat, bread, drink) {
    super.init(meat, bread);
    this.drink = drink;
  }
  drink() {
    print "How about a Bloody Mary?";
  }
}
```
lox is not completely object oriented because primitive types are not actually objects
## standard library
"goes beyond minimalism to outright nihilism" lmao

we have a built in print statement and then a clock function, that's it. if you want to make the language better once the book is done, this is the place to start.
# jlox - most of the book from here on out is going to be writing the interpreters, so those will be new md files
alright, time to start writing the [[jlox]] interpreter.
# 4. scanning
scanning is taking the text file and turning it into tokens

ok it does not look like the book will help me do it from scratch in C, might be worth doing it in java
update: doing it in java, we're reading the whole book
## recognizing lexemes
java is confusing as fuck but it looks like he's pretty much just using a switch statement.
### lexical errors
stopping around page 42 of pdf
# 5. representing code
## context free grammars
we are now using **context-free grammar** as our *formal grammar*, as opposed to regular language from the last section

we are moving from strictly lexical parsing into processing of syntactic grammars
### rules for grammars
to define certain strings in a grammar, you set up rules, also called **productions**. the strings that come from these rules are called **derivations**

productions have a **head** which is the name, and a **body** which describes what it generates. the body in its purest form is simply a list of **symbols**

two types of *symbols*: **terminal** which is something that doesn't need to be evaluated by another rule, and a **nonterminal** which is basically a pointer to another rule that needs to be evaluated

p.s. you can have multiple rules or productions with the same name.
### enhancing our notation
our example: breakfast
> each rule points to a combination of strings and other rules. the other rules get evaluated to end up with a long string.
> time for some syntactic sugar to make things nicer:
    > use () and | to group expressions and allow multiple options for each rule
    > abbreviate rules with *, + and ? to set repetition amounts

__EXAMPLE:__
[[crafting interpreters Lox grammar example]]
### a grammar for Lox expressions
we are not going to implement the entire syntactic grammar at once, we'll do just a little for now an add more later once the interpreter is up and running

for now, we will do these:
_literals_ - numbers, strings, booleans and nil
_unary expressions_ - a prefix ! to perform not and - for negative numbers
_binary expressions_ - infix arithmetic, logical operators
_parentheses_ - ()

in our grammar notation, how to implement those:
```
expression     → literal
               | unary
               | binary
               | grouping ;

literal        → NUMBER | STRING | "true" | "false" | "nil" ;
grouping       → "(" expression ")" ;
unary          → ( "-" | "!" ) expression ;
binary         → expression operator expression ;
operator       → "==" | "!=" | "<" | "<=" | ">" | ">="
               | "+"  | "-"  | "*" | "/" ;
```
notice here we capitalize NUMBER and STRING because they are single lexemes that can expand to multiple things
## implementing syntax trees
since we have recursive elements in our grammar - grouping, unary, binary refer back to expression - the data structure will form a tree. Since it represent the syntax of our code it is called a __syntax tree__.

the class Expr is a base class that each expression type is a subclass of
### disoriented objects
the tree object is not owned by the parser or interpreter, and has no methods
### metaprogramming the trees
alright, if I'm understanding this right, we are writing a tool that is seperate from the package that is a script to generate the classes for the syntax tree.

i was understanding right
## 5.3 working with trees
### the expression problem
alright the problem we have now is that to interpret, resolve, analyze our expressions we would need to define all of those methods on each of the classes, and when we want to create a new expression type we will have to write all new methods for it

conversely, if we do our definitions functionally, we will still have to add a pattern match to each new function for each type.

this is called the **Expression Problem** because it was first stumbled upon in the exact problem we are confronting now
### the visitor pattern
the visitor pattern is the most widely misunderstood design pattern
#### example:
say we have two kinds of pastries: beignets and crullers
```Java
abstract class Pastry {
}
class Beignet extends Pastry {
}
class Cruller extends Pastry {
}
```
we want to be able to add new pastry operations without having to define a new method to each class every time. here's how to do it:

first, define a seperate interface:
```java
interface PastryVisitor {
    void visitBeignet(Beignet beignet);
    void visitCruller(Cruller cruller);
}
```

each operation that can be performed on pastries is a new class that implements that interface, and it has a concrete method for each type of pastry. this keeps all methods together in one class

each subclass implements it

__NOTE:__
method **overloading** is when you have multiple implementations of a method with the same name in a class and they have different parameters
method **overriding** is when a subclass implements a method that is already defined in the parent class
```Java
abstract class Pastry {
    abstract void accept(PastryVisitor visitor);
}
class Beignet extends Pastry {
    @Override
    void accept(PastryVisitor visitor) {
        visitor.visitBeignet(this);
    }
}
class Cruller extends Pastry {
    @Override
    void accept(PastryVisitor visitor) {
        visitor.visitCruller(this);
    }
}
```
this lets us use *polymorphic dispatch* on the pastry classes to select the appropriate method on the visitor class

not sure i understand this, go over later
### visitors for expressions
in the above example, the functions return void, but we want our visitor functions to return things, and they're not necessarily the same type. so we will use generics

we are adding things to our GenerateAst script that are:
- defining the visitor interface, defining a visit method for each subclass.
- defining the abstract accept() method in the base class
- defining an accept() method in each subclass that calls the right visit method for it's type

__syn__:
i believe the purpose of this is to just keep all of the function definitions in the same class? not 100% sure tho
## 5.4 a (not very) pretty printer
making a pretty printer so that we can check if the AST is parsing correctly

__EX:__
if we are parsing: -123 * (45.67)
the pretty printer will print: (* (- 123) (group 45.67))

first, we create a new class (for some reason it's not in the tool package, it's in the lox package) called AstPrinter

the class implements the Visitor interface and defines functions for visiting each of our expression types and printing them

we are also writing a temporary main function for testing, because we haven't actually built our parser yet.

wow it worked :)
## challenges
skipping again for now :)
# 6. parsing expressions
time to write a parser :)
## 6.1 ambiguity and the parsing game
there is currently ambiguity in our grammar. for example:
expression: 6 / 3 - 1
could be parsed as (6 / 3) - 1 OR 6 / (3 - 1)

the way this is fixed is by defining rules for _precedence_ and _ambiguity_

__precedence__ is which operators are exaluated first in an expression with different operators, for example evaluating the / before the -
    operators with higher precedence are evaluated before operators with lower precedence
__associativity__ is which is evaluated first in an expression with the same operator, usually left to right. you can also have __non-associative__ operators which does not allow more than one of the operators in an expression. - is left associative, and assignment (=) is right associative

in lox, we are going to define the same presedence and associativity rules as C, this goes from lowest to highest precedence:
> Name	    Operators	Associates
> Equality	== !=	    Left
> Comparison	> >= < <=	Left
> Term	    - +	        Left
> Factor	    / *	        Left
> Unary	    ! -	        Right

currently, our grammar has all expressions in one rule. to institute precedence, we will create a new rule for each precedence level:
> expression -> ...
> equality -> ...
> comparison -> ...
> term -> ...
> factor -> ...
> unary -> ...
> primary -> ...

each rule above only matches expressions at its precedence level or higher

for example, unary only can match a unary like !negated or primary like 1234, and a term can match 1 + 2 and 3 * 4 / 5

now we write the productions for the new rules:

mapping expression to equality will allow it to map to all other rules
> expression -> equality ;

primary matches all literals and grouping expressions
> primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;

for unary we make it recursive because you can have a unary operator on a unary expression
> unary -> ( "!" | "-" ) unary | primary ;

now for multiplication and division. putting the recursive factor on the left side makes it left associative (does this even really matter?) this uses left recursion, which can apparently cause problems in parsers? he doesn't elaborate
> factor -> factor ( "/" | "*" ) unary | unary ;

instead we will define it as a _flat sequence_ of multiplitcations and divisions
> factor -> unary ( ( "\" | "\*" ) unary )\* ; // (IGNORE \'s) ;

now the rest, unexplained:
> equality -> comparison ( ( "!=" | "==" ) comparison )* ;
> comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
> term -> factor ( ( "-" | "+" ) factor )* ;
## 6.2 Recursive Descent Parsing
many different parsing techniques: LL(k), LR(1), LALR, parser combinators, Earley parsers, the shunting yard algorithm, packrat parsing

for our first interpreter, __recursive descent__ will be sufficient. simple but powerful

recursive descent is a __top-down parser__ because it starts from the top/outermost grammar rule (expression) and works its way down to the leaves of the AST

don't get confused: in a _top-down_ parser you reach the lowest-precedence expressions first because they may contain subexpressions of higher precedence

each rule will become a function. we can basically directly translate the rules:
- terminal - code to match or consume a token
- nonterminal - call to that rule's function
- | - if or switch statement
- * or + - while or for loop
- ? - if statement
### 6.2.1 the parser class
new parser class. each grammar rule will be a method on this class

not really taking notes, just look at the code. we are just translating the rules that we wrote about above

__QUESTION:__
why do we have to import the TokenType but not the Expr class?
## 6.3 syntax errors
the parsers two jobs are to:
- turn correct code into a syntax tree
- reject incorrect code

needs:
- detect and report errors
- avoid crashing or hanging (any valid string should be at least parsed)
wants:
- fast
- report all errors, not just one and drop
- avoid cascaded errors
### 6.3.1 panic mode error recovery
when the parser detects an error is enters panic mode

when we are in panic mode, if we would like to keep parsing after reporting the error, the parser must be able to __synchronize__.

it will jump out of rules until a target rule (statement for us, which we don't have yet) is reached and it can continue parsing
### 6.3.2 entering panic mode
mostly code here

our error method on the parser class does not throw the error directly because if the error isn't that bad you don't have to enter panic mode. another way to handle this is with __error productions__

however consume does throw an error?
### 6.3.3 synchronizing a recursive descent parser
the parser's state (the rule that it is parsing) is not tracked in fields. each rule being parsed is a function call so it is a frame on the call stack

when we want to synchronize, we will clear out the call stack frames. we will do that by __throwing an exception__, and catching the exception at the statement level which is where we want to synchronize to
## 6.4 wiring up the parser
we're not implementing much of our error handling yet, just calling the parser and returning null if it doesn't work

parse the text, then print with the AstPrinter
## challenges
skipping for now

1. add comma seperated expressions
2. add support for ternary operator ?:
3. add error for binary without left hand operator
# 7. evaluating expressions
two main questions in evaluating expressions:
1. what kinds of values do we produce?
2. how do we organize the chunks of code?
## 7.1 representing values
variables will be able to be represented with Java's Object class, allowing them to take on any value
## 7.2 evaluating expressions
our interpreter implements the Visitor interface
### 7.2.1 evaluating literals
a __literal__ is a piece of syntax that evaluates into a _value_

in the parser, we converted the literal _token_ into a _syntax tree node_. now we will convert the tree node into a __runtime value__
### 7.2.2 evaluating parentheses
just plucking the grouped expression off the grouping tree node and evaluating it, which calls .accept(this) on it.

__still not clear on the .accept(this) function and the whole visitor thing__
### 7.2.3 evaluating unary
evaluate the right expression first, then we will apply the unary operator to it

our interpreter is doing a __post-order traversal__ of the syntax tree, because each node evaluates it's children before the node itself is evaluated

to evaluate the bang operator we need to implement the concept of __truthiness__
### 7.2.4 truthiness and falsiness
in Lox, `false` and `nil` are falsy and literally everything else is true. we could implement other values 
### 7.2.5 evaluating binary operators
we have to handle a few cases here:
- comparisons, subtraction, division, multiplication are all casts to doubles and then you perform the operation.
- addition you have to check if it's adding numbers or concatenating strings first
- equal and not equal have a helper function because == just compares references in java
## 7.3 runtime errors
the runtime error should not completely kill the interpreter
### 7.3.1 detecting runtime errors
creating a check number operator function to check operand types

if it fails, it throws a RuntimeError which is a class that we created that extends a RuntimeException and contains the token so the error can be tracked

that was for unary, we also did the same for binary expresisons.
## 7.4 hooking up the interpreter
creating an interpret function that takes in a syntax tree, evaluates it, and the prints the result.
### 7.4.1 reporting runtime errors
function in Lox class that prints the error to report it

also creating a hadRuntimeError attribute on the Lox class

then, instead of printing the AST we pass it to the interpreter. now we're ready!!
### challenges
once again leaving for later, we just want to get this done
# 8. statements and state
we're going to add statement handling to the interpreter
## 8.1 statements
two main types:
1. expression statement - evaluate an expression that has side effects, like a function call
2. print statement - baking this right into the language so we can use it before the interpreter is actually complete

__new grammar rules:__
```
program -> statement* EOF ;

statement -> exprStmt | printStmt ;

exprStmt -> expression ";" ;
printStmt -> "print" expression ";" ;
```
### 8.1.1 statement syntax trees
we just added print and expression statements to the generate ast script, and ran it to product the Stmt.java class file.
### 8.1.2 parsing statements
edited the parser so that it respects our new grammar that contains a program as a series of statements and then EOF
### 8.1.3 executing statements
we are creating a new visitor interface, Stmt.Visitor because statements have their own base class
## 8.2 global variables
before we deal with scoping, we will implement the easiest variable - the global variable
### 8.2.1 variable syntax
updated the genast script to include the variable declaration syntax and ran it
### 8.2.2 parsing variables
changed the parser to follow the new grammar, it parses declarations instead of statements now
## 8.3 environments
the environment is just a hash table that maps the variable names to the variable values

its going to be its own class

we are choosing to allow variable __redifinition__. this could be an error if we want

if we can't find the variable in the map we throw a runtime error instead of a syntax error so that variables can be used in code before they are defined as long as they are not evaluated.
### 8.3.1 interpreting global variables
the interpreter has an instance of the environment class

then we implement visitor statements for variable statements and variable expressions that talk to the environment to define and evaluate the variables, respectively.
## 8.4 assignment
### 8.4.1 assignment syntax
updated the grammar to handle assignments

parsing the assignments is a little tricky because unlike other expressions, we don't want to evaluate the left hand side of the expression, we just want to save the variable it is corresponding to
### 8.4.2 assignment semantics
since we have a new syntax tree node, the interpreter gets a new visit method
## 8.5 scope
__lexical scope__ is when you are able to see the actual scopes in the code. lox uses this for variables. we can scope with curly braces. this is called __block scope__

__dynamic scope__ isn't knowable until runtime, which lox will implement for object methods and fields
### 8.5.1 nesting and shadowing
tweaking the environment so there is an enclosing scope that gets checked if the variable is not found in the current scope
### 8.5.2 block syntax and semantics
updated formal grammar to include blocks

implemented blocks in the genast scripts so there is a node type in the tree for block statements

implemented the visit method which creates a new environment then executes all of the statements within the block scope in the new environment, and replaces old one at end

also made a test target in the makefile to run the test.lox file
## challenges
leaving these for after again but they look fun
# 9. control flow
alright, this chapter is easy and it will grant our interpreter __turing completeness__
## 9.1 turing machines (briefly)
__turing machine__ was a machine that was used to compute all computable functions

if our language can do that, it is considered __turing complete__
## 9.2 conditional execution
__conditional__ control flow is being able to not execute some code
__looping__ control flow is being able to execute some code multiple times

in C, `if` lets you conditionally execute statements and the ternary operator lets you conditionally execute expressions

WOAH there only needs to be one statement in the if statement syntax because if we want more than one statement we use curly braces to make it a block!!! this is why C and java have that syntax!!!

adding a new node in the syntax tree for if statements

adding a check in the Stmt part of the parser to parse if statements

we have come upon the __dangling else__ problem which is common in parsing
    solve this by just attaching the else to the most recent if
## 9.3 logical operators
implemented `and` and `or`, only unique thing about these is the short circuit so we evaluate the left operand first, and check if it short circuits then eval the right if necessary
## 9.4 while loops
steps (we repeat these a lot):
1. update the grammar with whileStmt rule
2. create a node in the syntax tree for the whileStmt (update and run genast script)
3. edit `statement` function in parser to contain a match(WHILE) that calls the whileStatement function()
    3a. whileStatement function parses the while statement, returns the Stmt.While node
4. then in the interpreter, we implement our visitWhile interface, where we just test if the condition is true, and execute the statement while that is happening
## 9.5 for loops
for loops don't actually add any new functionality, they are just __syntactic sugar__

on the back end, we are going to do some __desugaring__ and use the while loop logic we already have 

that being said, we do not need to create a new syntax tree node, because we will use the while one. we go straight to the parser

we only had to touch the parser, all the desugaring happens there
## challenges
1. once we have _dynamic dispatch_ and _first class functions_, lox won't need branching statements. show how conditional logic can be implemented using those
2. looping can be implemented with those concepts as well, with a certain optimization, what is it?
3. implement `break` statements. this is a good one
# 10. functions
## 10.1 function calls
updating the grammar and committing bc im a loser lol

updated the syntax tree to contain a call expression
### 10.1.1 maximum argument counts
set a maximum argument count of 255 for our functions just in case there's some freak that tries to use this language
### 10.1.2 interpreting function calls
implementing the visitor method for calls
### 10.1.3 call type errors
making sure the callee is either a function or a class before we call it
### 10.1.4 checking arity
__arity__ is the number of arguments a function or operator expects.

we are going to throw a runtime error if there are too many or too few arguments provided
## 10.2 native functions

we are going to throw a runtime error if there are too many or too few arguments provided
## 10.2 native functions
__native functions__, aka primitives, eternal functions, foreign functions are implemented in the _host_ language

many languages have a __foreign function interface__ (FFI) that allows the user to implement functions in the host language themselves.
### 10.2.1 telling time
many languages have `print` as a native function, however we already implemented that.

instead, to benchmark things, we are going to create a `clock` function that will give the elapsed time by taking the difference of two calls

we created a global scope where the function will live

and the function is defined in the constructor of the interpreter class.

Lox is considered a Lisp-1 language because it stores variables and functions in the same namespace, Lisp-2 languages have seperate namespaces for each.
## 10.3 function declarations
new grammar rules for funDecl, function and parameters

implemented those rules in the parser
## 10.4 function objects
creating a new class to hold functions that implements LoxCallable

important not that we are creating a new environment at each call, which for example enables recursion to work

thne we can go into the interpreter and implement the visitFunctionStmt methods which takes the function statement syntax node and turns it into the runtime LoxFunction and binds it to a variable

"I don’t know about you, but that looks like an honest-to-God programming language to me." - this makes me very happy, i love this book
## 10.5 return statements
return statement. functions always technically have to return something in lox, it is nil by default
### 10.5.1 returning from calls
when you return you have to jump out of a bunch of calls, the way we will do that is with an exception

not very good way to do it, but here we evaluate our syntax tree by getting super deep recursively into the java call stack, and exceptions are a good way to escape that.

the exception is caught in the block execution part of the LoxCallable class.
## 10.6 local functions and closures
when we define a function, we save the environment at that time in a new environment stored in the function called __closure__.

when we run the function, we set the environment to be the closure stored on that function
## challenges
1. __smalltalk__ implementations don't have the performance cost of checking the number of params and args are equal at function call time, idek what those are
2. THIS ONE IS FUN: implement __anonymous (lambda) functions__.
    How do you handle the tricky case of an anonymous function expression occurring in an expression statement:
    `fun () {};`
3. talking about how parameters are stored in the environment on function call, and redefining them in the body of the function. i believe it behaves how ti should right now.
# 11. resolving and binding
we created a hole in our scoping logic when we added closures. this chapter is about patching that hole. we will also learn about _semantic analysis_
## 11.1 static scope
clear scope definition:
    a variable usage refers to the preceding declaration with the same name in the innermost scope that encloses the expression where the variable is used

what the fuck. in javascript if you use `var` it hoists the declaration to the top of the scope and just assigns the variables at the location var is used?? what purpose would this server OTHER than creating bugs??
### 11.1.1 scopes and mutable environments
__alright:__
the problem is that when we create the function, it's closure is a reference to it's current environment. so, when we add new variables to the current environment, they are also added to the closure even though they shouldn't be
### 11.1.2 persistent environments
__persistent data structures__ are a type of data structure that is never modified, but when a modification is made a new structure is created with a copy of the original and the modifications made (in practice, so it doesn't use tons of memory, the structures "share" data)

so the correct way to do this would be to have our environment be a persistent data structure, and the closure be a reference to the environment at the time of the definition of the function. the reference would change whenever the environment would change because a new data structure would be created.
    _we're not gonna do that tho_
## 11.2 semantic analysis
we're going to resolve variables early by counting how many hops through environments we have to do to resolve the variable

if it is the same amount, we know the variable we are looking for is the same one in the same environment
### 11.2.1 a variable resolution pass
after the parser produces a syntax tree, and before the interpreter start executing, we will do a single pass over the tree to resolve all the variables
    passes over the tree between parsing and execution are common
## 11.3 a resolver class
the class implements the visitor abstractions because we have to implement all of the nodes, however only a few kinds of nodes will actually have to do something:
    - block statement
    - function declaration
    - variable declaration
    - variable expressions
    - assignment expressions
### 11.3.1 resolving blocks
we are only resolving variables in local scopes

each local scope creates a new scope on the scope stack, which is just a map whose keys are strings that represent the identifiers of the objects in scope
### 11.3.2 resolving variable declarations

