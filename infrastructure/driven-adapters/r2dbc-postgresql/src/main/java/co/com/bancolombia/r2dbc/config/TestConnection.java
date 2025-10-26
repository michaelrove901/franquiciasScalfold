package co.com.bancolombia.r2dbc.config;


import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TestConnection implements CommandLineRunner {

    private final ConnectionFactory connectionFactory;

    @Override
    public void run(String... args) {
        System.out.println("🔍 Probando conexión R2DBC con PostgreSQL...");

        Mono.from(connectionFactory.create())
                .flatMapMany(c -> c.createStatement("SELECT 1").execute())
                .flatMap(r -> r.map((row, metadata) -> row.get(0)))
                .doOnNext(result -> System.out.println("✅ Conexion exitosa con PostgreSQL. Resultado: " + result))
                .doOnError(e -> System.err.println("❌ Error al conectar con PostgreSQL: " + e.getMessage()))
                .subscribe();
    }
}
