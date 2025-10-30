package com.project.news.controller;

import org.springframework.http.ResponseEntity;
import java.time.LocalDate;

public interface NewsApiController {

    String getNewsHeadlineOf ( String sources );

    String getEverythingWith(String q);

    String getEverythingWithin(String q, String from, String to);

    // New: download everything as a file (bulk JSON) with optional date range
    ResponseEntity<byte[]> downloadEverything(String q, LocalDate from, LocalDate to);
}
