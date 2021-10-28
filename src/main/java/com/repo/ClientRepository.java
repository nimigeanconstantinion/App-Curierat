package com.repo;

import com.model.Client;

import java.sql.*;

public class ClientRepository {
    private Client client;
    private String jdbcURL = "jdbc:mysql://localhost:3307/curierat";
    private String userName = "root";
    private String password = "root";
    private Connection connection;
    private Statement statement;

    public ClientRepository() {
        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println("Nu m-am putut conecta la baza de date");
        }
    }

    private void executeStatement(String comanda) {
        try {
            statement.execute(comanda);
        } catch (Exception e) {
            System.out.println("Nu am putut realiza interogarea");
            e.printStackTrace();
        }
    }

    public void addClient(Client c) {

        if (!isClient(c.getNume(), c.getEmail())) {
            System.out.println("____________________Sunt la adaugare Cleint");
            String sqlStr = String.format("insert into clienti (nume,localitate,adresa,telefon,email,parola,categorie) " +
                    "values('%s','%s','%s','%s','%s','%s',%d)",
                    c.getNume(),c.getLocalitate(),c.getAdresa(),c.getTelefon(),
                    c.getEmail(),c.getParola(),0
                    );

            executeStatement(sqlStr);
        } else {
            System.out.println("Clientul exista in BD");
        }
    }

    public void delClient(Client c){
        String sqlStr=String.format("delete from clienti where id=%d",c.getId());
        executeStatement(sqlStr);
    }

    public void updClientNume(Client c,String newnume){
        String sqlStr=String.format("update clienti set nume='%s' where id=%d",newnume,c.getId());
        if(!isClient(newnume,c.getEmail())){
            executeStatement(sqlStr);
        }else{
            System.out.println("Clientul exista deja in BD");
        }
    }

    public void updClientAdrTelEmail(Client c,String newLocalitate,String newAdr,String newTel,String newEmail){
        String sqlStr="update clienti ";
        String prfx="";
        if(newLocalitate.length()>0){
            sqlStr+=prfx+String.format("set localitate=newLocalitate");
            prfx=",";
        }
        if(newAdr.length()>0){
            sqlStr+=prfx+String.format("set adresa=newAdr");
            prfx=",";
        }
        if(newTel.length()>0){
            sqlStr+=prfx+String.format("set telefon=newTel");
            prfx=",";
        }
        if(newEmail.length()>0){
            sqlStr+=prfx+String.format("set email=newEmail");
            prfx=",";
        }
        sqlStr+=String.format(" where id=%d",c.getId());

        if(!isClient(c.getNume(),newAdr)){
            executeStatement(sqlStr);
        }else{
            System.out.println("Clientul exista deja in BD");
        }
    }

    public void updPass(Client c,String oldPass,String newPass){
        String sqlStr=String.format("update clienti set parola='%s' where id=%d",newPass,c.getId());
        if(isPassValid(c.getId(),oldPass)){
            executeStatement(sqlStr);
        }else{
            System.out.println("Parola veche este eronata!!");
        }

    }

    public boolean isClient(int id) {
        String sqlStr = String.format("select count(*) from clienti where id=%d", id);
        int rez = 0;
        try {
            executeStatement(sqlStr);
            ResultSet rs = statement.getResultSet();
            if(rs.next()){
                rez=Integer.parseInt(rs.getString(1));

            }else{
                System.out.println("Nu exista clientul in BD");
            }


        } catch (Exception e) {

            System.out.println("Nu am putut interoga BD!");
            e.printStackTrace();
        }
        return rez > 0;
    }

    public Client toClient(int id) {
        Client retC=null;
        String sqlStr=String.format("select * from clienti where id=%d",id);
        try{
            executeStatement(sqlStr);
            ResultSet rs=statement.getResultSet();
            if(rs.next()){
                retC=new Client(Integer.parseInt(rs.getString(1)),
                        rs.getString(2),
                        rs.getString(3),rs.getString(4),rs.getString(5),
                        rs.getString(6),Integer.parseInt(rs.getString(7)),rs.getString(8));
            }else{
                System.out.println("Clientul nu exista");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return retC;
    }

    public boolean isClient(String n, String em) {
        String sqlStr = String.format("select count(*) from clienti where nume='%s' and email='%s'", n, em);
        executeStatement(sqlStr);
        int rez = 0;
        try {
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                rez = Integer.parseInt(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Nu am putut interoga BD!");
        }
        return rez > 0;
    }

    public Client isPassValid(String nume,String pass){
        String sqlStr = String.format("select id from clienti where nume='%s' and parola='%s' COLLATE utf8mb4_bin",nume,pass);
        executeStatement(sqlStr);
        Client c=null;
        try {
            ResultSet rs = statement.getResultSet();
            if(rs.next()) {
                c=toClient(Integer.parseInt(rs.getString(1)));
            }
        } catch (Exception e) {
            System.out.println("Nu e ok parola sau ID-ul utilizatorului");
        }
        return c;
    }

    public boolean isPassValid(int id,String pass){
        String sqlStr = String.format("select count(*) from clienti where id=%d and parola='%s'", id,pass);
        executeStatement(sqlStr);
        int rez = 0;
        try {
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                rez = Integer.parseInt(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Nu e ok parola sau numele utilizatorului");
        }
        return rez > 0;
    }

    public int retId(Client c){
        int ret=-1;
        String sqlStr=String.format("select id from clienti where nume='%s' and email='%s'",c.getNume(),c.getEmail());
        executeStatement(sqlStr);
        try{
            ResultSet rs=statement.getResultSet();
            if(rs.next()){
                ret=Integer.parseInt(rs.getString(1));
            }
        }catch (Exception e){
            System.out.println("Nu exista clientul "+c.getNume());
        }
        return ret;
    }

    public void afisareClient(Client c){
        System.out.println("Detalii Client:");
        System.out.println("ID: "+c.getId());
        System.out.println("Nume: "+c.getNume());
        System.out.println("Adresa: Loc. "+c.getLocalitate()+", "+c.getAdresa());
        System.out.println("Email: "+c.getEmail()+". Tel. "+c.getTelefon());
        System.out.println("Parola: "+c.getParola());
    }

}
