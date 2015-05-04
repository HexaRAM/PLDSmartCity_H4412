#-*- coding: utf-8 -*-

from django.contrib import admin
from challengeLyon.models import *
from django import forms
from django.contrib.auth.models import Group
from django.contrib.auth.admin import UserAdmin
from django.contrib.auth.forms import ReadOnlyPasswordHashField


admin.site.register(Category)
admin.site.register(Type)
admin.site.register(Quizz)
admin.site.register(Metavalidation)
admin.site.register(Challenge)
admin.site.register(Challengeplayed)
admin.site.register(Question)
admin.site.register(Answer)
admin.site.register(Location)
admin.site.register(Validationitem)
admin.site.register(Useranswer)
admin.site.register(Picture)
admin.site.register(Validation)


class UserCreationForm(forms.ModelForm):
    """A form for creating new users. Includes all the required
    fields, plus a repeated password."""
    password1 = forms.CharField(label='Password', widget=forms.PasswordInput)
    password2 = forms.CharField(label='Password confirmation', widget=forms.PasswordInput)

    class Meta:
        model = ChallengeUser
        fields = ('email',)

    def clean_password2(self):
        # Check that the two password entries match
        password1 = self.cleaned_data.get("password1")
        password2 = self.cleaned_data.get("password2")
        if password1 and password2 and password1 != password2:
            raise forms.ValidationError("Passwords don't match")
        return password2

    def save(self, commit=True):
        # Save the provided password in hashed format
        user = super(UserCreationForm, self).save(commit=False)
        user.set_password(self.cleaned_data["password1"])
        if commit:
            user.save()
        return user


class ChallengeUserAdmin(UserAdmin):
	# The forms to add and change user instances
	
	add_form = UserCreationForm

	# The fields to be used in displaying the User model.
	# These override the definitions on the base UserAdmin
	# that reference specific fields on auth.User.
	list_display = ('email','is_admin',)
	list_filter = ('is_admin',)
	fieldsets = (
	    (None, {'fields': ('email', 'password')}),
	    ('Permissions', {'fields': ('is_admin',)}),
	)
	# add_fieldsets is not a standard ModelAdmin attribute. UserAdmin
	# overrides get_fieldsets to use this attribute when creating a user.
	add_fieldsets = (
	    (None, {
	        'classes': ('wide',),
	        'fields': ('email','password1', 'password2')}
	    ),
	)
	search_fields = ('email',)
	ordering = ('email',)
	filter_horizontal = ()


admin.site.register(ChallengeUser, ChallengeUserAdmin)
admin.site.unregister(Group)
