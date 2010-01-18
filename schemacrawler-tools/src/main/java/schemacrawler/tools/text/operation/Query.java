/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2010, Sualeh Fatehi.
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

package schemacrawler.tools.text.operation;


import schemacrawler.crawl.JavaSqlType.JavaSqlTypeGroup;
import schemacrawler.crawl.JavaSqlTypesUtility;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import sf.util.TemplatingUtility;
import sf.util.Utility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A SQL query. May be parameterized with ant-like variable references.
 *
 * @author sfatehi
 */
public final class Query
  implements Serializable {

  private static final long serialVersionUID = 2820769346069413473L;

  private static String getOrderByColumnsListAsString(final Table table) {
    final Column[] columnsArray = table.getColumns();
    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < columnsArray.length; i++) {
      final Column column = columnsArray[i];
      final JavaSqlTypeGroup javaSqlTypeGroup = JavaSqlTypesUtility
        .lookupSqlDataType(column.getType().getType())
        .getJavaSqlTypeGroup();
      if (javaSqlTypeGroup != JavaSqlTypeGroup.binary) {
        if (i > 0) {
          buffer.append(", ");
        }
        buffer.append(column.getName());
      }
    }
    return buffer.toString();
  }

  private final String name;

  private final String query;

  /**
   * Definition of a query, including a name, and parameterized or regular SQL.
   *
   * @param name  Query name.
   * @param query Query SQL.
   */
  public Query(final String name, final String query) {
    if (Utility.isBlank(name)) {
      throw new IllegalArgumentException("No name provided for the query");
    }
    this.name = name;

    if (Utility.isBlank(query)) {
      throw new IllegalArgumentException("No SQL provided for query '" + name
        + "'");
    }
    this.query = query;
  }

  /**
   * Gets the query name.
   *
   * @return Query name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the query SQL.
   *
   * @return Query SQL
   */
  public String getQuery() {
    return TemplatingUtility.expandTemplate(query);
  }

  /**
   * Gets the query with parameters substituted.
   *
   * @param table Table information
   *
   * @return Ready-to-execute quer
   */
  public String getQueryForTable(final Table table) {
    final Map<String, String> tableProperties = new HashMap<String, String>();
    if (table != null) {
      if (table.getSchema() != null) {
        tableProperties.put("schema", table.getSchema().getFullName());
      }
      tableProperties.put("table", table.getFullName());
      tableProperties.put("columns", table.getColumnsListAsString());
      tableProperties.put("orderbycolumns",
                          getOrderByColumnsListAsString(table));
      tableProperties.put("tabletype", table.getType().toString());
    }

    String sql = query;
    sql = TemplatingUtility.expandTemplate(sql, tableProperties);
    sql = TemplatingUtility.expandTemplate(sql);

    return sql;
  }

  /**
   * Determines if this query has substitutable parameters, and whether it should be run once for each table.
   *
   * @return If the query is to be run over each table
   */
  public boolean isQueryOver() {
    final Set<String> keys = TemplatingUtility.extractTemplateVariables(query);
    return keys.contains("table");
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return name + ":" + query;
  }

}
