package club.manhuang.project_reactive.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitConfiguration {

    private final AmqpAdmin amqpAdmin;

    public MessageConverter rabbitMessageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public void declareExchange(){

        Exchange exchange = ExchangeBuilder.directExchange("mh-exchange-name").build();
        this.amqpAdmin.declareExchange(exchange);

        Queue queue = QueueBuilder.durable("mh-queue-name").build();
        this.amqpAdmin.declareQueue(queue);
        this.amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("mh-routing-key").noargs());


    }


}
