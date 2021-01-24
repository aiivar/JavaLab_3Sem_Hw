package ru.itis.aivar.chat.client;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Args {

    @Parameter(names = "--server-ip")
    public String serverIp;

    @Parameter(names = "--server-port")
    public String serverPort;

}
