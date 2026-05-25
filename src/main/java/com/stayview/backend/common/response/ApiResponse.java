package com.stayview.backend.common.response;

public record ApiResponse<T>(
	String status,
	String message,
	T data
) {

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>("success", "요청이 성공했습니다.", data);
	}

	public static <T> ApiResponse<T> created(T data) {
		return new ApiResponse<>("success", "리소스가 생성되었습니다.", data);
	}

	public static ApiResponse<Void> ok() {
		return new ApiResponse<>("success", "요청이 성공했습니다.", null);
	}

	public static ApiResponse<Void> error(String message) {
		return new ApiResponse<>("error", message, null);
	}
}
