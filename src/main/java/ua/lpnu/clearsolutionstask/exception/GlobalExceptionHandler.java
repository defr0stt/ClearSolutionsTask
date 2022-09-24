package ua.lpnu.clearsolutionstask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    // handle custom exceptions
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException userNotFoundException,
                                                         WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                userNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND,
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserCreationException(UserCreationException userCreationException,
                                                         WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                userCreationException.getMessage(),
                HttpStatus.CONFLICT,
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<?> handleUserUpdateException(UserUpdateException userUpdateException,
                                                         WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                userUpdateException.getMessage(),
                HttpStatus.CONFLICT,
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    // handle global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception,
                                                       WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
