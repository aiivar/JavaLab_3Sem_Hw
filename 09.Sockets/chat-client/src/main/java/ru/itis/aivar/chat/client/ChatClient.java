package ru.itis.aivar.chat.client;


import ru.itis.aivar.chat.client.abstracts.Client;
import ru.itis.aivar.chat.client.abstracts.ClientEventListener;
import ru.itis.aivar.chat.client.exceptions.ChatClientException;
import ru.itis.aivar.chat.protocol.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatClient implements Client {
    protected final InetAddress address;
    protected final int port;
    protected Socket socket;
    protected List<ClientEventListener> listeners;
    protected boolean connected;
    protected MessageHandler messageHandler;

    public ChatClient(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        listeners = new ArrayList<>();
        connected = false;
    }

    @Override
    public void connect() throws ChatClientException {
        try {
            socket = new Socket(address, port);
            messageHandler = new MessageHandler(this);
            Thread messageHandlerThread = new Thread(messageHandler);
            messageHandlerThread.start();
            connected = true;
        }catch (IOException e){
            throw new ChatClientException("Can't connect", e);
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendMessage(Message message) throws ChatClientException {
        if (!connected){
            throw new ChatClientException("Not connected");
        }
        try {
            socket.getOutputStream().write(Message.getBytes(message));
            socket.getOutputStream().flush();
        } catch (IOException e){
            throw new ChatClientException("Can't send message",e);
        }
    }

    @Override
    public void registerListener(ClientEventListener listener) throws ChatClientException {
        if (connected){
            throw new ChatClientException("Connected");
        }
        listener.init();
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
        listeners.add(listener);
    }

}
