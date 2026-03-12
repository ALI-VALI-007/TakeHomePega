package com.read.reading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.read.reading.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByBookId(long bookId);
}
