# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
#
# Also note: You'll have to insert the output of 'django-admin sqlcustom [app_label]'
# into your database.
from __future__ import unicode_literals

from django.db import models


class Answer(models.Model):
    idanswer = models.IntegerField(db_column='idAnswer', primary_key=True)  # Field name made lowercase.
    question_idquestion = models.ForeignKey('Question', db_column='Question_idQuestion')  # Field name made lowercase.
    answercontent = models.CharField(db_column='answerContent', max_length=255, blank=True, null=True)  # Field name made lowercase.

    class Meta:
        db_table = 'Answer'


class Category(models.Model):
    idcategory = models.IntegerField(db_column='idCategory', primary_key=True)  # Field name made lowercase.
    name = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        db_table = 'Category'


class Challenge(models.Model):
    idchallenge = models.AutoField(db_column='idChallenge', primary_key=True)  # Field name made lowercase.
    title = models.CharField(max_length=45)
    description = models.CharField(max_length=255, blank=True, null=True)
    starttime = models.DateTimeField(db_column='startTime', blank=True, null=True)  # Field name made lowercase.
    endtime = models.DateTimeField(db_column='endTime', blank=True, null=True)  # Field name made lowercase.
    user_idusercreator = models.ForeignKey('User', db_column='user_idUserCreator')  # Field name made lowercase.
    category_idcategory = models.ForeignKey(Category, db_column='Category_idCategory')  # Field name made lowercase.
    type_idtype = models.ForeignKey('Type', db_column='Type_idType')  # Field name made lowercase.
    quizz_idquizz = models.ForeignKey('Quizz', db_column='Quizz_idQuizz')  # Field name made lowercase.
    metavalidation_idmetavalidation = models.ForeignKey('Metavalidation', db_column='MetaValidation_idMetaValidation')  # Field name made lowercase.
    abstract = models.CharField(max_length=55, blank=True, null=True)

    class Meta:
        db_table = 'Challenge'


class Challengeplayed(models.Model):
    idchallengeplayed = models.IntegerField(db_column='idChallengePlayed')  # Field name made lowercase.
    challenge_idchallenge = models.ForeignKey(Challenge, db_column='Challenge_idChallenge')  # Field name made lowercase.
    user_iduser = models.ForeignKey('User', db_column='user_idUser')  # Field name made lowercase.

    class Meta:
        db_table = 'ChallengePlayed'
        unique_together = (('idChallengePlayed', 'Challenge_idChallenge'),)


class ChallengeHasLocation(models.Model):
    challenge_idchallenge = models.ForeignKey(Challenge, db_column='Challenge_idChallenge')  # Field name made lowercase.
    location_idlocation = models.ForeignKey('Location', db_column='Location_idLocation')  # Field name made lowercase.

    class Meta:
        db_table = 'Challenge_has_Location'
        unique_together = (('Challenge_idChallenge', 'Location_idLocation'),)


class Groupuser(models.Model):
    idgroupuser = models.IntegerField(db_column='idGroupUser', primary_key=True)  # Field name made lowercase.
    title = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        db_table = 'GroupUser'


class GroupuserHasUser(models.Model):
    groupuser_idgroupuser = models.ForeignKey(Groupuser, db_column='GroupUser_idGroupUser')  # Field name made lowercase.
    user_iduser = models.ForeignKey('User', db_column='user_idUser')  # Field name made lowercase.

    class Meta:
        db_table = 'GroupUser_has_user'
        unique_together = (('GroupUser_idGroupUser', 'user_idUser'),)


class Location(models.Model):
    idlocation = models.IntegerField(db_column='idLocation', primary_key=True)  # Field name made lowercase.
    latitude = models.FloatField(blank=True, null=True)
    longitude = models.FloatField(blank=True, null=True)
    name = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        db_table = 'Location'


class Metavalidation(models.Model):
    idmetavalidation = models.IntegerField(db_column='idMetaValidation', primary_key=True)  # Field name made lowercase.
    picture_validation = models.IntegerField(blank=True, null=True)
    quizz_validation = models.IntegerField(blank=True, null=True)
    location_validation = models.IntegerField(blank=True, null=True)

    class Meta:
        db_table = 'MetaValidation'


