#-*- coding: utf-8 -*-

from rest_framework import viewsets
from rest_framework import generics
from django.conf import settings
from challengeLyon.models import *

from django.db.models import Count

class LocationViewSet(viewsets.ModelViewSet):
    queryset = Location.objects.all()
    serializer_class = LocationSerializer

class ChallengeViewSet(viewsets.ModelViewSet):
    queryset = Challenge.objects.all()
    serializer_class = ChallengeSerializer

# TODO
class HotChallengeViewSet(generics.ListAPIView, viewsets.GenericViewSet):
    HOT_CHALLENGES_NUMBER = 10
    serializer_class = HotChallengeSerializer

    def get_queryset(self):
        validationItems = Validation.objects.all().annotate(countValidations=Count('validationitem')).order_by('-countValidations')[:self.HOT_CHALLENGES_NUMBER].values('validationitem')
        hot = []
        for validationItem in validationItems:
            hot.append(validationItem.challengeplayed.challenge)
        return hot

        

class CategoryViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Category.objects.all()
    serializer_class = CategorySerializer

class UserViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = ChallengeUser.objects.all()
    serializer_class = UserSerializer

class PictureViewSet(viewsets.ModelViewSet):
    queryset = Picture.objects.all()
    serializer_class = PictureSerializer