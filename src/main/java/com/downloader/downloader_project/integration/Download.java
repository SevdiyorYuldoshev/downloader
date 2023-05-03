package com.downloader.downloader_project.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class Download {
    @Value("${app.name}")
    private String appName;
    @Autowired
    private Request request;

    public Map<String, byte[]> instaGetVideos(String url) throws IOException {
        Map<String, byte[]> resp = new HashMap<>();
        Map<String, List<String>> stringListMap = request.instagramRequest(url);

        if(stringListMap.get("Image") != null){
            stringListMap.get("Image").
                    parallelStream().forEach(s -> {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        try(BufferedInputStream inputStream = new BufferedInputStream(new URL(s).openConnection().getInputStream())) {
                            byte[] buffer = new byte[4096];
                            int length;

                            while ((length = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, length);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        resp.put(appName + UUID.randomUUID()+".jpeg", outputStream.toByteArray());
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        if(stringListMap.get("Video") != null){
            stringListMap.get("Video").parallelStream().forEach(s -> {
                ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
                try(BufferedInputStream inputStream2 = new BufferedInputStream(new URL(s).openConnection().getInputStream())) {
                    byte[] buffer = new byte[4096];
                    int length;

                    while ((length = inputStream2.read(buffer)) != -1) {
                        outputStream2.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                resp.put(appName + UUID.randomUUID()+".mp4", outputStream2.toByteArray());
                try {
                    outputStream2.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return resp;
    }

    public InputStream tiktokGetVideos(String url){
        try {
            return new BufferedInputStream(new URL(request.tiktokRequest(url)).openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream youtubeGetVideos(String url){
        try {
            return new BufferedInputStream(new URL(request.youtubeRequest(url)).openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
