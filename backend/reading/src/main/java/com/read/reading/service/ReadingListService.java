package com.read.reading.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.read.reading.dto.ReadingListItemDto;
import com.read.reading.model.Book;
import com.read.reading.model.UserBook;
import com.read.reading.model.UserBook.ReadStatus;
import com.read.reading.repository.BookRepository;
import com.read.reading.repository.UserBookRepository;

import jakarta.transaction.Transactional;

@Service
public class ReadingListService {

	private final BookRepository bookRepository;
	private final UserBookRepository userBookRepository;

	public ReadingListService(BookRepository bookRepository, UserBookRepository userBookRepository) {
		this.bookRepository = bookRepository;
		this.userBookRepository = userBookRepository;
	}

	@Transactional
	public void saveBook(String cognitoId, ReadingListItemDto dto){
		if (dto.getBookId()==null){
			Book curBook = new Book(dto.getTitle(),dto.getAuthor(),dto.getNotes());
			curBook = this.bookRepository.save(curBook);
			UserBook curUserBook = new UserBook(cognitoId,curBook,dto.getStatus());
			this.userBookRepository.save(curUserBook);
			return;
		}
		Optional<UserBook> existing = userBookRepository.findById_CognitoUserIdAndId_BookId(cognitoId,dto.getBookId());
		if(existing.isPresent()){
			UserBook curUserBook=existing.get();
			Book curBook=curUserBook.getBook();
			curBook.setAuthor(dto.getAuthor());
			curBook.setTitle(dto.getTitle());
			curBook.setNotes(dto.getNotes());
			curUserBook.setStatus(dto.getStatus());
			this.bookRepository.save(curBook);
			return;
		}
	}

	@Transactional
	public void deleteBook(Long bookId, String cognitoId){
		Optional<UserBook> existing = userBookRepository.findById_CognitoUserIdAndId_BookId(cognitoId, bookId);
		if (existing.isPresent()){
			UserBook curUserBook = existing.get();
			this.userBookRepository.delete(curUserBook);
			this.bookRepository.delete(curUserBook.getBook());
		}
	}

	@Transactional
	public void updateStatus(ReadingListItemDto dto, String cognitoId){
		Optional<UserBook> existing = userBookRepository.findById_CognitoUserIdAndId_BookId(cognitoId,dto.getBookId());
		if(existing.isPresent()){
			UserBook curUserBook=existing.get();
 			curUserBook.setStatus(dto.getStatus());
			this.userBookRepository.save(curUserBook);
		}
	}

	public List<UserBook> getUserBooks(String cognitoId, ReadStatus status){
		return userBookRepository.findById_CognitoUserIdAndStatus(cognitoId, status);
	}
}
