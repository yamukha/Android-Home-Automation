Android-Home-Automation
=======================


Android and PC based client-server system to home automation



Short Guide.


There are two parts.

1) Android based client;

2) PC based server (can be base on embedded device connected to LAN).

They communicate over UPD.


Clien application works like remote control device.

At start user should "ping" server by IP and get interface (list of commands)

If there are no response in defined format Android shows "Timeout!".

If requested server has implementation for remote controlling it sends in response list of commands.

After this user can invoke commands remotely from Android application.


Virtual buttons "+" and "-" used to select command from received list.


By default first command is "ping".

Predefined server IP is 192.168.1.4

By soft keyboard user can change it to other one.


Next future work:

1) implement .bat or .sh file with real set of commands;

2) link it by XML file to java server application.