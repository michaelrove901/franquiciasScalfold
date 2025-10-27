package co.com.bancolombia.config;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {

            FranchiseUseCase useCase = context.getBean(FranchiseUseCase.class);

            assertNotNull(useCase, "FranchiseUseCase bean should exist");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public FranchiseRepository franchiseRepository() {
            return new FranchiseRepository() {
                @Override
                public Mono<Franchise> save(Franchise franchise) {
                    return Mono.empty();
                }

                @Override
                public Mono<Franchise> findById(Long id) {
                    return Mono.empty();
                }

                @Override
                public Mono<Franchise> update(Franchise franchise) {
                    return Mono.empty();
                }
            };
        }

        @Bean
        public BranchRepository branchRepository() {
            return new BranchRepository() {
                @Override
                public Mono<Branch> save(Branch branch, Long franchiseId) {
                    return Mono.empty();
                }
            };
        }

        @Bean
        public ProductRepository productRepository() {
            return new ProductRepository() {

                @Override
                public Mono<Product> save(Product product, Long branchId) {
                    return null;
                }

                @Override
                public Mono<Void> deleteById(Long id) {
                    return null;
                }
            };
        }
    }
}
