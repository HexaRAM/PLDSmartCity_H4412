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

# TODO : créer une classe User qui hérite de celle ci-dessus et y ajouter les attributs supplémentaires dont on a besoin / à noter que Django gère les groupes !

# note : pas besoin de class ranking, on ajoute un attribut score dans User

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
class Location(models.Model):
    latitude = models.FloatField()
    longitude = models.FloatField()
    name = models.CharField(max_length=127)

    def __unicode__(self):
        return u"%s [%s,%s]"%(self.name,self.longitude,self.latitude)

from django.conf import settings

class Picture(models.Model):
    image = models.ImageField(upload_to = 'picture', default="upload.jpg") 
    name = models.CharField(max_length=127, verbose_name="Nom du fichier")

    def __unicode__(self):
        return u"Image : %s"%self.name

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
    description = models.TextField(blank=True, null=True)
    starttime = models.DateTimeField(blank=True, null=True, verbose_name="Date de début")
    endtime = models.DateTimeField(blank=True, null=True, verbose_name="Date de fin")
    creator = models.ForeignKey(ChallengeUser, verbose_name="Créateur")
    category = models.ForeignKey(Category, verbose_name="Catégorie")
    type = models.ForeignKey(Type, verbose_name="Type")
    quizz = models.ForeignKey(Quizz, null=True, blank=True)
    metavalidation = models.ForeignKey(Metavalidation, null=True)
    locations = models.ManyToManyField(Location, blank=True)

    def __unicode__(self):
        return u"%s [%s - %s]"%(self.title, self.category, self.type)

class Challengeplayed(models.Model):
    challenge = models.ForeignKey(Challenge)
    user = models.ForeignKey(ChallengeUser)
    score = models.IntegerField(default=0) # score gagnable du challenge lancé

    def save(self, *args, **kwargs):
        super(Challengeplayed, self).save(*args, **kwargs)
        if self.id is not None:
            # objet créé et ajouté
            validation = Validationitem(challengeplayed=self)
            validation.save()

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
    challengeplayed = models.ForeignKey(Challengeplayed) # plutôt un OneToOneField ici
    locations = models.ManyToManyField(Location, blank=True)
    pictures = models.ManyToManyField(Picture, blank=True)
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



# Serializers class

from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = ChallengeUser
        fields = ('url', 'email', 'ranking')

class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category
        fields = ('name', 'reward')

class LocationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Location

class ChallengeSerializer(serializers.ModelSerializer):
    starttime = serializers.DateTimeField()
    endtime = serializers.DateTimeField()
    creator = UserSerializer(read_only=True)

    class Meta:
        model = Challenge
        fields = ('url', 'title', 'description', 'starttime', 'endtime', 'creator', 'category', 'type', 'metavalidation', 'quizz', 'locations')

class HotChallengeSerializer(ChallengeSerializer):
    pass

class PictureSerializer(serializers.ModelSerializer):
    def validate_image(self, value):
        """
        Check image size
        """
        if value.size > settings.MEDIA_MAX_SIZE:
            max_size_o = int(settings.MEDIA_MAX_SIZE)
            max_size_mo = settings.MEDIA_MAX_SIZE/1024/1024
            raise serializers.ValidationError(u"L'image est trop lourde (maximum %s mo [%s o])"%(max_size_mo, max_size_o))
        return value

    class Meta:
        model = Picture
        fields = ('url', 'image', 'name')