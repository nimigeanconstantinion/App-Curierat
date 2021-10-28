package com.model;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Tarif {
    private LocalDateTime data1;
    private LocalDateTime date2;
    private double pret_min;
    private double pret_calc;
    private int greutate_max;
    private int distanta_max;


}
