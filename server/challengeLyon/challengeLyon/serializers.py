#-*- coding: utf-8 -*-

from rest_framework import serializers
from challengeLyon.models import *

class TypeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Type
        fields = ('name',)

class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category
        fields = ('name', 'reward')

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

class MetaValidationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Metavalidation
        fields = ('picture_validation', 'quizz_validation', 'location_validation')

class PictureSerializer(serializers.ModelSerializer):
    #image = serializers.ReadOnlyField('image.url')

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

class ChallengeSerializer(serializers.ModelSerializer):
    starttime = serializers.DateTimeField()
    endtime = serializers.DateTimeField()
    creator = UserSerializer(read_only=True)
    category = CategorySerializer(read_only=True)
    type = TypeSerializer(read_only=True)
    metavalidation = MetaValidationSerializer(read_only=True)

    class Meta:
        model = Challenge
        fields = ('url', 'title', 'description', 'starttime', 'endtime', 'creator', 'category', 'type', 'metavalidation', 'quizz', 'locations')

class ChallengeListSerializer(serializers.ModelSerializer):
    class Meta:
        model = Challenge
        fields = ('url', 'title', 'description')

class HotChallengeSerializer(ChallengeSerializer):
    pass


class ValidationItemSerializer(serializers.ModelSerializer):
    pictures = PictureSerializer(many=True)

    class Meta:
        model = Validationitem
        fields = ('url', 'challengeplayed', 'pictures', 'submitted', 'locations', 'users', 'useranswer_set')

# TODO : différencier la liste (renvoyer les id et une description du challenge) du spécifique (renvoyer le validationitem)
class ChallengePlayedSerializer(serializers.ModelSerializer):
    challenge = ChallengeSerializer(read_only=True)
    validationitem = ValidationItemSerializer(read_only=True)
    user = UserSerializer(read_only=True)

    class Meta:
        model = Challengeplayed
        fields = ('url', 'challenge', 'user', 'score', 'validated', 'validationitem')

class ChallengePlayedListSerializer(serializers.ModelSerializer):
    challenge = ChallengeListSerializer(read_only=True)

    class Meta:
        model = Challengeplayed
        fields = ('url', 'challenge', 'validated')