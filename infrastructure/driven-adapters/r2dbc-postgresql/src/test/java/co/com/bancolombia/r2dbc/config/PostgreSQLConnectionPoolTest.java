package co.com.bancolombia.r2dbc.config;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class PostgreSQLConnectionPoolTest {

    @Mock
    private ConnectionPool mockPool;

    @Mock
    private Connection mockConnection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockPool.create()).thenReturn(Mono.just(mockConnection));
        when(mockConnection.close()).thenReturn(Mono.empty());
    }

    @Test
    void connectionPool_canObtainConnection() {
        Mono<Connection> connectionMono = Mono.from(mockPool.create())
                .flatMap(conn -> Mono.from(conn.close()).thenReturn(conn));

        StepVerifier.create(connectionMono)
                .expectNext(mockConnection)
                .verifyComplete();
    }
}

