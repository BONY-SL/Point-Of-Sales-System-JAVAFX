package org.pos.project.possystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private String id ;
    private String name ;
    private String description ;
    private double unitPrice ;
    private String supplierId;
}