class Picture(models.Model):
    idpicture = models.IntegerField(db_column='idPicture', primary_key=True)  # Field name made lowercase.
    validationitem_idvalidationitem = models.ForeignKey('Validationitem', db_column='ValidationItem_idValidationItem')  # Field name made lowercase.
    name = models.CharField(max_length=127, blank=True, null=True)

    class Meta:
        db_table = 'Picture'


class Question(models.Model):
    idquestion = models.IntegerField(db_column='idQuestion', primary_key=True)  # Field name made lowercase.
    quizz_idquizz = models.ForeignKey('Quizz', db_column='Quizz_idQuizz')  # Field name made lowercase.
    questioncontent = models.CharField(db_column='questionContent', max_length=255, blank=True, null=True)  # Field name made lowercase.

    class Meta:
        db_table = 'Question'


class Quizz(models.Model):
    idquizz = models.IntegerField(db_column='idQuizz', primary_key=True)  # Field name made lowercase.
    title = models.CharField(max_length=45, blank=True, null=True)
    description = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        db_table = 'Quizz'


class Ranking(models.Model):
    user_iduser = models.ForeignKey('User', db_column='user_idUser')  # Field name made lowercase.
    ranking = models.IntegerField(db_column='Ranking', primary_key=True)  # Field name made lowercase.
    score = models.IntegerField(blank=True, null=True)

    class Meta:
        db_table = 'Ranking'


class Type(models.Model):
    idtype = models.IntegerField(db_column='idType', primary_key=True)  # Field name made lowercase.
    type_name = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        db_table = 'Type'


class Useranswer(models.Model):
    iduseranswer = models.IntegerField(db_column='idUserAnswer', primary_key=True)  # Field name made lowercase.
    question_idquestion = models.ForeignKey(Question, db_column='Question_idQuestion')  # Field name made lowercase.
    validationitem_idvalidationitem = models.ForeignKey('Validationitem', db_column='ValidationItem_idValidationItem')  # Field name made lowercase.
    useranswercontent = models.CharField(db_column='userAnswerContent', max_length=255, blank=True, null=True)  # Field name made lowercase.

    class Meta:
        db_table = 'UserAnswer'


class Validationitem(models.Model):
    idvalidationitem = models.IntegerField(db_column='idValidationItem', primary_key=True)  # Field name made lowercase.
    challengeplayed_idchallengeplayed = models.ForeignKey(Challengeplayed, db_column='ChallengePlayed_idChallengePlayed')  # Field name made lowercase.
    challengeplayed_challenge_idchallenge = models.ForeignKey(Challengeplayed, db_column='ChallengePlayed_Challenge_idChallenge')  # Field name made lowercase.

    class Meta:
        db_table = 'ValidationItem'


class ValidationitemHasLocation(models.Model):
    validationitem_idvalidationitem = models.ForeignKey(Validationitem, db_column='ValidationItem_idValidationItem')  # Field name made lowercase.
    location_idlocation = models.ForeignKey(Location, db_column='Location_idLocation')  # Field name made lowercase.

    class Meta:
        db_table = 'ValidationItem_has_Location'
        unique_together = (('ValidationItem_idValidationItem', 'Location_idLocation'),)


class User(models.Model):
    iduser = models.IntegerField(db_column='idUser', primary_key=True)  # Field name made lowercase.
    username = models.CharField(max_length=16)
    email = models.CharField(max_length=255, blank=True, null=True)
    password = models.CharField(max_length=32)
    address = models.CharField(max_length=255, blank=True, null=True)
    create_time = models.DateTimeField(blank=True, null=True)
    last_connection = models.DateTimeField(blank=True, null=True)

    class Meta:
        db_table = 'user'


class UserValidatedValidationitem(models.Model):
    user_iduser = models.ForeignKey(User, db_column='user_idUser')  # Field name made lowercase.
    validationitem_idvalidationitem = models.ForeignKey(Validationitem, db_column='ValidationItem_idValidationItem')  # Field name made lowercase.

    class Meta:
        db_table = 'user_validated_ValidationItem'
        unique_together = (('user_idUser', 'ValidationItem_idValidationItem'),)
from django.db import models

# Create your models here.
