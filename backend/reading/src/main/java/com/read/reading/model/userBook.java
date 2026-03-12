package com.read.reading.model;

import com.read.reading.Converter.ReadStatusConverter;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_book")
public class UserBook {

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "cognitoUserId", column = @Column(name = "cognito_user_id")),
		@AttributeOverride(name = "bookId", column = @Column(name = "book_id"))
	})
	private UserBookId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	@MapsId("bookId")
	private Book book;


	public enum ReadStatus {
		READ,
		UNREAD
	}

	@Convert(converter = ReadStatusConverter.class)
	@Column(name = "status", nullable = false)
	private ReadStatus status;

	public UserBook() {
		this.id = new UserBookId();
	}

	public UserBook(String cognitoUserId, Book book, ReadStatus status) {
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

	public ReadStatus getStatus() {
		return this.status;
	}

	public void setStatus(ReadStatus status) {
		this.status = status;
	}
}
