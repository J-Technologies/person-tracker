package nl.ordina.personen.handlers.json

import java.util

import com.datastax.driver.mapping.annotations.{Column, PartitionKey, Table}

import scala.annotation.meta.field

@Table(name = "PersoonEntry", caseSensitiveTable = true)
case class PersoonEntry(
  @(Column@field)(caseSensitive = true)
  @(PartitionKey@field)
  var bsn: String,
  @(Column@field)(caseSensitive = true)
  var naam: String,
  @(Column@field)(caseSensitive = true)
  var geslacht: String,
  @(Column@field)(caseSensitive = true)
  var geboortedatum: String,
  @(Column@field)(caseSensitive = true)
  var ouders: util.List[String],
  @(Column@field)(caseSensitive = true)
  var burgelijkeStaat: String,
  @(Column@field)(caseSensitive = true)
  var partner: String,
  @(Column@field)(caseSensitive = true)
  var overleden: Boolean
) {
  // needed by the datastax mapper
  def this() = this(null, null, null, null, null, null, null, false)
}
