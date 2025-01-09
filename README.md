# oneStar3gram-backend

## Installation du repo

Pour installer localement le code source, vous devez utiliser la commande suivante : 

- HTTPS : https://github.com/TechQuack/oneStar3gram-backend.git
- SSH : git@github.com:TechQuack/oneStar3gram-backend.git

## Lancement du projet

Afin de lancer notre projet, il suffit d'utiliser la commande : 

`docker-compose up --build`


## Compte de service Keycloak

Si nous voulons utiliser pleinement notre application, nous avons besoin d'avoir un compte Keycloak possédant certains droits et rôles.
Pour avoir cet utilisateur de créé par défaut, il suffit de lancer le backend **après** avoir lancé le docker du keycloak, l'utilisateur sera alors automatiquement créé dans keycloak.

Cependant, un certain nombre de paramètres et variables d'environnement sont à définir. Tout est centralisé et à modifier dans le fichier
`src/main/resources/application.properties`. 
Les variables à définir sont celles commençant par "keycloak-".

## Documentation APIs

Afin d'accéder à la documentation de nos différents endpoints et de nos APIs, nous pouvons nous rendre à l'URL : \
https://proxy-onestar3gram:8081/swagger-ui/index.html

Si nous voulons essayer nous-mêmes les routes nécessitant des rôles, il nous faudra un token.
Nous pouvons importer dans postman, la collection `OneStar3gram.postman_collection.json` présente dans le dossier `postman/` du repo Keycloak.
Il suffit ensuite de remplacer les fields username et password par nos identifiants et nous récupérons un token. 
Enfin, il faudra copier et coller ce token dans Swagger pour être authentifié.

## Lancement des tests

Pour lancer les tests présents sur notre projet il faut utiliser la commande : `mvn clean test`
