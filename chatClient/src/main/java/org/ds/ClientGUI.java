package org.ds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

public class ClientGUI extends JFrame {
    private ChatClientImpl chatClient;
    private final IChatServer server;

    private JTextField usernameField, passwordField, firstNameField, lastNameField, messageField;
    private JTextArea contactsChatArea, roomsChatArea;
    private DefaultListModel<String> roomsModel, contactsModel;
    private JList<String> roomsList, contactsList;
    private JButton signInButton, signUpButton, myContactsButton, addContactButton, myRoomsButton, createRoomButton, joinRoomButton,
            leaveRoomButton, deleteRoomButton, browseRoomsButton, browseMembersButton, signOutButton, sendButton;

    public ClientGUI(ChatClientImpl chatClient, IChatServer server) {
        this.server = server;
        this.chatClient = chatClient;

        initializeRegisterGUI();
    }

    public void setChatClient(ChatClientImpl client) {
        chatClient = client;
    }

    private void initializeRegisterGUI() {
        setTitle("Sign In To Remote Chat");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        signInButton = new JButton("Sign In");
        signUpButton = new JButton("Sign Up");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(signInButton, gbc);
        gbc.gridx = 1;
        loginPanel.add(signUpButton, gbc);

        add(loginPanel, BorderLayout.CENTER);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPanel.removeAll();
                gbc.gridx = 0;
                gbc.gridy = 0;
                loginPanel.add(new JLabel("Username:"), gbc);
                gbc.gridx = 1;
                loginPanel.add(usernameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                loginPanel.add(new JLabel("Password:"), gbc);
                gbc.gridx = 1;
                loginPanel.add(passwordField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                loginPanel.add(signInButton, gbc);

                loginPanel.revalidate();

                signInButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        signIn();
                    }
                });
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPanel.removeAll();
                gbc.gridx = 0;
                gbc.gridy = 0;
                loginPanel.add(new JLabel("Username:"), gbc);
                gbc.gridx = 1;
                loginPanel.add(usernameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                loginPanel.add(new JLabel("Password:"), gbc);
                gbc.gridx = 1;
                loginPanel.add(passwordField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                loginPanel.add(new JLabel("First Name:"), gbc);
                gbc.gridx = 1;
                loginPanel.add(firstNameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 3;
                loginPanel.add(new JLabel("Last Name:"), gbc);
                gbc.gridx = 1;
                loginPanel.add(lastNameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 2;
                loginPanel.add(signUpButton, gbc);

                loginPanel.revalidate();

                signUpButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        signUp();
                        signIn();
                    }
                });
            }
        });
    }

    private void initializeChatGUI() throws RemoteException {
        getContentPane().removeAll();
        setTitle("Remote Chat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebarPanel = new JPanel(new GridLayout(12, 1, 5, 5));
        myContactsButton = new JButton("My Contacts");
        addContactButton = new JButton("Add Contact");
        myRoomsButton = new JButton("My Rooms");
        createRoomButton = new JButton("Create Room");
        joinRoomButton = new JButton("Join Room");
        leaveRoomButton = new JButton("Leave Room");
        deleteRoomButton = new JButton("Delete Room");
        browseRoomsButton = new JButton("Browse Available Rooms");
        browseMembersButton = new JButton("Browse The Members");
        signOutButton = new JButton("Sign Out");

        sidebarPanel.add(myContactsButton);
        sidebarPanel.add(addContactButton);
        sidebarPanel.add(myRoomsButton);
        sidebarPanel.add(createRoomButton);
        sidebarPanel.add(joinRoomButton);
        sidebarPanel.add(leaveRoomButton);
        sidebarPanel.add(deleteRoomButton);
        sidebarPanel.add(browseRoomsButton);
        sidebarPanel.add(browseMembersButton);
        sidebarPanel.add(signOutButton);

        contactsModel = new DefaultListModel<>();
        contactsList = new JList<>(contactsModel);
        contactsChatArea = new JTextArea();
        contactsChatArea.setEditable(false);

        roomsModel = new DefaultListModel<>();
        roomsList = new JList<>(roomsModel);
        roomsChatArea = new JTextArea();
        roomsChatArea.setEditable(false);

        JPanel listsAndChatsPanel = new JPanel(new GridLayout(2, 2));
        listsAndChatsPanel.add(new JScrollPane(contactsList));
        listsAndChatsPanel.add(new JScrollPane(contactsChatArea));
        listsAndChatsPanel.add(new JScrollPane(roomsList));
        listsAndChatsPanel.add(new JScrollPane(roomsChatArea));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        messageField = new JTextField(50);
        sendButton = new JButton("Send");

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(sendButton, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(messageField, gbc);

        add(sidebarPanel, BorderLayout.WEST);
        add(listsAndChatsPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        List<String> userContactsList = server.getUserContacts(usernameField.getText());
        List<String> userRoomsList = server.getUserRooms(usernameField.getText());

        userContactsList.forEach(contactsModel::addElement);
        userRoomsList.forEach(roomsModel::addElement);

        myContactsButton.addActionListener(e -> showUserContacts());
        addContactButton.addActionListener(e -> addContact());
        myRoomsButton.addActionListener(e -> showUserRooms());
        createRoomButton.addActionListener(e -> createRoom());
        joinRoomButton.addActionListener(e -> joinRoom());
        leaveRoomButton.addActionListener(e -> leaveRoom());
        deleteRoomButton.addActionListener(e -> deleteRoom());
        browseRoomsButton.addActionListener(e -> browseAvailableRooms());
        browseMembersButton.addActionListener(e -> browseMembers());
        signOutButton.addActionListener(e -> signOut());
        sendButton.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    public void appendContactsMessage(String message) {
        contactsChatArea.append(message + "\n");
    }

    public void appendRoomsMessage(String message) {
        roomsChatArea.append(message + "\n");
    }

    private void signIn() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            if (server.signIn(username, password, chatClient)) {
                chatClient.setUsername(username);
                initializeChatGUI();
                setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Check credentials.");
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error logging in: " + e.getMessage());
        }
    }

    private void signUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        try {
            server.signUp(username, firstName, lastName, password);
            JOptionPane.showMessageDialog(this, "Account created successfully.");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error creating account: " + e.getMessage());
        }
    }

    private void addContact() {
        String contact = contactsList.getSelectedValue();

        if (contact == null || contact.isEmpty()) {
            contact = JOptionPane.showInputDialog(this, "Enter contact name:");
        }

        if (contact != null && !contact.isEmpty()) {
            try {
                server.addContact(chatClient.getUsername(), contact);
                contactsModel.addElement(contact);
                showUserContacts();
                JOptionPane.showMessageDialog(this, "Contact added: " + contact);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Error adding contact: " + e.getMessage());
            }
        } else if (contact != null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid contact name.");
        }
    }

    private void createRoom() {
        String roomName = JOptionPane.showInputDialog(this, "Enter room name:");
        if (roomName != null && !roomName.isEmpty()) {
            try {
                server.createRoom(roomName, chatClient.getUsername());
                showUserRooms();
                roomsModel.addElement(roomName);
                JOptionPane.showMessageDialog(this, "Room created: " + roomName);
                server.joinRoom(chatClient.getUsername(), roomName, chatClient);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Error creating room: " + e.getMessage());
            }
        } else if (roomName != null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid room name.");
        }
    }

    private void joinRoom() {
        String roomName = roomsList.getSelectedValue();
        if (roomName != null) {
            try {
                server.joinRoom(chatClient.getUsername(), roomName, chatClient);
                roomsModel.addElement(roomName);
                showUserRooms();
                JOptionPane.showMessageDialog(this, "Joined room: " + roomName);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Error joining room: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room from the list to join.");
        }
    }

    private void leaveRoom() {
        String roomName = roomsList.getSelectedValue();
        if (roomName != null) {
            try {
                server.leaveRoom(chatClient.getUsername(), roomName);
                showUserRooms();
                roomsModel.removeElement(roomName);
                JOptionPane.showMessageDialog(this, "Left room: " + roomName);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Error leaving room: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room from the list to leave.");
        }
    }

    private void deleteRoom() {
        String roomName = roomsList.getSelectedValue();
        if (roomName != null) {
            try {
                server.deleteRoom(roomName, chatClient.getUsername());
                showUserRooms();
                roomsModel.removeElement(roomName);
                JOptionPane.showMessageDialog(this, "Room deleted: " + roomName);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Error deleting room: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room from the list to delete.");
        }
    }

    private void showUserContacts() {
        List<String> userContactsList = null;
        try {
            userContactsList = server.getUserContacts(usernameField.getText());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        contactsModel.clear();
        userContactsList.forEach(contactsModel::addElement);
    }

    private void showUserRooms() {
        List<String> userRoomsList = null;
        try {
            userRoomsList = server.getUserRooms(usernameField.getText());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        roomsModel.clear();
        userRoomsList.forEach(roomsModel::addElement);
    }

    private void browseAvailableRooms() {
        List<String> availableRooms = null;
        try {
            availableRooms = server.getAvailableRooms();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        roomsModel.clear();
        availableRooms.forEach(roomsModel::addElement);
    }

    private void browseMembers() {
        String roomName = roomsList.getSelectedValue();
        List<String> roomMembers = null;
        try {
            roomMembers = server.getRoomMembers(roomName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        contactsModel.clear();
        roomMembers.forEach(contactsModel::addElement);
    }

    private void sendMessage() {
        String message = messageField.getText();
        String contact = contactsList.getSelectedValue();
        String roomName = roomsList.getSelectedValue();

        if (!message.isEmpty()) {
            try {
                if (contact != null) {
                    server.sendPrivateMessage(chatClient.getUsername(), contact, message);
                    contactsChatArea.append("Private to " + contact + ": " + message + "\n");
                } else if (roomName != null) {
                    server.sendMessage(chatClient.getUsername(), roomName, message);
                    roomsChatArea.append("To room " + roomName + ": " + message + "\n");
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a contact or room to send the message.");
                }
                messageField.setText("");
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Error sending message: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Enter a message to send.");
        }
    }

    private void signOut() {
        try {
            server.signOut(chatClient.getUsername());
            getContentPane().removeAll();
            initializeRegisterGUI();
            revalidate();
            repaint();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error logging out: " + e.getMessage());
        }
    }
}
