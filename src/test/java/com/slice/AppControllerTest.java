package com.slice;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static wiremock.org.hamcrest.MatcherAssert.assertThat;
import static wiremock.org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

@Testcontainers(disabledWithoutDocker = true)
class AppControllerTest {


    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().usingFilesUnderClasspath("wiremock"))
            .build();

    private Map<String, Object> getProperties() {
        return Collections.singletonMap("github.url",
                wireMockServer.baseUrl());
    }

    @Test
    void shouldGetAlbumById() {
        String userName = "spotify";
        try (EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class, getProperties())) {
            RestAssured.port = server.getPort();


            var response = """
                    {"items":[{"name":"README.md","url":"%s/repositories/383922649/contents/README.md"},{"name":"README.md","url":"%s/repositories/385832397/contents/README.md"}]}
                    """
                    .formatted(wireMockServer.baseUrl(), wireMockServer.baseUrl());

            wireMockServer.stubFor(get(urlEqualTo("/search/code?q=user%3Aspotify+filename%3Areadme.md"))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(response)));


            HashMap<String, Long> map = given().contentType(ContentType.JSON)
                    .when()
                    .get("api/v1/statistics/{userName}", userName)
                    .then()
                    .statusCode(200)
                    .extract().body().as(HashMap.class);

            assertThat(map.keySet(), containsInAnyOrder("opendatadiscovery", "https", "github"));

        }
    }
}
