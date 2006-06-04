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

package schemacrawler.util;


/**
 * Compares database objects according to the natural sort order.
 * 
 * @author sfatehi
 */
public class NaturalSortComparator
  implements SerializableComparator
{

  private static final long serialVersionUID = 3907214831827235382L;

  /**
   * Compare.
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   * @param o1
   *          First object to compare
   * @param o2
   *          Second object to compare
   * @return Comparision
   */
  public int compare(final Object o1, final Object o2)
  {
    if (o1.getClass() != o2.getClass())
    {
      throw new ClassCastException("Incompatibale classes for comparison -"
                                   + o1.getClass() + " and " + o2.getClass());
    }
    final Comparable obj1 = (Comparable) o1;
    final Comparable obj2 = (Comparable) o2;
    return obj1.compareTo(obj2);
  }

}
