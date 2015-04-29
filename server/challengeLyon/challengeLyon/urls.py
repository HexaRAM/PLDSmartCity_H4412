#-*- coding: utf-8 -*-

from django.conf.urls import include, url
from django.contrib import admin

from django.views.generic.base import RedirectView

urlpatterns = [
    # Examples:
    # url(r'^$', 'challengeLyon.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^$', RedirectView.as_view(url='admin/', permanent=True)),
    url(r'^admin/', include(admin.site.urls)),
]
