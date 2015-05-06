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