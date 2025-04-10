lancer l'application et utiliser postman par exemple pour tester:

pour reserver un creneau
http://localhost:8080/api/orders  : POST
avec body json  suivant:
{
  "customerName": "Test",
  "deliveryMode": "DELIVERY",
  "timeSlotId": 1
}

pour afficher les creneaux avec un mode specifique: 
http://localhost:8080/api/timeslots?mode=DELIVERY  : GET


pour appeler une operation non blanquante:
http://localhost:8080/api/timeslots/nonblocked  : GET


pour afficher un creneau specifique:
http://localhost:8080/api/timeslots/timeslot?id=2 : GET


Proposer une API REST consommable via http pour interagir avec les services réalisés dans le MVP:
j'ai mis en place l'api restfull avec les TU


Implémenter les principes HATEOAS dans votre API REST:
j'ai mis en place un exemple concret de ce principe getAvailableTimeSlots du controlleur TimeSlotController


Utiliser une solution non-bloquante:
j'ai mis un exemple des appels non bloquants grace un webflux, voir getAvailableTimeSlotsNonBlocked du controlleur TimeSlotController

Documenter l'API REST:
j'ai mis en place la documentation de l'api grace à l'outil swagger
http://localhost:8080/swagger-ui/index.html: pour visualiser les endpoints


Persistence:
Proposer une solution de persistence des données:
j'ai mis en place une base de donnees integrée H2 pour persister les donnnees 
avec un remplissage des creneaux dans la table lors du demarrage de l'api grace au composant DataInitializer

localhost:8080/h2-console/ : pour visualiser les donnees

Proposer une solution de cache:
j'ai mis en place un exemple de cache au niveau du service TimeSlotServiceImpl methode: getTimeSlotById


Stream (Bonus) : 
pour des exigences technologiques tels que la presence obligatoire d'un broker pour tester application, j'ai opté pour la solution streaming des donnees.
au niveau du controlleur TimeSlotController la methode streamAvailableTimeSlots montre un exmple concret de streming,
il faut activer mode streaming dans le fichier de propriete : delivery.api.mode=streaming
