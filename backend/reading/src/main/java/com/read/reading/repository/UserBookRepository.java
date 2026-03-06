package com.read.reading.repository;

import com.read.reading.model.UserBook;
import com.read.reading.model.UserBookId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends JpaRepository<UserBook, UserBookId> {

	List<UserBook> findById_CognitoUserIdOrderById_BookIdDesc(String cognitoUserId);

	Optional<UserBook> findById_CognitoUserIdAndId_BookId(String cognitoUserId, Long bookId);

	long countById_BookId(Long bookId);
}
