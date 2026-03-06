package com.read.reading.model;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for UserBook: (CognitoUserId, BookId).
 */
public class UserBookId implements Serializable {

	@Column(name = "CognitoUserId")
	private String cognitoUserId;

	@Column(name = "BookId")
	private Long bookId;

	public UserBookId() {
	}

	public UserBookId(String cognitoUserId, Long bookId) {
		this.cognitoUserId = cognitoUserId;
		this.bookId = bookId;
	}

	public String getCognitoUserId() {
		return cognitoUserId;
	}

	public void setCognitoUserId(String cognitoUserId) {
		this.cognitoUserId = cognitoUserId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserBookId that = (UserBookId) o;
		return Objects.equals(cognitoUserId, that.cognitoUserId) && Objects.equals(bookId, that.bookId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognitoUserId, bookId);
	}
}
