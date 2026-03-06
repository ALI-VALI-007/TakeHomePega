package com.read.reading.dto;

/**
 * One item on a user's reading list (request/response body).
 * Combines book fields with the user's read/unread status.
 * The {@code id} is the book id and is used in URLs for get/update/delete.
 */
public class ReadingListItemDto {

	/** Same as bookId; used as the resource id in API paths. */
	private Long id;
	private Long bookId;

	/** Book metadata. */
	private String title;
	private String author;
	private String notes;

	/** Whether the user has read this book. */
	private boolean readStatus;

	public ReadingListItemDto() {
	}

	public ReadingListItemDto(Long id, Long bookId, String title, String author, String notes, boolean readStatus) {
		this.id = id;
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.notes = notes;
		this.readStatus = readStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isReadStatus() {
		return readStatus;
	}

	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}
}
