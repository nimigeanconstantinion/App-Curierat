package com.repo;

import com.model.Colet;
import com.model.StatusColet;
import com.model.Track;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColetRepositoryTest {

    @Test
    void getColet() {
        ColetRepository cr=new ColetRepository();
        
    }

    @Test
    void getColete() {
    }

    @Test
    void getDataColet() {
    }

    @Test
    void getStatus() {
        ColetRepository cr=new ColetRepository();
        List<Track> ls=new ArrayList<>();
        Colet c=new Colet("H");
        ls=cr.getStatus(c);
        System.out.println(ls);
    }

    @Test
    void getLastStatus() {
        ColetRepository cr=new ColetRepository();
        List<Colet> cl=new ArrayList<>();
        String awbb="HSCJ98787793";
        System.out.println(cr.getLastStatus(cr.getColet(awbb)));
    }

    @Test
    void testGetColet() {
        ColetRepository cr=new ColetRepository();
        List<Colet> clist=new ArrayList<>();
        clist=cr.getColeteLocal("Bucuresti");
        System.out.println(clist.size());
    }

    @Test
    void getpret() {
        ColetRepository cr=new ColetRepository();
        Colet c=cr.getColet("HXZS76537182");
        System.out.println(cr.getpret(c, LocalDateTime.now()));
    }
}