package API_Gateway.dev.API_Gateway.config;

import API_Gateway.dev.API_Gateway.filters.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GatewayConfig implements WebMvcConfigurer {

    private AuthorizationFilter authorizationFilter;

    @Autowired
    public void setAuthorizationFilter(AuthorizationFilter authorizationFilter) {
        this.authorizationFilter = authorizationFilter;
    }
}