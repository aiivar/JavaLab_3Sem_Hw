package ru.itis.aivar.chat.serverpack.server.listeners;

import ru.itis.aivar.chat.protocol.Message;
import ru.itis.aivar.chat.serverpack.server.ChatServer;
import ru.itis.aivar.chat.serverpack.server.Connection;
import ru.itis.aivar.chat.serverpack.server.abstracts.AbstractServerEventListener;
import ru.itis.aivar.chat.serverpack.server.exceptions.ChatServerException;
import ru.itis.aivar.chat.serverpack.server.exceptions.ServerEventListenerException;

import java.nio.charset.StandardCharsets;

public class MessageListener extends AbstractServerEventListener {
    @Override
    public void init(ChatServer server) {
        super.init(server);
        types.add(Message.TYPE_MESSAGE);
    }

    @Override
    public void handle(Connection connection, Message message) throws ServerEventListenerException {
        if (message.getType() == Message.TYPE_MESSAGE){
            try {
                String str = new String(message.getData());
                str = connection.getUsername().concat(": ").concat(str);
                Message message1 = Message.createMessage(Message.TYPE_MESSAGE, str.getBytes(StandardCharsets.UTF_8));
                server.sendBroadcastMessageExcept(connection, message1);
            } catch (ChatServerException e) {
                throw new ServerEventListenerException(e);
            }
        }
    }
}
