package org.ds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final List<String> contacts;
    private final List<String> joinedRooms;

    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.contacts = new ArrayList<>();
        this.joinedRooms = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public List<String> getJoinedRooms() {
        return joinedRooms;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public void addContact(String contact) {
        contacts.add(contact);
    }

    public void addRoom(String roomName) {
        joinedRooms.add(roomName);
    }

    public void removeRoom(String roomName) {
        joinedRooms.remove(roomName);
    }
}