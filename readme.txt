----------------- Projet Web Services -----------------

-------- Groupe : Laurent NGETH & Steeven NOVO --------

------------------- How to install --------------------


Partie client : 
	- Django
	- zeep (pour faire des requêtes au service SOAP)

Service SOAP :
	- axis2-1.6.2

Service REST :
	- Org.restlet.jar
	- Restlet

Base de donnée:
	- Wampp

Importer la base de donnée dans phpmyadmin, changer les identifiants de connexion à la base de donnée dans le code source des services webs.
Les services sont hébergés sur Tomcat Server 9.0



--------------------- How to run ----------------------

Démarrer wampp.
Start le serveur tomcat.
Aller dans le directory "mysite", lancer la commande : python manage.py runserver
Aller dans un navigateur à l'adresse: localhost:8000/

--------------------- Tâches --------------------------


X	Create REST Train Filtering service B	6
X	Create SOAP Train Booking service A	4
X	Interaction between two services	4
X	Test with Web service Client (instead of using Eclipse's Web service Explorer)	2
-	Work with complex data type (class, table, etc.)	2
X	Work with database (in text file, xml, in mysql, etc.)	2
X	Postman	2
X	OpenAPI	3
-	BPMS	5