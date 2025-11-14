package jack.labs.mark77.global;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Builder
public class ApiResponse<T> {
    private final int code;       // HTTP Status Code
    private final String message; // Response Result Message -> "Success", OK
    private final T data;         // Service 계층의 메서드가 Return 타입이 제네릭 선언 된다.

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .build();
    }
}
