package nl.ordina.personen.handlers.json

import javax.persistence.{Basic, Entity, Id}

import scala.annotation.meta.field

/**
  * Created by gle21221 on 5-8-2016.
  */
@Entity
case class PersoonEntry(@(Id@field) bsn: String, @(Basic@field) isOverleden: Boolean) {

}
