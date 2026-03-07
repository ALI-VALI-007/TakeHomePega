package com.read.reading.controller;

import com.read.reading.dto.ReadingListItemDto;
import com.read.reading.service.ReadingListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReadingListController.class)
class ReadingListControllerTest {

	private static final String USER_ID_HEADER = "X-User-Id";
	private static final String USER_ID = "test-user-123";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ReadingListService service;

	@Nested
	@DisplayName("when X-User-Id is missing or blank")
	class MissingUserId {

		@Test
		@DisplayName("GET /api/reading-list returns 400")
		void listReturns400() throws Exception {
			mockMvc.perform(get("/api/reading-list"))
					.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("GET /api/reading-list/1 returns 400")
		void getByIdReturns400() throws Exception {
			mockMvc.perform(get("/api/reading-list/1"))
					.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("POST /api/reading-list returns 400")
		void createReturns400() throws Exception {
			mockMvc.perform(post("/api/reading-list")
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"title\":\"x\",\"author\":\"y\",\"readStatus\":false}"))
					.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("PUT /api/reading-list/1 returns 400")
		void updateReturns400() throws Exception {
			mockMvc.perform(put("/api/reading-list/1")
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"title\":\"x\",\"author\":\"y\",\"readStatus\":false}"))
					.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("DELETE /api/reading-list/1 returns 400")
		void deleteReturns400() throws Exception {
			mockMvc.perform(delete("/api/reading-list/1"))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("when X-User-Id is present")
	class WithUserId {

		@Test
		@DisplayName("GET /api/reading-list returns 200 and list from service")
		void listReturns200() throws Exception {
			ReadingListItemDto dto = new ReadingListItemDto(1L, 1L, "Title", "Author", null, false);
			when(service.list(USER_ID)).thenReturn(List.of(dto));

			mockMvc.perform(get("/api/reading-list").header(USER_ID_HEADER, USER_ID))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(1)))
					.andExpect(jsonPath("$[0].title").value("Title"))
					.andExpect(jsonPath("$[0].author").value("Author"))
					.andExpect(jsonPath("$[0].readStatus").value(false));

			verify(service).list(USER_ID);
		}

		@Test
		@DisplayName("GET /api/reading-list/1 returns 200 when found")
		void getByIdReturns200WhenFound() throws Exception {
			ReadingListItemDto dto = new ReadingListItemDto(1L, 1L, "Title", "Author", "Notes", true);
			when(service.getById(USER_ID, 1L)).thenReturn(Optional.of(dto));

			mockMvc.perform(get("/api/reading-list/1").header(USER_ID_HEADER, USER_ID))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(1))
					.andExpect(jsonPath("$.title").value("Title"))
					.andExpect(jsonPath("$.readStatus").value(true));

			verify(service).getById(USER_ID, 1L);
		}

		@Test
		@DisplayName("GET /api/reading-list/99 returns 404 when not found")
		void getByIdReturns404WhenNotFound() throws Exception {
			when(service.getById(USER_ID, 99L)).thenReturn(Optional.empty());

			mockMvc.perform(get("/api/reading-list/99").header(USER_ID_HEADER, USER_ID))
					.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("POST /api/reading-list returns 201 and created DTO")
		void createReturns201() throws Exception {
			ReadingListItemDto created = new ReadingListItemDto(2L, 2L, "New Book", "Author", null, false);
			when(service.create(eq(USER_ID), any(ReadingListItemDto.class))).thenReturn(created);

			mockMvc.perform(post("/api/reading-list")
							.header(USER_ID_HEADER, USER_ID)
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"title\":\"New Book\",\"author\":\"Author\",\"readStatus\":false}"))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id").value(2))
					.andExpect(jsonPath("$.title").value("New Book"));

			verify(service).create(eq(USER_ID), any(ReadingListItemDto.class));
		}

		@Test
		@DisplayName("PUT /api/reading-list/1 returns 200 when updated")
		void updateReturns200WhenFound() throws Exception {
			ReadingListItemDto updated = new ReadingListItemDto(1L, 1L, "Updated", "Author", null, true);
			when(service.update(eq(USER_ID), eq(1L), any(ReadingListItemDto.class))).thenReturn(Optional.of(updated));

			mockMvc.perform(put("/api/reading-list/1")
							.header(USER_ID_HEADER, USER_ID)
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"title\":\"Updated\",\"author\":\"Author\",\"readStatus\":true}"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.title").value("Updated"))
					.andExpect(jsonPath("$.readStatus").value(true));

			verify(service).update(eq(USER_ID), eq(1L), any(ReadingListItemDto.class));
		}

		@Test
		@DisplayName("PUT /api/reading-list/99 returns 404 when not found")
		void updateReturns404WhenNotFound() throws Exception {
			when(service.update(eq(USER_ID), eq(99L), any(ReadingListItemDto.class))).thenReturn(Optional.empty());

			mockMvc.perform(put("/api/reading-list/99")
							.header(USER_ID_HEADER, USER_ID)
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"title\":\"x\",\"author\":\"y\",\"readStatus\":false}"))
					.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("DELETE /api/reading-list/1 returns 204 when deleted")
		void deleteReturns204WhenDeleted() throws Exception {
			when(service.delete(USER_ID, 1L)).thenReturn(true);

			mockMvc.perform(delete("/api/reading-list/1").header(USER_ID_HEADER, USER_ID))
					.andExpect(status().isNoContent())
					.andExpect(content().string(""));

			verify(service).delete(USER_ID, 1L);
		}

		@Test
		@DisplayName("DELETE /api/reading-list/99 returns 404 when not found")
		void deleteReturns404WhenNotFound() throws Exception {
			when(service.delete(USER_ID, 99L)).thenReturn(false);

			mockMvc.perform(delete("/api/reading-list/99").header(USER_ID_HEADER, USER_ID))
					.andExpect(status().isNotFound());
		}
	}
}
