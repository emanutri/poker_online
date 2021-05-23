package it.prova.pokeronline.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class YouWinException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public YouWinException(String message) {
		super(message);
	}
}
