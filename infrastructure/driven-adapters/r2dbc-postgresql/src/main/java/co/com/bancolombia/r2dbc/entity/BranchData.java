package co.com.bancolombia.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("branches")
public class BranchData {
    @Id
    private String id;
    private String name;
    private Long franchiseId;
}
