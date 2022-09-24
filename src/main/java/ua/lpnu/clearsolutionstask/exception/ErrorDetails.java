package ua.lpnu.clearsolutionstask.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorDetails {
    private Date time;
    private String message;
    private HttpStatus httpStatus;
    private String details;
}
