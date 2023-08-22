package com.andresinho20049.authservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class Pagination {

	public static PageRequest getPageRequest(Integer page, Integer size, String order, String direction) {

		if (page == null || size == null)
			return null;

		if (order == null)
			return PageRequest.of(page, size);

		Direction dir = direction == null || direction.equalsIgnoreCase("ASC") ? Direction.ASC : Direction.DESC;
		return PageRequest.of(page, size, Sort.by(dir, order));
	}

}
