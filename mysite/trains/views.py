from django.shortcuts import render, redirect

from .forms import TrainForm

from zeep import Client
from zeep.plugins import HistoryPlugin
from lxml import etree
import json
from datetime import datetime, timedelta

# Create your views here.
def index(request):
    return render(request, 'index.html')

def searchTrain(request):
    # if this is a POST request we need to process the form data
    if request.method == "POST":
        # create a form instance and populate it with data from the request:
        form = TrainForm(request.POST)
        # check whether it's valid:
        if form.is_valid():
            idDeparture = request.POST.get('departure')
            idArrival = request.POST.get('arrival')
            nbTickets = form.cleaned_data['nbTickets']
            travelClass = form.cleaned_data['travelClass']
            outboundTS, outboundDateTime = convert_to_timestamp(form.cleaned_data['outboundDate'], form.cleaned_data['outboundTime'])
            returnTS, returnDateTime = convert_to_timestamp(form.cleaned_data['returnDate'], form.cleaned_data['returnTime'])

            res = json.loads(appel_train_filtering_soap(idDeparture, idArrival, outboundTS, returnTS, nbTickets, travelClass))
            print("résultat requête :", res)
            print("conversion du résultat en json :", type(res))
            
            outboundTrains = []
            returnTrains = []
            errorMessage = ""
            for t in res:
                if ("error" in t):
                    errorMessage = t['error']
                    break
                if (is_in_the_day(outboundDateTime, t["dateDepart"])):
                    outboundTrains.append(t)
                else:
                    returnTrains.append(t)
            
            return render(request, "allTrain.html", {'errorMessage': errorMessage, 'outboundTrains': outboundTrains, 'returnTrains': returnTrains, 'travelClass': travelClass, 'nbTickets': nbTickets })
    else:
        allStations = json.loads(get_all_stations_soap())
        form = TrainForm()
    return render(request, 'searchTrain.html', {'form': form, 'allStations': allStations})
    
def convert_to_timestamp(date, time):
    dateTime = date.strftime("%Y-%m-%d")
    dateTime += " " + time + ":00:00"
    return ((int) (datetime.strptime(dateTime, "%Y-%m-%d %H:%M:%S").timestamp() * 1000), dateTime)

def is_in_the_day(startDatetime, datetimeToCheck):
    dateStart = datetime.strptime(startDatetime, '%Y-%m-%d %H:%M:%S')
    endOfDay = datetime(dateStart.year, dateStart.month, dateStart.day, 23, 59, 59)
    dateToCheck = datetime.strptime(datetimeToCheck, '%Y-%m-%d %H:%M:%S')
    if dateStart <= dateToCheck <= endOfDay:
        return True
    return False

def appel_train_filtering_soap(idDeparture, idArrival, outboundDateTime, returnDateTime, nbTickets, travelClass):
    # URL du service WSDL
    wsdl_url = 'http://localhost:8080/SOAPTrainBooking/services/TrainSearch?wsdl'

    history = HistoryPlugin()
    # Création d'un client SOAP
    client = Client(wsdl_url, plugins=[history])
    
    # Appel de la méthode du service
    result = client.service.searchByAllCriterias(idDeparture=idDeparture,
                                      idArrival=idArrival,
                                      outboundDateTime=outboundDateTime,
                                      returnDateTime=returnDateTime,
                                      nbTickets=nbTickets,
                                      travelClass=travelClass)
    
    xml_soap = etree.tostring(history.last_received["envelope"], encoding="unicode", pretty_print=True)
    print(xml_soap)
    return result

def get_all_stations_soap():
    wsdl_url = 'http://localhost:8080/SOAPTrainBooking/services/StationSearch?wsdl'

    history = HistoryPlugin()
    # Création d'un client SOAP
    client = Client(wsdl_url, plugins=[history])
    
    # Appel de la méthode du service
    result = client.service.getAllStations()
    
    xml_soap = etree.tostring(history.last_received["envelope"], encoding="unicode", pretty_print=True)
    print(xml_soap)
    return result

def reserveTrip(request):
    if request.method == "POST":
        print("icicicii", request.POST.get('outboundVoyage'))
        outboundVoyage = json.loads(request.POST.get('outboundVoyage'))
        print("outbbounnd :", outboundVoyage)
        if (request.POST.get('returnVoyage') == 'None' or request.POST.get('returnVoyage') is None):
            returnVoyage = ""
        else:
            returnVoyage = json.loads(request.POST.get('returnVoyage'))
        
        print("retourVoyage :", returnVoyage)
        
        nbTickets = int(request.POST.get('nbTickets'))
        return render(request, 'reservation.html', {'outboundVoyage': outboundVoyage, 'returnVoyage': returnVoyage, 'nbTickets': nbTickets })
    else:
        return redirect('index')

def comfirmReservation(request):
    if request.method == "POST":
        outboundVoyageValidated = json.loads(request.POST.get('outboundVoyageValidated'))
        returnVoyageValidated = 0
        if (request.POST.get('returnVoyageValidated') is not None):
            returnVoyageValidated = json.loads(request.POST.get('returnVoyageValidated'))
        print("outbound flex:", outboundVoyageValidated)
        print("return flex:", returnVoyageValidated)
        
        return render(request, 'success.html', {})
    else:
        return redirect(request, 'index')