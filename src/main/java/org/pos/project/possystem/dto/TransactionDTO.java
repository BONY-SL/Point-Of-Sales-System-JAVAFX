package org.pos.project.possystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionDTO {

    private int transactionId;
    private Date orderDate;
    private List<OrderDetailsDTO> orderDetailList;
}
