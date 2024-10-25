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
        if (path.matches("^/api/resource(/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        AuthorizationResponse userName = null;

        try {
            if (path.startsWith("/api/fuel-quota")) {
                userName = authServiceClient.validateStationOwner(authHeader).getBody();
            } else if (path.startsWith("/api/vehicle")) {
                userName = authServiceClient.validateGeneralUser(authHeader).getBody();
            }

            if (userName == null || userName.getUserName() == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized request in gateway");
                return;
            }
            request.setAttribute("userName", userName);

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error contacting auth service: " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}

