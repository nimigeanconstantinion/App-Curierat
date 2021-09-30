package com.view;

import com.model.Conexiune;
import com.model.Track;
import com.repo.ClientRepository;
import com.repo.ColetRepository;
import com.repo.ComandaRepository;
import com.repo.TrackRepository;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ViewHome {
    private ClientRepository clientRepo;
    private ComandaRepository comandaRepo;
    private ColetRepository coletRepo;
    private TrackRepository traceRepo;
    private Scanner sc=new Scanner(System.in);
    private List<Track> track;
    private List<String> huburi;
    private List<String> statusuri;
    private Statement statement;
    private ViewClient vwc;
    public ViewHome(){
            clientRepo=new ClientRepository();
            comandaRepo=new ComandaRepository();
            coletRepo=new ColetRepository();
            traceRepo=new TrackRepository();
            track=new ArrayList<>();
            statement=new Conexiune().getStatement();
            huburi=loadHuburi();
            statusuri=loadStatusuri();
            vwc=new ViewClient();
            playHome();
    }

    public List<String> loadHuburi(){
        String sqlStr="select * from huburi";
        List<String> rest=new ArrayList<>();
        try{
            statement.execute(sqlStr);
            ResultSet rs=statement.getResultSet();
            while(rs.next()){
                rest.add(rs.getString(1));
            }
        }catch(Exception e){
            System.out.println("Nu am putut incarca huburile");
        }
        Collections.sort(rest);
        return rest;
    }

    public List<String> loadStatusuri(){
        String sqlStr="select * from status_colet";
        List<String> rest=new ArrayList<>();
        try{
            statement.execute(sqlStr);
            ResultSet rs=statement.getResultSet();
            while(rs.next()){
                rest.add(rs.getString(1));
            }
        }catch(Exception e){
            System.out.println("Nu am putut incarca statusurile");
        }
        Collections.sort(rest);
        return rest;
    }

    public void playHome(){
        boolean go=true;
        while(go){
            meniu();
            int opt=Integer.parseInt(sc.nextLine());
            switch (opt){
                case 1:
                    awbTrack();
                    break;
                case 2:
                    vwc.login();
                    break;
                case 0:
                    go=false;
                    break;
            }


        }
    }

    public void meniu(){
        System.out.println("Alegeti una din optiunile de mai jos");
        System.out.println("1. AWB tracking");
        System.out.println("2. LANSARE/GESTIUNE Comenzi");
        System.out.println("0. Iesire");
    }

    public void awbTrack(){
        System.out.print("Dati AWB-ul coletului: ");
        String awb=sc.nextLine();
        track=traceRepo.getTrack(awb);
        if(track.size()>0){
        for(Track t:track){
            System.out.println(t);
        }}else{
            System.out.println("NU exista AWB in baza de date!!");
        }
    }
}
