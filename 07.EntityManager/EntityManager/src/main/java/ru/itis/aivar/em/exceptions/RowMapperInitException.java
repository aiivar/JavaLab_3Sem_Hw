package ru.itis.aivar.em.exceptions;

public class RowMapperInitException extends RuntimeException{
    public RowMapperInitException() {
        super();
    }

    public RowMapperInitException(String message) {
        super(message);
    }

    public RowMapperInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public RowMapperInitException(Throwable cause) {
        super(cause);
    }

    protected RowMapperInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
