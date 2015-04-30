#-*- coding: utf-8 -*-

from rest_framework import viewsets
from django.conf import settings
from challengeLyon.models import *

class ChallengeViewSet(viewsets.ModelViewSet):
    queryset = Challenge.objects.all()
    serializer_class = ChallengeSerializer

class UserViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = ChallengeUser.objects.all()
    serializer_class = UserSerializer

class PictureViewSet(viewsets.ModelViewSet):
    queryset = Picture.objects.all()
    serializer_class = PictureSerializer