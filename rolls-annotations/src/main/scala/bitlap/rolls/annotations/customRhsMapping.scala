package bitlap.rolls.annotations

import scala.annotation.ConstantAnnotation

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
final case class customRhsMapping(
  idColumn: String,
  nameColumns: String,
  tableName: String
) extends ConstantAnnotation
