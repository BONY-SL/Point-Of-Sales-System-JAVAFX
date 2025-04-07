package org.pos.project.possystem.tm;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductStock {

    private Integer id;
    private Integer productId;
    private double quantity;
    private LocalDateTime updateTime;
}
