package ru.itis.aivar.chat.serverpack.server;


import ru.itis.aivar.chat.serverpack.server.abstracts.Server;
import ru.itis.aivar.chat.serverpack.server.exceptions.ChatServerException;

import java.net.Socket;
import java.util.Objects;

public class Connection implements Runnable{

    private Long id;
    private String username;
    private Socket socket;
    private Server server;
    private boolean alive;

    public Connection(Long id, String username, Socket socket, Server server) {
        this.id = id;
        this.username = username;
        this.socket = socket;
        this.server = server;
        this.alive = true;
    }

    private void handle() throws ChatServerException {
        while (isAliveConnection()){
            server.handleConnection(this);
        }
        if (!isAlive()){
            server.removeConnection(this);
        }
    }

    @Override
    public void run() {
        Thread.yield();
        try {
            handle();
        } catch (ChatServerException e) {
            shutdown();
            server.removeConnection(this);
        }
    }

    public boolean isAliveConnection(){
        return alive;
    }

    public void shutdown(){
        this.alive = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return alive == that.alive && Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(socket, that.socket) && Objects.equals(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, socket, server, alive);
    }
}
