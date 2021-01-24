package ru.itis.aivar.chat.client.abstracts;



import ru.itis.aivar.chat.client.exceptions.ClientEventListenerException;
import ru.itis.aivar.chat.protocol.Message;

import java.util.List;

public interface ClientEventListener extends Runnable{

    void init();
    void handle(Message message) throws ClientEventListenerException;
    List<Integer> getTypes();

    void submit(Message message);
}
