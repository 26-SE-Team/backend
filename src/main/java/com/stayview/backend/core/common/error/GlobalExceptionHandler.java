package com.stayview.backend.core.common.error;

import com.stayview.backend.core.common.response.ApiResponse;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
		return ResponseEntity
			.status(exception.getErrorCode().status())
			.body(ApiResponse.error(exception.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(Collectors.joining(", "));

		return ResponseEntity
			.badRequest()
			.body(ApiResponse.error(message));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ApiResponse<Void>> handleMissingHeaderException(MissingRequestHeaderException exception) {
		return ResponseEntity
			.status(ErrorCode.UNAUTHORIZED.status())
			.body(ApiResponse.error("필수 헤더가 누락되었습니다: " + exception.getHeaderName()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleDataIntegrityException() {
		return ResponseEntity
			.status(ErrorCode.CONFLICT.status())
			.body(ApiResponse.error("데이터 무결성 제약조건을 위반했습니다."));
	}
}
