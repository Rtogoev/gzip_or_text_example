package com.example.demo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

@RestController
public class GzipController {

    public static byte[] compress(byte[] body) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos)) {
            gzipOutputStream.write(body);
        }
        return baos.toByteArray();
    }

    @GetMapping(value = "/zip", produces = {"application/zip", "application/text"})
    public String gzip() {
        return "TEST_ZIP";
    }

    @GetMapping("/text")
    public ResponseEntity<byte[]> text(@RequestHeader("Accept-Encoding") String header) throws IOException {
        if (header.equals("gzip")) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/xml");
            headers.add("Content-Encoding", "gzip");
            return new ResponseEntity<>(
                    compress("GZIP".getBytes(StandardCharsets.UTF_8)),
                    headers,
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>("TEXT".getBytes(), HttpStatus.OK);
    }

    private static class TestClass {
        private final int x;
        private final int y;

        private TestClass(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
