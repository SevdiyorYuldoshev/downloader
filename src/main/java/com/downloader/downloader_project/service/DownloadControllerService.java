package com.downloader.downloader_project.service;

import com.downloader.downloader_project.integration.Download;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DownloadControllerService {

    private final Download download;


    public ResponseEntity<Map<String, byte[]>> instaDownload(String url) {
        if(!url.contains("instagram.com")){
            return ResponseEntity
                    .notFound()
                    .build();
        }


        try {
            return ResponseEntity
                    .ok()
                    .body(download.instaGetVideos(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<InputStreamResource> tiktokDownload(String url) {
        if (!url.contains("tiktok.com")){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4");
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(download.tiktokGetVideos(url)));
    }


    public ResponseEntity<InputStreamResource> youtubeDownload(String url) {
//        if (!url.contains("youtube.com/shorts") || !url.contains("youtu.be")){
//            return ResponseEntity
//                    .notFound()
//                    .build();
//        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4");
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(download.youtubeGetVideos(url)));
    }
}
