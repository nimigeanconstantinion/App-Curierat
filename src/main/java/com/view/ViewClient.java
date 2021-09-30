package com.view;

import com.model.*;
import com.repo.ClientRepository;
import com.repo.ColetRepository;
import com.repo.ComandaRepository;
import com.repo.TrackRepository;

import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewClient {
    private ClientRepository clientRepo;
    private ComandaRepository comandaRepo;
    private ColetRepository coletRepo;
    private TrackRepository trackRepo;
    private Statement statement;
    private Scanner sc;
    private Client client;
    private Comanda comanda;

    public ViewClient() {
        clientRepo = new ClientRepository();
        comandaRepo = new ComandaRepository();
        coletRepo = new ColetRepository();
        trackRepo = new TrackRepository();
        statement = new Conexiune().getStatement();
        sc = new Scanner(System.in);
    }


    public void goClient() {
        boolean quit = false;
        while (!quit) {
            meniu1();
            int opt = Integer.parseInt(sc.nextLine());
            switch (opt) {
                case 1:
                    login();
                    break;
                case 2:

                case 0:
                    quit = true;
                    break;
            }
        }
    }

    public void goWork() {
        boolean quit = false;
        while (!quit) {
            meniuWork();
            int opt = Integer.parseInt(sc.nextLine());
            switch (opt) {
                case 1:
                    addColet();
                    break;
                case 2:
                    showComenzi();
                    break;
                case 3:
                    delComanda();
                    break;
                case 4:
                    delColet();
                    break;


                case 0:
                    quit = true;
                    break;
            }
        }
    }

    public void goGest() {
        boolean quit = false;
        while (!quit) {
            meniuGest();
            int opt = Integer.parseInt(sc.nextLine());
            switch (opt) {
                case 1:
                    changeColet();
                    break;
                case 0:
                    quit = true;
                    break;
            }
        }
    }

    public void meniu1() {
        System.out.println("Alegeti o optiune:");
        System.out.println("1. Login");
        System.out.println("2. Creare utilizator");
        System.out.println("0. Exit");
    }

    public void meniuWork() {
        System.out.println("Alegeti o optiune:");
        System.out.println("1. Adauga un COLET");
        System.out.println("2. Comenzile mele");
        System.out.println("3. Anuleaza comanda");
        System.out.println("4. Anuleaza colet");
        System.out.println("0. Exit");
    }

    public void meniuGest() {
        System.out.println("Alegeti o optiune:");
        System.out.println("1. Modifica statusul unui colet");
        System.out.println("0. Exit");
    }


    public void login() {
        System.out.print("Dati ID-ul DVS: ");
        int userID = Integer.parseInt(sc.nextLine());
        System.out.print("Parola: ");
        String pass = sc.nextLine();
        if (clientRepo.isPassValid(userID, pass)) {
            this.client = clientRepo.toClient(userID);
            if (client.getCategorie() == 0) {
                goWork();
            } else {
                goGest();
            }
        }
    }

    public void addColet() {
        if (comandaRepo.getComandaActiva(client.getId()) != null) {
            System.out.println("Nu am o comanda activa");
            comandaRepo.add(new Comanda(client.getId()));
            comanda = comandaRepo.getComandaActiva(client.getId());
        } else {
            System.out.println("flag  kkk");
            comanda = comandaRepo.getComandaActiva(client.getId());
        }
        System.out.println("Comanda id="+comanda.getId());
        System.out.print("Indicati destinatia: ");
        String adresa = sc.nextLine();
        Colet c = new Colet(comanda.getId(), adresa);
        String awb = c.getAwb();
        coletRepo.add(c);
        comanda.setNr_colete(comanda.getNr_colete() + 1);
        c = coletRepo.getColet(awb);
        Track t = new Track(c.getId_comanda(), c.getAwb());
        trackRepo.add(t);
        comanda.setNr_colete(comanda.getNr_colete()+1);
        System.out.println("Sunt la upd comanda");
        comandaRepo.updComanda(comanda);
        System.out.println("Gataaaaaaaaaaaaaaaaaaaaa");
    }

    public void showComenzi() {
        List<Comanda> listaComenzi = new ArrayList<>();
        List<Colet> listaColete = new ArrayList<>();
        List<Track> listaTrack = new ArrayList<>();
        listaComenzi = comandaRepo.getComenziClient(client.getId());
        for (Comanda c : listaComenzi) {
            comandaRepo.printComanda(c);
        }
    }

    public void delComanda() {
        System.out.print("Dati ID-ul comenzii de sters :");
        int idC = Integer.parseInt(sc.nextLine());
        if (comandaRepo.getComandaActiva(client.getId()).getId() == idC) {
            comandaRepo.del(comandaRepo.getComanda(idC));
        } else {
            System.out.println("Comanda nu exista sau este deja in procesare/livrare");
        }
    }

    public void delColet() {
        System.out.print("Dati AWB-ul coletului de sters :");
        String awbC = sc.nextLine();
        Colet c = new Colet(awbC);
        String lastStatus = coletRepo.getLastStatus(c);
        if (lastStatus.length()==0) {
            coletRepo.del(new Colet(awbC));
            comanda.setNr_colete(comanda.getNr_colete() -1);
            if(comanda.getNr_colete()==0){
                comandaRepo.del(comanda);
            }else{
                comandaRepo.updComanda(comanda);
            }
        } else {
            System.out.println("Nu putem sterge un colet la care a inceput livrarea");
        }
    }

    public void changeColet(){
        System.out.print("Dati AWB-ul coletului: ");
        String awbT=sc.nextLine();
        Colet colet=coletRepo.getColet(awbT);
        if(colet!=null){
            List<Track> ltrk=trackRepo.getTrack(awbT);
            System.out.println("Track-ul coletului este:");
            System.out.println(ltrk);
            System.out.println("Alegeti una din starile in care trece coletul:");
            List<String> lst=trackRepo.getStari();
            int rk=1;
            String newstare="";
            for(int i=1;i<lst.size();i++){
                System.out.println(i+". "+lst.get(i));
                rk++;
            }
            int chst=Integer.parseInt(sc.nextLine());
            if(chst>=1&&chst<=4) {
                newstare=lst.get(chst);
            }else{
                System.out.println("Ati ales o optiune care nu exista in intervalul de selectie!");
            }
            System.out.print("Dati localitatea in care receptionati coletul: ");
            String locatie=sc.nextLine();
            if(locatie.toUpperCase().equals(colet.getLocalitate_destinatie().toUpperCase())){
                if(chst==2){
                    newstare=lst.get(3);
                }
            }
            Track newTrack=new Track(colet.getId_colet(), colet.getAwb());
            newTrack.setData_status(LocalDateTime.now());
            newTrack.setStatus(newstare);
            newTrack.setLocatie_id(locatie);
            trackRepo.add(newTrack);

        }else{
            System.out.println("Nu exista un colet cu awb-ul introdus!!");
        }

    }
}
