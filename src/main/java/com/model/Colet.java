package com.model;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;

@Data
@NoArgsConstructor@AllArgsConstructor@ToString
@Setter@Getter
public class Colet implements Comparable<Colet>{
    private int id_colet;
    private String awb;
    private int id_comanda;
    private int greutate;
    private int distanta;
    private String localitate_destinatie;
    private String adresa_destinatie;
    private String status;

    public Colet(int id_comanda,String adresa_destinatie){
        this.id_comanda=id_comanda;
        this.awb=genAWB();
        this.greutate=0;
        this.localitate_destinatie="";
        this.id_colet=0;
        this.adresa_destinatie=adresa_destinatie;
        this.status="";
    }
    public Colet(String awb){
        this.awb=awb;
    }

@Override
public boolean equals(Object o){
    Colet c=(Colet) o;
    return this.awb.equals(c.getAwb());
}
@Override
    public int compareTo(Colet c){
    return 0;
}

public String genAWB(){
    String lit="ABCDEFGHIJKLMNOPQRSTUVXYZ";
    String retawb="";
    int poz;
    int min=1,max=24;

    for(int i=0;i<4;i++){
        poz=(int) Math.floor(Math.random()*(max-min+1)+min);
        //poz=1+Math.round((float) Math.random()*24);
        retawb+=lit.substring(poz,++poz);
    }
    min=1000;
    max=9999;
    String cif1=String.valueOf((int) Math.floor(Math.random()*(max-min+1)+min));
    String cif2=String.valueOf((int) Math.floor(Math.random()*(max-min+1)+min));
    return retawb+cif1+cif2;
}

}
