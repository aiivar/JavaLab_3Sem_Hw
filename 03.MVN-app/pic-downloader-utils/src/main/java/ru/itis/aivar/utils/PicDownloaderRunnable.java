package ru.itis.aivar.utils;

public class PicDownloaderRunnable implements Runnable {

    private String url;
    private String folder;

    public PicDownloaderRunnable(String url, String folder) {
        this.url = url;
        this.folder = folder;
    }

    @Override
    public void run() {
        execute(url, folder);
    }

    private void execute(String url, String folder) {
        PicDownloader picDownloader = new PicDownloader();
        picDownloader.download(url, folder);
        System.out.println("Downloaded");
    }
}
