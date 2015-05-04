#!/bin/bash

echo "python manage.py dumpdata > dump"

# drop database
mysql -uroot -p < drop.sql
mysql -uroot -p < create.sql
rm -rf challengeLyon/challengeLyon/migrations/*
echo "python manage.py makemigrations challengeLyon"
echo "python manage.py migrate"
echo "python manage.py loaddata dump"
