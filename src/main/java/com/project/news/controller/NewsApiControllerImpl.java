package com.project.news.controller;

import com.project.news.api.NewsApiCaller;
import com.project.news.service.ArticleExpander;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDate;

@RestController
public class NewsApiControllerImpl implements NewsApiController {

    private final NewsApiCaller newsApiCaller;
    private final ArticleExpander articleExpander;

    // Constructor injection is preferred over field injection
    public NewsApiControllerImpl(NewsApiCaller newsApiCaller, ArticleExpander articleExpander) {
        this.newsApiCaller = newsApiCaller;
        this.articleExpander = articleExpander;
    }


    @GetMapping(path = "/api/get")
    public String firsController() {
        return "got headline of ";
    }

    @GetMapping(path = "api/getCustom")
    public String getCustomHeadline(@RequestParam String sources) {
        return "request param " + sources;
    }

    @Override
    @GetMapping(path = "api/headlinesOf", params = "sources")
    public String getNewsHeadlineOf(String sources) {

        return newsApiCaller.getTopNewsHeadlineFrom(sources);

    }

    @Override
    @GetMapping(path = "api/everything", params = "q")
    public String getEverythingWith(String q) {
        return newsApiCaller.getEverythingWithCaller(q);
    }

    @Override
    @GetMapping(path = "api/everythingWithin")
    public String getEverythingWithin(@RequestParam String q, @RequestParam String from, @RequestParam String to) {

        return newsApiCaller.getEverythingWithinCaller(q, from, to);
    }

    // New endpoint: download bulk JSON from the everything endpoint
    @Override
    @Operation(summary = "Download bulk JSON from the Everything endpoint",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bulk JSON file",
                            content = @Content(mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string", format = "binary")))
            })
    @GetMapping(path = "/api/downloadEverything", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadEverything(
            @Parameter(description = "Search query (q)", required = true) @RequestParam String q,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        byte[] data = newsApiCaller.getEverythingBulk(q, from, to);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String safeQ = (q == null || q.isEmpty()) ? "all" : q.replaceAll("[^a-zA-Z0-9-_\\.]+", "_");
        String datePart = "";
        if (from != null || to != null) {
            String f = (from == null) ? "" : from.toString();
            String t = (to == null) ? "" : to.toString();
            datePart = "-" + (f.isEmpty() ? "_" : f) + "_to_" + (t.isEmpty() ? "_" : t);
        }
        String filename = "everything-" + safeQ + datePart + ".json";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    // New endpoint: download expanded JSON where 'content' is replaced by scraped full text when requested
    @Override
    @Operation(summary = "Download bulk JSON and optionally expand article content by scraping source pages",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bulk JSON file (possibly expanded)",
                            content = @Content(mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string", format = "binary")))
            })
    @GetMapping(path = "/api/downloadEverythingExpanded", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadEverythingExpanded(
            @Parameter(description = "Search query (q)", required = true) @RequestParam String q,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "If true, attempt to fetch each article url and replace 'content' with full extracted text") @RequestParam(required = false) Boolean expandFull
    ) {
        byte[] data = newsApiCaller.getEverythingBulk(q, from, to);
        boolean expand = (expandFull != null && expandFull.booleanValue());
        byte[] out = articleExpander.expandArticles(data, expand);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String safeQ = (q == null || q.isEmpty()) ? "all" : q.replaceAll("[^a-zA-Z0-9-_\\.]+", "_");
        String datePart = "";
        if (from != null || to != null) {
            String f = (from == null) ? "" : from.toString();
            String t = (to == null) ? "" : to.toString();
            datePart = "-" + (f.isEmpty() ? "_" : f) + "_to_" + (t.isEmpty() ? "_" : t);
        }
        String filename = "everything-" + safeQ + datePart + (expand ? "-expanded" : "") + ".json";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(out);
    }


}
