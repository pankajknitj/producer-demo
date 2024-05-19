package com.pankaj.producerdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pankaj.producerdemo.model.LibraryEvent;
import com.pankaj.producerdemo.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller to receive request */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LibraryEventController {
    private final LibraryService libraryService;
    @PostMapping("/library-event")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent event) throws JsonProcessingException {
        log.info("request received");

        /* call to service layer*/
        libraryService.publishEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
}
