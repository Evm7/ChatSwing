# ChatSwing Project
### 3rd and last project for SAD Assignemnt of UPC Engineering of Telecomunicacions ETSETB.

#### ChatSwing is a chat between multiple clients based on JAVA Swing and one multi-threading Server.
  * Server is based on Selector NIO and Reactor pattern.
  * Client is multithreading: main thread works as the collector information from the user, whereas the ClientThread communicates with the server and also process the information.
As we can see, ChatClient uses Swing to ease the interaction of user.

The importance of the project was based on the Pattern reactor and the initialitzation of Swing.
Furthermore, there was a cumpolsary rule of creating two classes based on Socket and ServerSocket which only make try-catch statements in order not to throw exceptions.

