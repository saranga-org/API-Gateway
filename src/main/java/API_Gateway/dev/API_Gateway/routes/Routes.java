package API_Gateway.dev.API_Gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {
    @Bean
    public RouterFunction<ServerResponse> fuelQuotaServiceRoute(){
        return GatewayRouterFunctions.route("fuel_quota_service")
                .route(RequestPredicates.path("/api/fuel-quota"), HandlerFunctions.http("http://localhost:8084"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRoute(){
        return GatewayRouterFunctions.route("notification_service")
                .route(RequestPredicates.path("/api/notification"),HandlerFunctions.http("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> resourceServiceRoute(){
        return GatewayRouterFunctions.route("resource_service")
                .route(RequestPredicates.path("/api/resource"),HandlerFunctions.http("http://localhost:8082"))
                .build();
    }
}
