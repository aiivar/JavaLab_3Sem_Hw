package ru.itis.aivar.chat.client.exceptions;

public class ChatClientException extends Exception{
    public ChatClientException() {
        super();
    }

    public ChatClientException(String message) {
        super(message);
    }

    public ChatClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatClientException(Throwable cause) {
        super(cause);
    }

    protected ChatClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
