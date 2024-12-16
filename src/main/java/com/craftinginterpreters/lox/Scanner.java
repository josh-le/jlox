package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
    // properties
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    // constructor
    Scanner(String source) {
        this.source = source;
    }

    // fill token list
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // beginning of next lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    // contruct token object and add to list
    private void scanToken() {
        char c = advance();
        switch (c) {
            // single token characters
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            // two character tokens
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            // STOPPED HERE
            case '=':
                addToken(match('
            default:
                Lox.error(line, "Unexpected character: " + c);
                break;
        }
    }

    // check if we are at end of string
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // move current up, return char
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    // add token, not sure why there's two
    private void addToken(TokenType type) {
        addToken(type, null);
    }
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
