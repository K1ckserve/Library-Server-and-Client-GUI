package src.test.java;

import Serverside.libraryServer;
import common.Catalog;
import common.User;
import network.Client;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class clientTest {
    private libraryServer server;
    private Client client;
    private Client client2;

    public clientTest() throws Exception {
        this.server = new libraryServer();
        Thread severThread = new Thread(() -> {
            server.setupNetworking();
        });
        severThread.start();
        this.client = new Client();
        this.client2 = new Client();
    }

    @Test
    public void connectToServer() throws IOException, ClassNotFoundException {
        assertDoesNotThrow(() -> client.connectToServer("192.168.1.200", 1028));
        assertDoesNotThrow(() -> client2.connectToServer("192.168.1.200", 1028));
        client.createNewUser("austin", "ooo");
        client.resetPassword("austin", "123");

        assertDoesNotThrow(() -> client.sendLoginCredentials("austin", "123"));
        assertDoesNotThrow(() -> client2.sendLoginCredentials("tan", "456"));
        User user = client.getUser();
        assertTrue(user.getUsername().equals("austin"));
        client.setupNetworking();
        client2.setupNetworking();
        Catalog c = client.getClientCatalog();
        assertTrue(c.books.isEmpty());

    }
}