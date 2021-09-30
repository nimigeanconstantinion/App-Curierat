package com.repo;

import com.model.Track;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TrackRepository {
    private String jdbcURL="jdbc:mysql://localhost:3307/curierat";
    private String username="root";
    private String password="root";
    private Connection connection;
    private Statement statement;
    private DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TrackRepository(){
        try{
            connection= DriverManager.getConnection(jdbcURL,username,password);
            statement=connection.createStatement();
        }catch (Exception e){
            System.out.println("Nu m-am putut conecta la BD!");
            e.printStackTrace();
        }
    }

    private void executeStatement(String comanda){
        try{
            statement.execute(comanda);
        }catch (Exception e){
            System.out.println("nu am putut efectua interogarea");
        }
    }

    public void add(Track t){
        if(!isTrace(t)){
            String sqlStr=String.format("insert into status (id_colet,awb_colet,data_status," +
                            "status,locatie_id)" +
                    " values(%d,'%s','%s','%s','%s')",t.getId_colet(),t.getAwb_colet(),
                    t.getData_status().format(dtf),t.getStatus(),t.getLocatie_id());
            executeStatement(sqlStr);
        }
    }

    public void del(Track t){
        String sqlStr=String.format("delete from status where id_colet=%d and awb_colet='%s' and " +
                "locatie_id='%s'",t.getId_colet(),t.getAwb_colet(),t.getLocatie_id());
        executeStatement(sqlStr);
    }

    public void delAll(int id_comanda){
        String sqlStr=String.format("delete from status where id_colet in(select id_colet from colete where id_comanda=%d)",id_comanda);
        executeStatement(sqlStr);
    }

    public void upd(Track t){

    }

    public Track lastStatus(int id_colet){
        List<Track> lt=new ArrayList<>();
        Track retTrack =null;
        String sqlStr=String.format("select * from status where id_colet=%d order by data_status desc",id_colet);
        try{
            executeStatement(sqlStr);
            ResultSet rs=statement.getResultSet();
            if(rs.next()){
                retTrack =new Track(id_colet,rs.getString(2),
                        LocalDateTime.parse(rs.getString(3),dtf),rs.getString(4),
                        rs.getString(5));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return retTrack;
    }

    public List<Track> getTrack(String awb){
        List<Track> lt=new ArrayList<>();
        Track retTrack =null;
        //COLLATE utf8_bin;
        String sqlStr=String.format("select * from status where awb_colet='%s' COLLATE utf8mb4_bin order by data_status asc",awb);
        try{
            executeStatement(sqlStr);
            ResultSet rs=statement.getResultSet();
            while(rs.next()){
                 retTrack = new Track(Integer.parseInt(rs.getString(1)),rs.getString(2),
                        LocalDateTime.parse(rs.getString(3),dtf),rs.getString(4),
                        rs.getString(5));
                 lt.add(retTrack);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lt;
    }

    public boolean isTrace(Track t){
        String sqlStr=String.format("select count(*) from status where id_colet=%d and awb_colet='%s' and " +
                "locatie_id='%s' and status='%s'",t.getId_colet(),t.getAwb_colet(),t.getLocatie_id(),t.getStatus());
        try{
            executeStatement(sqlStr);
            ResultSet rs=statement.getResultSet();
            if(rs.next()){
                return Integer.parseInt(rs.getString(1))>0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDeletable(Track t){
        return true;
    }

    public List<String> getStari(){
        List<String> stari=new ArrayList<>();
        String sqlStr=String.format("select status from status_colet");
        executeStatement(sqlStr);
        try{
            ResultSet rs=statement.getResultSet();
            while(rs.next()){
                stari.add(rs.getString(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return stari;
    }


    public List<String> getHub(){
        List<String> huburi=new ArrayList<>();
        String sqlStr=String.format("select locatie from huburi");
        executeStatement(sqlStr);
        try{
            ResultSet rs=statement.getResultSet();
            while(rs.next()){
                huburi.add(rs.getString(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return huburi;
    }

}
