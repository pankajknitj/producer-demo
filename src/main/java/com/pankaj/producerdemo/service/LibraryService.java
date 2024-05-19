package com.pankaj.producerdemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pankaj.producerdemo.config.KafkaProducer;
import com.pankaj.producerdemo.model.LibraryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

/** Library service layer*/
@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryService {
    /* Template to publish event to kafka*/
    private final KafkaProducer kafkaProducer;

    /* exposing method to controller layer*/
    public void publishEvent(LibraryEvent event) throws JsonProcessingException {
        /* call to default ack method*/
//        kafkaProducer.publishToLibrary(event).whenComplete((sendResult, throwable)->{
//            if(throwable!=null)
//                handleFailure(throwable);
//            else handleSuccess(sendResult);
//        });

        /* call to manual ack method
        * passing a call back method to act on success/failure
        * it will be called after all retries*/
        kafkaProducer.publishToLibrary2(event).whenComplete((sendResult, throwable)->{
            // means no error
            if(throwable!=null)
                handleFailure(throwable);
            else handleSuccess(sendResult);
        });
    }

    /* to handle successful event*/
    private void handleSuccess(SendResult<Integer, String> sendResult) {
        log.info("payload published: {}", sendResult.getProducerRecord());
    }

    /* To handle unsuccessfully event*/
    private void handleFailure(Throwable throwable) {
        log.error("An Error occurred while publishing: {}", throwable.getMessage());
    }

}
