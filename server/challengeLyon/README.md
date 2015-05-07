# Liste des services

[Lien vers les webservices](http://vps165185.ovh.net/)

De nombreuses informations et la liste exhaustive des services de base (récupération des informations du modèle) sont présentes sur cette page.

## Core

- Jouer un challenge : `GET /challenges/<id>/play`
- Lister les challenges en cours : `GET /challengePlayed/`
- Afficher les informations sur un challenge en cours : `GET /challengePlayed/<id>/`
    - Obtenir la liste des photos d'un challenge :
    ```python
        retour = appel de l'URL
        pictures = retour['validationitem']['picturechallengeplayed_set']
    ```
- Upload une image ou une location pour la validation d'un challenge : `POST /picturesChallengePlayed/` ou `POST /locationsChallengePlayed/` les paramètres à envoyer se retrouvent ici :
    - [Paramètres pour upload une image](http://vps165185.ovh.net/picturesChallengePlayed/)
    - [Paramètres pour upload une location](http://vps165185.ovh.net/locationsChallengePlayed/)
- Submit un challenge en cours : `GET /challengePlayed/<id>/submit`
    - Dans le cas d'un challenge **Vélo'V**, il faut également envoyer la position de l'utilisateur sous la forme suivante :
    `GET /challengePlayed/<id>/submit/?longitude=4.872572&latitude=45.781869`
- Lister les challenges à valider : `GET /toValidate/`
    - Valider un challenge : `GET /challengePlayed/<id>/validate/`
    - Ne pas valider un challenge : `GET /challengePlayed/<id>/unvalidate/`


## Manipulation des données du Grand Lyon

- Obtenir la station de Vélo'V la plus proche pour **prendre un vélo** (avec des vélos disponibles) : `GET /getClosestStation/` avec `latitude` et `longitude` en paramètre
    - Exemple avec le département informatique : `/getClosestStation/?latitude=45.781869&longitude=4.872572`
    - Retour ordinaire :
    ```
    "status": "ok",
    "result": {
        "nom": "DOUA / Gaston Berger",
        "velos_posables": 28,
        "longitude": 4.87132691289157,
        "km": 0.1409458520443689,
        "latitude": 45.780944479741,
        "velos_disponibles": 1,
        "id": "10102"
    }
    ```
- Obtenir la station de Vélo'V la plus proche pour **poser son vélo** (avec des places de parking disponibles) : `GET /getClosestStation/` avec `latitude` `longitude` et `arrivee` en paramètre

    - Exemple avec le département informatique : `http://vps165185.ovh.net/getClosestStation/?latitude=45.781869&longitude=4.872572&arrivee`