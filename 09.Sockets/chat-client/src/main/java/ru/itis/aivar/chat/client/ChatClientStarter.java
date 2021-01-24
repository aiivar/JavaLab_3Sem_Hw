package ru.itis.aivar.chat.client;

import com.beust.jcommander.JCommander;
import ru.itis.aivar.chat.client.exceptions.ChatClientException;
import ru.itis.aivar.chat.client.listeners.MessageListener;
import ru.itis.aivar.chat.protocol.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientStarter {

    public static void main(String[] argv) {
        Args args = new Args();
        JCommander.newBuilder().addObject(args).build().parse(argv);

        String ip = args.serverIp;
        int port = Integer.parseInt(args.serverPort);

        try {
            ChatClient client = new ChatClient(InetAddress.getByName(ip), port);
            client.registerListener(new MessageListener());
            client.connect();
            client.sendMessage(Message.createMessage(Message.TYPE_INIT_CONNECTION, new byte[]{0}));

            Scanner sc = new Scanner(System.in);
            while (true){
                String message = sc.nextLine();
                if (message.equals("/quit")){
                    client.sendMessage(Message.createMessage(Message.TYPE_EXIT, new byte[]{0}));
                    System.exit(0);
                } else {
                    client.sendMessage(Message.createMessage(Message.TYPE_MESSAGE, message.getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch (UnknownHostException | ChatClientException e) {
            System.err.println("Connection problems");
            System.exit(0);
        }


    }

}
