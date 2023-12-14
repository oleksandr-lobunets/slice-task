package com.slice.client;


import com.slice.dto.FileWithContentDto;
import com.slice.dto.SearchResponseDto;
import com.slice.utils.ClientUtils;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.uri.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.AUTHORIZATION;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Prototype
public class GithubClient {

    @Value("${github.url}")
    private String githubUrl;
    private final HttpClient httpClient;


    @Value("${github.token}")
    private String token;

    @Value("${github.filename}")
    private String filename;


    public GithubClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Mono<LinkedHashMap<String, Long>> getListRepositories(final String username) {
        URI uri = UriBuilder.of(githubUrl)
                .path("search")
                .path("code")
                .queryParam("q", "user:%s filename:%s".formatted(username, filename))
                .build();

        HttpRequest<?> req = buildRequest(uri);
        Flux<SearchResponseDto> response = Flux.from(httpClient.retrieve(req, Argument.of(SearchResponseDto.class)));

        return response.collectList()
                .map(items -> items.stream()
                        .flatMap(responseDto -> responseDto.getItems().stream())
                        .filter(fileInfo -> fileInfo.getName().equalsIgnoreCase(filename))
                        .map(item -> getFileContentAndCount(item.getUrl()))
                        .toList())
                .flatMap(mapList ->
                        Flux.fromIterable(mapList)
                                .flatMap(Mono::flux)
                                .collectList())
                .map(monoMap -> monoMap.stream()
                        .flatMap(x -> x.entrySet().stream())
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                        .entrySet()
                        .stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .limit(3)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new))
                );
    }

    private MutableHttpRequest<Object> buildRequest(URI uri) {
        return HttpRequest.GET(uri)
                .header(ACCEPT, "application/vnd.github.v3+json")
                .header(USER_AGENT, "HttpClient")
                .header(AUTHORIZATION, "Bearer %s".formatted(token));
    }


    private Mono<Map<String, Long>> getFileContentAndCount(final String fileUrl) {
        URI uri = UriBuilder.of(fileUrl).build();

        HttpRequest<?> req = buildRequest(uri);

        return Flux.from(httpClient.retrieve(req, Argument.of(FileWithContentDto.class)))
                .map(FileWithContentDto::getContent)
                .map(ClientUtils::decodeBase64)
                .collectList()
                .map(content -> content.stream()
                        .flatMap(item -> Arrays.stream(item.split("\\W+")))
                        .filter(word -> word.length() > 4)
                        .map(String::toLowerCase)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                );

    }

}
