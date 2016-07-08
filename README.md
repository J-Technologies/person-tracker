# Person - tracker

Een Basisregistratie Personen.

De applicatie implementeert een basisregistratie voor personen, hun belangrijke
onderlinge relaties en hun woonadressen. In grote lijnen volgt het de
specificatie van de BRP, zij het met beperkte scope.

De Basisregistratie Personenis gekozen omdat veel van de specificaties openbaar
zijn en online vindbaar zijn.

De implementatie is gebaseerd op CQRS en Event Sourcing, met behulp van het
[Axon framework](http://www.axonframework.com).


## Doel van het speerpunt

Het doel van dit speerpunt is te laten zien dat CQRS/Event Sourcing een bij
uitstek geschikt architectuurpatroon is voor een basisregistratie. Event
Sourcing maakt het makkelijk om

- alle wijzigingen die hebben geleid tot de huidige toestand te bewaren
- de toestand op een willekeurig tijdstip in het verleden te reconstrueren
- het verleden te corrigeren op basis van nieuwe informatie, en
- gegevens over personen vanuit verschillende gezichtspunten anders weer te geven.

Een alternatief voor het representeren van historie in een registratie is een
[Temporal Database](https://en.wikipedia.org/wiki/Temporal_database). Het
programmeren tegen een temporal database is echter veel moeilijker en is in de
praktijk alleen realiseerbaar door gebruik te maken van code generatie vanuit
een formeel model, wat weer zijn eigen complexiteit met zich meebrengt.

## Te realiseren functionaliteit

Behalve een back end voor de eigenlijke registratie realiseren we:

- Een web interface om mutaties in te voeren en informatie op te vragen
- Het opvragen realiseren we in drie varianten:
    - De huidige toestand op het moment van opvragen
    - Alle gebeurtenissen die tot de huidige toestand hebben geleid
    - De toestand op een willekeurig tijdstip in het verleden.


## Technology stack

De person tracker gebruikt de volgende technology stack.

### Front End

- EcmaScript 6 translated to EcmaScript 3 for execution
- React

### Back End:

- Scala
- Axon
- JavaDB (korte termijn), PostgreSQL (mogelijk later)
- JPA voor de Event Store en Query Stores
- Guice voor Dependency Injection


Openbaar beschikbare documentatie:

- [Stelselcatalogus](http://www.digitaleoverheid.nl/onderwerpen/stelselinformatiepunt/stelsel-van-basisregistraties/stelselvoorzieningen/stelselcatalogus/authentieke-gegevens)
- [Logisch ontwerp BRP](http://www.operatiebrp.nl/afnemers/logisch-ontwerp-brp)
- [Logisch Ontwerp BRP Bijlage A, 30-07-2015.pdf](http://www.operatiebrp.nl/operatie-brp/nieuws/bijlage-gegevenswoordenboek-logisch-ontwerp-brp-gepubliceerd)

![](http://vignette1.wikia.nocookie.net/dragons-crown/images/b/bf/Orc.png/revision/latest?cb=20140311062419)
