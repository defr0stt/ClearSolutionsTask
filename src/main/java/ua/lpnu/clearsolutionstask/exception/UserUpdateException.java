package ua.lpnu.clearsolutionstask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.IM_USED)
public class UserUpdateException extends RuntimeException {

    public UserUpdateException(String message) {
        super(message);
    }
}
