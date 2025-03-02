package com.example.subscription.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateSubscriptionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DuplicateSubscriptionException(String message) {
        super(message);
    }
}
