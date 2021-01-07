package club.manhuang.project_reactive;


import club.manhuang.project_reactive.rabbitmq.BaseSyncService;
import club.manhuang.project_reactive.rabbitmq.client.ClientProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * @author aff
 */
@Log4j2
@Component
@RequiredArgsConstructor
@EnableScheduling
public class GlobalScheduledTimer {

    private final Map<String, BaseSyncService> seviceMap;

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final ObjectMapper objectMapper;



    @Scheduled(fixedDelay = 1000 * 60)
    public void autoSyncFast(){
        log.info("autoSyncFast");
        this.reactiveStringRedisTemplate.scan(ScanOptions.scanOptions()
                .match("data:connect-wise:sync:tenant:properties:"+"*").count(10).build())
                .flatMap(key -> this.reactiveStringRedisTemplate.opsForValue().get(key))
                .map(data -> this.objectMapper.convertValue(data, ClientProperties.class))
                .filterWhen(clientProperties -> this.reactiveStringRedisTemplate.opsForValue()
                        .size("data:connect-wise:sync:anchor:task:on-off:"+clientProperties.getTenantId())
                        .defaultIfEmpty(0L)
                        .map(r -> r == 0)
                )
                .flatMap(clientProperties -> Flux.fromStream(this.seviceMap.values().parallelStream())
                            .limitRate(8,4)
                            .delayElements(Duration.ofSeconds(10))
                            .flatMap(v -> Flux.concatDelayError(
                                            v.syncFast(clientProperties).onErrorResume(err -> Mono.empty())
                                        )
                                    )
                )
                .subscribe(res -> log.debug("Sync test success"),
                        err -> log.debug("Sync test error. msg:{}",err.getMessage()));
    }



    public void TestGroupedFlux(){
        Flux.just(
                new Person("A", 0),
                new Person("B", 0),
                new Person("c", 1),
                new Person("d", 1),
                new Person("X", 2)
        )
        .groupBy(Person::getGender)
        .flatMap(integerPersonGroupedFlux -> Mono.just(integerPersonGroupedFlux.key())
                                            .zipWith(integerPersonGroupedFlux.count()))
        .subscribe(System.out::println);
    }



    /**
     * 用于被去重的类，由于使用HashSet进行去重，所以需要重写hashCode和equals方法
     * 当id相同时即认为两个实例相同
     */
    private class Person {
        private String name;
        //性别：0为男，1为女，2位Unkown
        private int gender;

        public Person(String name, int gender) {
            this.name = name;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public int getGender() {
            return gender;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", gender=" + gender +
                    '}';
        }
    }


}

