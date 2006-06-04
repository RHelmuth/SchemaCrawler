/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2006, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package schemacrawler.tools.operation;


import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Database operations.
 */
public final class Operation
  implements Serializable
{

  private static final long serialVersionUID = -5097434654628745480L;

  private static final Operation COUNT = new Operation("COUNT", "Row Count",
      "SELECT COUNT(*) FROM ${table}",
      "{0,choice,0#empty|0<{0,number,integer} rows}");

  private static final Operation DROP = new Operation("DROP", "Drop Table",
      "DROP ${tabletype} ${table}", "dropped");

  private static final Operation TRUNCATE = new Operation("TRUNCATE",
      "Truncate Table", "DELETE FROM ${table}",
      "truncated; {0,choice,0#was already empty|0<had {0,number,integer} rows}");

  private static final Operation DUMP = new Operation("DUMP", "Dump",
      "SELECT ${columns} FROM ${table} ORDER BY ${columns}", "");

  private static final Operation QUERYOVER = new Operation("QUERYOVER", "",
      "Query Over Table", "{0,choice,0#-|0<{0,number,integer}}");

  private static final Operation[] OPERATION_ALL = new Operation[] {
    COUNT, DROP, TRUNCATE, DUMP, QUERYOVER
  };

  private final transient String operation;
  private final transient String operationDescription;
  private final transient String query;
  private final transient String countMessageFormat;

  private Operation(final String name, final String description,
                    final String query, final String countMessageFormat)
  {
    ordinal = nextOrdinal++;
    this.operation = name;
    this.operationDescription = description;
    this.query = query;
    this.countMessageFormat = countMessageFormat;
  }

  private String getOperation()
  {
    return operation;
  }

  public String getOperationDescription()
  {
    return operationDescription;
  }

  public String getQuery()
  {
    return query;
  }

  public String getCountMessageFormat()
  {
    return countMessageFormat;
  }

  public boolean isSelectOperation()
  {
    return this == QUERYOVER || this == DUMP;
  }

  public boolean isQueryOver()
  {
    return this.getOperation().equals(QUERYOVER.getOperation());
  }

  public boolean isAggregateOperation()
  {
    return this == COUNT;
  }

  /**
   * Gets the enumeration value for the query over operation.
   * 
   * @return Query over operation
   */
  public static Operation queryOverOperation()
  {
    return QUERYOVER;
  }

  /**
   * {@inheritDoc}
   * 
   * @see Object#toString()
   */
  public String toString()
  {
    return operation;
  }

  /**
   * Find the enumeration value corresponding to the string.
   * 
   * @param operationString
   *          String value of table type
   * @return Enumeration value
   */
  public static Operation valueOf(final String operationString)
  {

    Operation operation = null;

    for (int i = 0; i < OPERATION_ALL.length; i++)
    {
      if (OPERATION_ALL[i].toString().equalsIgnoreCase(operationString))
      {
        operation = OPERATION_ALL[i];
        break;
      }
    }

    return operation;

  }

  // The 4 declarations below are necessary for serialization
  private static int nextOrdinal;
  private final int ordinal;

  private static final Operation[] VALUES = OPERATION_ALL;

  Object readResolve()
    throws ObjectStreamException
  {
    return VALUES[ordinal]; // Canonicalize
  }

}
