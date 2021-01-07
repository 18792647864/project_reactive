package club.manhuang.project_reactive.rabbitmq;

import club.manhuang.project_reactive.rabbitmq.client.ClientProperties;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BaseSyncService {

    default Flux<JsonNode> syncFast(ClientProperties clientProperties){
        return Flux.empty();
    }


    default Mono<Void> syncSlower(ClientProperties clientProperties){
        return Mono.empty();
    }


    default Mono<Void> syncSlow(ClientProperties clientProperties){
        return Mono.empty();
    }

    default Mono<Void> manualSync(ClientProperties clientProperties){
        return Mono.empty();
    }




}
