package co.com.bancolombia.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("products")
public class ProductData {
    @Id
    private Long id;
    private String name;
    private Integer stock;
    private Long branchId;
}
