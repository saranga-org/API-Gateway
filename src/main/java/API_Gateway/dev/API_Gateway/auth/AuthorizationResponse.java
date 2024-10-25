package API_Gateway.dev.API_Gateway.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorizationResponse {
    private String userName;

}
