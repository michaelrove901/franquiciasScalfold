package co.com.bancolombia.usecase.franchise;

import lombok.RequiredArgsConstructor;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class FranchiseUseCase {

    private static final Logger log = LoggerFactory.getLogger(FranchiseUseCase.class);
    private final FranchiseRepository FranchiseRepository;

    
    public Mono<Franchise> createFranchise(String name) {
        Franchise f = Franchise.builder().name(name).build();
        return FranchiseRepository.save(f)
                .doOnNext(fr -> log.info("Franchise created: id={} name={}", fr.getId(), fr.getName()))
                .doOnError(err -> log.error("Error creating franchise: {}", err.getMessage()));
    }

   
    public Mono<Franchise> addBranch(Long franchiseId, String branchName) {
        return FranchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> {
                    Branch b = Branch.builder()
                            .id(UUID.randomUUID().toString())
                            .name(branchName)
                            .build();
                    fr.getBranches().add(b);
                    return FranchiseRepository.update(fr);
                })
                .doOnNext(fr -> log.info("Branch {} added to franchise {}", branchName, franchiseId));
    }

    
    public Mono<Franchise> addProductToBranch(Long franchiseId, String branchId, String productName, int stock) {
        return FranchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> Mono.fromCallable(() -> {
                    Branch branch = fr.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

                    Product p = Product.builder()
                            .productId(UUID.randomUUID().toString())
                            .name(productName)
                            .stock(stock)
                            .branchId(branchId)
                            .build();
                    branch.getProducts().add(p);
                    return fr;
                }).flatMap(FranchiseRepository::update))
                .doOnNext(x -> log.info("Product {} added to branch {} of franchise {}", productName, branchId, franchiseId));
    }

    public Mono<Franchise> removeProduct(Long franchiseId, String branchId, String productId) {
        return FranchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> Mono.fromCallable(() -> {
                    Branch branch = fr.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

                    boolean removed = branch.getProducts().removeIf(p -> Optional.ofNullable(p.getProductId()).orElse("").equals(productId));
                    if (!removed) throw new IllegalArgumentException("Product not found");
                    return fr;
                }).flatMap(FranchiseRepository::update));
    }

    public Mono<Franchise> updateProductStock(Long franchiseId, String branchId, String productId, int newStock) {
        return FranchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(fr -> Mono.fromCallable(() -> {
                    Branch branch = fr.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

                    Product product = branch.getProducts().stream()
                            .filter(p -> Optional.ofNullable(p.getProductId()).orElse("").equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

                    product.setStock(newStock);
                    return fr;
                }).flatMap(FranchiseRepository::update));
    }

    public Flux<ProductWithBranch> topProductPerBranch(Long franchiseId) {
        return FranchiseRepository.findById(franchiseId)
                .flatMapMany(fr ->
                        Flux.fromIterable(fr.getBranches())
                                .flatMap(branch -> Flux.fromIterable(branch.getProducts())
                                        .sort(Comparator.comparing(Product::getStock).reversed())
                                        .next()
                                        .map(product -> new ProductWithBranch(branch.getId(), branch.getName(), product))
                                )
                )
                .switchIfEmpty(Flux.error(new IllegalArgumentException("Franchise not found")));
    }

    public record ProductWithBranch(String branchId, String branchName, Product product) {}
}
