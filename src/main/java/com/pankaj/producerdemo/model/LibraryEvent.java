package com.pankaj.producerdemo.model;

public record LibraryEvent(
        int libraryEventId,
        LibraryEventType libraryEventType,
        Book book
) {
}
