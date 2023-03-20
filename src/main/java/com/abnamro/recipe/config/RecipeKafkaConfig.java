package com.abnamro.recipe.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * Kafka configuration class for recipe
 */
@Configuration
public class RecipeKafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public NewTopic recipeTopic(){
        return TopicBuilder.name("recipe").build();
    }

    @Bean
    public ProducerFactory<String,Object> producerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,org.springframework.kafka.support.serializer.JsonSerializer.class);
         return new DefaultKafkaProducerFactory<>(config);

    }

    @Bean
    public ProducerFactory<String,String> producerFactoryForString(){
        Map<String,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,org.springframework.kafka.support.serializer.StringOrBytesSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);

    }

  @Bean(value = "kafkaTemplate")
  public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean(value="kafkaTemplateStr")
    public KafkaTemplate kafkaTemplateForString(){
        return new KafkaTemplate<>(producerFactoryForString());
    }

}
