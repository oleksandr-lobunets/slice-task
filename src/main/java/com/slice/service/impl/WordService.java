package com.slice.service.impl;

import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

public interface WordService {

    Mono<LinkedHashMap<String, Long>> getTopWords(String userName);
}
