package club.manhuang.project_reactive.rabbitmq.service;


import club.manhuang.project_reactive.common.QueryParams;
import club.manhuang.project_reactive.rabbitmq.BaseSyncService;
import club.manhuang.project_reactive.rabbitmq.client.ClientProperties;
import club.manhuang.project_reactive.utils.AbstractToolsUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class RabbitMqSericveImpl extends AbstractToolsUtil implements BaseSyncService {

    @Override
    public Flux<JsonNode> syncFast(ClientProperties clientProperties){

         return super.cwWebClient.fetch(clientProperties)
                .expand(this::autoNextPage)
                .delayElements(Duration.ofMillis(200))
                .flatMap(responseEntity -> {
                    if(ObjectUtils.isEmpty(responseEntity)){
                        return Flux.empty();
                    }
                    return Flux.just(Objects.requireNonNull(responseEntity.getBody()));
                 })
                 .limitRate(50,25);
    }


    private Mono<ResponseEntity<JsonNode>> autoNextPage(ResponseEntity<JsonNode> resp) {
        List<String> links = resp.getHeaders().getValuesAsList(HttpHeaders.LINK);
        URI newUrl = links.parallelStream().map(Link::valueOf)
                .filter(link -> link.hasRel(LinkRelation.of("next")))
                .map(link -> UriComponentsBuilder.fromHttpUrl(URLDecoder.decode(link.getHref(), Charset.defaultCharset()))
                        .build().toUri())
                .findAny().orElse(null);

        if (ObjectUtils.isEmpty(newUrl)) {
            return Mono.empty();
        }
        log.debug("===Request next url [{}]===", newUrl.toString());
        return super.cwWebClient.request(newUrl);
    }


}
