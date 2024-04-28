package src.test.java;

import Serverside.libraryServer;
import common.User;
import network.libraryClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class libraryClientTest {
    private libraryServer server;
    private libraryClient client;
    private libraryClient client2;
    public libraryClientTest() throws Exception {
         this.server = new libraryServer();
         Thread severThread = new Thread(()-> {
             server.setupNetworking();
         });
         severThread.start();
        this.client = new libraryClient();
        this.client2 = new libraryClient();
    }
    @Test
    public void aaaaaconnectToServer() throws IOException, ClassNotFoundException {
        assertDoesNotThrow(() -> client.connectToServer("192.168.1.200", 1028));
        assertDoesNotThrow(() -> client2.connectToServer("192.168.1.200", 1028));
        client.createNewUser("austin", "ooo");
        client.resetPassword("austin","123");

        assertDoesNotThrow(() -> client.sendLoginCredentials("austin", "123"));
        assertDoesNotThrow(() -> client2.sendLoginCredentials("tan","456"));
        User user = client.getUser();
        assertTrue(user.getUsername().equals("austin"));

    }
//    @Test
//    public void aaaacreateNewUser() throws IOException, ClassNotFoundException {
//        client.createNewUser("austin", "ooo");
//        client.sendLoginCredentials("austin", "ooo");
//    }
//    @Test
//    public void aaasendLoginCredentials() {
//        assertDoesNotThrow(() -> client.sendLoginCredentials("ben","123"));
//        assertDoesNotThrow(() -> client2.sendLoginCredentials("tan","456"));
//    }

//    @Test
//    public void aagetUser() {
//        User user = client.getUser();
//        assertTrue(user.getUsername().equals("ben"));
//    }


    @Test
    public void resetPassword() {
    }

    @Test
    public void main() {
    }

    @Test
    public void setupNetworking() {
    }

    @Test
    public void updateCatalog() {
    }

    @Test
    public void getCatalog() {
    }

    @Test
    public void getClientCatalog() {
    }

    @Test
    public void recieveAnItem() {
    }

    @Test
    public void sendAObject() {
    }
}