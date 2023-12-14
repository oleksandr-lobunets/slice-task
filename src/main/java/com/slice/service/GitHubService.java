package com.slice.service;

import com.slice.client.GithubClient;
import com.slice.service.impl.WordService;
import io.micronaut.context.annotation.Prototype;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@Prototype
public class GitHubService implements WordService {

    private final GithubClient githubClient;

    public GitHubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public Mono<LinkedHashMap<String, Long>> getTopWords(final String userName) {
        return githubClient.getListRepositories(userName);
    }

}
