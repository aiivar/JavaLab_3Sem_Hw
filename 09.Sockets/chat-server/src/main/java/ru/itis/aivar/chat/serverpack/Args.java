package ru.itis.aivar.chat.serverpack;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Args {

    @Parameter(names = "--port")
    public String port;

}
