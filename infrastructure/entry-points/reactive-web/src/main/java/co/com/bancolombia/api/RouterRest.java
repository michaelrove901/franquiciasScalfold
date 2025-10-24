package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
@Bean
    public RouterFunction<ServerResponse> routes(HandlerRest handler) {
        return route(POST("/api/franchises"), handler::createFranchise)
                .andRoute(POST("/api/franchises/{franchiseId}/branches"), handler::addBranch)
                .andRoute(POST("/api/franchises/{franchiseId}/branches/{branchId}/products"), handler::addProduct)
                .andRoute(DELETE("/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}"), handler::removeProduct)
                .andRoute(PUT("/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock"), handler::updateStock)
                .andRoute(GET("/api/franchises/{franchiseId}/top-products"), handler::topProductPerBranch);
    }
}
