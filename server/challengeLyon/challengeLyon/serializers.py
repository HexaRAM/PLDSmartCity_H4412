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

class MetaValidationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Metavalidation
        fields = ('picture_validation', 'quizz_validation', 'location_validation')

class ChallengeSerializer(serializers.ModelSerializer):
    starttime = serializers.DateTimeField()
    endtime = serializers.DateTimeField()
    creator = UserSerializer(read_only=True)
    #category = CategorySerializer()
    #type = TypeSerializer()
    metavalidation = MetaValidationSerializer()

    class Meta:
        model = Challenge
        fields = ('url', 'title', 'description', 'starttime', 'endtime', 'creator', 'category', 'type', 'metavalidation', 'quizz')

class ChallengeListSerializer(serializers.ModelSerializer):
    class Meta:
        model = Challenge
        fields = ('url', 'title', 'description')

class HotChallengeSerializer(ChallengeSerializer):
    pass

class PictureChallengePlayedSerializer(serializers.ModelSerializer):
    #validationitem = ValidationItemToShowChallengePlayedSerializer()

    class Meta:
        model = PictureChallengePlayed
        fields = ('url', 'image', 'description', 'validationitem')

    def validate_image(self, value):
        """
        Check image size
        """
        if value.size > settings.MEDIA_MAX_SIZE:
            max_size_o = int(settings.MEDIA_MAX_SIZE)
            max_size_mo = settings.MEDIA_MAX_SIZE/1024/1024
            raise serializers.ValidationError(u"L'image est trop lourde (maximum %s mo [%s o])"%(max_size_mo, max_size_o))
        return value

class ValidationItemSerializer(serializers.ModelSerializer):
    picturechallengeplayed_set = PictureChallengePlayedSerializer('image', many=True, read_only=True)

    class Meta:
        model = Validationitem
        fields = ('id', 'url', 'challengeplayed', 'submitted', 'users', 'useranswer_set', 'picturechallengeplayed_set')

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

class ValidationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Validation

class ValidationItemToShowChallengePlayedSerializer(serializers.ModelSerializer):
    challengeplayed = ChallengePlayedSerializer(read_only=True)
    class Meta:
        model = Validationitem
        fields = ('id', 'challengeplayed',)


class PictureChallengeSerializer(serializers.ModelSerializer):
    challenge = ChallengeSerializer()

    class Meta:
        model = PictureChallenge
        fields = ('url', 'image', 'description', 'challenge',)

    def validate_image(self, value):
        """
        Check image size
        """
        if value.size > settings.MEDIA_MAX_SIZE:
            max_size_o = int(settings.MEDIA_MAX_SIZE)
            max_size_mo = settings.MEDIA_MAX_SIZE/1024/1024
            raise serializers.ValidationError(u"L'image est trop lourde (maximum %s mo [%s o])"%(max_size_mo, max_size_o))
        return value

class LocationChallengeSerializer(serializers.ModelSerializer):
    #validationitem = ValidationItemSerializer(read_only=True)
    class Meta:
        model = LocationChallenge
        fields = ('longitude', 'latitude', 'name', 'validationitem')

class LocationChallengePlayedSerializer(serializers.ModelSerializer):
    validationitem = ValidationItemToShowChallengePlayedSerializer(read_only=True)
    class Meta:
        model = LocationChallengePlayed
        fields = ('longitude', 'latitude', 'name', 'validationitem')

class ToValidateSerializer(serializers.ModelSerializer):
    pass