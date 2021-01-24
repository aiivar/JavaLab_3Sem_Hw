package ru.kpfu.itis.minsafin.aivar.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;


public class PicDownloader {

    public void download(String urlString, String folderString) {
        URL url = null;
        try {
            url = URI.create(urlString).toURL();
        } catch (MalformedURLException e) {
            throw new PictureDownloadingException();
        }
        String filename = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
        File picture = new File(Paths.get(folderString).resolve(filename).toString());
        if (!picture.exists()){
            try {
                picture.createNewFile();
            } catch (IOException e) {
                throw new PictureDownloadingException();
            }
        }
        try (BufferedInputStream in = new BufferedInputStream(url.openStream()); FileOutputStream fileOutputStream = new FileOutputStream(picture)) {
            byte[] buffer = new byte[1024];
            int reading;
            while ((reading = in.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, reading);
            }
        } catch (IOException e){
            throw new PictureDownloadingException();
        }
    }

}
