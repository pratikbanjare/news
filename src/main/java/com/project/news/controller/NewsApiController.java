package com.project.news.controller;

public interface NewsApiController {

    public String getNewsHeadlineOf ( String sources );

    public String getEverythingWith(String q);

    public String getEverythingWithin(String q, String from, String to);
}
