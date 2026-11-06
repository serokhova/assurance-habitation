# Projet Assurance-Habitation

# Environnement Docker local

Ce Compose lance PostgreSQL et Apache Artemis sur votre ordinateur. Les ports sont publiés uniquement sur `127.0.0.1` et les données restent dans des volumes Docker locaux. Les identifiants fournis sont publics et réservés à cet environnement : ne les utilisez pas sur le VPS ni pour des données réelles.

## Prérequis

- Installez Docker Desktop depuis la documentation officielle correspondant à votre système : [Windows](https://docs.docker.com/desktop/setup/install/windows-install/), [macOS](https://docs.docker.com/desktop/setup/install/mac-install/) ou [Linux](https://docs.docker.com/desktop/setup/install/linux/). Sous Linux, vous pouvez aussi installer Docker Engine avec le plugin Compose en suivant les instructions pour [votre distribution](https://docs.docker.com/engine/install/).
- Démarrez Docker Desktop, ou vérifiez que le service Docker Engine fonctionne.
- Une application Java/Spring lancée sur votre ordinateur, ou dans un conteneur Docker, est nécessaire pour utiliser la base et le broker. Vous pouvez toutefois démarrer les services seuls pour explorer la console Artemis.

Après l'installation, ouvrez un terminal (PowerShell sous Windows, Terminal sous macOS/Linux) et vérifiez que Docker et Compose répondent :

```sh
docker --version
docker compose version
docker run --rm hello-world
```

Si la commande `docker` renvoie une erreur de permission sous Linux, consultez les [étapes post-installation officielles](https://docs.docker.com/engine/install/linux-postinstall/) ou préfixez temporairement les commandes Docker avec `sudo`.

## Démarrer les services

Depuis ce dossier, lancez :

```sh
docker compose up -d
docker compose ps
```

Vous pouvez créer plusieurs schémas dans la base locale `projet`.

| Service | Hôte | Port | Identifiant | Mot de passe |
| --- | --- | ---: | --- | --- |
| PostgreSQL | `localhost` | `5432` | `atelier` | `atelier-local-uniquement` |
| Artemis Core/JMS | `localhost` | `61616` | `atelier` | `atelier-local-uniquement` |
| Console Artemis | <http://localhost:8161> | `8161` | `atelier` | `atelier-local-uniquement` |

Dans une application Spring lancée directement sur votre ordinateur, vous pouvez utiliser ces propriétés :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projet
spring.datasource.username=atelier
spring.datasource.password=atelier-local-uniquement
spring.artemis.mode=native
spring.artemis.broker-url=tcp://localhost:61616
spring.artemis.user=atelier
spring.artemis.password=atelier-local-uniquement
```

Si votre application Spring tourne elle-même dans Docker Compose, `localhost` désigne son propre conteneur. Ajoutez alors le service Spring à ce Compose et utilisez `postgres:5432` et `artemis:61616` comme hôtes.

## Arrêter et réinitialiser

Arrêtez les conteneurs sans effacer les données :

```sh
docker compose down
```

Pour effacer également la base et les messages stockés, supprimez les volumes :

```sh
docker compose down -v
```

Cette suppression est irréversible.

## Personnaliser

Copiez `.env.example` vers `.env` pour changer les ports ou identifiants. Les scripts de `postgres/init` ne sont exécutés qu'à la création d'un volume PostgreSQL vide. Si vous modifiez les paramètres après le premier démarrage, appliquez les changements dans PostgreSQL ou supprimez le volume local avec `docker compose down -v`.

Les versions des images sont épinglées dans le Compose. Une mise à niveau majeure de PostgreSQL demande une migration des données, pas seulement un changement de tag.
