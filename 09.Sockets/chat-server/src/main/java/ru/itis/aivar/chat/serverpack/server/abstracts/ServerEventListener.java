package ru.itis.aivar.chat.serverpack.server.abstracts;


import ru.itis.aivar.chat.protocol.Message;
import ru.itis.aivar.chat.serverpack.server.ChatServer;
import ru.itis.aivar.chat.serverpack.server.Connection;
import ru.itis.aivar.chat.serverpack.server.exceptions.ServerEventListenerException;

import java.util.List;

public interface ServerEventListener extends Runnable {

    void handle(Connection connection, Message message) throws ServerEventListenerException;

    void init(ChatServer server);

    void submit(Connection connection, Message message);
    List<Integer> getTypes();

}
