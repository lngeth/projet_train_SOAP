from django import forms
from django.forms import SelectDateWidget

class TrainForm(forms.Form):
    # outbound date
    outboundDate = forms.DateField(
      label='Sélectionnez une date', widget=SelectDateWidget(empty_label=("Choisissez une année", "Choisissez un mois", "Choisissez un jour")))
    outboundTime = forms.ChoiceField(
        choices=[(str(i).zfill(2), str(i).zfill(2)) for i in range(25)],
        label='Sélectionnez une heure'
    )
    
    # return date
    returnDate = forms.DateField(
      label='Sélectionnez une date', widget=SelectDateWidget(empty_label=("Choisissez une année", "Choisissez un mois", "Choisissez un jour")))
    returnTime = forms.ChoiceField(
        choices=[(str(i).zfill(2), str(i).zfill(2)) for i in range(25)],
        label='Sélectionnez une heure'
    )
    
    nbTickets = forms.IntegerField()
    travelClass = forms.ChoiceField(
        choices=[("First", "Première"), 
                 ("Business", "Business"),
                 ("Standard", "Standard")],
        label='Sélectionnez le type de siège'
    )
