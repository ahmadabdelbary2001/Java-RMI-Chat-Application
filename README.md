# Java RMI Chat Application

## Overview
This project is a chat application implemented using Java RMI (Remote Method Invocation). It consists of a server and client that communicate over a network. The client provides a graphical interface built with Swing, and the server manages chat rooms, user authentication, and message routing.

## Features

### Chat Client
- User authentication (sign in and sign up)
- Send and receive private messages
- Join and leave chat rooms
- Send messages to chat rooms
- Real-time updates for messages and user status
- Graphical user interface using Swing

### Chat Server
- User authentication and management
- Chat room creation, joining, and leaving
- Message routing between users and rooms
- Supports multiple concurrent users

## Project Structure

### Server-Side
src/
├── org/ds/
│   ├── IChatClient.java       # Client stub interface
│   ├── IChatServer.java       # Server service interface
│   ├── ChatServerImpl.java    # RMI service implementation
│   ├── User.java              # User entity class  
│   ├── Room.java              # Chat room management
│   └── Main.java              # Server bootstrap

### Client-Side
src/
├── org/ds/
│   ├── IChatClient.java       # Client callback interface
│   ├── IChatServer.java       # Server interface  
│   ├── ChatClientImpl.java    # RMI client implementation
│   ├── ClientGUI.java         # Swing interface
│   └── Main.java              # Client entry point

## Getting Started

### Prerequisites
- Java JDK 23
- Maven
- Java RMI (included in JDK)
- Swing
