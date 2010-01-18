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

package schemacrawler.crawl;


import schemacrawler.schema.ColumnDataType;
import schemacrawler.schema.Procedure;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;

/**
 * Represents the database schema.
 *
 * @author Sualeh Fatehi
 */
class MutableSchema
  extends AbstractNamedObject
  implements Schema {

  private static final long serialVersionUID = 3258128063743931187L;

  private final SchemaReference schemaRef;
  private final ColumnDataTypes columnDataTypes = new ColumnDataTypes();
  private final NamedObjectList<MutableTable> tables = new NamedObjectList<MutableTable>();
  private final NamedObjectList<MutableProcedure> procedures = new NamedObjectList<MutableProcedure>();

  MutableSchema() {
    this(new SchemaReference(null, null));
  }

  MutableSchema(final SchemaReference schemaRef) {
    super(schemaRef.getFullName());
    this.schemaRef = schemaRef;
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getCatalogName()
   */
  public String getCatalogName() {
    return schemaRef.getCatalogName();
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getColumnDataType(java.lang.String)
   */
  public MutableColumnDataType getColumnDataType(final String name) {
    return columnDataTypes.lookup(name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Database#getSystemColumnDataTypes()
   */
  public ColumnDataType[] getColumnDataTypes() {
    return columnDataTypes.values()
      .toArray(new ColumnDataType[columnDataTypes
        .size()]);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getFullName()
   */
  @Override
  public String getFullName() {
    return schemaRef.getFullName();
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getProcedure(java.lang.String)
   */
  public MutableProcedure getProcedure(final String name) {
    return procedures.lookup(this, name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getProcedures()
   */
  public Procedure[] getProcedures() {
    return procedures.values()
      .toArray(new Procedure[procedures.size()]);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getSchemaName()
   */
  public String getSchemaName() {
    return schemaRef.getSchemaName();
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getTable(java.lang.String)
   */
  public MutableTable getTable(final String name) {
    return tables.lookup(this, name);
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.schema.Schema#getTables()
   */
  public Table[] getTables() {
    return tables.values()
      .toArray(new Table[tables.size()]);
  }

  void addColumnDataType(final MutableColumnDataType columnDataType) {
    if (columnDataType != null) {
      columnDataTypes.add(columnDataType);
    }
  }

  void addProcedure(final MutableProcedure procedure) {
    procedures.add(procedure);
  }

  void addTable(final MutableTable table) {
    tables.add(table);
  }

  MutableColumnDataType lookupColumnDataTypeByType(final int type) {
    return columnDataTypes.lookupColumnDataTypeByType(type);
  }

  void removeProcedure(final Procedure procedure) {
    procedures.remove(procedure);
  }

  void removeTable(final Table table) {
    tables.remove(table);
  }

  void setTablesSortOrder(final NamedObjectSort sort) {
    tables.setSortOrder(sort);
  }

}
