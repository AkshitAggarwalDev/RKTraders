package RKTraders.web.Exceptions;

import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;

    private String message;

    private int status;

    private LocalDateTime timestamp;


}
