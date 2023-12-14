package com.slice.controller.impl;


import com.slice.controller.WebController;
import com.slice.service.impl.WordService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@Controller("/api/v1")
public class AppController implements WebController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    private final WordService wordService;

    public AppController(WordService wordService) {
        this.wordService = wordService;
    }

    @Get(uri = "/statistics/{userName}", produces = MediaType.APPLICATION_JSON)
    public Mono<LinkedHashMap<String, Long>> getWordsCount(final String userName) {
        logger.info("Username: {}", userName);
        return wordService.getTopWords(userName);
    }


}
