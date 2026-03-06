package com.read.reading.controller;

import com.read.reading.dto.ReadingListItemDto;
import com.read.reading.service.ReadingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Reading list API. User identity is provided via X-User-Id header (Cognito sub or similar when auth is enabled).
 */
@RestController
@RequestMapping("/api/reading-list")
@CrossOrigin(origins = "*")
public class ReadingListController {

	private static final String USER_ID_HEADER = "X-User-Id";

	private final ReadingListService service;

	public ReadingListController(ReadingListService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<ReadingListItemDto>> list(
			@RequestHeader(value = USER_ID_HEADER, required = false) String userId) {
		if (invalidUserId(userId)) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(service.list(userId.trim()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReadingListItemDto> getById(
			@RequestHeader(value = USER_ID_HEADER, required = false) String userId,
			@PathVariable Long id) {
		if (invalidUserId(userId)) {
			return ResponseEntity.badRequest().build();
		}
		return service.getById(userId.trim(), id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ReadingListItemDto> create(
			@RequestHeader(value = USER_ID_HEADER, required = false) String userId,
			@RequestBody ReadingListItemDto dto) {
		if (invalidUserId(userId)) {
			return ResponseEntity.badRequest().build();
		}
		ReadingListItemDto created = service.create(userId.trim(), dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReadingListItemDto> update(
			@RequestHeader(value = USER_ID_HEADER, required = false) String userId,
			@PathVariable Long id,
			@RequestBody ReadingListItemDto dto) {
		if (invalidUserId(userId)) {
			return ResponseEntity.badRequest().build();
		}
		return service.update(userId.trim(), id, dto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@RequestHeader(value = USER_ID_HEADER, required = false) String userId,
			@PathVariable Long id) {
		if (invalidUserId(userId)) {
			return ResponseEntity.badRequest().build();
		}
		boolean deleted = service.delete(userId.trim(), id);
		return deleted
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

	private static boolean invalidUserId(String userId) {
		return userId == null || userId.isBlank();
	}
}
