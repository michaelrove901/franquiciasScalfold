package co.com.bancolombia.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("franchises")
public class FranchiseData {
    @Id
    private Long id;
    private String name;
}
