package com.read.reading.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.read.reading.dto.ReadingListItemDto;
import com.read.reading.model.UserBook;
import com.read.reading.model.UserBook.ReadStatus;
import com.read.reading.service.ReadingListService;


@RestController
@RequestMapping("/api/reading-list")
@CrossOrigin(origins = "*")
public class ReadingListController {

	private static final String USER_ID_HEADER = "X-User-Id";

	private final ReadingListService service;

	public ReadingListController(ReadingListService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Void> createBook(@RequestBody ReadingListItemDto dto, 
		@RequestHeader(value="X-User-Id", required=true) String cognitoUserId
	) {
		if (invalidUserId(cognitoUserId) || !isValidDto(dto)){
			return ResponseEntity.badRequest().build();
		}	
		service.saveBook(cognitoUserId.trim(), dto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long bookId, 
		@RequestHeader(value="X-User-Id", required=true) String cognitoUserId
	) {
		if (invalidUserId(cognitoUserId) || bookId == null){
			return ResponseEntity.badRequest().build();
		}
		service.deleteBook(bookId, cognitoUserId);
		return ResponseEntity.ok().build();
	}

	@PatchMapping
	public ResponseEntity<Void> updateStatus (@RequestBody ReadingListItemDto dto, 
		@RequestHeader(value="X-User-Id", required=true) String cognitoUserId
	) {
		if (invalidUserId(cognitoUserId) || dto.getBookId() == null){
			return ResponseEntity.badRequest().build();
		}
		service.updateStatus(dto, cognitoUserId);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<List<ReadingListItemDto>> getBooks (@RequestParam ReadStatus status, 
		@RequestHeader(value="X-User-Id", required=true) String cognitoUserId
	) {
		if (invalidUserId(cognitoUserId) || status == null){
			return ResponseEntity.badRequest().build();
		}
		List<UserBook> books = service.getUserBooks(cognitoUserId,status);
		List<ReadingListItemDto> res = new ArrayList<>();
		books.forEach(curBook -> {
			res.add(new ReadingListItemDto(
				curBook.getBook().getBookId(),curBook.getBook().getTitle(),curBook.getBook().getAuthor(),curBook.getBook().getNotes(),curBook.getStatus()));
		});
		return ResponseEntity.ok(res);	
	}	

	private boolean isValidDto(ReadingListItemDto dto) {
    return dto != null &&
        dto.getTitle() != null && !dto.getTitle().isBlank()
        && dto.getAuthor() != null && !dto.getAuthor().isBlank()
        && dto.getStatus() != null;
	}

	private static boolean invalidUserId(String userId) {
		return userId == null || userId.isBlank();
	}
}
