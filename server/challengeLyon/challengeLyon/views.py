#-*- coding: utf-8 -*-

from rest_framework import viewsets
from rest_framework import generics
from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import detail_route
from django.conf import settings
from challengeLyon.models import *

from django.db.models import Count

class LocationViewSet(viewsets.ModelViewSet):
    queryset = Location.objects.all()
    serializer_class = LocationSerializer

class ChallengeViewSet(viewsets.ModelViewSet):
    queryset = Challenge.objects.all()
    serializer_class = ChallengeSerializer

# TODO : remove this viewset (la validation se fait automatiquement quand un utilisateur vote sur un challenge)
# Sert d'exemple pour créer des vues custom
class ChallengePlayedViewSet(viewsets.ViewSet):
    """
        Validation d'un challenge joué
    """
    @detail_route(methods=['get'])
    def validate(self, request, pk=None):
        print pk
        if pk is not None:
            try:
                challengeplayed = Challengeplayed.objects.get(id=pk)
                challengeplayed.validate()
                challengeplayed.save()
                return Response({'status': 'challenge validated'})
            except:
                return Response("Wrong Challenge Played", status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response("Unknown id", status=status.HTTP_400_BAD_REQUEST)


class HotChallengeViewSet(generics.ListAPIView, viewsets.GenericViewSet):
    HOT_CHALLENGES_NUMBER = 10
    serializer_class = HotChallengeSerializer

    def get_queryset(self):
        #challengeplayed = Challengeplayed.objects.filter(validated = True)
        challenges = Challenge.objects.all().extra(select = {
            'num_played' : """
                SELECT COUNT(*) FROM challengeLyon_challengeplayed WHERE validated = 1 AND challengeLyon_challengeplayed.challenge_id = challengeLyon_challenge.id
            """
        }).order_by('-num_played')[:self.HOT_CHALLENGES_NUMBER]
        return challenges
        

class CategoryViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Category.objects.all()
    serializer_class = CategorySerializer

class UserViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = ChallengeUser.objects.all()
    serializer_class = UserSerializer

class PictureViewSet(viewsets.ModelViewSet):
    queryset = Picture.objects.all()
    serializer_class = PictureSerializer