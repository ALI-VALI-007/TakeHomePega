package com.read.reading.service;

import com.read.reading.dto.ReadingListItemDto;
import com.read.reading.model.Book;
import com.read.reading.model.UserBook;
import com.read.reading.repository.BookRepository;
import com.read.reading.repository.UserBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReadingListService {

	private final BookRepository bookRepository;
	private final UserBookRepository userBookRepository;

	public ReadingListService(BookRepository bookRepository, UserBookRepository userBookRepository) {
		this.bookRepository = bookRepository;
		this.userBookRepository = userBookRepository;
	}

	@Transactional(readOnly = true)
	public List<ReadingListItemDto> list(String cognitoUserId) {
		List<UserBook> userBooks = userBookRepository.findById_CognitoUserIdOrderById_BookIdDesc(cognitoUserId);
		return userBooks.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Optional<ReadingListItemDto> getById(String cognitoUserId, Long bookId) {
		return userBookRepository.findById_CognitoUserIdAndId_BookId(cognitoUserId, bookId)
				.map(this::toDto);
	}

	@Transactional
	public ReadingListItemDto create(String cognitoUserId, ReadingListItemDto dto) {
		Book book = new Book(dto.getTitle(), dto.getAuthor(), dto.getNotes());
		book = bookRepository.save(book);

		UserBook userBook = new UserBook(cognitoUserId, book, dto.isReadStatus());
		userBook = userBookRepository.save(userBook);

		return toDto(userBook);
	}

	@Transactional
	public Optional<ReadingListItemDto> update(String cognitoUserId, Long bookId, ReadingListItemDto dto) {
		Optional<UserBook> existing = userBookRepository.findById_CognitoUserIdAndId_BookId(cognitoUserId, bookId);
		if (existing.isEmpty()) {
			return Optional.empty();
		}

		UserBook userBook = existing.get();
		Book book = userBook.getBook();

		if (dto.getTitle() != null) {
			book.setTitle(dto.getTitle());
		}
		if (dto.getAuthor() != null) {
			book.setAuthor(dto.getAuthor());
		}
		if (dto.getNotes() != null) {
			book.setNotes(dto.getNotes());
		}
		bookRepository.save(book);

		userBook.setStatus(dto.isReadStatus());
		userBookRepository.save(userBook);

		return Optional.of(toDto(userBook));
	}

	@Transactional
	public boolean delete(String cognitoUserId, Long bookId) {
		Optional<UserBook> existing = userBookRepository.findById_CognitoUserIdAndId_BookId(cognitoUserId, bookId);
		if (existing.isEmpty()) {
			return false;
		}

		userBookRepository.delete(existing.get());

		// Remove the book from the Book table if no other user has it
		if (userBookRepository.countById_BookId(bookId) == 0) {
			bookRepository.deleteById(bookId);
		}
		return true;
	}

	private ReadingListItemDto toDto(UserBook userBook) {
		Book book = userBook.getBook();
		Long bookId = book.getBookId();
		return new ReadingListItemDto(
				bookId,
				bookId,
				book.getTitle(),
				book.getAuthor(),
				book.getNotes(),
				userBook.isStatus()
		);
	}
}
