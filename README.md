NetworkedVehicles
=================

UCL Computer Science Networked Vehicles Team

=================

Airboard & Nearby & NetworkedCabs

For the applications to work they need to be placed on a local server and a browser pointed to that location. We recommend Apache2 or Microsoft Server. 
PHP scripts are used and therefore they need to run on a local server in order to be tested. Also, in COMP2013 we have presented how our cars should run a local Apache2 server that uses the car hardware ( simulated with the Gadgeteer). We have stayed with this designed and the applications are based on that principle. For example, the music app will not play any music if the page is not loaded on the localhost. 

Cloud(Azure)

Our GitHub repository contains a copy of the files hosted on the Azure web servers ,at 
http://networkedvehicles.azurewebsites.net/ , so those files can be tested simply by making POST requests to the cloud. Valid requests are documented in the implementation section for the cloud.

For the database, we have provided a SQL file in the MySQL Database folder that will replicate our database if it is imported on a MySQL server, this database is also hosted on the Azure servers.

Android Applications - FindMyCar & MusicSync

These applications were developed using the recommended Android development environment:
Eclipse and the Android SDK. To run and test them simply clone the repository and import the android folders into the Eclipse Workspace.FindMyCar & MusicSync can then be tested using the Android Virtual Machine or on any Android Device.
