package API_Gateway.dev.API_Gateway.clients;

import API_Gateway.dev.API_Gateway.auth.AuthorizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service", url = "http://localhost:8082")
public interface AuthServiceClient {
    @GetMapping("/api/fuel-quota")
    ResponseEntity<AuthorizationResponse> validateStationOwner(@RequestHeader("Authorization") String token);

    @GetMapping("/api/vehicle-service")
    ResponseEntity<AuthorizationResponse> validateGeneralUser(@RequestHeader("Authorization") String token);
}
