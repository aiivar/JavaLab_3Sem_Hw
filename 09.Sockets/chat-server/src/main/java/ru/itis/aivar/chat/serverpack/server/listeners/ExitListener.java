package ru.itis.aivar.chat.serverpack.server.listeners;

import ru.itis.aivar.chat.protocol.Message;
import ru.itis.aivar.chat.serverpack.server.ChatServer;
import ru.itis.aivar.chat.serverpack.server.Connection;
import ru.itis.aivar.chat.serverpack.server.abstracts.AbstractServerEventListener;
import ru.itis.aivar.chat.serverpack.server.exceptions.ChatServerException;
import ru.itis.aivar.chat.serverpack.server.exceptions.ServerEventListenerException;

import java.nio.charset.StandardCharsets;

public class ExitListener extends AbstractServerEventListener {
    @Override
    public void init(ChatServer server) {
        super.init(server);
        types.add(Message.TYPE_EXIT);
    }

    @Override
    public void handle(Connection connection, Message message) throws ServerEventListenerException {
        if (message.getType() == Message.TYPE_EXIT){
            connection.shutdown();
            try {
                server.sendBroadcastMessageExcept(connection, Message.createMessage(Message.TYPE_MESSAGE, connection.getUsername().concat(" disconnected...").getBytes(StandardCharsets.UTF_8)));
            } catch (ChatServerException e) {
                throw new ServerEventListenerException(e);
            }
        }
    }
}
