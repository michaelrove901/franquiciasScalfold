package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.FranchiseData;

import java.util.List;

public class FranchiseMapper {

    public static FranchiseData toData(Franchise model) {
        FranchiseData data = new FranchiseData();
        if (model.getId() != null) {
            data.setId(model.getId());
        }
        data.setName(model.getName());
        return data;
    }

    public static Franchise toModel(FranchiseData data, List<Branch> branches) {
        return Franchise.builder()
                .id(data.getId())
                .name(data.getName())
                .branches(branches)
                .build();
    }
}
