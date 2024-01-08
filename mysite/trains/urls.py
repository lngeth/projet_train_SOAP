from django.urls import path

from .views import index, searchTrain, reserveTrip, comfirmReservation

urlpatterns = [
    path('', index, name="index"),
    path('search', searchTrain, name="searchTrain"),
    path('reservation', reserveTrip, name="reserveTrip"),
    path('reservation/send', comfirmReservation, name="comfirmReservation"),
]