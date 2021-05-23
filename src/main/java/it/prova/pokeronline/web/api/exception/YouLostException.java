package it.prova.pokeronline.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ACCEPTED)
public class YouLostException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public YouLostException(String message) {
		super(message);
	}

}
