package org.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChatServer extends Remote {
    void signUp(String username, String firstName, String lastName, String password) throws RemoteException;
    boolean signIn(String username, String password, IChatClient client) throws RemoteException;
    void signOut(String username) throws RemoteException;
    void addContact(String username, String contact) throws RemoteException;
    void createRoom(String roomName, String creatorUsername) throws RemoteException;
    void deleteRoom(String roomName, String creatorUsername) throws RemoteException;
    void joinRoom(String username, String roomName, IChatClient client) throws RemoteException;
    void leaveRoom(String username, String roomName) throws RemoteException;
    void sendMessage(String sender, String roomName, String message) throws RemoteException;
    void sendPrivateMessage(String sender, String recipient, String message) throws RemoteException;
    List<String> getAvailableRooms() throws RemoteException;
    List<String> getRoomMembers(String roomName) throws RemoteException;
    List<String> getUserRooms(String username) throws RemoteException;
    List<String> getUserContacts(String username) throws RemoteException;
}