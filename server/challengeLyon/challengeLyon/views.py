#-*- coding: utf-8 -*-

from django.shortcuts import get_object_or_404

from rest_framework import viewsets
from rest_framework import generics
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.decorators import detail_route, api_view
from django.conf import settings
from challengeLyon.models import *
from challengeLyon.serializers import *

from django.db.models import Count
from django.utils import timezone





class ValidationViewSet(generics.CreateAPIView, viewsets.GenericViewSet):
    queryset = Validation.objects.all()
    serializer_class = ValidationSerializer

class ValidationItemViewSet(viewsets.ModelViewSet):
    queryset = Validationitem.objects.all()
    serializer_class = ValidationItemSerializer

class ChallengeViewSet(viewsets.ModelViewSet):
    queryset = Challenge.objects.all()
    serializer_class = ChallengeSerializer

    @detail_route(methods=['get'])
    def play(self, request, pk=None):
        if pk is not None:
            try:
                challenge = Challenge.objects.get(id=pk)

                score = challenge.category.reward

                #print u"Vous jouez le challenge %s pour %s point(s)."%(challenge, score)

                if challenge.starttime is not None:
                    if challenge.starttime > timezone.now():
                        return Response({'status':"Ce challenge n'est pas encore actif. Date de début : %s"%challenge.starttime}, status=status.HTTP_400_BAD_REQUEST)

                if challenge.endtime is not None:
                    if challenge.endtime < timezone.now():
                        return Response({'status':"Ce challenge n'est plus actif depuis le %s"%challenge.endtime}, status=status.HTTP_400_BAD_REQUEST)

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
        try:
            challengeplayed = Challengeplayed.objects.filter(user=request.user).get(pk=pk)
        except:
            return Response({'status':'Challenge inconnu (ou pas le vôtre :p)'}, status=status.HTTP_400_BAD_REQUEST)
        serializer = ChallengePlayedSerializer(challengeplayed, read_only=True, context={'request': request})
        return Response(serializer.data)

    @detail_route(methods=['get'])
    def submit(self, request, pk=None):
        if pk is not None:
            try:
                challengeplayed = Challengeplayed.objects.filter(user=request.user).get(id=pk)
            except:
                return Response({'status':'Challenge inconnu (ou pas le vôtre :p)'}, status=status.HTTP_400_BAD_REQUEST)
            challengeplayed.validationitem.submitted = True
            try:
                challengeplayed.validationitem.save()
            except:
                return Response({'status':'Déjà existant'}, status=status.HTTP_400_BAD_REQUEST)
            return Response({'status': 'Soumis à validation !'})
        else:
            return Response({'status':'ID inconnu'}, status=status.HTTP_400_BAD_REQUEST)

    # TODO : add validation when votes reach a score above 10
    @detail_route(methods=['GET'])        
    def validate(self, request, pk=None):
        if pk is not None:
            try:
                challengeplayed = Challengeplayed.objects.exclude(user=request.user).get(id=pk)
            except:
                return Response({'status':'Challenge inconnu (ou le vôtre :p)'}, status=status.HTTP_400_BAD_REQUEST)
            if not challengeplayed.toValidate():
                return Response({'status':'This challenge doesn\'t need to be voted'}, status=status.HTTP_400_BAD_REQUEST)
            validation = Validation(user=request.user, validationitem=challengeplayed.validationitem, vote=1)
            try:
                validation.save()
            except:
                return Response({'status':'Déjà voté'}, status=status.HTTP_400_BAD_REQUEST)
            score = challengeplayed.validationitem.get_score()
            if score > Challengeplayed.LIMIT_TO_VALIDATE:
                # winner
                challengeplayed.validate()
                challengeplayed.save()
                user = challengeplayed.user
                user.ranking += challengeplayed.score
                user.save()
            return Response({'status': 'Vote enregistré !'})
        return Response({'status':'ID inconnu'}, status=status.HTTP_400_BAD_REQUEST)

    @detail_route(methods=['GET'])
    def unvalidate(self, request, pk=None):
        if pk is not None:
            try:
                challengeplayed = Challengeplayed.objects.exclude(user=request.user).get(id=pk)
            except:
                return Response({'status':'Challenge inconnu (ou le vôtre :p)'}, status=status.HTTP_400_BAD_REQUEST)
            if not challengeplayed.toValidate():
                return Response({'status':'This challenge doesn\'t need to be voted.'}, status=status.HTTP_400_BAD_REQUEST)
            validation = Validation(user=request.user, validationitem=challengeplayed.validationitem, vote=-1)
            try:
                validation.save()
            except:
                return Response({'status':'Déjà voté'}, status=status.HTTP_400_BAD_REQUEST)
            return Response({'status': 'Vote enregistré !'})
        return Response({'status':'ID inconnu'}, status=status.HTTP_400_BAD_REQUEST)

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


class ToValidateViewSet(viewsets.ReadOnlyModelViewSet):
    serializer_class = ToValidateSerializer

    def get_queryset(self):
        context = self.get_serializer_context()
        request = context['request']
        user = request.user

        challengesplayed = Challengeplayed.objects.filter(validationitem__submitted= True).filter(validated = False).exclude(user = user).exclude(validationitem__users = user)
        return challengesplayed

# Own Views

@api_view(['GET'])
def getClosestStation(request):
    location = None
    try:
        latitude = request.query_params['latitude']
        longitude = request.query_params['longitude']
        location = Location(latitude, longitude)
    except:
        return Response({"status": "Impossible de créer la localisation à partir des paramètres reçus."}, status=status.HTTP_400_BAD_REQUEST)
    station = None
    try:
        arrivee = False
        try:
            request.query_params['arrivee']
            arrivee = True
        except:
            pass

        station = location.getClosestStation(arrivee)
    except:
        return Response({"status": "Impossible d'obtenir la station la plus proche"}, status=status.HTTP_400_BAD_REQUEST)
    if station is None:
        return Response({"status": "Aucune station plus proche"}, status=status.HTTP_400_BAD_REQUEST)
    return Response({"status":"ok", "result": station.serialize()})