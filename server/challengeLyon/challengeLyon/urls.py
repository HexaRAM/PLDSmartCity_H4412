#-*- coding: utf-8 -*-

from django.conf.urls import include, url
from django.contrib import admin

from rest_framework import routers
from challengeLyon.views import ChallengeViewSet, UserViewSet


router = routers.DefaultRouter()
router.register(r'challenges', ChallengeViewSet)
router.register(r'users', UserViewSet)

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    #url(r'^api-token-auth/', views.obtain_auth_token),
    url(r'^auth/', include('djoser.urls')),
]