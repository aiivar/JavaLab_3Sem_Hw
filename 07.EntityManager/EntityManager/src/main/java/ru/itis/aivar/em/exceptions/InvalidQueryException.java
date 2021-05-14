package ru.itis.aivar.em.exceptions;

public class InvalidQueryException extends Exception{
    public InvalidQueryException() {
        super();
    }

    public InvalidQueryException(String message) {
        super(message);
    }

    public InvalidQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidQueryException(Throwable cause) {
        super(cause);
    }

    protected InvalidQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
