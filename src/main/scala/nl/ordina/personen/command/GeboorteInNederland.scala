package nl.ordina.personen.command

import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.Geboorte
import org.axonframework.commandhandling.TargetAggregateIdentifier

case class GeboorteInNederland(
                                @TargetAggregateIdentifier burgerservicenummer: Burgerservicenummer,
                                naam: SamengesteldeNaam,
                                geslacht: Geslachtsaanduiding = null,
                                geboorte: Geboorte,
                                bijhoudingspartij: Partij
                              )
