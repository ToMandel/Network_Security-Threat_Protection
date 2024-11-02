package netSec.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotAcceptableException extends RuntimeException {

	private static final long serialVersionUID = -2085402046545329137L;

	public UserNotAcceptableException() {
	}

	public UserNotAcceptableException(String message) {
		super(message);
	}

	public UserNotAcceptableException(Throwable cause) {
		super(cause);
	}

	public UserNotAcceptableException(String message, Throwable cause) {
		super(message, cause);
	}
}
