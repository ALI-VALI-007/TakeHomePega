package com.read.reading.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.read.reading.model.UserBook;
import com.read.reading.model.UserBook.ReadStatus;
import com.read.reading.model.UserBookId;

public interface UserBookRepository extends JpaRepository<UserBook, UserBookId> {
	List<UserBook> findById_CognitoUserIdAndStatus(String cognitoUserId, ReadStatus status);
	Optional<UserBook> findById_CognitoUserIdAndId_BookId(String cognitoUserId, Long bookId);
}
