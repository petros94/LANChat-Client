# LANChat-Client

### A Lightweight Chat Application with GUI. 

LAN Chat is a simple one-to-many messaging platform, to be used in local network environments. The platform consists of a client app and a server app. The clients communicate with the server, and vice versa according to the diagram below:

![alt text](https://github.com/petros94/LANChat-Client/blob/master/docs/Node%20Diagram.png-)

The App provides a simple GUI to display and send messages. In case of a connection drop, the user is notified. 

### Simple HTTP-based communication

The communication is achieved using HTTP GET and PUT requests. This ensures a reliable and portable communication, and allows compatibility between different platforms.

### Expandable Interface

By replacing the chat GUI with another front end, the application can be used when a one-to-all communication is needed. Such a case might be data acquisition from multiple devices (for example IoT sensors connected to a central node). The portable nature of the app, allow it to be ported to any device running java.
