package it.prova.pokeronline.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class NoCreditException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoCreditException(String message) {
		super(message);
	}
}
