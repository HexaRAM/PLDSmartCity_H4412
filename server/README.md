# Serveur WEB

## Install pip
```
$ sudo apt-get install python-pip

$ sudo apt-get update

```
## Install virtualenv and install the project
```
$ sudo pip install virtualenvwrapper

$ cd [your development directory]

$ virtualenv ./

$ ln -s bin/activate activate

$ source activate

$ git clone https://github.com/HexaRAM/PLDSmartCity_H4412.git

$ cd  cd PLDSmartCity_H4412/server/challengeLyon

$ pip install -r requirements.txt

```
To check the django version:
```
$ python

>>> import django

>>> print(django.get_version())

1.8

```

