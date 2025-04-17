package org.pos.project.possystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceDTO {

    private String name;
    private int qty;
    private double unitPrice;
    private double total;
    private String date;
    private int orderId;
    private double netTotal;

    public InvoiceDTO(String name, int qty, double unitPrice, String date, int orderId, double netTotal) {
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.total = qty * unitPrice;
        this.date = date;
        this.orderId = orderId;
        this.netTotal = netTotal;
    }

}
