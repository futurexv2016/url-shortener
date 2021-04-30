package com.bondle.shortenurl.repository;

import com.bondle.shortenurl.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ShortenUrlRepository extends JpaRepository<Url, UUID>, JpaSpecificationExecutor<Url> {
    List<Url> findByShortenedUrl(String shortenedUrl);
}
