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

def login(request):
    if request.method == "POST":
        name = request.POST.get('name')
        
        res = appel_get_client_id_by_name_soap(name)
        if (res == "No client available" or res == "" or res is None):
            message = "Introuvable dans la BD"
            return render(request, 'login.html', { 'message': message })
        else:
            idClient = json.loads(res)
            request.session['idClient'] = idClient
            
        return redirect('index')
    else:
        return render(request, 'login.html')

def register(request):
    if request.method == "POST":
        name = request.POST.get('name')
        
        res = appel_create_client_soap(name)
        if (res == "No client created" or name == ""):
            message = "Nom incorrect"
        else:
            success = json.loads(res)
            if (success):
                message = "Création réussie !"
                return render(request, 'login.html', {'message': message})
            else:
                message = "Utilisateur déjà existant"
        return render(request, 'register.html', {'message': message})
    else:
        return render(request, 'register.html')

def logout(request):
    request.session.flush()
    
    return redirect('index')

def searchTrain(request):
    if (request.session.get('idClient') is None):
        return render(request, 'login.html', {'message': "Vous devez d'abord vous connectez avant de rechercher des trains !"})
    
    if request.method == "POST":
        form = TrainForm(request.POST)
        if form.is_valid():
            idDeparture = request.POST.get('departure')
            idArrival = request.POST.get('arrival')
            nbTickets = form.cleaned_data['nbTickets']
            travelClass = form.cleaned_data['travelClass']
            outboundTS, outboundDateTime = convert_to_timestamp(form.cleaned_data['outboundDate'], form.cleaned_data['outboundTime'])
            returnTS, returnDateTime = convert_to_timestamp(form.cleaned_data['returnDate'], form.cleaned_data['returnTime'])

            prixBilletOutbound = 0
            prixBilletReturn = 0
            outboundTrains = []
            returnTrains = []
            errorMessage = ""
            
            res = appel_train_filtering_soap(idDeparture, idArrival, outboundTS, returnTS, nbTickets, travelClass)
            if (res != "No available train"):
                res = json.loads(res)
                for t in res:
                    if (is_in_the_day(outboundDateTime, t["dateDepart"])):
                        outboundTrains.append(t)
                        if (prixBilletOutbound == 0):
                            if (travelClass == "First"):
                                prixBilletOutbound = t['prixFirst']
                            elif (travelClass == "Standard"):
                                prixBilletOutbound = t['prixStandard']
                            else:
                                prixBilletOutbound = t['prixPremium']
                    else:
                        returnTrains.append(t)
                        if (prixBilletReturn == 0):
                            if (travelClass == "First"):
                                prixBilletReturn = t['prixFirst']
                            elif (travelClass == "Standard"):
                                prixBilletReturn = t['prixStandard']
                            else:
                                prixBilletReturn = t['prixPremium']
            else:
                errorMessage = "No available train"
                
            print("résultat requête :", res)
            print("conversion du résultat en json :", type(res))
            
            return render(request, "allTrain.html", {'errorMessage': errorMessage, 'outboundTrains': outboundTrains, 'returnTrains': returnTrains, 'travelClass': travelClass, 'nbTickets': nbTickets, 'prixBilletOutbound': prixBilletOutbound, 'prixBilletReturn': prixBilletReturn })
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
    if (request.session.get('idClient') is None):
        return render(request, 'login.html', {'message': "Vous devez d'abord vous connectez avant de rechercher des trains !"})
    
    if request.method == "POST":
        outboundVoyage = json.loads(request.POST.get('outboundVoyage'))

        if (request.POST.get('returnVoyage') == 'None' or request.POST.get('returnVoyage') is None):
            returnVoyage = ""
        else:
            returnVoyage = json.loads(request.POST.get('returnVoyage'))
        
        nbTickets = int(request.POST.get('nbTickets'))
        return render(request, 'reservation.html', {'outboundVoyage': outboundVoyage, 'returnVoyage': returnVoyage, 'nbTickets': nbTickets })
    else:
        return redirect('index')

def comfirmReservation(request):
    if (request.session.get('idClient') is None):
        return render(request, 'login.html', {'message': "Vous devez d'abord vous connectez avant de rechercher des trains !"})
    
    if request.method == "POST":
        idClient = request.session.get('idClient')
        
        outboundVoyageValidated = json.loads(request.POST.get('outboundVoyageValidated'))
        idClient = [idClient]
        flex = [outboundVoyageValidated['flex']]
        travelClass = [outboundVoyageValidated['travelClass']]
        idVoyage = [outboundVoyageValidated['idVoyage']]
        
        returnVoyageValidated = 0
        if (request.POST.get('returnVoyageValidated') is not None):
            returnVoyageValidated = json.loads(request.POST.get('returnVoyageValidated'))
            idClient.append(idClient)
            flex.append(returnVoyageValidated['flex'])
            travelClass.append(returnVoyageValidated['travelClass'])
            idVoyage.append(returnVoyageValidated['idVoyage'])
        print("outbound flex:", outboundVoyageValidated)
        print("return flex:", returnVoyageValidated)
        
        res = appel_reservation_train_soap(idClient, flex, travelClass, idVoyage, outboundVoyageValidated['nbTickets'])
        return render(request, 'recapReservation.html', {'statusReservation': res})
    else:
        return redirect(request, 'index')
    
def appel_reservation_train_soap(idClient, flex, travelClass, idVoyage, nbTickets):
    wsdl_url = 'http://localhost:8080/SOAPTrainBooking/services/TrainReservation?wsdl'

    history = HistoryPlugin()
    # Création d'un client SOAP
    client = Client(wsdl_url, plugins=[history])
    
    # Appel de la méthode du service
    result = client.service.reserveTrain(idClient=idClient,
                                      flex=flex,
                                      travelClass=travelClass,
                                      idVoyage=idVoyage,
                                      nbTickets=nbTickets)
    
    xml_soap = etree.tostring(history.last_received["envelope"], encoding="unicode", pretty_print=True)
    print(xml_soap)
    return result

def appel_get_client_id_by_name_soap(name):
    wsdl_url = 'http://localhost:8080/SOAPTrainBooking/services/Client?wsdl'

    history = HistoryPlugin()
    
    client = Client(wsdl_url, plugins=[history])
    
    result = client.service.getClientIdByName(name=name)
    
    xml_soap = etree.tostring(history.last_received["envelope"], encoding="unicode", pretty_print=True)
    print(xml_soap)
    return result

def appel_create_client_soap(name):
    wsdl_url = 'http://localhost:8080/SOAPTrainBooking/services/Client?wsdl'

    history = HistoryPlugin()
    
    client = Client(wsdl_url, plugins=[history])
    
    result = client.service.createClient(name=name)
    
    xml_soap = etree.tostring(history.last_received["envelope"], encoding="unicode", pretty_print=True)
    print(xml_soap)
    return result