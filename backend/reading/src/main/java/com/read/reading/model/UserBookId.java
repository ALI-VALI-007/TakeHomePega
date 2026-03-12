package com.read.reading.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;

public class UserBookId implements Serializable {

	@Column(name = "cognito_user_id")
	private String cognitoUserId;

	@Column(name = "book_id")
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
