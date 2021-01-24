package ru.itis.aivar.exceptions.db;

public class NoneUniqueValueException extends DbException{

    public NoneUniqueValueException() {
        super();
    }

    public NoneUniqueValueException(String message) {
        super(message);
    }

    public NoneUniqueValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoneUniqueValueException(Throwable cause) {
        super(cause);
    }

    protected NoneUniqueValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
