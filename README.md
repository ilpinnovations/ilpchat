# ilpchat
ILP INNOVATIONS CHAT APPLICATION:

This is a cloud based chatting application. It has three levels SUPERADMIN, ADMIN and TRAINEE. The application’s has database in both Parse Cloud Relational Database and Android SQLite Database. It works with the synchronization of both databases. The logic to make two databases is to reduce the traffic of data on the internet by not downloading the data that has been already downloaded and saved in the android device. The images are saved in the byte array format in the SQLite database whereas videos are saved in a folder automatically created by the application. 
This application is created for interaction between associates and faculty. 
Initially after the registration the role of every user will be trainee except the SUPERADMIN whose details will be hardcoded in the cloud’s database.

The application has following functions:-
•	Superadmin can make any associate admin by entering his/her Employee id in MAKE-ADMIN page.
•	When that associate opens his application again he will automatically redirected to Admin page.
•	ADMIN can add new groups from the CREATE-GROUP  page as well as delete those groups.
•	ADMIN can see the list of people in the group and can also see the details of each person by clicking on his/her name.
•	TRAINEE when open application will be asked to choose their groups after choosing the group a CHAT page will open
•	In the chat page all SUPERADMIN,ADMIN,TRAINEE can send text messages, Videos and Images.
•	Push notification will be sent to every member of the group in which the message was sent excluding the one who has sent the message.
•	All types of messages(text, images and videos) can be deleted by long clicking on the message and confirming YES in the dialog.
•	Password is only required if you enter the SUPERADMIN credentials in the REGISTER page.
•	If you uninstalled the application from the phone and want to login again. Your entered credentials will be match with the saved credentials and if they are same then you will be logged in.


API USED IN APPLICATION:-

Parse API

Picasso API

Android APPCOMPAT SUPPORT V7 LIBRARY

Android-Multiple-file-Selector-Dialog


