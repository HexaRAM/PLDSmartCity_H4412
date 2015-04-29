#-*- coding: utf-8 -*-

from rest_framework import viewsets
from challengeLyon.models import *

class ChallengeViewSet(viewsets.ModelViewSet):
    queryset = Challenge.objects.all()
    serializer_class = ChallengeSerializer

class UserViewSet(viewsets.ModelViewSet):
    queryset = ChallengeUser.objects.all()
    serializer_class = UserSerializer