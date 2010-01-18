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

package schemacrawler.tools.integration.scripting;


import schemacrawler.schema.Database;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.executable.BaseExecutable;
import sf.util.FileUtility;
import sf.util.Utility;

import java.io.*;
import java.sql.Connection;
import java.util.concurrent.ExecutionException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Main executor for the scripting engine integration.
 *
 * @author Sualeh Fatehi
 */
public final class ScriptRenderer
  extends BaseExecutable {

  private static final long serialVersionUID = -2232328675306451328L;

  public ScriptRenderer() {
    super("script");
  }

  /**
   * {@inheritDoc}
   *
   * @see schemacrawler.tools.integration.SchemaRenderer#render(schemacrawler.schema.Database, java.sql.Connection,
   *      java.lang.String, java.io.Writer)
   */
  @Override
  public final void executeOn(final Database database,
                              final Connection connection)
    throws Exception {
    final String scriptFileName = outputOptions.getOutputFormatValue();
    if (Utility.isBlank(scriptFileName)) {
      throw new SchemaCrawlerException("No script file provided");
    }
    final Reader reader;
    final File scriptFile = new File(scriptFileName);
    if (scriptFile.exists() && scriptFile.canRead()) {
      reader = new FileReader(scriptFile);
    }
    else {
      final InputStream inputStream = ScriptRenderer.class
        .getResourceAsStream("/" + scriptFileName);
      if (inputStream != null) {
        reader = new InputStreamReader(inputStream);
      }
      else {
        throw new SchemaCrawlerException("Cannot load script, "
          + scriptFileName);
      }
    }

    try {
      // Create a new instance of the engine
      final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
      ScriptEngine scriptEngine = scriptEngineManager
        .getEngineByExtension(FileUtility.getFileExtension(scriptFile));
      if (scriptEngine == null) {
        scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
      }
      if (scriptEngine == null) {
        throw new SchemaCrawlerException("Script engine not found");
      }

      final Writer writer = outputOptions.openOutputWriter();

      // Set up the context
      scriptEngine.getContext()
        .setWriter(writer);
      scriptEngine.put("database", database);
      scriptEngine.put("connection", connection);

      // Evaluate the script
      scriptEngine.eval(reader);

      outputOptions.closeOutputWriter(writer);
    }
    catch (final ScriptException e) {
      throw new ExecutionException("Could not evaluate script", e);
    }
  }

}
