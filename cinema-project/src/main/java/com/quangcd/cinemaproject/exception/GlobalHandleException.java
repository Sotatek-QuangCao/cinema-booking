package com.quangcd.cinemaproject.exception;

import com.quangcd.cinemaproject.configuration.Translator;
import com.quangcd.cinemaproject.dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleException {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);

        String fieldName = fieldError.getField(); // ví dụ: "title"
        String defaultMessageKey = fieldError.getDefaultMessage(); // ví dụ: "field.required"

        // Lấy tên hiển thị cho field
        String displayName = messageSource.getMessage("field." + fieldName, null, fieldName, LocaleContextHolder.getLocale());

        // truyền tên field hiển thị vào {0}
        String localizedMessage = messageSource.getMessage(defaultMessageKey, new Object[]{displayName}, LocaleContextHolder.getLocale());

        return ResponseEntity.badRequest().body(BaseResponse.builder()
                .code(400)
                .message(localizedMessage)
                .build());
    }
}

