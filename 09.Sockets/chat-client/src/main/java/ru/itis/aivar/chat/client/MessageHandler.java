package ru.itis.aivar.chat.client;

import ru.itis.aivar.chat.client.abstracts.ClientEventListener;
import ru.itis.aivar.chat.protocol.Message;

import java.io.IOException;

public class MessageHandler implements Runnable{

    protected ChatClient client;

    public MessageHandler(ChatClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true){
            try {
                Message message = Message.readMessage(client.socket.getInputStream());
                for (ClientEventListener listener : client.listeners){
                    if (listener.getTypes().contains(message.getType())){
                        listener.submit(message);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
