package ua.lpnu.clearsolutionstask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserCreationException extends RuntimeException {

    public UserCreationException(String message) {
        super(message);
    }
}