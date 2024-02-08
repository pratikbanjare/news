package com.project.news.controller;

import com.project.news.api.NewsApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsApiControllerImpl implements NewsApiController {

    @Autowired
    private NewsApiCaller newsApiCaller;


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


}
