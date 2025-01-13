run: src/main/java/com/craftinginterpreters/lox/*.java
	javac -d bin/classes -cp src/main/java src/main/java/com/craftinginterpreters/lox/*.java
	java -cp bin/classes com.craftinginterpreters.lox.Lox

genast: src/main/java/com/craftinginterpreters/tool/GenerateAst.java
	javac -d bin/classes -cp src/main/java src/main/java/com/craftinginterpreters/tool/GenerateAst.java
	java -cp bin/classes com.craftinginterpreters.tool.GenerateAst src/main/java/com/craftinginterpreters/lox
