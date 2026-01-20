-- Mini Projet : Gestion d'une Pharmacie
-- MySQL script (Workbench-ready)
-- Creates database + tables exactly as specified in the assignment PDF.
-- Tables:
-- Pharmacien, Gestionnaire, Client, Medicament, Vente, Commande, StockHistorique

DROP DATABASE IF EXISTS pharmacie_db;
CREATE DATABASE pharmacie_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE pharmacie_db;

-- =========================
-- 1) Users
-- =========================
CREATE TABLE Pharmacien (
  id_pharmacien INT AUTO_INCREMENT PRIMARY KEY,
  nom           VARCHAR(60) NOT NULL,
  prenom        VARCHAR(60) NOT NULL,
  login         VARCHAR(60) NOT NULL UNIQUE,
  pwd           VARCHAR(120) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE Gestionnaire (
  id_gestionnaire INT AUTO_INCREMENT PRIMARY KEY,
  nom             VARCHAR(60) NOT NULL,
  prenom          VARCHAR(60) NOT NULL,
  login           VARCHAR(60) NOT NULL UNIQUE,
  pwd             VARCHAR(120) NOT NULL
) ENGINE=InnoDB;

-- =========================
-- 2) Client + Medicament
-- =========================
CREATE TABLE Client (
  id_client INT AUTO_INCREMENT PRIMARY KEY,
  nom       VARCHAR(60) NOT NULL,
  prenom    VARCHAR(60) NOT NULL,
  email     VARCHAR(120),
  adresse   VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE Medicament (
  id_medicament  INT AUTO_INCREMENT PRIMARY KEY,
  nom            VARCHAR(80) NOT NULL,
  dosage         VARCHAR(40),
  stock          INT NOT NULL DEFAULT 0,
  prix_unitaire  DECIMAL(10,2) NOT NULL DEFAULT 0
) ENGINE=InnoDB;

-- =========================
-- 3) Vente / Commande / Historique Stock
-- (id_*# in the statement = foreign keys)
-- =========================
CREATE TABLE Vente (
  id_vente      INT AUTO_INCREMENT PRIMARY KEY,
  id_pharmacien INT NOT NULL,
  id_client     INT NOT NULL,
  id_medicament INT NOT NULL,
  quantite      INT NOT NULL,
  `date`        DATETIME NOT NULL,

  CONSTRAINT fk_vente_pharmacien FOREIGN KEY (id_pharmacien) REFERENCES Pharmacien(id_pharmacien),
  CONSTRAINT fk_vente_client     FOREIGN KEY (id_client)     REFERENCES Client(id_client),
  CONSTRAINT fk_vente_medicament FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament)
) ENGINE=InnoDB;

CREATE INDEX idx_vente_pharmacien ON Vente(id_pharmacien);
CREATE INDEX idx_vente_client     ON Vente(id_client);
CREATE INDEX idx_vente_medicament ON Vente(id_medicament);
CREATE INDEX idx_vente_date       ON Vente(`date`);

CREATE TABLE Commande (
  id_commande     INT AUTO_INCREMENT PRIMARY KEY,
  id_gestionnaire INT NOT NULL,
  id_medicament   INT NOT NULL,
  quantite        INT NOT NULL,
  `date`          DATETIME NOT NULL,

  CONSTRAINT fk_cmd_gestionnaire FOREIGN KEY (id_gestionnaire) REFERENCES Gestionnaire(id_gestionnaire),
  CONSTRAINT fk_cmd_medicament   FOREIGN KEY (id_medicament)   REFERENCES Medicament(id_medicament)
) ENGINE=InnoDB;

CREATE INDEX idx_cmd_gestionnaire ON Commande(id_gestionnaire);
CREATE INDEX idx_cmd_medicament   ON Commande(id_medicament);
CREATE INDEX idx_cmd_date         ON Commande(`date`);

CREATE TABLE StockHistorique (
  id_medicament INT NOT NULL,
  quantite      INT NOT NULL,
  `date`        DATETIME NOT NULL,

  CONSTRAINT fk_hist_medicament FOREIGN KEY (id_medicament) REFERENCES Medicament(id_medicament)
) ENGINE=InnoDB;

CREATE INDEX idx_hist_medicament ON StockHistorique(id_medicament);
CREATE INDEX idx_hist_date       ON StockHistorique(`date`);

-- =========================
-- 4) Sample data (optional, but useful to start coding)
-- =========================
INSERT INTO Pharmacien(nom, prenom, login, pwd) VALUES
  ('Ali', 'Sami', 'ph1', '1234'),
  ('Haddad', 'Mouna', 'ph2', '1234');

INSERT INTO Gestionnaire(nom, prenom, login, pwd) VALUES
  ('Ben', 'Sara', 'gs1', '1234');

INSERT INTO Client(nom, prenom, email, adresse) VALUES
  ('Khaled','Amine','amine@example.com','Tunis'),
  ('Trabelsi','Nour','nour@example.com','Ariana');

INSERT INTO Medicament(nom, dosage, stock, prix_unitaire) VALUES
  ('Paracetamol','500mg',20,2.50),
  ('Ibuprofen','400mg',3,3.20),
  ('Amoxicilline','1g',1,6.80);

-- Example sale + stock history (optional)
-- (Make sure ids exist; these use the first inserted rows)
INSERT INTO Vente(id_pharmacien, id_client, id_medicament, quantite, `date`)
VALUES (1, 1, 1, 2, NOW());

-- Update stock & record history for the example sale
UPDATE Medicament SET stock = stock - 2 WHERE id_medicament = 1;
INSERT INTO StockHistorique(id_medicament, quantite, `date`)
SELECT id_medicament, stock, NOW() FROM Medicament WHERE id_medicament = 1;
