package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.r2dbc.entity.FranchiseData;
import co.com.bancolombia.r2dbc.mapper.FranchiseMapper;
import co.com.bancolombia.r2dbc.repository.BranchRepository;
import co.com.bancolombia.r2dbc.repository.FranchiseRepository;
import co.com.bancolombia.r2dbc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranchisePostgresqlAdapter implements FranchiseRepository {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return franchiseRepository.save(FranchiseMapper.toData(franchise))
                .map(saved -> {
                    franchise.setId(saved.getId());
                    return franchise;
                });
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return franchiseRepository.findById(id)
                .flatMap(franchiseData ->
                        branchRepository.findAllByFranchiseId(franchiseData.getId())
                                .flatMap(branchData ->
                                        productRepository.findAllByBranchId(branchData.getId())
                                                .map(FranchiseMapper::toModel)
                                                .collectList()
                                                .map(products -> FranchiseMapper.toModel(branchData, products))
                                )
                                .collectList()
                                .map(branches -> FranchiseMapper.toModel(franchiseData, branches))
                );
    }

    @Override
    public Flux<Franchise> findAll() {
        return franchiseRepository.findAll()
                .flatMap(frData -> findById(frData.getId()));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return branchRepository.findAllByFranchiseId(id)
                .flatMap(branch -> productRepository.findAllByBranchId(branch.getId())
                        .flatMap(p -> productRepository.deleteById(p.getId()))
                        .then(branchRepository.deleteById(branch.getId()))
                )
                .then(franchiseRepository.deleteById(id));
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return save(franchise);
    }
}
