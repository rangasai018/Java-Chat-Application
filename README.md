Java Chat Application

A multi-client chat application built with Java sockets that allows multiple users to connect to a central server and exchange messages in real-time.

Features

Multi-client support - Multiple users can connect simultaneously  
Real-time messaging - Messages are instantly broadcasted to all connected clients  
User identification - Each client has a unique username  
Timestamps - Every message includes a timestamp (HH:MM:SS format)  
Join/Leave notifications - Users are notified when someone joins or leaves  
Graceful disconnect - Use `/exit` command to disconnect cleanly  
Server logging - Server logs all activities to console  

Project Structure

Java Chat Application/
├── Server.java          # Chat server (listens on port 1234)
├── Client.java          # Chat client (connects to server)
└── README.md            # This file

Requirements

- Java JDK 8 or higher (tested with OpenJDK 25)
- Windows PowerShell (or any terminal with Java installed)

Installation

1. Install JDK (if not already installed):
   - Download OpenJDK from [adoptium.net](https://adoptium.net/) or [oracle.com](https://www.oracle.com/java/technologies/downloads/)
   - Install to default location or note the installation path

2. Verify Java Installation:
   powershell
   java -version
   javac -version
   

Setup & Configuration

Add JDK to PATH (Windows)

Run this in PowerShell before compiling/running:
powershell
$env:Path = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot\bin;' + $env:Path

Or add permanently in System Environment Variables:
- Right-click "This PC" → Properties → Advanced system settings
- Click "Environment Variables"
- Add JDK bin path to `PATH` variable

How to Run

Step 1: Navigate to Project Directory
powershell
cd "C:\Users\chira\Desktop\Java Chat Application"

Step 2: Compile Java Files
powershell
javac Server.java Client.java

Step 3: Start the Server (Terminal A)
powershell
java Server

Expected output:
Server started... Waiting for clients

Step 4: Connect Clients (Terminal B, C, etc.)
powershell
java Client

You will be prompted:
Enter your username: Alice
Connected to chat server
Type messages and press Enter. Type /exit to quit.

Step 5: Send Messages
- Type any message and press Enter
- Messages are instantly visible to all connected clients

Usage Guide

Basic Chat
Enter your username: Alice
Connected to chat server
Type messages and press Enter. Type /exit to quit.

[14:30:52] Alice: Hello everyone
[14:31:05] Bob: Hi Alice!
[14:31:12] Alice: How are you?
[14:31:15] Bob: Good! You?

Available Commands:

| Command | Description |
|---------|-------------|
| `/exit` | Disconnect from the server and close the client |
| Any other text | Send as a regular message |

Server Console Output:
Server started... Waiting for clients
Alice connected
[14:30:45] Alice joined the chat
Bob connected
[14:30:58] Bob joined the chat
[14:31:05] [14:31:05] Alice: Hello everyone
[14:31:12] [14:31:12] Bob: Hi Alice!

How It Works:

Architecture

Server.java
- Listens on port 1234 for incoming connections
- Accepts multiple clients simultaneously
- Maintains a list of connected clients
- Broadcasts messages from any client to all others
- Tracks usernames and manages join/leave events

Client.java
- Connects to server on localhost:1234
- Prompts user for username on startup
- Runs two concurrent threads:
  - Send thread: Reads user input from keyboard and sends to server
  - Receive thread: Listens for messages from server and displays them
- Supports graceful disconnection with `/exit` command

Message Flow

Client1 (Alice)          Server               Client2 (Bob)
    │                      │                      │
    ├─ "alice" ────────→   │                      │
    │                    [stores]                 │
    │                      │                      │
    │                      │   ← "bob" ───────────┤
    │                    [stores]                 │
    │                      │                      │
    ├─ "Hello" ────────→   ├─ "[14:31:05] alice: Hello" ──→
    │                      │                      │
    │    ← "[14:31:12] bob: Hi Alice!" ────────── ├─ "Hi Alice!"

Disconnecting

Stop a Client
Press `Ctrl+C` in the client terminal, or type:
/exit

Stop the Server
Press `Ctrl+C` in the server terminal.

Or stop background server process:
powershell
Get-CimInstance Win32_Process |
  Where-Object { $_.CommandLine -and $_.CommandLine -match '\bServer\b' } |
  ForEach-Object { Stop-Process -Id $_.ProcessId -Force }

Troubleshooting

"javac is not recognized"
Solution: Add JDK bin to PATH:
powershell
$env:Path = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot\bin;' + $env:Path

"Connection refused" or "Cannot connect to server"
Solution: Ensure server is running in another terminal before starting client.

Multiple messages on one line
Solution: Press Enter to flush the buffer. This is a minor display issue and doesn't affect functionality.

Port 1234 already in use
Solution: Stop other applications using port 1234, or modify `Server.java` to use a different port (e.g., `new ServerSocket(5000)`).

Code Explanation

Key Classes

Server.java - ClientHandler (Inner Class)
java
- Runs in separate thread for each client
- Reads username from client
- Broadcasts messages to all connected clients
- Handles disconnection and cleanup

Client.java
java
- Prompts for username
- Creates receive thread for incoming messages
- Main thread handles user input
- Sends messages to server

Performance & Limitations

- Max Connections: Limited by system resources (typically 100-1000+)
- Message Size: Limited by buffer size (effectively unlimited for typical chat)
- Network: Works only on localhost (127.0.0.1) or LAN with port forwarding
- Threading: One thread per client

Future Enhancements

Possible improvements:
- [ ] Private messaging between specific users
- [ ] User list command (`/users`)
- [ ] Persistent message logging to file
- [ ] GUI with Swing or JavaFX
- [ ] Support for network connections (not just localhost)
- [ ] User authentication and password protection
- [ ] Message encryption
- [ ] Emoji/rich text support
- [ ] Room/channel system
- [ ] Admin commands and moderation

Example Session

Terminal A (Server):
powershell
PS> java Server
Server started... Waiting for clients
Alice connected
[14:30:45] Alice joined the chat
Bob connected
[14:30:58] Bob joined the chat
[14:31:05] [14:31:05] Alice: Hello everyone
[14:31:12] [14:31:12] Bob: Hi Alice!
Bob disconnected
[14:31:20] Bob left the chat


Terminal B (Client - Alice):
powershell
PS> java Client
Enter your username: Alice
Connected to chat server
Type messages and press Enter. Type /exit to quit.

[14:30:45] Alice joined the chat
[14:30:58] Bob joined the chat
Hello everyone
[14:31:05] Alice: Hello everyone
[14:31:12] Bob: Hi Alice!
Hi Alice!
/exit
Disconnecting...


Terminal C (Client - Bob):
powershell
PS> java Client
Enter your username: Bob
Connected to chat server
Type messages and press Enter. Type /exit to quit.

[14:30:58] Bob joined the chat
[14:31:05] Alice: Hello everyone
Hi Alice!
[14:31:12] Bob: Hi Alice!
/exit
Disconnecting...


Author & License

Created as a Java networking learning project.  
Free to use and modify.

Support

For issues or questions:
1. Check the Troubleshooting section
2. Verify Java installation with `java -version`
3. Ensure server is running before connecting clients
4. Check that port 1234 is not blocked by firewall

---

Version: 2.0 (with usernames, timestamps, and exit command)  
Last Updated: December 19, 2025