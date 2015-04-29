#-*- coding: utf-8 -*-
from django.db import models


# Bloc User
from django.contrib.auth.models import User

# TODO : créer une classe User qui hérite de celle ci-dessus et y ajouter les attributs supplémentaires dont on a besoin / à noter que Django gère les groupes !

# note : pas besoin de class ranking, on ajoute un attribut score dans User


# Bloc Challenge
class Location(models.Model):
    latitude = models.FloatField()
    longitude = models.FloatField()
    name = models.CharField(max_length=127)

    def __unicode__(self):
        return u"%s [%s,%s]"%(self.name,self.longitude,self.latitude)

class Picture(models.Model):
    name = models.CharField(max_length=127, verbose_name="Nom du fichier")

    def __unicode__(self):
        return u"Image : %s"%self.name

class Category(models.Model):
    name = models.CharField(max_length=45)

    def __unicode__(self):
        return u"Catégorie : %s"%self.name

    class Meta:
        verbose_name_plural = "Categories"

class Type(models.Model):
    name = models.CharField(max_length=45)

    def __unicode__(self):
        return u"Type : %s"%self.name

class Quizz(models.Model):
    title = models.CharField(max_length=45)
    description = models.TextField(blank=True, null=True)

    def __unicode__(self):
        return u"Quizz : %s"%self.title

class Metavalidation(models.Model):
    picture_validation = models.BooleanField(default=False)
    quizz_validation = models.BooleanField(default=False)
    location_validation = models.BooleanField(default=False)

    def __unicode__(self):
        return u"Photos : %s / Quizz : %s / Lieux : %s"%(self.picture_validation, self.quizz_validation, self.location_validation)

class Challenge(models.Model):
    title = models.CharField(max_length=45)
    description = models.TextField(blank=True, null=True)
    starttime = models.DateTimeField(blank=True, null=True, verbose_name="Date de début")
    endtime = models.DateTimeField(blank=True, null=True, verbose_name="Date de fin")
    creator = models.ForeignKey(User, verbose_name="Créateur")
    category = models.ForeignKey(Category, verbose_name="Catégorie")
    type = models.ForeignKey(Type, verbose_name="Type")
    quizz = models.ForeignKey(Quizz, null=True, blank=True)
    metavalidation = models.ForeignKey(Metavalidation, null=True)
    locations = models.ManyToManyField(Location, blank=True)

    def __unicode__(self):
        return u"%s [%s - %s]"%(self.title, self.category, self.type)

class Challengeplayed(models.Model):
    challenge = models.ForeignKey(Challenge)
    user = models.ForeignKey(User)
    score = models.IntegerField(default=0) # score gagnable du challenge lancé

    def __unicode__(self):
        return u"%s lancé par %s [score : %s]"%(self.challenge, self.user, self.score)

    class Meta:
        unique_together = (('challenge', 'user'),)
        verbose_name_plural = "ChallengesPlayed"

# Bloc Quizz
class Question(models.Model):
    quizz = models.ForeignKey(Quizz)
    content = models.TextField(verbose_name="Contenu")
    goodAnswer_id = models.IntegerField(null=True, blank=True)

    def __unicode__(self):
        return u"Question : %s [Réponse ID : %s]"%(self.content, self.goodAnswer_id)

class Answer(models.Model):
    question = models.ForeignKey(Question)
    content = models.TextField(verbose_name="Contenu")

    def __unicode__(self):
        return u"Réponse à la question %s : %s"%(self.question.id, self.content)

# Bloc validation
class Validationitem(models.Model):
    challengeplayed = models.ForeignKey(Challengeplayed)
    locations = models.ManyToManyField(Location, blank=True)
    pictures = models.ManyToManyField(Picture, blank=True)
    users = models.ManyToManyField(User, verbose_name="Validations", blank=True)

    def __unicode__(self):
        return u"Validation du challenge %s"%(self.challengeplayed.challenge)

class Useranswer(models.Model):
    question = models.ForeignKey(Question)
    answer = models.ForeignKey(Answer)
    challengeplayed = models.ForeignKey(Challengeplayed)

    def __unicode__(self):
        return u"Réponse d'un utilisateur à la question %s : %s"%(self.question, self.answer)