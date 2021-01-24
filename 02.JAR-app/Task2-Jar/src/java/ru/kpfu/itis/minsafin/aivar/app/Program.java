package ru.kpfu.itis.minsafin.aivar.app;

import com.beust.jcommander.JCommander;
import ru.kpfu.itis.minsafin.aivar.utils.PicDownloaderRunnable;
import ru.kpfu.itis.minsafin.aivar.utils.URLStringParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {
    public static void main(String[] argv) {
        Args args = new Args();
        JCommander.newBuilder().addObject(args).build().parse(argv);
        ExecutorService service;
        switch (args.mode){
            case "multi-thread":
                service = Executors.newFixedThreadPool(Integer.parseInt(args.count));
                break;
            case "one-thread":
                service = Executors.newSingleThreadExecutor();
                break;
            default:
                service = Executors.newSingleThreadExecutor();
                break;
        }
        String[] files = new URLStringParser().parse(args.files);
        for (int i = 0; i < files.length; i++) {
            Runnable runnable = new PicDownloaderRunnable(files[i], args.folder);
            service.submit(runnable);
        }
        service.shutdown();
    }
}