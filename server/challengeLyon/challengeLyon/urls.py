#-*- coding: utf-8 -*-

from django.conf.urls import include, url, patterns
from django.contrib import admin
from django.conf import settings

from rest_framework import routers
from challengeLyon.views import *

# TODO : changer tous les services qui contiennent un attribut users (overload la méthode perform_create et attribuer users avec request.user)

# TODO : add validation when votes reach a score above 10

# TODO : cron qui tourne tous les soirs pour supprimer les fichiers images qui ne sont plus liés dans la DB

router = routers.DefaultRouter()
router.register(r'challenges', ChallengeViewSet)
router.register(r'users', UserViewSet) # TODO : isAdminOnly
router.register(r'categories', CategoryViewSet)
router.register(r'hot', HotChallengeViewSet, base_name="hot")
router.register(r'toValidate', ToValidateViewSet, base_name="tovalidate")
router.register(r'challengePlayed', ChallengePlayedViewSet, base_name="challengeplayed")
router.register(r'validationItem', ValidationItemViewSet)
#router.register(r'validation', ValidationViewSet)
router.register(r'picturesChallengePlayed', PictureChallengePlayedViewSet)

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^auth/', include('djoser.urls')),
]

if settings.DEBUG:
    # static files (images, css, javascript, etc.)
    urlpatterns += patterns('',
        (r'^media/(?P<path>.*)$', 'django.views.static.serve', {
        'document_root': settings.MEDIA_ROOT}))