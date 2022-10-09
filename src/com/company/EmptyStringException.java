package com.company;
public class EmptyStringException extends IllegalArgumentException {
    public EmptyStringException() {
        super("Передана пустая строка!");
    }
}
