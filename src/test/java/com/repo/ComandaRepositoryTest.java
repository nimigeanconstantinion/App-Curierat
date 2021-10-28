package com.repo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComandaRepositoryTest {

    @Test
  public void testgetComandaActiva(){
        ComandaRepository cp=new ComandaRepository();
        System.out.println(cp.getComandaActiva(1).getStare_comanda());
    }
}