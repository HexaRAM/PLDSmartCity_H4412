# Liste des services

- Jouer un challenge : `GET /challenges/<id>/play`
- Lister les challenges en cours : `GET /challengePlayed/`
- Afficher les informations sur un challenge en cours : `GET /challengePlayed/<id>/`
    - Obtenir la liste des photos d'un challenge :
    ```python
        retour = appel de l'URL
        pictures = retour['validationitem']['picturechallengeplayed_set']
    ```
- Submit un challenge en cours : `GET /challengePlayed/<id>/submit`
- Lister les challenges à valider : `GET /toValidate/`
    - Valider un challenge : `GET /challengePlayed/<id>/validate/`
    - Ne pas valider un challenge : `GET /challengePlayed/<id>/unvalidate/`

- Obtenir la station de Vélo'V la plus proche : `GET /getClosestStation/` avec latitude et longitude en paramètre
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