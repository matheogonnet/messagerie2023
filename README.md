

# Projet de Messagerie Instantanée en Java

## Description du Projet

Ce projet consiste en une application de messagerie instantanée développée en Java, inspirée par des applications telles que Discord (partie textuelle). Le projet est divisé en deux applications principales : un serveur et un client. Le serveur gère les clients ainsi que les données des utilisateurs et des conversations via une base de données. Le client permet aux utilisateurs de dialoguer en temps réel.

## Fonctionnalités

- Connexion des utilisateurs avec un pseudo et un mot de passe sécurisé.
- Affichage en temps réel des messages avec nom d'utilisateur et timestamp.
- Liste des utilisateurs avec leurs statuts (en ligne, hors ligne, absent).
- Mise à jour du statut de l'utilisateur.
- Gestion des utilisateurs : Utilisateurs normaux, modérateurs et administrateurs.
- Stockage des données dans une base de données MySQL.

## Comment Lancer le Projet


## Prérequis

| Prérequis         | Description                             |
|-------------------|-----------------------------------------|
| Java JDK          | Version 1.8 ou supérieure               |
| Serveur MySQL     | Pour la gestion de la base de données   |

## Installation

1. **Clonage du Projet** : Cloner le projet depuis le repository Git dans votre IDE Java.
2. **Configuration de MySQL** : Installer et configurer un serveur MySQL (Wamp Server, MAMP, etc.).
3. **Initialisation de la Base de Données** : Exécuter le script SQL `pi2023.sql` depuis votre mySQL, pour initialiser la base de données.

## Lancement du Serveur

Pour mettre en service le serveur de la messagerie instantanée, suivez ces étapes :

1. **Compilation** :
    - Ouvrez votre IDE Java.
    - Localisez le fichier `MainServer` dans le projet : `src/server/MainServer.java`.
    - Compilez le projet pour générer un fichier exécutable `.jar` pour le serveur.

2. **Exécution** :
    - Lancez le serveur directement depuis votre IDE en exécutant le `MainServer` (**Conseillé**).
    - Sinon, vous pouvez exécuter le fichier `.jar` généré en utilisant une ligne de commande ou en double-cliquant sur le fichier, selon la configuration de votre système.
    - Si vous exécutez via un terminal, utilisez la commande : `java -jar chemin_vers_le_fichier_serveur.jar`.

3. **Démarrage du Serveur** :
    - À l'exécution, le serveur commencera à écouter les connexions entrantes des clients.
    - Suivez les instructions et les informations affichées dans la console de votre IDE ou dans le terminal pour vérifier que le serveur fonctionne correctement.

4. **Commandes de Gestion du Serveur** :
    - Le serveur est équipé d'un système de commandes pour sa gestion.
    - Vous pouvez stopper le serveur en saisissant `/disconnected` dans la console du serveur.
    - Cette commande déclenchera la fermeture de toutes les connexions client et l'arrêt du serveur.

5. **Fonctionnalités du Serveur** :
    - Le serveur gère les connexions des clients, l'authentification des utilisateurs, l'envoi et la réception de messages.
    - Il assure également la connexion avec la base de données MySQL pour la gestion des données utilisateurs et des conversations.


## Lancement du Client

Pour lancer un client dans la messagerie instantanée, suivez ces étapes :

1. **Compilation** :
    - Ouvrez votre IDE Java.
    - Localisez le fichier `MainClient` dans le projet :`src/client/MainClient.java`.
    - Compilez le projet pour générer un fichier exécutable `.jar` pour le client.

2. **Exécution** :
    - Vous pouvez exécuter le client directement depuis votre IDE en lançant le `MainClient` (**Conseillé**).
    - Ou, exécutez le fichier `.jar` généré à partir d'une ligne de commande ou en double-cliquant sur le fichier (selon votre configuration système).
    - Si vous exécutez via un terminal, utilisez la commande :`java -jar chemin_vers_le_fichier_client.jar`.

3. **Connexion** :
    - Une fois l'application client lancée, une interface graphique s'ouvrira.
    - Entrez vos informations de connexion (pseudo et mot de passe) pour accéder à la messagerie.
    - Si vous n'avez pas de compte, vous devrez probablement en créer un via l'interface ou suivre les instructions fournies pour l'enregistrement.

4. **Utilisation de la Messagerie** :
    - Après vous être connecté, vous pouvez utiliser l'interface graphique pour interagir avec les autres utilisateurs, envoyer des messages, voir les statuts, etc.




## Architecture du Projet

| Composant          | Description                              | Technologie/Langage Utilisé |
|--------------------|------------------------------------------|-----------------------------|
| **Client**         | Interface utilisateur pour la messagerie | Java, Swing (GUI)           |
| **Serveur**        | Gestion des connexions et des données    | Java, DAO (Accès aux données) |
| **Base de Données**| Stockage des données des utilisateurs    | SQL, MySQL                  |
| **Communication**  | Échange de données entre client et serveur | TCP/IP                     |


## Contribution et Support

Pour toute question ou contribution au projet, veuillez ouvrir une issue ou une pull request sur le repository Git du projet.

## Auteurs
Mathéo, Arthur, Valentin, Kaito, Sandra