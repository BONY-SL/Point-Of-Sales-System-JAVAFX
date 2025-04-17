package org.pos.project.possystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQtyReportDTO {
    private Date date;
    private int id;
    private int qty;
}