package com.repo;

import com.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

public class ComandaRepository {
    private String jdbcURL = "jdbc:mysql://localhost:3307/curierat";
    private String username = "root";
    private String password = "root";
    private Connection connection;
    private Statement statement;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ColetRepository coletRepository;
    private ClientRepository clientRepository;
    private TrackRepository trackRepository;
    public ComandaRepository() {
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println("Nu m-am putut conecta la BD!");
            e.printStackTrace();
        }
        coletRepository=new ColetRepository();
        clientRepository=new ClientRepository();
        trackRepository=new TrackRepository();
    }

    private void executeStatement(String comanda) {
        try {
            System.out.println("************ "+comanda);
            statement.execute(comanda);
        } catch (Exception e) {
            System.out.println("nu am putut efectua interogarea");
        }
    }

    public void add(Comanda c) {
        if (getIdActiv(c.getId_client()) < 0) {
            String sqlStr = String.format("insert into comenzi (id_client,data_comanda,nr_colete," +
                            "pret_total," +
                            "nr_factura,stare_comanda) values(%d,'%s',0,0,'',0)", c.getId_client(),
                    LocalDateTime.now().format(dtf));
            executeStatement(sqlStr);
        } else {
            System.out.println("Clientul are o comanda in stare initiata, faceti update!!");
        }

    }

    public void del(Comanda c) {
        if (isDeletable(c.getId())) {
            String sqlStr = String.format("delete from comenzi where id=%d", c.getId());
            executeStatement(sqlStr);
        } else {
            System.out.println("Comanda este nu mai poate fi stearsa din BD!!!");
        }

    }

    public void updComanda(Comanda newC) {
        String df="";
        if(newC.getData_factura()!=null){
            System.out.println("nu e nula data la updComanda");
            df=newC.getData_factura().format(dtf);
        }
        System.out.println("Sunt la update comanda");
        System.out.println("Id comanda="+newC.getId()+" noul nr de colete="+newC.getNr_colete());

        if (getIdActiv(newC.getId_client()) > 0) {
            String sqlStr = String.format("update comenzi set nr_colete=%d,pret_total=%d,nr_factura='%s'," +
                            "data_factura='%s',stare_comanda=%d where id=%d", newC.getNr_colete(), newC.getPret_total(),
                    newC.getNr_factura(),df, newC.getStare_comanda(),newC.getId());
            executeStatement(sqlStr);
        } else {
            System.out.println("Nu aveti o comanda ce poate fi modificata!!");
        }

    }

    public boolean isComanda(Comanda c) {
        String sqlStr = String.format("select count(*) from comenzi where id=%d", c.getId());
        try {
            executeStatement(sqlStr);
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                return Integer.parseInt(rs.getString(1)) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getIdActiv(int id_client) {
        int retId = -1;
        String sqlStr = String.format("select id from comenzi where id_client=%d and stare_comanda=0", id_client);
        try {
            executeStatement(sqlStr);
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                retId = Integer.parseInt(rs.getString(1));
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
        return retId;
    }


    public Comanda getComandaActiva(int id_client) {
        Comanda c = new Comanda();
        if (getIdActiv(id_client) > 0) {
            String sqlStr = String.format("select * from comenzi where id_client=%d and stare_comanda=0", id_client);
            try {
                executeStatement(sqlStr);
                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    c.setId(Integer.parseInt(rs.getString(1)));
                    c.setId_client(Integer.parseInt(rs.getString(2)));
                    c.setData_comanda(LocalDateTime.parse(rs.getString(3), dtf));
                    c.setNr_colete(Integer.parseInt(rs.getString(4)));
                    c.setPret_total(Integer.parseInt(rs.getString(5)));
                    c.setNr_factura(rs.getString(6));
                    if (rs.getString(7) != null) {
                        c.setData_factura(LocalDateTime.parse(rs.getString(8), dtf));
                    } else {
                        c.setData_factura(null);
                    }
                    c.setStare_comanda(Integer.parseInt(rs.getString(8)));
                    return c;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    public Comanda getComanda(int id) {
        Comanda c = null;
        String sqlStr = String.format("select * from comenzi where id=%d", id);
        try {
            executeStatement(sqlStr);
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                c=new Comanda();
                c.setId(Integer.parseInt(rs.getString(1)));
                c.setId_client(Integer.parseInt(rs.getString(2)));
                c.setData_comanda(LocalDateTime.parse(rs.getString(3), dtf));
                c.setNr_colete(Integer.parseInt(rs.getString(4)));
                c.setPret_total(Integer.parseInt(rs.getString(5)));
                c.setNr_factura(rs.getString(6));
                if (rs.getString(7) != null) {
                    c.setData_factura(LocalDateTime.parse(rs.getString(8), dtf));
                } else {
                    c.setData_factura(null);
                }
                c.setStare_comanda(Integer.parseInt(rs.getString(8)));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public boolean isDeletable(int id_Comanda) {
        String sqlStr = String.format("select count(*) from comenzi where stare_comanda=0");
        try {
            executeStatement(sqlStr);
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                return Integer.parseInt(rs.getString(1)) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Comanda> getComenziClient(int idC) {
        List<Comanda> retL = new ArrayList<>();
        List<Integer> listaId=new ArrayList<>();
        String sqlStr = String.format("select id from comenzi where id_client=%d", idC);
        executeStatement(sqlStr);
        try {
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                listaId.add(Integer.parseInt(rs.getString(1)));
            }
            for(int k:listaId){
                retL.add(getComanda(k));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retL;
    }

    public void printComanda(Comanda c){
        List<Colet> listaColete=new ArrayList<>();
        listaColete=coletRepository.getColete(c.getId());
        Client cl=new Client();
        cl=clientRepository.toClient(c.getId_client());
        System.out.println("***************************************************************");
        System.out.println("Comanda :"+c.getId()+" din data de "+c.getData_comanda().format(dtf));
        System.out.println("Client: "+cl.getNume());
        System.out.println("Adresa: "+cl.getAdresa());
        System.out.println("Nr de colete="+c.getNr_colete());
        System.out.println("Cost comanda="+getPretActual(c));
        for(Colet colet:listaColete){
            System.out.println("----------------------------------------------------------------");
            System.out.println("Colet ");
            System.out.println(colet);
            System.out.println("Tracking AWB:");
            for(Track t:coletRepository.getStatus(colet)){
                System.out.println("* "+t);
            }
        }

    }

    public int getPretActual(Comanda c){
        List<Colet> lista=coletRepository.getColete(c.getId());
        int cost=0;
        for(Colet colet:lista){
            if(!colet.getStatus().equals("anulat")){
                cost+=coletRepository.makePret(colet);
            }
        }
        return cost;
    }
}
