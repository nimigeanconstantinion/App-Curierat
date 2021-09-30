package com.repo;

import com.model.Colet;
import com.model.StatusColet;
import com.model.Track;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ColetRepository {
    private String jdbcURL = "jdbc:mysql://localhost:3307/curierat";
    private String userName = "root";
    private String password = "root";
    private Connection connection;
    private Statement statement;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ColetRepository() {
        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println("Nu am reusit conectarea la BD");
        }
    }

    private void executeStatement(String s) {
        try{
            statement.execute(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void add(Colet c) {
        if (!isColet(c)) {
            String sqlStr = String.format("insert into colete (awb,id_comanda,greutate,distanta," +
                            "localitate_destinatie,adresa_destinatie,status) values('%s',%d,%d,%d,'%s','%s','%s')",
                    c.getAwb(), c.getId_comanda(), c.getGreutate(), c.getDistanta(), c.getLocalitate_destinatie(), c.getAdresa_destinatie(),
                    c.getStatus());
            System.out.println(sqlStr);
            executeStatement(sqlStr);
        }
    }

    public void del(Colet c){
        System.out.println("Sunt la stergerea coletului "+c.getAwb());
        String sqlStr=String.format("delete from status where awb_colet='%s'",c.getAwb());
        executeStatement(sqlStr);
        sqlStr=String.format("delete from colete where awb='%s'",c.getAwb());
        executeStatement(sqlStr);
    }

    public void upd(Colet c){
        String sqlStr=String.format("update colete set greutate=%d,distanta=%d,localitate_destinatie='%s'," +
                "adresa_destinatie='%s',status='%s' where id_colet=%d",c.getGreutate(),c.getDistanta(),
                c.getLocalitate_destinatie(),c.getAdresa_destinatie(),c.getStatus());
        executeStatement(sqlStr);
    }

    public boolean isColet(Colet c) {
        String sqlStr = String.format("select count(*) from colete where id_colet=%d", c.getId_colet());
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

    public int makePret(Colet c){
        String datacolet=getDataColet(c);
        if(datacolet!=null){
            String sqlStr=String.format("select pret from tarif where data1<='%s' and data2>='%s'",datacolet,datacolet);
            executeStatement(sqlStr);
            try{
                ResultSet rs=statement.getResultSet();
                if(rs.next()){
                    return Integer.parseInt(rs.getString(1))*c.getGreutate()*c.getDistanta();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public Colet getColet(String awb) {
        String sqlStr = String.format("select * from colete where awb='%s'",awb);
        Colet ret = null;
        executeStatement(sqlStr);
        try {

            ResultSet rs=statement.getResultSet();
            if(rs.next()){
                ret=new Colet(Integer.parseInt(rs.getString(1)),
                        rs.getString(2),Integer.parseInt(rs.getString(3)),
                        Integer.parseInt(rs.getString(4)),Integer.parseInt(rs.getString(5)),
                        rs.getString(6),rs.getString(7),
                        rs.getString(8));
            }
        } catch (Exception e) {
            System.out.println("Nu am gasit AWB");
        }
        return ret;
    }

    public List<Colet> getColete(int idCom){
        List<Colet> lista=new ArrayList<>();
        List<String> listawb=new ArrayList<>();
        String sqlStr=String.format("select * from colete where id_comanda=%d",idCom);
        try{
            executeStatement(sqlStr);
            ResultSet rs=statement.getResultSet();
            while(rs.next()){
                listawb.add(rs.getString(2));
            }
            for(String s:listawb){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    public String getDataColet(Colet c){
        String sqlStr=String.format("select data_comanda from comenzi where id=%d",c.getId_colet());
        executeStatement(sqlStr);
        try{
            ResultSet rs=statement.getResultSet();
            if(rs.next()){
                return rs.getString(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Track> getStatus(Colet c){
        List<Track> ls=new ArrayList<>();
        String sqlStr=String.format("select * from status where awb_colet='%s'",c.getAwb());
        executeStatement(sqlStr);
        Track t;
        try{
            ResultSet rs=statement.getResultSet();
            while(rs.next()){
               t= new Track(Integer.parseInt(rs.getString(1)),
                       rs.getString(2),LocalDateTime.parse(rs.getString(3),dtf),
                       rs.getString(4),rs.getString(5));
                ls.add(t);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ls;
    }

    public String getLastStatus(Colet c){
        List<Track> ls=getStatus(c);
        if(ls.size()>0){
            return ls.get(ls.size()-1).getStatus();
        }
        return null;
    }
}
    // public List<Track>

