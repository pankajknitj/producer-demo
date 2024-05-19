package com.pankaj.producerdemo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pankaj.producerdemo.model.LibraryEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Setting up a kafka producer*/
@Configuration
@RequiredArgsConstructor
public class KafkaProducer {
    /** Dependencies*/
    private final KafkaTemplate<Integer,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic}")
    private String TOPIC;

    /** Producing message with default acknowledgement*/
    public CompletableFuture<SendResult<Integer,String>> publishToLibrary(LibraryEvent event) throws JsonProcessingException {
        int key = event.libraryEventId();
        String msg = objectMapper.writeValueAsString(event);
        return kafkaTemplate.send(TOPIC,key,msg);
    }

    /** Publishing messages with manual Acknowledgement*/
    public CompletableFuture<SendResult<Integer,String>> publishToLibrary2(LibraryEvent event) throws JsonProcessingException {
        /* Preparing key and message to send*/
        int key = event.libraryEventId();
        String msg = objectMapper.writeValueAsString(event);

        /* Headers for additional metadata*/
        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("sourceSystem","DACA".getBytes()));

        /* Producer record, we can send without this also, but its good t wrap everything inside this*/
        ProducerRecord<Integer,String> payload = new ProducerRecord<>(TOPIC,null,key,msg,headers);

        /* It will return a CompletableFuture, using this we can set callback function to handle error/success*/
        return kafkaTemplate.send(payload);
    }
}
