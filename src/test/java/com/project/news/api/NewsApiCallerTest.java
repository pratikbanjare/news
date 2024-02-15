package com.project.news.api;

import com.project.news.client.NewsClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NewsApiCallerTest {

    @InjectMocks
    NewsApiCaller newsApiCaller;

    @Value("${api.key}")
    private String apiKey;

    @Value(("${url.headline}"))
    private String headlineUrl;

    @Value("${url.everything}")
    private String everythingUrl;

    @Mock
    private NewsClient newsClient;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(newsApiCaller, "apiKey", "API_KEY");
        ReflectionTestUtils.setField(newsApiCaller, "headlineUrl", "https://newsapi.org/v2/top-headlines");
        ReflectionTestUtils.setField(newsApiCaller, "everythingUrl", "https://newsapi.org/v2/everything");

    }

    @Test
    public void getTopNewsHeadlineFrom() {

        String result = "result";
        when(newsClient.httpRequestCall(any())).thenReturn(result);
        when(newsClient.createHtpGetRequest(any())).thenCallRealMethod();
        Assertions.assertEquals(result, newsApiCaller.getTopNewsHeadlineFrom("sources"));

    }

    @Test
    public void getEverythingWithCaller() {
        String result = "keyword";
        when(newsClient.httpRequestCall(any())).thenReturn(result);
        when(newsClient.createHtpGetRequest(any())).thenCallRealMethod();
        Assertions.assertEquals(result, newsApiCaller.getEverythingWithCaller(result));
    }

    @Test
    public void getEverythingWithinCaller() {
        String result = "result";
        when(newsClient.httpRequestCall(any())).thenReturn(result);
        when(newsClient.createHtpGetRequest(any())).thenCallRealMethod();
        Assertions.assertEquals(result, newsApiCaller.getEverythingWithinCaller("keyword", "from", "to"));

    }

}
