package org.apache.ibatis.jdbc;


import org.apache.ibatis.type.TypeHandlerRegistry;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.ibatis.type.TypeHandler;
import java.sql.ResultSetMetaData;
import org.apache.ibatis.io.Resources;
import java.util.HashMap;
import java.util.Locale;
import java.sql.Connection;

public class SqlRunnerProduct {
	private final TypeHandlerRegistry typeHandlerRegistry;
	private boolean useGeneratedKeySupport;

	public SqlRunnerProduct(TypeHandlerRegistry TypeHandlerRegistry) {
		this.typeHandlerRegistry = TypeHandlerRegistry;
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}

	public void setUseGeneratedKeySupport(boolean useGeneratedKeySupport) {
		this.useGeneratedKeySupport = useGeneratedKeySupport;
	}

	/**
	* Executes a SELECT statement that returns multiple rows.
	* @param sql   The SQL
	* @param args  The arguments to be set on the statement.
	* @return  The list of rows expected.
	* @throws SQLException  If statement preparation or execution fails
	*/
	public List<Map<String, Object>> selectAll(String sql, Object args, Connection thisConnection,
			SqlRunner sqlRunner) throws SQLException {
		try (PreparedStatement ps = thisConnection.prepareStatement(sql)) {
			sqlRunner.setParameters(ps, args);
			try (ResultSet rs = ps.executeQuery()) {
				return getResults(rs);
			}
		}
	}

	/**
	* Executes an INSERT statement.
	* @param sql   The SQL
	* @param args  The arguments to be set on the statement.
	* @return  The number of rows impacted or BATCHED_RESULTS if the statements are being batched.
	* @throws SQLException  If statement preparation or execution fails
	*/
	public int insert(String sql, Object args, Connection thisConnection, SqlRunner sqlRunner) throws SQLException {
		PreparedStatement ps;
		if (useGeneratedKeySupport) {
			ps = thisConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} else {
			ps = thisConnection.prepareStatement(sql);
		}
		try {
			sqlRunner.setParameters(ps, args);
			ps.executeUpdate();
			if (useGeneratedKeySupport) {
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					List<Map<String, Object>> keys = getResults(generatedKeys);
					if (keys.size() == 1) {
						Map<String, Object> key = keys.get(0);
						Iterator<Object> i = key.values().iterator();
						if (i.hasNext()) {
							Object genkey = i.next();
							if (genkey != null) {
								try {
									return Integer.parseInt(genkey.toString());
								} catch (NumberFormatException e) {
								}
							}
						}
					}
				}
			}
			return SqlRunner.NO_GENERATED_KEY;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	* Executes an UPDATE statement.
	* @param sql   The SQL
	* @param args  The arguments to be set on the statement.
	* @return  The number of rows impacted or BATCHED_RESULTS if the statements are being batched.
	* @throws SQLException  If statement preparation or execution fails
	*/
	public int update(String sql, Object args, Connection thisConnection, SqlRunner sqlRunner) throws SQLException {
		try (PreparedStatement ps = thisConnection.prepareStatement(sql)) {
			sqlRunner.setParameters(ps, args);
			return ps.executeUpdate();
		}
	}

	public List<Map<String, Object>> getResults(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		List<String> columns = new ArrayList<>();
		List<TypeHandler<?>> typeHandlers = new ArrayList<>();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 0, n = rsmd.getColumnCount(); i < n; i++) {
			columns.add(rsmd.getColumnLabel(i + 1));
			try {
				Class<?> type = Resources.classForName(rsmd.getColumnClassName(i + 1));
				TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(type);
				if (typeHandler == null) {
					typeHandler = typeHandlerRegistry.getTypeHandler(Object.class);
				}
				typeHandlers.add(typeHandler);
			} catch (Exception e) {
				typeHandlers.add(typeHandlerRegistry.getTypeHandler(Object.class));
			}
		}
		while (rs.next()) {
			Map<String, Object> row = new HashMap<>();
			for (int i = 0, n = columns.size(); i < n; i++) {
				String name = columns.get(i);
				TypeHandler<?> handler = typeHandlers.get(i);
				row.put(name.toUpperCase(Locale.ENGLISH), handler.getResult(rs, name));
			}
			list.add(row);
		}
		return list;
	}
}