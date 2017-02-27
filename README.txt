DESCRIPTION-OVERVIEW
In Client Socket we 
*connect to a remote machine
*send and receive data
*close a connection

In Server Socket we
*Bind to a port
*listen for incoming data
*accept connections from remote machines on the bound port
Send and receive data
*Close a connection

Why Multithreaded process?
*A thread of control is a sequence of instructions being executed within the context of a process. It has its own program counter and stack.
*A traditional process has a single thread of control
*A Multithreaded process has two or more threads within the same context. They share the same set of open files, child processes, timers, etc.
*Each thread has its own id.


Creation, Compilation and Execution of Server Side
--------------------------------------------------
1.Run Command Prompt->cmd->Choose the path where the ServerSide java file exists
2.Compile the Server by typing-in "javac ServerSide.java"
3.Run the Server by typing-in "java ServerSide"

Creation, Compilation and Execution of Client Side
--------------------------------------------------
1.Command Prompt->cmd->Choose the path where the ClientSide java file exists
2.Compile the Client by typing-in "javac ClientSide.java"
3.Run the Server by typing-in "java ClientSide localhost 8080 hi.txt"

Steps to run in web-browser
---------------------------
1->Run "ServerSide.java"
2->Open web browser and type the path->localhost:8080/hi.txt
   where 8080 is the port number specified and hi.txt is the file created to be displayed with its contents.