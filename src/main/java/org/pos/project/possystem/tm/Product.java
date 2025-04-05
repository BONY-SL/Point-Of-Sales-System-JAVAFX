package org.pos.project.possystem.tm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {

    private Integer id ;
    private String name ;
    private String description ;
    private double unitPrice ;
    private String supplierId;
}
