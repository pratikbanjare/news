package com.project.news.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsApiControllerImpl implements NewsApiController {

    @Override
    @GetMapping(path = "/api/get")
    public String getNewsHeadlineOf() {
        return "got headline of ";
    }
}
