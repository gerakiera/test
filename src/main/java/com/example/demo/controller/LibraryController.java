package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.model.Reader;
import com.example.demo.model.Transaction;
import com.example.demo.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @PostMapping("/transaction")
    //@PreAuthorize("isAuthenticated()")
    public Transaction executionTransaction(@RequestParam Long bookId, @RequestParam Long readerId,
                                            @RequestParam String transactionType) {
        return libraryService.executionTransaction(bookId, readerId, transactionType);
    }

    @GetMapping("/popular-author")
    //@PreAuthorize("isAuthenticated()")
    public Author getMostPopularAuthor(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return libraryService.getMostPopularAuthor(startDate, endDate);
    }

    @GetMapping("/most-active-reader")
    //@PreAuthorize("isAuthenticated()")
    public Reader getMostActiveReader() {
        return libraryService.getMostActiveReader();
    }

    @GetMapping("/readers")
    public List<Reader> getSortedReaders() {
        return libraryService.getSortedReaders();
    }
}
