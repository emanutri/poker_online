package it.prova.pokeronline.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnouthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnouthorizedException(String message) {
		super(message);
	}
}
