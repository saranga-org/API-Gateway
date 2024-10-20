package API_Gateway.dev.API_Gateway.filters;

import API_Gateway.dev.API_Gateway.auth.AuthorizationResponse;
import API_Gateway.dev.API_Gateway.clients.AuthServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip authentication for these endpoints
        if (path.startsWith("/api/resource/auth/register") ||
                path.startsWith("/api/resource/auth/authenticate") ||
                path.startsWith("/api/resource/otp/**")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check for missing or invalid Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }



        try {
            AuthorizationResponse isValidRequest = null;
            // Forward the Bearer token to the auth service for validation
            if (path.startsWith("/api/fuel-quota")) {
                isValidRequest = authServiceClient.validateStationOwner(authHeader).getBody();
            } else if (path.startsWith("/api/vehicle-service")) {
                isValidRequest = authServiceClient.validateGeneralUser(authHeader).getBody();
            }

            // Check if the response from the auth service is null or unauthorized
            if (isValidRequest == null || !isValidRequest.isAuthorized()) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized request in gateway");
                return;
            }

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error contacting auth service: " + e.getMessage());
            return;
        }

        // Proceed if the token is valid and authorized
        filterChain.doFilter(request, response);
    }
}
