package org.pos.project.possystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartDTO {

    private Integer itemCode;
    private String name;
    private double qty;
    private double unitPrice;
    private double total;
}
