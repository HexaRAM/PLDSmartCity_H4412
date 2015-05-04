#-*- coding: utf-8 -*-

from django.conf.urls import include, url, patterns
from django.contrib import admin
from django.conf import settings

from rest_framework import routers
from challengeLyon.views import *

# TODO : changer tous les services qui contiennent un attribut users (overload la méthode perform_create et attribuer users avec request.user)

router = routers.DefaultRouter()
router.register(r'challenges', ChallengeViewSet)
router.register(r'users', UserViewSet) # TODO : isAdminOnly
router.register(r'pictures', PictureViewSet)
router.register(r'categories', CategoryViewSet)
router.register(r'hot', HotChallengeViewSet, base_name="hot")
router.register(r'challengePlayed', ChallengePlayedViewSet, base_name="challengeplayed")

router.register(r'validationItem', ValidationItemViewSet)

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