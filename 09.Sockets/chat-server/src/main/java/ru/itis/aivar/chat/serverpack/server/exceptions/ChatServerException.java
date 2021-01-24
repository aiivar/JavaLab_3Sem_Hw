package ru.itis.aivar.chat.serverpack.server.exceptions;

public class ChatServerException extends Exception{
    public ChatServerException() {
        super();
    }

    public ChatServerException(String message) {
        super(message);
    }

    public ChatServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatServerException(Throwable cause) {
        super(cause);
    }

    protected ChatServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
