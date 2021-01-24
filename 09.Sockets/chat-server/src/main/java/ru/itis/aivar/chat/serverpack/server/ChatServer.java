package ru.itis.aivar.chat.serverpack.server;

import ru.itis.aivar.chat.protocol.Message;
import ru.itis.aivar.chat.serverpack.server.abstracts.Server;
import ru.itis.aivar.chat.serverpack.server.abstracts.ServerEventListener;
import ru.itis.aivar.chat.serverpack.server.exceptions.ChatServerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServer implements Server {

    protected List<Connection> connections;
    protected List<ServerEventListener> listeners;
    protected ServerSocket serverSocket;
    protected boolean started;
    protected int port;

    public ChatServer(int port) {
        init(port);
    }

    @Override
    public void registerListener(ServerEventListener listener) throws ChatServerException {
        if (started) {
            throw new ChatServerException("Server has been started");
        }
        listener.init(this);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
        listeners.add(listener);
    }

    @Override
    public void sendMessage(Connection connection, Message message) throws ChatServerException {
        if (!started) {
            throw new ChatServerException("Server hasn't been started");
        }
        try {
            if (connections.contains(connection)) {
                connection.getSocket().getOutputStream().write(Message.getBytes(message));
                connection.getSocket().getOutputStream().flush();
            } else{
                throw new ChatServerException("No such connection");
            }
        } catch (IOException e) {
            throw new ChatServerException("Can't send message", e);
        }
    }

    @Override
    public void sendBroadcastMessage(Message message) throws ChatServerException {
        if (!started) {
            throw new ChatServerException("Server hasn't been started");
        }
        try {
            byte[] messageBytes = Message.getBytes(message);
            for (Connection connection : connections){
                connection.getSocket().getOutputStream().write(messageBytes);
                connection.getSocket().getOutputStream().flush();
            }
        }catch (IOException e){
            throw new ChatServerException("Can't send message", e);
        }
    }

    @Override
    public void sendBroadcastMessageExcept(Connection exceptConnection, Message message) throws ChatServerException {
        if (!started) {
            throw new ChatServerException("Server hasn't been started");
        }
        try {
            byte[] messageBytes = Message.getBytes(message);
            for (Connection connection : connections){
                if (connection.equals(exceptConnection)){
                    continue;
                }
                connection.getSocket().getOutputStream().write(messageBytes);
                connection.getSocket().getOutputStream().flush();
            }
        }catch (IOException e){
            throw new ChatServerException("Can't send message", e);
        }
    }

    @Override
    public void handleConnection(Connection connection) throws ChatServerException {
        try{
            Message message = Message.readMessage(connection.getSocket().getInputStream());
            System.out.println("New message:");
            System.out.println(Message.toString(message));
            for(ServerEventListener listener : listeners){
                if(listener.getTypes().contains(message.getType())){
                    listener.submit(connection, message);
                }
            }
        }
        catch(IOException ex){
            throw new ChatServerException("Problem with handling connection.", ex);
        }
    }

    private void init(int port) {
        this.connections = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.port = port;
    }

    @Override
    public void start() throws ChatServerException {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.started = true;
            System.out.println("Server was started...");

            while (true) {
                Socket s = serverSocket.accept();

                Connection connection = initConnection(s);
                connections.add(connection);
                Thread thread = new Thread(connection);
                thread.start();
            }
        } catch (IOException e) {
            throw new ChatServerException("Game server starting problems...", e);
        }
    }

    @Override
    public void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    private Connection initConnection(Socket s) throws ChatServerException {
        try {
            Message message = Message.readMessage(s.getInputStream());
            if (message.getType() == Message.TYPE_INIT_CONNECTION) {
                String username = "User#" + connections.size();
                sendBroadcastMessage(Message.createMessage(Message.TYPE_MESSAGE, username.concat(" connected...").getBytes(StandardCharsets.UTF_8)));
                return new Connection(
                        (long) connections.size(),
                        username,
                        s,
                        this
                );
            } else {
                throw new ChatServerException("Connection initialization problem...");
            }
        } catch (IOException e) {
            throw new ChatServerException("Connection initialization problem...", e);
        }
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
