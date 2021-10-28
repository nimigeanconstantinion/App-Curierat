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
    private String localitate;
    private String adresa;
    private String email;
    private String parola;
    private int categorie;
    private String telefon;

    public Client(String nume,String email){
        this.nume=nume;
        this.email=email;
    }

    @Override
    public boolean equals(Object o){
        Client c=(Client) o;
        return email.equals(c.getEmail());
    }

    @Override
    public int compareTo(Client o) {
        return nume.compareTo(o.getNume());
    }

}
