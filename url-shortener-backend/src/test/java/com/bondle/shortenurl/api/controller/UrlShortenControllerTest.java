package com.bondle.shortenurl.api.controller;

import com.bondle.shortenurl.dto.ShortUrlRequest;
import com.bondle.shortenurl.dto.ShortUrlResponse;
import com.bondle.shortenurl.service.URLShortenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class UrlShortenControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private URLShortenService URLShortenService;

	@Test
	void shortenUrl_should_success() throws Exception {
		// Given
		final String longURL = "http://google.com.vn/?query=long";
		final String shortURL = "http://short/xyz";

		ShortUrlResponse mockResponse = new ShortUrlResponse();
		mockResponse.setId(UUID.randomUUID());
		mockResponse.setUrl(longURL);
		mockResponse.setShortenedUrl(shortURL);
		given(
				URLShortenService.createShortUrl(any(ShortUrlRequest.class))
		).willReturn(
				mockResponse
		);

		ShortUrlRequest requestDto = new ShortUrlRequest();
		requestDto.setUrl(longURL);
		String requestBody = new ObjectMapper().writeValueAsString(requestDto);

		// When
		ResultActions mvcResult = mockMvc.perform(
				post(
						"/api/v1/shortening"
				).contentType(
						MediaType.APPLICATION_JSON
				).content(
						requestBody
				)
		);

		// Then
		mvcResult.andExpect(
				status().isCreated()
		).andExpect(
				jsonPath("$.url").value(longURL)
		).andExpect(
				jsonPath("$.shortenedUrl").value(shortURL)
		);
	}

	@Test
	void listURL_should_success() throws Exception {
		// Given
		List<ShortUrlResponse> mockList = new ArrayList<>();
		mockList.add(
				ShortUrlResponse.builder()
						.id(UUID.randomUUID())
						.url("http://long.url.com")
						.shortenedUrl("http://short/xyz")
						.createdAt(LocalDateTime.now())
						.build()
		);
		mockList.add(
				ShortUrlResponse.builder()
						.id(UUID.randomUUID())
						.url("http://long2.url.com")
						.shortenedUrl("http://short2/xyz")
						.createdAt(LocalDateTime.now())
						.build()
		);

		given(
				URLShortenService.listUrls(anyObject(), any(Pageable.class))
		).willReturn(
				new PageImpl<>(mockList)
		);

		// When
		ResultActions mvcResult = mockMvc.perform(
				get(
						"/api/v1/shortening"
				).contentType(
						MediaType.APPLICATION_JSON
				)
		);

		// Then
		mvcResult.andExpect(
				status().isOk()
		).andExpect(
				jsonPath("$.content", hasSize(mockList.size()))
		).andExpect(
				jsonPath("$.totalElements", is(mockList.size()))
		);
	}

	@Test
	void getLongURL_should_success() throws Exception {
		// Given
		String longUrl = "original.url.com.vn/very-long-path";
		String shortUrl = "localhost:8080/xyz123";
		given(
				URLShortenService.getLongUrl(any(String.class))
		).willReturn(
				longUrl
		);

		// When
		ResultActions mvcResult = mockMvc.perform(
				get(
						String.format("/api/v1/shortening/origin?shortenedUrl=%s", shortUrl)
				).contentType(
						MediaType.APPLICATION_JSON
				)
		);

		// Then
		mvcResult.andExpect(
				status().isOk()
		).andExpect(
				content().string(longUrl)
		);
	}
}

