package com.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Client implements Comparable<Client> {
    private int id;
    private String nume;
    private String adresa;
    private String telefon;
    private String email;
    private String parola;
    private int categorie;

    public Client(String nume,String adresa){
        this.nume=nume;
        this.adresa=adresa;
    }

    @Override
    public boolean equals(Object o){
        Client c=(Client) o;
        return nume.equals(c.getNume())&&adresa.equals(c.getAdresa());
    }

    @Override
    public int compareTo(Client o) {
        return nume.compareTo(o.getNume());
    }
}
