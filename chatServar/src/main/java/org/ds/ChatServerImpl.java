package org.ds;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatServerImpl extends UnicastRemoteObject implements IChatServer {
    private final Map<String, User> signedUpUsers;
    private final Map<String, IChatClient> signedInClients;
    private final Map<String, Room> rooms;
    private final Map<String, List<String>> pendingPrivateMessages;

    public ChatServerImpl() throws RemoteException {
        this.signedUpUsers = new HashMap<>();
        this.signedInClients = new HashMap<>();
        this.rooms = new HashMap<>();
        this.pendingPrivateMessages = new HashMap<>();
    }

    @Override
    public void signUp(String username, String firstName, String lastName, String password) throws RemoteException {
        if (!signedUpUsers.containsKey(username)) {
            signedUpUsers.put(username, new User(username, firstName, lastName, password));
            System.out.println("User registered: " + username);
        } else {
            System.out.println("User already exists: " + username);
        }
    }

    @Override
    public boolean signIn(String username, String password, IChatClient client) throws RemoteException {
        User user = signedUpUsers.get(username);
        if (user != null && user.verifyPassword(password)) {
            signedInClients.put(username, client);
            System.out.println(username + " logged in successfully.");
            deliverPendingPrivateMessages(username, client);

            List<String> userRooms = user.getJoinedRooms();
            for (String roomName : userRooms) {
                Room room = rooms.get(roomName);
                if (room != null) {
                    room.deliverPendingMessages(username, client);
                }
            }

            return true;
        }
        System.out.println("Login failed for " + username);
        return false;
    }

    @Override
    public void signOut(String username) throws RemoteException {
        signedInClients.remove(username);
        System.out.println(username + " logged out.");
    }

    @Override
    public void addContact(String username, String contact) throws RemoteException {
        signedUpUsers.get(username).addContact(contact);
    }

    @Override
    public void createRoom(String roomName, String creatorUsername) throws RemoteException {
        if (!rooms.containsKey(roomName)) {
            rooms.put(roomName, new Room(roomName, creatorUsername));
            System.out.println("Chat room created: " + roomName);
        } else {
            System.out.println("Chat room already exists: " + roomName);
        }
    }

    @Override
    public void deleteRoom(String roomName, String creatorUsername) throws RemoteException {
        Room room = rooms.get(roomName);
        if (room != null && room.getCreatorUsername().equals(creatorUsername)) {
            String deletionMessage = "The room '" + roomName + "' has been deleted by the owner and is no longer available.";
            room.broadcastMessage(roomName, deletionMessage);
            rooms.remove(roomName);
            System.out.println("Chat room deleted: " + roomName);
        } else {
            System.out.println("Room deletion failed: " + roomName + " does not exist or unauthorized attempt.");
        }
    }

    @Override
    public void joinRoom(String username, String roomName, IChatClient client) throws RemoteException {
        Room room = rooms.get(roomName);
        if (room != null) {
            room.joinRoom(username, client);
            signedUpUsers.get(username).addRoom(roomName);
            System.out.println(username + " joined room: " + roomName);
        } else {
            System.out.println("Room join failed: " + roomName + " does not exist.");

        }
    }

    @Override
    public void leaveRoom(String username, String roomName) throws RemoteException{
        Room room = rooms.get(roomName);
        room.leaveRoom(username);
        signedUpUsers.get(username).removeRoom(roomName);
    }

    @Override
    public void sendMessage(String sender, String roomName, String message) throws RemoteException{
        Room room = rooms.get(roomName);
        if (room != null) {
            room.broadcastMessage(roomName, "(" + sender + "):" + message);
        }
    }

    @Override
    public void sendPrivateMessage(String sender, String recipient, String message) throws RemoteException {
        IChatClient recipientClient = signedInClients.get(recipient);
        String formattedMessage = "Private message from " + sender + ": " + message;

        if (recipientClient != null) {
            try {
                recipientClient.receivePrivateMessage(sender, message);
                System.out.println("Private message sent from " + sender + " to " + recipient);
            } catch (RemoteException e) {
                System.err.println("Failed to send private message to " + recipient + ": storing message for later delivery.");
                storePendingPrivateMessage(recipient, formattedMessage);
            }
        } else {
            System.out.println("User offline: storing private message for " + recipient);
            storePendingPrivateMessage(recipient, formattedMessage);
        }
    }

    @Override
    public List<String> getAvailableRooms() throws RemoteException {
        return new ArrayList<>(rooms.keySet());
    }

    @Override
    public List<String> getRoomMembers(String roomName) throws RemoteException {
        Room room = rooms.get(roomName);
        if (room != null) {
            return room.getMembers();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getUserContacts(String username) throws RemoteException{
        return signedUpUsers.get(username).getContacts();
    }

    @Override
    public List<String> getUserRooms(String username) throws RemoteException {
        return signedUpUsers.get(username).getJoinedRooms();
    }

    private void storePendingPrivateMessage(String recipient, String message) {
        pendingPrivateMessages.computeIfAbsent(recipient, k -> new ArrayList<>()).add(message);
    }

    private void deliverPendingPrivateMessages(String username, IChatClient client) {
        List<String> messages = pendingPrivateMessages.get(username);

        if (messages != null && !messages.isEmpty()) {
            try {
                for (String message : messages) {
                    client.receivePrivateMessage("Server", message);
                }
                System.out.println("Delivered " + messages.size() + " pending private messages to " + username);
                messages.clear();
            } catch (RemoteException e) {
                System.err.println("Failed to deliver pending private messages to " + username + ": " + e.getMessage());
            }
        }
    }
}