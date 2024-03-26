package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

/*
 * We are storing Lox variables here
 * so
 * var x = 12345;
 * becomes
 * ['x'] => Integer object 12345
 * in the values hashmap
 */
class Environment {
    private final Map<String, Object> values = new HashMap<>();
    final Environment enclosing;

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) return enclosing.get(name); //Look in parent scope

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    //var x = 1
    void define(String name, Object value) {
        values.put(name, value);
    }

    //x=1
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value); //Look in parent scope
            return;
        }


        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

}
