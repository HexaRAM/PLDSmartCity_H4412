#-*- coding: utf-8 -*-

from django.shortcuts import get_object_or_404

from rest_framework import viewsets
from rest_framework import generics
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.decorators import detail_route
from django.conf import settings
from challengeLyon.models import *
from challengeLyon.serializers import *

from django.db.models import Count

class ValidationViewSet(generics.CreateAPIView, viewsets.GenericViewSet):
    queryset = Validation.objects.all()
    serializer_class = ValidationSerializer

class ValidationItemViewSet(viewsets.ModelViewSet):
    queryset = Validationitem.objects.all()
    serializer_class = ValidationItemSerializer

    # TODO : TO REMOVE
    # @detail_route(methods=['GET','POST'])
    # def addPicture(self, request, pk):
    #     if request.method == "GET":
    #         queryset = Picture.objects.filter(validationitem__challengeplayed__id=pk)
    #         seria = PictureSerializer(queryset, context={'request': request}, many=True)
    #         self.queryset = queryset
    #         return Response(seria.data)

    #     if request.method == "POST":
    #         validationitem = self.get_object()
    #         serializer = PictureSerializer(data=request.DATA, files=request.FILES)
    #         if serializer.is_valid():
    #             picture = serializer.save()
    #             validationitem.pictures.add(picture)
    #             return Response({'status': 'picture uploaded'})
    #         else:
    #             return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class ChallengeViewSet(viewsets.ModelViewSet):
    queryset = Challenge.objects.all()
    serializer_class = ChallengeSerializer

    @detail_route(methods=['get'])
    def play(self, request, pk=None):
        if pk is not None:
            try:
                score = 0
                if request.data.get('score') is not None:
                    score = request.data.get('score')

                challenge = Challenge.objects.get(id=pk)
                challengeplayed = Challengeplayed(challenge=challenge, user=request.user, score=score)
                challengeplayed.save()
                return Response({'status': 'Good Luck !'})
            except:
                return Response({'status':"Current challenge already played [maybe]"}, status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response("Unknown id", status=status.HTTP_400_BAD_REQUEST)

class ChallengePlayedViewSet(viewsets.ViewSet):
    def list(self, request):
        query = Challengeplayed.objects.filter(user=request.user)
        serializer = ChallengePlayedListSerializer(query, many=True, read_only=True, context={'request': request})
        return Response(serializer.data)

    def retrieve(self, request, pk=None):
        query = Challengeplayed.objects.all()
        challengeplayed = get_object_or_404(query, pk=pk)
        serializer = ChallengePlayedSerializer(challengeplayed, read_only=True, context={'request': request})
        return Response(serializer.data)

    @detail_route(methods=['get'])
    def submit(self, request, pk=None):
        if pk is not None:
            try:
                challengeplayed = Challengeplayed.objects.get(id=pk)
                challengeplayed.validationitem.submitted = True
                challengeplayed.validationitem.save()
                return Response({'status': 'Soumis à validation !'})
            except:
                return Response({'status':'Unknown challenge'}, status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response({'status':'Unknown id'}, status=status.HTTP_400_BAD_REQUEST)

    # @detail_route(methods=['get'])
    # def validate(self, request, pk=None):
    #     if pk is not None:
    #         try:
    #             challengeplayed = Challengeplayed.objects.get(id=pk)
    #             challengeplayed.validate()
    #             challengeplayed.save()
    #             return Response({'status': 'challenge validated'})
    #         except:
    #             return Response("Wrong Challenge Played", status=status.HTTP_400_BAD_REQUEST)
    #     else:
    #         return Response("Unknown id", status=status.HTTP_400_BAD_REQUEST)

    @detail_route(methods=['GET', 'POST'])
    def picture(self, request, pk):
        if request.method == "GET":
            pictures = Picture.objects.filter(validationitem__challengeplayed__id=pk)
            seria = PictureSerializer(pictures, context={'request': request}, many=True)
            return Response(seria.data)

        if request.method == "POST":
            serializer = PictureSerializer(data=request.DATA, files=request.FILES)
            if serializer.is_valid():
                serializer.save()
                # TODO : add link between picture & validationitem
                return Response({'status': 'picture uploaded'})
            else:
                return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


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

class PictureChallengePlayedViewSet(viewsets.ModelViewSet):
    queryset = PictureChallengePlayed.objects.all()
    serializer_class = PictureChallengePlayedSerializer


class ToValidateViewSet(viewsets.ModelViewSet):
    serializer_class = ToValidateSerializer

    def get_queryset(self):
        challengesplayed = Challengeplayed.objects.filter(validationitem.submitted == True).filter(validated == False).filter(user != self.request.get('user'))
        return challengesplayed