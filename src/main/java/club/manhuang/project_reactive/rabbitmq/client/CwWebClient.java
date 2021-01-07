package club.manhuang.project_reactive.rabbitmq.client;

import club.manhuang.project_reactive.common.QueryParams;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;


/**
 * {"appId":"jexet",
 * "site":"cw.jexet.com",
 * "publicKey":"KrqNzemIHjgd5N6U",
 * "privateKey":"v7GbX6Ozi8oRQBxk",
 * "tenantId":1285403951449878530}
 */
@Log4j2
@Service
public class CwWebClient {

    private final static String CW_CLIENT_ID = "b9feb0a1-787c-46b9-9d85-b6f60e2b927a";

    private final WebClient webClient;


    public CwWebClient(){

        this.webClient = WebClient.builder()
                .defaultHeader("Accept","application/vnd.connectwise.com+json")
                .defaultHeader("ClientId",CW_CLIENT_ID)
                .build();
    }

    public Mono<ResponseEntity<JsonNode>> request(URI uri){

        String appId = "jexet";
        String publicKey = "KrqNzemIHjgd5N6U";
        String privateKey = "v7GbX6Ozi8oRQBxk";
        return this.webClient.get().uri(uri)
                .headers(httpHeaders -> {
                    httpHeaders.setBasicAuth(appId+"+"+publicKey,privateKey);
                })
                .exchangeToMono(clientResponse -> clientResponse.toEntity(JsonNode.class))
                .timeout(Duration.ofMinutes(2));


    }



    public Mono<ResponseEntity<JsonNode>> fetch(ClientProperties clientProperties){
        return this.request(UriComponentsBuilder.newInstance().scheme("https")
                .host(clientProperties.getSite())
                .path("/service/tickets")
                .queryParams(QueryParams.withDefault())
                .build().toUri());

    }






}
