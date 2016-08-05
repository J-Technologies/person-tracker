package nl.ordina.personen.handlers.json

import javax.persistence.{Basic, Entity, Id}

import scala.annotation.meta.field

@Entity
case class PersoonEntry(@(Id@field) bsn: String, @(Basic@field) naam: String, @(Basic@field) isOverleden: Boolean)
