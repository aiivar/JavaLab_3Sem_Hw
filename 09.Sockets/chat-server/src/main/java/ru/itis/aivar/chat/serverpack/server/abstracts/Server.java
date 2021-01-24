package ru.itis.aivar.chat.serverpack.server.abstracts;


import ru.itis.aivar.chat.protocol.Message;
import ru.itis.aivar.chat.serverpack.server.Connection;
import ru.itis.aivar.chat.serverpack.server.exceptions.ChatServerException;

public interface Server {


    void registerListener(ServerEventListener listener) throws ChatServerException;

    void sendMessage(Connection connection, Message message) throws ChatServerException;
    void sendBroadcastMessage(Message message) throws ChatServerException;
    void sendBroadcastMessageExcept(Connection exceptConnection, Message message) throws ChatServerException;
    void handleConnection(Connection connection) throws ChatServerException;
    void start() throws ChatServerException;
    void removeConnection(Connection connection);

}
