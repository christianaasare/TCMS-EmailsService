package iot.turntabl.springweb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.CountDownLatch;

//@SpringBootApplication
public class MesagingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MesagingService.class);

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("customer"));
        return container;
    }
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    Receiver receiver(CountDownLatch latch) {
        return new Receiver(latch);
    }

    @Bean
    CountDownLatch latch() {
        return new CountDownLatch(1);
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    public static void main(String[] args) throws InterruptedException {

        ApplicationContext ctx = SpringApplication.run(MesagingService.class, args);

//        StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
//        CountDownLatch latch = ctx.getBean(CountDownLatch.class);

//        LOGGER.info("Sending message...");
//        template.convertAndSend("customer", "New customer created");

//        latch.await();

    }
}
