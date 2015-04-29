#-*- coding: utf-8 -*-

# TODO : rajouter toutes les méthodes def __unicode__(self):
from django.db import models


# Bloc User
from django.contrib.auth.models import User

# TODO : créer une classe User qui hérite de celle ci-dessus et y ajouter les attributs supplémentaires dont on a besoin / à noter que Django gère les groupes !

# note : pas besoin de class ranking, on ajoute un attribut score dans User


# Bloc Challenge
class Category(models.Model):
    name = models.CharField(max_length=45)

    class Meta:
        verbose_name_plural = "Categories"

class Type(models.Model):
    name = models.CharField(max_length=45)

class Quizz(models.Model):
    title = models.CharField(max_length=45)
    description = models.TextField(blank=True, null=True)

class Metavalidation(models.Model):
    picture_validation = models.BooleanField(default=False)
    quizz_validation = models.BooleanField(default=False)
    location_validation = models.BooleanField(default=False)

class Challenge(models.Model):
    title = models.CharField(max_length=45)
    description = models.TextField(blank=True, null=True)
    starttime = models.DateTimeField(blank=True, null=True, verbose_name="Date de début")
    endtime = models.DateTimeField(blank=True, null=True, verbose_name="Date de fin")
    creator = models.ForeignKey(User, verbose_name="Créateur")
    category = models.ForeignKey(Category, verbose_name="Catégorie")
    type = models.ForeignKey(Type, verbose_name="Type")
    quizz = models.ForeignKey(Quizz, null=True)
    metavalidation = models.ForeignKey(Metavalidation, null=True)

class Challengeplayed(models.Model):
    challenge = models.ForeignKey(Challenge)
    user = models.ForeignKey(User)
    score = models.IntegerField(default=0) # score gagnable du challenge lancé

    class Meta:
        unique_together = (('challenge', 'user'),)
        verbose_name_plural = "ChallengesPlayed"

# Bloc Quizz
class Question(models.Model):
    quizz = models.ForeignKey(Quizz)
    content = models.TextField(verbose_name="Contenu")
    goodAnswer_id = models.IntegerField(null=True)

class Answer(models.Model):
    question = models.ForeignKey(Question)
    content = models.TextField(verbose_name="Contenu")



# Bloc validation
class Location(models.Model):
    latitude = models.FloatField()
    longitude = models.FloatField()
    name = models.CharField(max_length=127)


# TODO : gestion des liaisons n-n (reprendre le diagramme de classe et les autres classes tranquillement)
# Picture et ValidationItem
# Location et ValidationItem


#class Picture(models.Model):
#    validationItem = models.ForeignKey('Validationitem')
#    name = models.CharField(max_length=127)

# ajouter les n-n avec Picture / Location / Quizz (peut-être juste une ForeignKey pour le questionnaire)
class Validationitem(models.Model):
    challengeplayed = models.ForeignKey(Challengeplayed)

# revoir le modèle des questions/réponses avec juste des id_reponse (à la place des textes)
class Useranswer(models.Model):
    question = models.ForeignKey(Question)
    user = models.ForeignKey(User)
    content = models.CharField(max_length=255, verbose_name="Réponse") # à changer en reponse_id et comparer si ce reponse_id vaut bien le question.goodAnswer_id