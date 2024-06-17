package com.mycompany.app.Util;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class MessageQueue
{
    public static KafkaConsumer<String, String> createConsumer() {
        String bootstrapServers = "localhost:9092";
        String groupId = "db-app";
        String topic = "students_commands";

        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
            bootstrapServers
        );
        
        properties.setProperty(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
            StringDeserializer.class.getName()
        );

        properties.setProperty(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
            StringDeserializer.class.getName()
        );
        
        properties.setProperty(
            ConsumerConfig.GROUP_ID_CONFIG, 
            groupId
        );
        
        properties.setProperty(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, 
            "earliest"
        );

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
            properties
        );

        // subscribe consumer to our topic(s)
        consumer.subscribe(Arrays.asList(topic));

        return consumer;
    }

    public static KafkaProducer<String, String> createProducer() {
        String bootstrapServers = "localhost:9092";

        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
            bootstrapServers
        );
        
        properties.setProperty(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName()
        );
        
        properties.setProperty(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName()
        );

        // create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(
            properties
        );

        return producer;
    }
}
