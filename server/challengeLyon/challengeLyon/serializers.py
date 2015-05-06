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
    creator = UserSerializer(read_only=True)
    #category = CategorySerializer()
    #type = TypeSerializer()
    metavalidation = MetaValidationSerializer()
    play = serializers.HyperlinkedIdentityField(view_name='challenge-play', read_only=True)

    class Meta:
        model = Challenge
        fields = ('url', 'play', 'title', 'summary', 'description', 'starttime', 'endtime', 'creator', 'category', 'type', 'metavalidation', 'quizz')

    def computeMetaValidation(self, metadata):
        data_keys = ["picture_validation", "quizz_validation", "location_validation"]
        datas = {}

        for (key, value) in metadata.iteritems():
            datas[key] = value

        metavalidation = None
        try:
            metavalidation, created = Metavalidation.objects.get_or_create(**datas)
        except:
            pass

        return metavalidation

    def create(self, validated_data):
        metavalidation = self.computeMetaValidation(validated_data['metavalidation'])

        if metavalidation is None:
            raise ValidationError("Impossible de récupérer les informations relatives au challenge (metavalidation)")

        try:
            metavalidation.save()
        except Exception as e:
            raise ValidationError("Impossible de sauvegarder la metavalidation")

        quizz = None
        if metavalidation.quizz_validation:
            if hasattr(validated_data, 'quizz'):
                quizz = Quizz(validated_data['quizz']) # to change
                quizz.save()
            else:
                raise ValidationError("Aucun quizz associé au challenge.")

        challenge = Challenge(
            title=validated_data['title'],
            summary=validated_data['summary'],
            description=validated_data['description'],
            starttime=validated_data['starttime'],
            endtime=validated_data['endtime'],
            creator=self.context['request'].user,
            category=validated_data['category'],
            type=validated_data['type'],
            metavalidation=metavalidation,
            quizz=quizz
        )
        try:
            challenge.save()
        except:
            raise ValidationError("Impossible de sauvegarder le challenge")
        return challenge

class ChallengeListSerializer(serializers.ModelSerializer):
    class Meta:
        model = Challenge
        fields = ('url', 'title', 'summary')

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
    submit = serializers.HyperlinkedIdentityField(view_name='challengeplayed-submit')

    class Meta:
        model = Challengeplayed
        fields = ('url', 'submit', 'challenge', 'user', 'score', 'validated', 'validationitem')

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

class ToValidateSerializer(ChallengePlayedListSerializer):
    validate = serializers.HyperlinkedIdentityField(view_name='challengeplayed-validate')
    unvalidate = serializers.HyperlinkedIdentityField(view_name='challengeplayed-unvalidate')

    class Meta:
        model = Challengeplayed
        fields = ('validate', 'unvalidate', 'challenge', 'validated')