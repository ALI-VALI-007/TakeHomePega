package com.read.reading.service;

import com.read.reading.dto.ReadingListItemDto;
import com.read.reading.model.Book;
import com.read.reading.model.UserBook;
import com.read.reading.model.UserBookId;
import com.read.reading.repository.BookRepository;
import com.read.reading.repository.UserBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadingListServiceTest {

	private static final String USER_ID = "cognito-user-123";
	private static final Long BOOK_ID = 1L;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private UserBookRepository userBookRepository;

	@InjectMocks
	private ReadingListService service;

	private Book book;
	private UserBook userBook;

	@BeforeEach
	void setUp() {
		book = new Book("The Great Gatsby", "F. Scott Fitzgerald", "A classic.");
		book.setBookId(BOOK_ID);
		userBook = new UserBook(USER_ID, book, false);
		userBook.setId(new UserBookId(USER_ID, BOOK_ID));
	}

	@Nested
	@DisplayName("list")
	class ListTests {

		@Test
		@DisplayName("returns empty list when user has no books")
		void returnsEmptyWhenNoBooks() {
			when(userBookRepository.findById_CognitoUserIdOrderById_BookIdDesc(USER_ID))
					.thenReturn(List.of());

			List<ReadingListItemDto> result = service.list(USER_ID);

			assertThat(result).isEmpty();
			verify(userBookRepository).findById_CognitoUserIdOrderById_BookIdDesc(USER_ID);
		}

		@Test
		@DisplayName("returns DTOs for each user book")
		void returnsListOfDtos() {
			when(userBookRepository.findById_CognitoUserIdOrderById_BookIdDesc(USER_ID))
					.thenReturn(List.of(userBook));

			List<ReadingListItemDto> result = service.list(USER_ID);

			assertThat(result).hasSize(1);
			ReadingListItemDto dto = result.get(0);
			assertThat(dto.getId()).isEqualTo(BOOK_ID);
			assertThat(dto.getTitle()).isEqualTo("The Great Gatsby");
			assertThat(dto.getAuthor()).isEqualTo("F. Scott Fitzgerald");
			assertThat(dto.getNotes()).isEqualTo("A classic.");
			assertThat(dto.isReadStatus()).isFalse();
		}
	}

	@Nested
	@DisplayName("getById")
	class GetByIdTests {

		@Test
		@DisplayName("returns empty when user book not found")
		void returnsEmptyWhenNotFound() {
			when(userBookRepository.findById_CognitoUserIdAndId_BookId(USER_ID, BOOK_ID))
					.thenReturn(Optional.empty());

			Optional<ReadingListItemDto> result = service.getById(USER_ID, BOOK_ID);

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("returns DTO when user book exists")
		void returnsDtoWhenFound() {
			when(userBookRepository.findById_CognitoUserIdAndId_BookId(USER_ID, BOOK_ID))
					.thenReturn(Optional.of(userBook));

			Optional<ReadingListItemDto> result = service.getById(USER_ID, BOOK_ID);

			assertThat(result).isPresent();
			assertThat(result.get().getTitle()).isEqualTo("The Great Gatsby");
			assertThat(result.get().isReadStatus()).isFalse();
		}
	}

	@Nested
	@DisplayName("create")
	class CreateTests {

		@Test
		@DisplayName("saves book and user book and returns DTO")
		void createsBookAndUserBook() {
			ReadingListItemDto input = new ReadingListItemDto();
			input.setTitle("1984");
			input.setAuthor("George Orwell");
			input.setNotes("Dystopia.");
			input.setReadStatus(true);

			Book savedBook = new Book(input.getTitle(), input.getAuthor(), input.getNotes());
			savedBook.setBookId(2L);
			when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

			UserBook savedUserBook = new UserBook(USER_ID, savedBook, true);
			savedUserBook.setId(new UserBookId(USER_ID, 2L));
			when(userBookRepository.save(any(UserBook.class))).thenReturn(savedUserBook);

			ReadingListItemDto result = service.create(USER_ID, input);

			assertThat(result.getTitle()).isEqualTo("1984");
			assertThat(result.getAuthor()).isEqualTo("George Orwell");
			assertThat(result.getNotes()).isEqualTo("Dystopia.");
			assertThat(result.isReadStatus()).isTrue();
			assertThat(result.getId()).isEqualTo(2L);

			ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
			verify(bookRepository).save(bookCaptor.capture());
			assertThat(bookCaptor.getValue().getTitle()).isEqualTo("1984");

			ArgumentCaptor<UserBook> userBookCaptor = ArgumentCaptor.forClass(UserBook.class);
			verify(userBookRepository).save(userBookCaptor.capture());
			assertThat(userBookCaptor.getValue().getId().getCognitoUserId()).isEqualTo(USER_ID);
			assertThat(userBookCaptor.getValue().isStatus()).isTrue();
		}
	}

	@Nested
	@DisplayName("update")
	class UpdateTests {

		@Test
		@DisplayName("returns empty when user book not found")
		void returnsEmptyWhenNotFound() {
			when(userBookRepository.findById_CognitoUserIdAndId_BookId(USER_ID, BOOK_ID))
					.thenReturn(Optional.empty());

			ReadingListItemDto dto = new ReadingListItemDto();
			dto.setTitle("Updated");
			Optional<ReadingListItemDto> result = service.update(USER_ID, BOOK_ID, dto);

			assertThat(result).isEmpty();
			verify(bookRepository, never()).save(any());
		}

		@Test
		@DisplayName("updates book and status and returns DTO")
		void updatesAndReturnsDto() {
			when(userBookRepository.findById_CognitoUserIdAndId_BookId(USER_ID, BOOK_ID))
					.thenReturn(Optional.of(userBook));
			when(bookRepository.save(any(Book.class))).thenReturn(book);
			when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);

			ReadingListItemDto dto = new ReadingListItemDto();
			dto.setTitle("Updated Title");
			dto.setAuthor("Updated Author");
			dto.setNotes("Updated notes");
			dto.setReadStatus(true);

			Optional<ReadingListItemDto> result = service.update(USER_ID, BOOK_ID, dto);

			assertThat(result).isPresent();
			assertThat(result.get().getTitle()).isEqualTo("Updated Title");
			assertThat(result.get().isReadStatus()).isTrue();

			verify(bookRepository).save(book);
			assertThat(book.getTitle()).isEqualTo("Updated Title");
			assertThat(book.getAuthor()).isEqualTo("Updated Author");
			assertThat(book.getNotes()).isEqualTo("Updated notes");

			verify(userBookRepository).save(userBook);
			assertThat(userBook.isStatus()).isTrue();
		}
	}

	@Nested
	@DisplayName("delete")
	class DeleteTests {

		@Test
		@DisplayName("returns false when user book not found")
		void returnsFalseWhenNotFound() {
			when(userBookRepository.findById_CognitoUserIdAndId_BookId(USER_ID, BOOK_ID))
					.thenReturn(Optional.empty());

			boolean result = service.delete(USER_ID, BOOK_ID);

			assertThat(result).isFalse();
			verify(userBookRepository, never()).delete(any());
		}

		@Test
		@DisplayName("deletes user book and returns true")
		void deletesUserBook() {
			when(userBookRepository.findById_CognitoUserIdAndId_BookId(USER_ID, BOOK_ID))
					.thenReturn(Optional.of(userBook));
			when(userBookRepository.countById_BookId(BOOK_ID)).thenReturn(0L);

			boolean result = service.delete(USER_ID, BOOK_ID);

			assertThat(result).isTrue();
			verify(userBookRepository).delete(userBook);
			verify(bookRepository).deleteById(BOOK_ID);
		}

		@Test
		@DisplayName("does not delete book when another user has it")
		void doesNotDeleteBookWhenOtherUsersHaveIt() {
			when(userBookRepository.findById_CognitoUserIdAndId_BookId(USER_ID, BOOK_ID))
					.thenReturn(Optional.of(userBook));
			when(userBookRepository.countById_BookId(BOOK_ID)).thenReturn(1L);

			boolean result = service.delete(USER_ID, BOOK_ID);

			assertThat(result).isTrue();
			verify(userBookRepository).delete(userBook);
			verify(bookRepository, never()).deleteById(eq(BOOK_ID));
		}
	}
}
