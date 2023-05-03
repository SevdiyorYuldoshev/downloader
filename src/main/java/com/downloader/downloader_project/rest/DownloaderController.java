package com.downloader.downloader_project.rest;


import com.downloader.downloader_project.service.DownloadControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("download")
@RequiredArgsConstructor
public class DownloaderController {
    private final DownloadControllerService downloadControllerService;

    @GetMapping(value = "/insta")
    public ResponseEntity<Map<String, byte[]>> instaDownload(@RequestParam String url) {
        return downloadControllerService.instaDownload(url);
    }

    @GetMapping(value = "/tiktok")
    public ResponseEntity<InputStreamResource> tiktokDownload(@RequestParam String url,
                                                              @RequestParam(defaultValue = "null") String sender_device,
                                                              @RequestParam(defaultValue = "null") String web_id) {
        return downloadControllerService.tiktokDownload(url);
    }

    @GetMapping(value = "/youtube")
    public ResponseEntity<InputStreamResource> youtubeDownload(@RequestParam String url) {
        return downloadControllerService.youtubeDownload(url);
    }
}
