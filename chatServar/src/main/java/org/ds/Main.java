package org.ds;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        System.setProperty("java.security.policy", "./ServerPolicy.policy");
        Registry registry = LocateRegistry.createRegistry(5099);
        IChatServer server = new ChatServerImpl();
        registry.bind("ChatSvc", server);
        System.out.println("Chat Server is running...");
    }
}