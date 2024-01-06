use train_project;

DROP TABLE IF EXISTS billet;
DROP TABLE IF EXISTS voyage;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS train;

CREATE TABLE train(
   id INT AUTO_INCREMENT PRIMARY KEY,
   nom VARCHAR(50),
   maxFirstClassePlaces INT NOT NULL,
   maxBusinessClassePlaces INT NOT NULL,
   maxStandardClassePlaces INT NOT NULL
);

CREATE TABLE client(
   id INT AUTO_INCREMENT PRIMARY KEY,
   nom VARCHAR(50)
);

CREATE TABLE station(
   id INT AUTO_INCREMENT PRIMARY KEY,
   nom VARCHAR(50)
);

CREATE TABLE voyage(
   id INT AUTO_INCREMENT PRIMARY KEY,
   dateDepart DATETIME,
   dateArrivee DATETIME,
   idStationDepart INT NOT NULL,
   idStationArrivee INT NOT NULL,
   idTrain INT NOT NULL,
   FOREIGN KEY(idStationDepart) REFERENCES station(id),
   FOREIGN KEY(idStationArrivee) REFERENCES station(id),
   FOREIGN KEY(idTrain) REFERENCES train(id)
);

CREATE TABLE billet(
   id INT AUTO_INCREMENT PRIMARY KEY,
   flexible BOOLEAN,
   classe VARCHAR(50),
   idClient INT NOT NULL,
   idVoyage INT NOT NULL,
   FOREIGN KEY(idClient) REFERENCES client(id),
   FOREIGN KEY(idVoyage) REFERENCES voyage(id)
);

INSERT INTO train(id, nom, maxFirstClassePlaces, maxBusinessClassePlaces, maxStandardClassePlaces) VALUES(1, 'BX45', 5, 5, 5);
INSERT INTO client(id, nom) VALUES(1, 'Steerent');
INSERT INTO station(id, nom) VALUES(1, 'Nice'), (2, 'Paris');
INSERT INTO voyage(id, dateDepart, dateArrivee, idStationDepart, idStationArrivee, idTrain) VALUES
(1, '2023-01-01 12:00:00', '2023-01-01 14:00:00', 2, 1, 1);
INSERT INTO billet(id, flexible, classe, idClient, idVoyage) VALUES
(1, TRUE, 'First', 1, 1);