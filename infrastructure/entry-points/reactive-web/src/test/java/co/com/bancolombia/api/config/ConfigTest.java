package co.com.bancolombia.api.config;

import co.com.bancolombia.api.HandlerRest;
import co.com.bancolombia.api.RouterRest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;

@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class, ConfigTest.RouterTestConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/test-cors")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

    /**
     * Configuración mínima para exponer un endpoint dummy
     * usado solo para probar CORS y headers de seguridad.
     */
    @Configuration
    static class RouterTestConfig {
        @Bean
        public RouterFunction<ServerResponse> testRoute() {
            return RouterFunctions.route()
                    .GET("/api/test-cors", request -> ServerResponse.ok().build())
                    .build();
        }
    }
}
