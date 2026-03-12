package com.read.reading.dto;
import com.read.reading.model.UserBook.ReadStatus;
public class ReadingListItemDto {

	private Long bookId;
	private String title;
	private String author;
	private String notes;
	private ReadStatus status;

	public ReadingListItemDto() {
	}

	public ReadingListItemDto(Long bookId, String title, String author, String notes, ReadStatus status) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.notes = notes;
		this.status = status;
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

	public ReadStatus getStatus() {
		return this.status;
	}

	public void setStatus(ReadStatus status) {
		this.status = status;
	}
}
