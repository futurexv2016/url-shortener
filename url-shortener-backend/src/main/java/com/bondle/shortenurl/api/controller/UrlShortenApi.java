package com.bondle.shortenurl.api.controller;

import com.bondle.shortenurl.dto.ShortUrlRequest;
import com.bondle.shortenurl.dto.ShortUrlResponse;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-04-22T12:57:02.920Z")


@Validated
@Api(value = "shorten", description = "the shorten API")
@RequestMapping(value = "api/v1/shortening")
public interface UrlShortenApi {

	@ApiOperation(value = "List previous shortened URL", nickname = "listURL", notes = "List previous shortened URL", response = ShortUrlResponse.class, responseContainer = "Pageable", tags = {"List",})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Expand successful", response = ShortUrlResponse.class, responseContainer = "Pageable")})
	@GetMapping
	ResponseEntity<Page<ShortUrlResponse>> listURL(String shortenedUrl, Pageable pageable);

	@ApiOperation(value = "Expand and redirect to origin URL", nickname = "getLongURL", notes = "Expand and redirect to origin URL", tags = {"Expand"})
	@ApiResponses(value = {
			@ApiResponse(code = 302, message = "Get longURL successful")})
	@GetMapping(value = "/origin")
	ResponseEntity<String> getLongURL(@ApiParam(value = "Shorten URL", required = true) @RequestParam String shortenedUrl);

	@ApiOperation(value = "Input an url to shorten", nickname = "shortenUrl", notes = "", response = ShortUrlResponse.class, tags = {"Shorten",})
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successful shortened URL", response = ShortUrlResponse.class),
			@ApiResponse(code = 405, message = "Invalid URL")})
	@PostMapping(
			produces = {"application/json"},
			consumes = {"application/json"})
	ResponseEntity<ShortUrlResponse> shortenUrl(@ApiParam(value = "URL to be shortened", required = true) @Valid @RequestBody ShortUrlRequest url);

}
