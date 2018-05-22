package ru.antowka.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_GATEWAY, reason="query limit exceeded")  // 502
public class RequestLimitException extends RuntimeException {
}
