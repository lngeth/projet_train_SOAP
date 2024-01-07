from django.urls import path

from .views import index, searchTrain

urlpatterns = [
    path('', index, name="index"),
    path('search', searchTrain, name="searchTrain"),
]