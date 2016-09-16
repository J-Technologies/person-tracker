package nl.ordina.personen.handlers.json

import com.datastax.driver.mapping.annotations.{Column, PartitionKey, Table}

import scala.annotation.meta.field

@Table(name = "PersoonEntry", caseSensitiveTable = true)
case class PersoonEntry(@(Column@field)(caseSensitive = true)
@(PartitionKey@field)
bsn: String,
  @(Column@field)(caseSensitive = true)
  naam: String,
  @(Column@field)(caseSensitive = true)
  overleden: Boolean
) {
  // needed by the datastax mapper
  def this() = this(null, null, false)
}
