package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.BranchDTO;
import co.com.bancolombia.api.dto.FranchiseDTO;
import co.com.bancolombia.api.dto.ProductDTO;
import co.com.bancolombia.api.dto.StockDTO;
import co.com.bancolombia.model.franchise.Franchise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

class HandlerRestTest {

    @Mock
    private HandlerRest handlerRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test create franchise handler")
    void testCreateFranchiseHandler() {
        Franchise response = new Franchise();
        response.setId(1L);
        response.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(FranchiseDTO.class)).thenReturn(Mono.just(new FranchiseDTO("Test Franchise")));

        when(handlerRest.createFranchise(request)).thenReturn(ServerResponse.ok().body(fromValue(response)));

        Mono<ServerResponse> result = handlerRest.createFranchise(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> {
                    assert res != null;
                })
                .verifyComplete();
    }
    @Test
    @DisplayName("Test add branch handler")
    void testAddBranchHandler() {
        Franchise response = new Franchise();
        response.setId(1L);
        response.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(BranchDTO.class)).thenReturn(Mono.just(new BranchDTO("Test Branch")));

        when(handlerRest.addBranch(request)).thenReturn(ServerResponse.ok().body(fromValue(response)));

        Mono<ServerResponse> result = handlerRest.addBranch(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> {
                    assert res != null;
                })
                .verifyComplete();
    }
    @Test
    @DisplayName("Test add product handler")
    void testAddProductHandler() {
        Franchise response = new Franchise();
        response.setId(1L);
        response.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.pathVariable("branchId")).thenReturn("1");
        when(request.bodyToMono(ProductDTO.class)).thenReturn(Mono.just(new ProductDTO("Test Product", 10)));

        when(handlerRest.addProduct(request)).thenReturn(ServerResponse.ok().body(fromValue(response)));

        Mono<ServerResponse> result = handlerRest.addProduct(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> {
                    assert res != null;
                })
                .verifyComplete();
    }
    @Test
    @DisplayName("Test remove product handler")
    void testRemoveProductHandler() {
        Franchise response = new Franchise();
        response.setId(1L);
        response.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.pathVariable("branchId")).thenReturn("1");
        when(request.pathVariable("productId")).thenReturn("1");

        when(handlerRest.removeProduct(request)).thenReturn(ServerResponse.ok().body(fromValue(response)));

        Mono<ServerResponse> result = handlerRest.removeProduct(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> {
                    assert res != null;
                })
                .verifyComplete();
    }
    @Test
    @DisplayName("Test update stock handler")
    void testUpdateStockHandler() {
        Franchise response = new Franchise();
        response.setId(1L);
        response.setName("Test Franchise");

        // Mock del ServerRequest
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");
        when(request.pathVariable("branchId")).thenReturn("1");
        when(request.pathVariable("productId")).thenReturn("1");
        when(request.bodyToMono(StockDTO.class)).thenReturn(Mono.just(new StockDTO(20)));

        when(handlerRest.updateStock(request)).thenReturn(ServerResponse.ok().body(fromValue(response)));

        Mono<ServerResponse> result = handlerRest.updateStock(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> {
                    assert res != null;
                })
                .verifyComplete();
    }
    @Test
    @DisplayName("Test top products per branch handler")
    void testTopProductPerBranchHandler() {
        Franchise response = new Franchise();
        response.setId(1L);
        response.setName("Test Franchise");

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("1");

        // Mock del handler
        when(handlerRest.topProductPerBranch(request)).thenReturn(ServerResponse.ok().body(fromValue(response)));

        Mono<ServerResponse> result = handlerRest.topProductPerBranch(request);

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(res -> {
                    assert res != null;
                })
                .verifyComplete();
    }

}
