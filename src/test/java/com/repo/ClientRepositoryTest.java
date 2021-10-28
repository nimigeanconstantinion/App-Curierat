package com.repo;
import com.model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientRepositoryTest {

@Test
public void testisClient(){
    ClientRepository clientrepo=new ClientRepository();
    clientrepo.isClient(1);
}

    @Test
    void addClient() {
        ClientRepository clientRepository=new ClientRepository();
        clientRepository.addClient(new Client("Nimigean Ion","Suceava"));
    }

    @Test
    void delClient() {
       ClientRepository clientRepository=new ClientRepository();
       clientRepository.delClient(new Client("Nimigean ","Suceava"));
    }

    @Test
    void updClientNume() {
    }

    @Test
    void updClientAdrTelEmail() {
    }

    @Test
    void updPass() {
    }

    @Test
    void isClient() {
        ClientRepository clientRepository=new ClientRepository();
        assertEquals(true,clientRepository.isClient("Nimigean Ion","constantin@yahoo.com"));
    }

    @Test
    void testIsClient() {
    }

    @Test
    void isPassValid() {
    }
}