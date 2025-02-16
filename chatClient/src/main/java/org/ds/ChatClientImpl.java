package org.ds;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements IChatClient {
    private String username;
    private final ClientGUI clientGUI;

    public ChatClientImpl(ClientGUI clientGUI) throws RemoteException {
        this.clientGUI = clientGUI;
    }

    @Override
    public void receiveMessage(String roomName, String message) throws RemoteException {
        SwingUtilities.invokeLater(() -> clientGUI.appendRoomsMessage("[" + roomName +"] " + message));
    }

    @Override
    public void receivePrivateMessage(String sender, String message) throws RemoteException {
        SwingUtilities.invokeLater(() -> clientGUI.appendContactsMessage("Private message from " + sender + ": " + message));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
