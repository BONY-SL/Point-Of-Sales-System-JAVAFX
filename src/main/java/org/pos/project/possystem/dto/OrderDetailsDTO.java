package org.pos.project.possystem.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailsDTO {

    private int transactionId;
    private int prductId;
    private int qty;
}
