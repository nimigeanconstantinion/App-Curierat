package com.view;

import com.model.*;
import com.repo.ClientRepository;
import com.repo.ColetRepository;
import com.repo.ComandaRepository;
import com.repo.TrackRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewOperator {
    private ClientRepository clientRepo;
    private ColetRepository coletRepo;
    private ComandaRepository comRepo;
    private TrackRepository trkRepo;
    private Scanner sc;
    private Statement statement;
    private Client client;
    private Comanda comanda;
//    private Connection conexx;
//    private String username;
//    private String password;
//    private String jdbcURL="jdbc:mysql://localhost:3307/curierat";

    public ViewOperator(Client c){
        clientRepo=new ClientRepository();
        coletRepo=new ColetRepository();
        comRepo=new ComandaRepository();
        trkRepo=new TrackRepository();
        sc=new Scanner(System.in);
        statement=new Conexiune().getStatement();
        client=c;
        goGest();
    }

    private void executeStatement(String str){
        try{
            statement.execute(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void goGest(){
        boolean go=true;
        while(go){
            meniu1();
            int opt=Integer.parseInt(sc.nextLine());
            switch (opt){
                case 1:
                    if(client.getCategorie()==1){
//                        System.out.print("AWB colet :");
//                        String awb= sc.nextLine();
//
//                        Colet clt=new Colet();
//                        clt=coletRepo.getColet(awb);
//                        coletRepo=new ColetRepository(clt);
//                        trkRepo=new TrackRepository();
//                        Track track=new Track(clt.getId_colet(),clt.getAwb());
//                        track.setStatus("preluat de curier");
//                        track.setLocatie_id(client.getLocalitate());
//                        track.setData_status(LocalDateTime.now());
//                        trkRepo.add(track);
                          coletRepo=new ColetRepository();
                          List<Colet> lista=new ArrayList<>();
                          lista=coletRepo.getColeteLocal(client.getLocalitate());
                        System.out.println("aveti "+lista.size()+" colete de preluat");
                          if(lista.size()>0){
                              int nr=1;
                              for(Colet xc:lista){
                                  System.out.println(nr+" "+xc.toString());
                              }
                              System.out.print("Alegeti nr crt al coletului pe care il preluati : ");
                              int rank=Integer.parseInt(sc.nextLine());
                        Colet clt=new Colet();
                        clt=lista.get(rank-1);
                        coletRepo=new ColetRepository(clt);
                        trkRepo=new TrackRepository();
                        Track track=new Track(clt.getId_colet(),clt.getAwb());
                        track.setStatus("preluat de curier");
                        track.setLocatie_id(client.getLocalitate());
                        track.setData_status(LocalDateTime.now());
                        trkRepo.add(track);

                          }else{
                              System.out.println("Nu aveti colete de preluat");
                          }
                    }else{

                    }
                case 0:
                    go=false;
                    break;
            }
        }
    }
    public void meniu1() {
        System.out.println("Alegeti o optiune:");
        System.out.println("1. Receptie colet");
        System.out.println("2. Trimite colet");
        System.out.println("3. Livreaza colet la adresa");
        System.out.println("0. Exit");
    }
}
