package org.ds;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements Serializable {
    private final String roomName;
    private final String creatorUsername;
    private final List<String> members;
    private final Map<String, IChatClient> clientMap;
    private final Map<String, List<String>> pendingMessages;

    public Room(String roomName, String creatorUsername) {
        this.roomName = roomName;
        this.creatorUsername = creatorUsername;
        this.members = new ArrayList<>();
        this.clientMap = new HashMap<>();
        this.pendingMessages = new HashMap<>();
    }

    public void joinRoom(String username, IChatClient client) throws RemoteException {
        if (!members.contains(username)) {
            members.add(username);
            clientMap.put(username, client);
            broadcastMessage(this.roomName, username + " has joined the room.");
        }
    }

    public void leaveRoom(String username) throws RemoteException {
        members.remove(username);
        clientMap.remove(username);
        broadcastMessage(this.roomName, username + " has left the room.");
    }

    public void broadcastMessage(String roomName, String message) throws RemoteException {
        for (String member : members) {
            IChatClient client = clientMap.get(member);

            if (client != null) {
                try {
                    client.receiveMessage(roomName, message);
                } catch (RemoteException e) {
                    System.err.println("Failed to send message to " + member + ": storing message for later delivery.");
                    storePendingMessage(member, message);
                }
            } else {
                System.out.println("User offline: storing message for " + member);
                storePendingMessage(member, message);
            }
        }
    }

    private void storePendingMessage(String username, String message) {
        pendingMessages.computeIfAbsent(username, k -> new ArrayList<>()).add(message);
    }

    public void deliverPendingMessages(String username, IChatClient client) throws RemoteException {
        List<String> messages = pendingMessages.get(username);

        if (messages != null && !messages.isEmpty()) {
            try {
                for (String message : messages) {
                    client.receiveMessage(roomName, message);
                }
                System.out.println("Delivered " + messages.size() + " pending messages to " + username);
                messages.clear();
            } catch (RemoteException e) {
                System.err.println("Failed to deliver pending messages to " + username + ": " + e.getMessage());
            }
        }
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getRoomName() {
        return roomName;
    }

    public Map<String, IChatClient> getClientMap() {
        return clientMap;
    }

    public List<String> getMembers() {
        return members;
    }
}
