from django.shortcuts import render, redirect

from .forms import TrainForm

from zeep import Client
from zeep.plugins import HistoryPlugin
from lxml import etree
import json
from datetime import datetime

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
            outboundDateTime = convert_to_timestamp(form.cleaned_data['outboundDate'], form.cleaned_data['outboundTime'])
            returnDateTime = convert_to_timestamp(form.cleaned_data['returnDate'], form.cleaned_data['returnTime'])
            
            res = json.loads(appel_train_filtering_soap(idDeparture, idArrival, outboundDateTime, returnDateTime, nbTickets, travelClass))
            print("résultat requête :", res)
            print("conversion du résultat en json :", type(res))
            
            return render(request, "allTrain.html", {'res': res })
    else:
        allStations = json.loads(get_all_stations_soap())
        form = TrainForm()
    return render(request, 'searchTrain.html', {'form': form, 'allStations': allStations})
    
def convert_to_timestamp(date, time):
    dateTime = date.strftime("%Y-%m-%d")
    dateTime += " " + time + ":00:00"
    return (int) (datetime.strptime(dateTime, "%Y-%m-%d %H:%M:%S").timestamp() * 1000)

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