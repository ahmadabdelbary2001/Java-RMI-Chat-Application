package org.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatClient extends Remote {
    void receiveMessage(String roomName, String message) throws RemoteException;
    void receivePrivateMessage(String sender, String message) throws RemoteException;
}
