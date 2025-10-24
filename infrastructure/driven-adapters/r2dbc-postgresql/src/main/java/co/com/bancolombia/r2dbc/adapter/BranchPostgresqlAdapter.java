package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.r2dbc.entity.BranchData;
import co.com.bancolombia.r2dbc.mapper.BranchMapper;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class BranchPostgresqlAdapter implements BranchRepository {
    private final ProductDataRepository productDataRepository;
    private final BranchDataRepository branchDataRepository;

    @Override
    public Mono<Branch> save(Branch branch, Long franchiseId) {
        BranchData data = BranchMapper.toData(branch, franchiseId);
        return branchDataRepository.save(data)
                .map(saved -> branch.toBuilder().id(saved.getId()).build());
    }


}