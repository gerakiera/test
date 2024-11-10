package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Reader;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReaderRepository;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LibraryService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public Transaction executionTransaction(Long bookId, Long readerId, String transactionType) {
        validateUserAuthentication();
        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new RuntimeException("Reader not found."));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found."));

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setBook(book);
        transaction.setReader(reader);

        return transactionRepository.save(transaction);
    }

    public Author getMostPopularAuthor(LocalDateTime startDate, LocalDateTime endDate) {
        validateUserAuthentication();
        List<Transaction> transactions = transactionRepository.findAll();
        Map<Author, Integer> authorCount = new HashMap<>();

        for(Transaction transaction : transactions) {
            LocalDateTime date = transaction.getTransactionDateTime();
            if((date.isAfter(startDate) || date.isEqual(startDate)) &&
                    (date.isBefore(endDate) || date.isEqual(endDate))) {
                Author author = transaction.getBook().getAuthors().get(0);
                authorCount.put(author, authorCount.getOrDefault(author, 0) + 1);
            }
        }
        Author mostPopularAuthor = null;
        int maxCount = 0;

        for(Map.Entry<Author, Integer> entry : authorCount.entrySet()) {
            if(entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPopularAuthor = entry.getKey();
            }
        }
        return mostPopularAuthor;
    }

    public Reader getMostActiveReader() {
        validateUserAuthentication();
        List<Transaction> transactions = transactionRepository.findAll();
        Map<Reader, Integer> readerCount = new HashMap<>();

        for(Transaction transaction : transactions) {
            Reader reader = transaction.getReader();
            readerCount.put(reader, readerCount.getOrDefault(reader, 0) + 1);
        }
        Reader mostActiveReader = null;
        int maxCount = 0;

        for(Map.Entry<Reader, Integer> entry : readerCount.entrySet()) {
            if(entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostActiveReader = entry.getKey();
            }
        }
        return mostActiveReader;
    }

    public List<Reader> getSortedReaders () {
        List<Reader> readers = readerRepository.findAll();
        Map<Reader, Integer> unreturnedCount = new HashMap<>();
        for (Reader reader : readers) {
            int count = 0;
            List<Transaction> transactions = transactionRepository.findAll();
            for (Transaction transaction : transactions) {
                if (transaction.getReader().equals(reader) && transaction.getTransactionType().equals("borrow")) {
                    count++;
                }
            }
            unreturnedCount.put(reader, count);
        }
        readers.sort((r1, r2) -> {
            return Integer.compare(unreturnedCount.get(r2), unreturnedCount.get(r1));
        });
        return readers;
    }
    private void validateUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated.");
        }
    }
}
