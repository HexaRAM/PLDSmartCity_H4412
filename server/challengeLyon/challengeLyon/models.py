#-*- coding: utf-8 -*-
from django.db import models


# Bloc User
from django.contrib.auth.models import User
from django.utils import timezone
from django.utils.http import urlquote
from django.utils.translation import ugettext_lazy as _
from django.core.mail import send_mail
from django.core.exceptions import ValidationError
from django.contrib.auth.models import (
BaseUserManager, AbstractBaseUser
)

# Custom User
class ChallengeUserManager(BaseUserManager):

    def _create_user(self, email, password, is_admin, **extra_fields):
        if not email:
            raise ValueError('Users must have an email address')
        user = self.model(
            email = self.normalize_email(email),
            is_admin = is_admin,
            **extra_fields
        )
        user.set_password(password)
        user.save(using = self._db)
        return user

    def create_user(self, email, password, **extra_fields):
        return self._create_user(email, password, False, **extra_fields)

    def create_superuser(self, email,password, **extra_fields):
        return self._create_user(email, password, True, **extra_fields)


class ChallengeUser(AbstractBaseUser):
    
    email = models.EmailField(max_length = 254, unique = True)
    is_admin = models.BooleanField(default=False,
    help_text=_('Designates whether the user can log into this admin '
        'site.'))
    is_active = models.BooleanField(default=True,
    help_text=_('Designates whether this user should be treated as '
        'active. Unselect this instead of deleting accounts.'))
    date_joined = models.DateTimeField(default=timezone.now)

    ranking = models.IntegerField(default=0) 
    objects = ChallengeUserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

    def get_full_name(self):
        return self.email

    def get_short_name(self):
        return self.email

    def __str__(self):
        return self.email   
    def __unicode__(self):
        return self.email      
    def has_perm(self, perm, obj=None):
        return True
    def has_module_perms(self, app_label):
        return True
    @property
    def is_staff(self):
        return self.is_admin

# Bloc Challenge
from django.conf import settings

class Category(models.Model):
    name = models.CharField(max_length=45)
    reward = models.IntegerField(default=10)

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
    summary = models.CharField(max_length=200)
    description = models.TextField(blank=True, null=True)
    starttime = models.DateTimeField(blank=True, null=True, verbose_name="Date de début")
    endtime = models.DateTimeField(blank=True, null=True, verbose_name="Date de fin")
    creator = models.ForeignKey(ChallengeUser, verbose_name="Créateur")
    category = models.ForeignKey(Category, verbose_name="Catégorie")
    type = models.ForeignKey(Type, verbose_name="Type")
    quizz = models.ForeignKey(Quizz, null=True, blank=True)
    metavalidation = models.ForeignKey(Metavalidation, null=True)

    def save(self, *args, **kwargs):
        self.clean()
        super(Challenge, self).save(*args, **kwargs)

    def clean(self):
        if self.metavalidation.quizz_validation and self.quizz is None:
            raise ValidationError('Quizz manquant')

    def __unicode__(self):
        return u"%s [%s - %s]"%(self.title, self.category, self.type)

class Challengeplayed(models.Model):
    challenge = models.ForeignKey(Challenge)
    user = models.ForeignKey(ChallengeUser)
    score = models.IntegerField(default=0) # score gagnable du challenge lancé
    validated = models.BooleanField(default=False)
    starttime = models.DateTimeField(auto_now=False, auto_now_add=True, verbose_name="Date de lancement du challenge")

    def save(self, *args, **kwargs):
        new = False
        if self.id is None:
            new = True
        super(Challengeplayed, self).save(*args, **kwargs)
        if new and self.id is not None:
            # objet créé et ajouté
            validation = Validationitem(challengeplayed=self)
            validation.save()
            self.validationitem = validation
            self.save()

    def validate(self):
        self.validated = True

    def toValidate(self):
        if not self.validated and self.validationitem.submitted:
            return True
        return False 

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
    challengeplayed = models.OneToOneField(Challengeplayed)
    submitted = models.BooleanField(default=False)
    users = models.ManyToManyField(ChallengeUser, verbose_name="Validations", blank=True, through="Validation", through_fields=('validationitem','user',))

    def __unicode__(self):
        return u"Validation du challenge %s"%(self.challengeplayed.challenge)

class Validation(models.Model):
    validationitem = models.ForeignKey(Validationitem)
    user = models.ForeignKey(ChallengeUser)
    vote = models.IntegerField(default=0)

    class Meta:
        unique_together = ('user', 'validationitem')

    def save(self, *args, **kwargs):
        self.clean()
        super(Validation, self).save(*args, **kwargs)

    def clean(self):
        if not self.validationitem.submitted:
            raise ValidationError('Impossible de voter. Le challenge n\'a pas été validé.')

    def __unicode__(self):
        return u"%s a voté %s pour le challenge %s"%(self.user, self.vote, self.validationitem.challengeplayed.challenge)

    def changeVote(self, value=0):
        self.vote = value

    def upvote(self):
        self.changeVote(1)

    def downvote(self):
        self.changeVote(-1)

    def removeVote(self):
        self.changeVote(0)

class Useranswer(models.Model):
    question = models.ForeignKey(Question)
    answer = models.ForeignKey(Answer)
    validationitem = models.ForeignKey(Validationitem)
    def __unicode__(self):
        return u"Réponse d'un utilisateur à la question %s : %s"%(self.question, self.answer)

class PictureChallenge(models.Model):
    image = models.ImageField(upload_to = 'challenge', default="upload.jpg") 
    description = models.TextField(verbose_name="Description brève de la photo")
    challenge = models.ForeignKey(Challenge)

    def __unicode__(self):
        return u"Image (%s) : %s"%(self.challenge, self.description)

class PictureChallengePlayed(models.Model):
    image = models.ImageField(upload_to = 'challengePlayed', default="upload.jpg")
    description = models.TextField(verbose_name="Description brève de la photo")
    validationitem = models.ForeignKey(Validationitem)

    def save(self, *args, **kwargs):
        self.clean()
        super(PictureChallengePlayed, self).save(*args, **kwargs)

    def clean(self):
        if self.validationitem.submitted:
            raise ValidationError('Impossible de rajouter une photo. Le challenge a été submit.')

    def __unicode__(self):
        return u"Image (%s) : %s"%(self.validationitem.challengeplayed, self.description)

class LocationChallenge(models.Model):
    latitude = models.FloatField()
    longitude = models.FloatField()
    name = models.CharField(max_length=127)
    challenge = models.ForeignKey(Challenge)

    def __unicode__(self):
        return u"(Challenge %s) %s [%s,%s]"%(self.challenge.id, self.name,self.longitude,self.latitude)

class LocationChallengePlayed(models.Model):
    latitude = models.FloatField()
    longitude = models.FloatField()
    name = models.CharField(max_length=127)
    validationitem = models.ForeignKey(Validationitem)

    def __unicode__(self):
        return u"(ChallengePlayed %s) %s [%s,%s]"%(self.validationitem.challengeplayed.id, self.name,self.longitude,self.latitude)