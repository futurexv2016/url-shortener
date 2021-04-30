package com.bondle.shortenurl.util;

import com.bondle.shortenurl.dto.ShortUrlResponse;
import com.bondle.shortenurl.entity.Url;
import lombok.experimental.UtilityClass;

/**
 * Mapper util to map/filter data of DB and data returned to clients
 * @author david.ho
 */

@UtilityClass
public class URLMapper {
	public static ShortUrlResponse convertURLEntityToResponse(Url url) {
		ShortUrlResponse shorten = new ShortUrlResponse();
		shorten.setShortenedUrl(url.getShortenedUrl());
		shorten.setUrl(url.getOriginalUrl());
		shorten.setId(url.getId());
		shorten.setCreatedAt(url.getCreatedAt());
		return shorten;
	}
}
