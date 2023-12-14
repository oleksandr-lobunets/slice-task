package com.slice.controller;

import io.micronaut.http.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;


public interface AppControllerImpl {

    Mono<LinkedHashMap<String, Long>> getWordsCount(@PathVariable String userName);
}
