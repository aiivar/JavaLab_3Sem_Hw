package ru.itis.aivar.chat.serverpack.server.abstracts;



import ru.itis.aivar.chat.protocol.Message;
import ru.itis.aivar.chat.serverpack.server.ChatServer;
import ru.itis.aivar.chat.serverpack.server.Connection;
import ru.itis.aivar.chat.serverpack.server.exceptions.ServerEventListenerException;

import java.util.*;

public abstract class AbstractServerEventListener implements ServerEventListener{

    protected boolean init;
    protected ChatServer server;
    protected Queue<Pair<Connection, Message>> queue;
    protected List<Integer> types = new ArrayList<>();

    @Override
    public void init(ChatServer server) {
        this.init = true;
        this.server = server;
        this.queue = new LinkedList<>();
        //Add types in inheritor-class
    }

    @Override
    public void submit(Connection connection, Message message) {
        queue.offer(new Pair<>(connection, message));
    }

    @Override
    public List<Integer> getTypes() {
        return types;
    }

    @Override
    public void run() {
        while (true){
            Thread.yield();
            if (!queue.isEmpty()){
                try {
                    Pair<Connection, Message> pair = queue.poll();
                    Connection connection = pair.getFirst();
                    Message message = pair.getSecond();
                    handle(connection, message);
                } catch (ServerEventListenerException e) {
                    throw new IllegalStateException("Can't handle message");
                }
            }
        }
    }
}
