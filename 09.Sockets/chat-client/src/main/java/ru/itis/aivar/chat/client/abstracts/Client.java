package ru.itis.aivar.chat.client.abstracts;


import ru.itis.aivar.chat.client.exceptions.ChatClientException;
import ru.itis.aivar.chat.protocol.Message;

public interface Client {

    void connect() throws ChatClientException;
    void disconnect();
    void sendMessage(Message message) throws ChatClientException;
    void registerListener(ClientEventListener listener) throws ChatClientException;
}
