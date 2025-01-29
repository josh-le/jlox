run: src/main/java/com/craftinginterpreters/lox/*.java
	javac -d bin/classes -cp src/main/java src/main/java/com/craftinginterpreters/lox/*.java
	java -cp bin/classes com.craftinginterpreters.lox.Lox

genast: src/main/java/com/craftinginterpreters/tool/GenerateAst.java
	# i think maybe i should recompile the lox dir as well?: craftinginterpreters/* in the first line
	javac -d bin/classes -cp src/main/java src/main/java/com/craftinginterpreters/tool/GenerateAst.java
	java -cp bin/classes com.craftinginterpreters.tool.GenerateAst src/main/java/com/craftinginterpreters/lox

# pretty print AST
# probably should also recompile all directories?
pp: src/main/java/com/craftinginterpreters/lox/AstPrinter.java
	javac -d bin/classes -cp src/main/java src/main/java/com/craftinginterpreters/lox/*.java
	java -cp bin/classes com.craftinginterpreters.lox.AstPrinter
