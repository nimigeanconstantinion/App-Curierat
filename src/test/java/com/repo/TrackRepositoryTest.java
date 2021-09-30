package com.repo;

import com.model.Track;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrackRepositoryTest {

    @Test
    void add() {
    }

    @Test
    void del() {
    }

    @Test
    void delAll() {
    }

    @Test
    void upd() {
    }

    @Test
    void lastStatus() {
        TrackRepository tr=new TrackRepository();
        Track ttest=new Track();
        System.out.println(tr.lastStatus(1));

    }

    @Test
    void getTrack() {
    }

    @Test
    void isTrace() {
        TrackRepository tr=new TrackRepository();
        Track ttest=new Track();
        ttest.setId_colet(1);
        ttest.setAwb_colet("H");
        ttest.setLocatie_id("Suceava");
        ttest.setStatus("initiat");
        System.out.println(tr.isTrace(ttest));
    }

    @Test
    void isDeletable() {
    }

    @Test
    void getStari() {
        TrackRepository tr=new TrackRepository();
        List<String> lh=tr.getStari();
        System.out.println(lh);

    }

    @Test
    void getHub() {
        TrackRepository tr=new TrackRepository();
        List<String> lh=tr.getHub();
        System.out.println(lh);
    }
}