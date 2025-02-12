package jack.labs.mark77.global;

import lombok.*;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final int code;
    private final String error;
    private final String requestPath;
    private final String requestMessage;

    public static ErrorResponse of(int code, String error, String requestPath, String requestMessage) {
        return new ErrorResponse(code, error, requestPath, requestMessage);
    }
}
