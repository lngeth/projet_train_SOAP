from django.urls import path

from .views import index, searchTrain, reserveTrip, comfirmReservation, login, register, logout

urlpatterns = [
    path('', index, name="index"),
    path('search', searchTrain, name="searchTrain"),
    path('reservation', reserveTrip, name="reserveTrip"),
    path('reservation/send', comfirmReservation, name="comfirmReservation"),
    path('login', login, name="login"),
    path('register', register, name="register"),
    path('logout', logout, name="logout"),
]