package com.repo;

import com.model.Colet;
import com.model.StatusColet;
import com.model.Track;
import org.junit.jupiter.api.Test;

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
    }
}