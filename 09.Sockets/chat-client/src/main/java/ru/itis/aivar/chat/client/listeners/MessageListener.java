package ru.itis.aivar.chat.client.listeners;

import ru.itis.aivar.chat.client.abstracts.AbstractClientEventListener;
import ru.itis.aivar.chat.client.exceptions.ClientEventListenerException;
import ru.itis.aivar.chat.protocol.Message;

public class MessageListener extends AbstractClientEventListener {

    @Override
    public void init() {
        super.init();
        types.add(Message.TYPE_MESSAGE);
    }

    @Override
    public void handle(Message message) throws ClientEventListenerException {
        if (message.getType() == Message.TYPE_MESSAGE){
            String messageStr = new String(message.getData());
            System.out.println(messageStr);
        }
    }
}
