package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .flatMap(dto -> franchiseUseCase.createFranchise(dto.getName()))
                .flatMap(franchise -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchise))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        return request.bodyToMono(BranchDTO.class)
                .flatMap(dto -> franchiseUseCase.addBranch(franchiseId, dto.getName()))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        String branchId = request.pathVariable("branchId");

        return request.bodyToMono(ProductDTO.class)
                .flatMap(dto -> franchiseUseCase.addProductToBranch(franchiseId, branchId, dto.getName(), dto.getStock()))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return franchiseUseCase.removeProduct(franchiseId, branchId, productId)
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return request.bodyToMono(StockDTO.class)
                .flatMap(dto -> franchiseUseCase.updateProductStock(franchiseId, branchId, productId, dto.getStock()))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> topProductPerBranch(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(franchiseUseCase.getTopProductPerBranch(franchiseId), FranchiseUseCase.ProductWithBranch.class)
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }
}