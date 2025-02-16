package org.ds;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        try {
            IChatServer server = (IChatServer) Naming.lookup("rmi://localhost:5099/ChatSvc");
            SwingUtilities.invokeLater(() -> {
                ClientGUI gui = new ClientGUI(null, server);
                try {
                    ChatClientImpl client = new ChatClientImpl(gui);
                    gui.setChatClient(client);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                gui.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the server.");
        }
    }
}
