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
    private ViewOperator viewOp;
    public ViewClient() {
        clientRepo = new ClientRepository();
        comandaRepo = new ComandaRepository();
        coletRepo = new ColetRepository();
        trackRepo = new TrackRepository();
        statement = new Conexiune().getStatement();
        sc = new Scanner(System.in);
    }


    public void goClient() {
        System.out.println("sunt in goclient");
        boolean quit = false;
        while (!quit) {
            meniu1();
            int opt = Integer.parseInt(sc.nextLine());
            switch (opt) {
                case 1:
                    login();
                    break;
                case 2:
                    creareClient();
                    break;
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
            viewOp=new ViewOperator(client);

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
        }else{
            System.out.println("voi lansa goclient");
            goClient();
        }
    }

    public  void creareClient(){
        System.out.print("Dati numele Dvs: ");
        String nume=sc.nextLine();
        System.out.print("Dati Localitatea de Domiciliu: ");
        String loca=sc.nextLine();
        System.out.print("Dati Adresa: ");
        String adr=sc.nextLine();
        System.out.print("Dati email-ul: ");
        String email=sc.nextLine();
        System.out.print("Dati Telefonul: ");
        String tel=sc.nextLine();
        System.out.print("Alegeti o parola: ");
        String pass=sc.nextLine();

        if(!clientRepo.isClient(nume,email)){
            Client cl=new Client();
            cl.setNume(nume);
            cl.setLocalitate(loca);
            cl.setAdresa(adr);
            cl.setEmail(email);
            cl.setCategorie(0);
            cl.setTelefon(tel);
            cl.setParola(pass);
            clientRepo.addClient(cl);
            int idc=clientRepo.retId(cl);
            if(idc>0){

                System.out.println("Notati datele dvs :");
                clientRepo.afisareClient( clientRepo.toClient(idc));

            }else{
                System.out.println("Client existent sau nu am reusit adaugarea cleintului");
            }

        }else{
            System.out.println("Client existent in baza de date!!");
        }
    }

    public void addColet() {
        comanda=comandaRepo.getComandaActiva(client.getId());
        if (comanda.getId() == 0) {
            comandaRepo.add(new Comanda(client.getId()));
            comanda = comandaRepo.getComandaActiva(client.getId());
        }
        System.out.println("Comanda id="+comanda.getId());

        System.out.print("Indicati Destinatarul: ");
        String destinatar = sc.nextLine();

        System.out.print("Indicati Localitatea: ");
        String localitate = sc.nextLine();

        System.out.print("Indicati adresa: ");
        String adresa = sc.nextLine();

        System.out.print("Greutatea:  ");
        int greutate = Integer.parseInt(sc.nextLine());

        System.out.print("Distanta: ");
        int distanta = Integer.parseInt(sc.nextLine());

        System.out.print("Plata Ramburs 0/1: ");
        int ramburs = Integer.parseInt(sc.nextLine());
        boolean bramb=false;
        if(ramburs>0){
            bramb=true;
        }

        Colet c = new Colet(comanda.getId(), adresa);
        c.setLocalitate_destinatie(localitate);
        c.setDestinatar(destinatar);
        c.setDistanta(distanta);
        c.setGreutate(greutate);
        c.setPlata_ramburs(bramb);

        System.out.println(coletRepo.getpret(c,LocalDateTime.now()));
        c.setPret(coletRepo.getpret(c,LocalDateTime.now()));
        String awb = c.getAwb();
        coletRepo.add(c);
        int totcolete=comanda.getNr_colete();
        int prevcost=comanda.getPret_total();
        comanda.setNr_colete(totcolete+1);
        comanda.setPret_total(prevcost+c.getPret());
        c = coletRepo.getColet(awb);
        Track t = new Track(c.getId_colet(), c.getAwb());
        t.setStatus("initiat");
        trackRepo.add(t);
        comandaRepo.updComanda(comanda);
    }

    public void showComenzi() {
        List<Comanda> listaComenzi = new ArrayList<>();
        List<Colet> listaColete = new ArrayList<>();
        List<Track> listaTrack = new ArrayList<>();

        listaComenzi = comandaRepo.getComenziClient(client.getId());
        for (Comanda c : listaComenzi) {
            comandaRepo.printComanda(c);
            listaColete=coletRepo.getColete(c.getId());
            for(Colet cl:listaColete){
                System.out.println(cl.toString());
            }
        }
    }

    public void delComanda() {
        System.out.print("Dati ID-ul comenzii de sters :");
        int idC = Integer.parseInt(sc.nextLine());
        if (comandaRepo.getComandaActiva(client.getId()).getId() == idC) {
            System.out.println("da este comanda activa cu nr="+idC);
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
            coletRepo.del(c);
            comanda.setNr_colete(comanda.getNr_colete() -1);
            comanda.setPret_total(comanda.getPret_total()-c.getPret());
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
