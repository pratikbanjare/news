package com.project.news.controller;

import com.project.news.api.NewsApiCaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class NewsApiControllerTest {

    @InjectMocks
    private NewsApiControllerImpl newsApiController;

    @Mock
    private NewsApiCaller newsApiCaller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup () {
        mockMvc = MockMvcBuilders.standaloneSetup(newsApiController).build();
    }

    @Test
    public void getNewsHeadlineOf() throws Exception {
        String sources = "cnn";

        when(newsApiCaller.getTopNewsHeadlineFrom(sources)).thenReturn("headline");

        mockMvc.perform(get("/api/headlinesOf").param("sources", sources))
                .andExpect(status().isOk())
                .andExpect(content().string("headline"));

        verify(newsApiCaller).getTopNewsHeadlineFrom(sources);
        verifyNoMoreInteractions(newsApiCaller);
    }

    @Test
    public void getEverythingWith() throws Exception {
        String keyword = "bitcoin";
        when(newsApiCaller.getEverythingWithCaller(keyword)).thenReturn("everything");

        mockMvc.perform(get("/api/everything").param("q",keyword))
                .andExpect(status().isOk())
                .andExpect(content().string("everything"));

        verify(newsApiCaller).getEverythingWithCaller(keyword);
        verifyNoMoreInteractions(newsApiCaller);

    }

    @Test
    public void getEverythingWithin() throws Exception {

        String keyword = "bitcion";
        String from = "2024-01-01";
        String to = "2024-01-02";
        when(newsApiCaller.getEverythingWithinCaller(keyword,from, to)).thenReturn("get news between");

        mockMvc.perform(get("/api/everythingWithin").param("q",keyword).param("from",from).param("to",to))
                .andExpect(status().isOk())
                .andExpect(content().string("get news between"));

        verify(newsApiCaller).getEverythingWithinCaller(keyword,from,to);
        verifyNoMoreInteractions(newsApiCaller);

    }
}
