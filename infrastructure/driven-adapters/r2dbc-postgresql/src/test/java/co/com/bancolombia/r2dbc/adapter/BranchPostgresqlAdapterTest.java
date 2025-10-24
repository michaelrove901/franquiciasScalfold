package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.r2dbc.entity.BranchData;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

class BranchPostgresqlAdapterTest {

    @InjectMocks
    private BranchPostgresqlAdapter adapter;

    @Mock
    private BranchDataRepository branchDataRepository;

    @Mock
    private ProductDataRepository productDataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldReturnSavedBranch_withId() {

        Branch branch = Branch.builder()
                .name("Sucursal Test")
                .products(new ArrayList<>())
                .build();
        Long franchiseId = 1L;

        BranchData savedData = new BranchData();
        savedData.setId(100L);
        savedData.setName(branch.getName());
        savedData.setFranchiseId(franchiseId);

        when(branchDataRepository.save(org.mockito.ArgumentMatchers.any(BranchData.class)))
                .thenReturn(Mono.just(savedData));


        Mono<Branch> result = adapter.save(branch, franchiseId);


        StepVerifier.create(result)
                .expectNextMatches(b -> b.getId().equals(100L) && b.getName().equals("Sucursal Test"))
                .verifyComplete();
    }
}
