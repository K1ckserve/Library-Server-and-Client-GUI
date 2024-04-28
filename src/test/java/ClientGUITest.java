package src.test.java;

import GUI.ClientGUI;
import javafx.stage.Stage;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientGUITest {

    @Test
    public void start() throws Exception {
        ClientGUI c = new ClientGUI();
        c.start(new Stage());
    }
}