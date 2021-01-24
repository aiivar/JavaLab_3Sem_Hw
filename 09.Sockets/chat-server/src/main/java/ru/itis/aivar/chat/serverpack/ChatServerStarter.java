package ru.itis.aivar.chat.serverpack;

import com.beust.jcommander.JCommander;
import ru.itis.aivar.chat.serverpack.server.ChatServer;
import ru.itis.aivar.chat.serverpack.server.exceptions.ChatServerException;
import ru.itis.aivar.chat.serverpack.server.listeners.ExitListener;
import ru.itis.aivar.chat.serverpack.server.listeners.MessageListener;


public class ChatServerStarter extends ChatServer {

    public static void main(String[] argv) {
        Args args = new Args();
        JCommander.newBuilder().addObject(args).build().parse(argv);
        int port = Integer.parseInt(args.port);
        ChatServerStarter server = new ChatServerStarter(port);
        try {
            server.registerListener(new ExitListener());
            server.registerListener(new MessageListener());
            server.start();
        } catch (ChatServerException e) {
            throw new IllegalStateException(e);
        }
    }

    public ChatServerStarter(int port) {
        super(port);
    }
}
