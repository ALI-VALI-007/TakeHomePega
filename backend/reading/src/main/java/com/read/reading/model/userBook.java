package com.read.reading.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserBook")
public class UserBook {

	@jakarta.persistence.EmbeddedId
	private UserBookId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BookId", nullable = false)
	@MapsId("bookId")
	private Book book;

	/** Read status. Stored as INTEGER in SQLite (0 = unread, 1 = read). */
	@Column(name = "Status", nullable = false)
	private boolean status;

	public UserBook() {
		this.id = new UserBookId();
	}

	public UserBook(String cognitoUserId, Book book, boolean status) {
		this.id = new UserBookId(cognitoUserId, book.getBookId());
		this.book = book;
		this.status = status;
	}

	public UserBookId getId() {
		return id;
	}

	public void setId(UserBookId id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
