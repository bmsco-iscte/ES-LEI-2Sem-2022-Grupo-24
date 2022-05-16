/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author Clinton Begin
 */
public class SqlRunner {

  private SqlRunnerProduct sqlRunnerProduct;

public static final int NO_GENERATED_KEY = Integer.MIN_VALUE + 1001;

  private final Connection connection;
  public SqlRunner(Connection connection) {
    this.connection = connection;
	this.sqlRunnerProduct = new SqlRunnerProduct(new TypeHandlerRegistry());
  }

  public void setUseGeneratedKeySupport(boolean useGeneratedKeySupport) {
    sqlRunnerProduct.setUseGeneratedKeySupport(useGeneratedKeySupport);
  }

  /**
   * Executes a SELECT statement that returns one row.
   *
   * @param sql  The SQL
   * @param args The arguments to be set on the statement.
   * @return The row expected.
   * @throws SQLException If less or more than one row is returned
   */
  public Map<String, Object> selectOne(String sql, Object... args) throws SQLException {
    List<Map<String, Object>> results = sqlRunnerProduct.selectAll(sql, args, this.connection, this);
    if (results.size() != 1) {
      throw new SQLException("Statement returned " + results.size() + " results where exactly one (1) was expected.");
    }
    return results.get(0);
  }

  /**
   * Executes a SELECT statement that returns multiple rows.
   *
   * @param sql  The SQL
   * @param args The arguments to be set on the statement.
   * @return The list of rows expected.
   * @throws SQLException If statement preparation or execution fails
   */
  public List<Map<String, Object>> selectAll(String sql, Object... args) throws SQLException {
    return sqlRunnerProduct.selectAll(sql, args, this.connection, this);
  }

  /**
   * Executes an INSERT statement.
   *
   * @param sql  The SQL
   * @param args The arguments to be set on the statement.
   * @return The number of rows impacted or BATCHED_RESULTS if the statements are being batched.
   * @throws SQLException If statement preparation or execution fails
   */
  public int insert(String sql, Object... args) throws SQLException {
    return sqlRunnerProduct.insert(sql, args, this.connection, this);
  }

  /**
   * Executes an UPDATE statement.
   *
   * @param sql  The SQL
   * @param args The arguments to be set on the statement.
   * @return The number of rows impacted or BATCHED_RESULTS if the statements are being batched.
   * @throws SQLException If statement preparation or execution fails
   */
  public int update(String sql, Object... args) throws SQLException {
    return sqlRunnerProduct.update(sql, args, this.connection, this);
  }

  /**
   * Executes a DELETE statement.
   *
   * @param sql  The SQL
   * @param args The arguments to be set on the statement.
   * @return The number of rows impacted or BATCHED_RESULTS if the statements are being batched.
   * @throws SQLException If statement preparation or execution fails
   */
  public int delete(String sql, Object... args) throws SQLException {
    return sqlRunnerProduct.update(sql, args, this.connection, this);
  }

  /**
   * Executes any string as a JDBC Statement.
   * Good for DDL
   *
   * @param sql The SQL
   * @throws SQLException If statement preparation or execution fails
   */
  public void run(String sql) throws SQLException {
    try (Statement stmt = connection.createStatement()) {
      stmt.execute(sql);
    }
  }

  /**
   * @deprecated Since 3.5.4, this method is deprecated. Please close the {@link Connection} outside of this class.
   */
  @Deprecated
  public void closeConnection() {
    try {
      connection.close();
    } catch (SQLException e) {
      //ignore
    }
  }

  public void setParameters(PreparedStatement ps, Object... args) throws SQLException {
    for (int i = 0, n = args.length; i < n; i++) {
      if (args[i] == null) {
        throw new SQLException("SqlRunner requires an instance of Null to represent typed null values for JDBC compatibility");
      } else if (args[i] instanceof Null) {
        ((Null) args[i]).getTypeHandler().setParameter(ps, i + 1, null, ((Null) args[i]).getJdbcType());
      } else {
        TypeHandler typeHandler = sqlRunnerProduct.getTypeHandlerRegistry().getTypeHandler(args[i].getClass());
        if (typeHandler == null) {
          throw new SQLException("SqlRunner could not find a TypeHandler instance for " + args[i].getClass());
        } else {
          typeHandler.setParameter(ps, i + 1, args[i], null);
        }
      }
    }
  }

}
