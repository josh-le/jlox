run: src/main/java/com/craftinginterpreters/lox/*.java
	javac -d bin/classes -cp src/main/java src/main/java/com/craftinginterpreters/lox/*.java
	java -cp bin/classes com.craftinginterpreters.lox.Lox
