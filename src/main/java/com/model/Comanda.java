package com.model;
import lombok.*;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor@AllArgsConstructor@ToString
@Setter@Getter
public class Comanda implements Comparable<Comanda>{
    private int id;
    private int id_client;
    private LocalDateTime data_comanda;
    private int nr_colete;
    private int pret_total;
    private String nr_factura;
    private LocalDateTime data_factura;
    private int stare_comanda;

    public Comanda(int id_client){
        this.id_client=id_client;
        this.data_comanda=LocalDateTime.now();
        this.nr_colete=0;
        this.nr_factura="";
        this.stare_comanda=0;
    }
    @Override
    public boolean equals(Object o){
        Comanda c=(Comanda) o;
        return true;
    }

    public int compareTo(Comanda c){
        return 0;
    }

}
