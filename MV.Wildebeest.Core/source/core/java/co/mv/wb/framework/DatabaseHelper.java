// Wildebeest Migration Framework
// Copyright © 2013 - 2018, Matheson Ventures Pte Ltd
//
// This file is part of Wildebeest
//
// Wildebeest is free software: you can redistribute it and/or modify it under
// the terms of the GNU General Public License v2 as published by the Free
// Software Foundation.
//
// Wildebeest is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with
// Wildebeest.  If not, see http://www.gnu.org/licenses/gpl-2.0.html

package co.mv.wb.framework;

import co.mv.wb.FaultException;
import org.joda.time.DateTime;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Provides a set of convenience methods for working with JDBC-accessed databases.
 *
 * @since 1.0
 */
public class DatabaseHelper
{
	/**
	 * Executes a SQL statement against the database represented by the supplied DataSource.
	 *
	 * @param dataSource the DataSource that represents the database to work with
	 * @param sql        the SQL statement to execute against the target database.
	 * @throws SQLException may be thrown due to a mal-formed SQL statement, connectivity problem,
	 *                      or some other issue.
	 * @since 1.0
	 */
	public static void execute(
		DataSource dataSource,
		String sql,
		boolean splitStatements) throws SQLException
	{
		if (dataSource == null) throw new ArgumentNullException("dataSource");
		if (sql == null) throw new ArgumentNullException("sql");
		if ("".equals(sql)) throw new IllegalArgumentException("sql cannot be empty");

		Connection conn = null;
		PreparedStatement ps = null;

		try
		{
			conn = dataSource.getConnection();

			if (splitStatements)
			{
				String[] statements = sql.split("\\;");

				for (String statement : statements)
				{
					if (!"".equals(statement.trim()))
					try
					{
						ps = conn.prepareStatement(statement);
						ps.execute();
					}
					finally
					{
						DatabaseHelper.release(ps);
					}
				}
			}

			else
			{
				try
				{
					ps = conn.prepareStatement(sql);
					ps.execute();
				}
				finally
				{
					DatabaseHelper.release(ps);
				}
			}
		}
		finally
		{
			DatabaseHelper.release(conn);
		}
	}

	/**
	 * Executes a SQL statement against the database represented by the supplied DataSource.
	 *
	 * @param dataSource the DataSource that represents the database to work with
	 * @param sql        the SQL statement to execute against the target database.
	 * @param params     the SQL query parameters
	 * @throws SQLException may be thrown due to a mal-formed SQL statement, connectivity problem,
	 *                      or some other issue.
	 * @since 1.0
	 */
	public static void execute(
		DataSource dataSource,
		String sql,
		List<SqlParameters> params) throws SQLException
	{
		if (dataSource == null) throw new ArgumentNullException("dataSource");
		if (sql == null) throw new ArgumentNullException("sql");
		if ("".equals(sql)) throw new IllegalArgumentException("sql cannot be empty");

		Connection conn = null;
		PreparedStatement ps = null;

		try
		{
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);

			for (int i = 0; i < params.size(); i++)
			{
				SqlParameters p = params.get(i);

				if (p.getValue() instanceof String)
				{
					ps.setString(i + 1, (String)p.getValue());
				}
				else if (p.getValue() instanceof DateTime)
				{
					ps.setTimestamp(i + 1, new java.sql.Timestamp((((DateTime)p.getValue()).getMillis())));
				}
				else if (p.getValue() instanceof Object)
				{
					ps.setObject(i + 1, p.getValue().toString());
				}
			}

			ps.execute();
		}
		finally
		{
			DatabaseHelper.release(ps);
			DatabaseHelper.release(conn);
		}
	}

	/**
	 * Executes a SQL query against the database represented by the supplied DataSource and returns the value from the
	 * first column of the single resultant row as an Object.
	 *
	 * @param dataSource the DataSource that represents the database to work with
	 * @param sql        the SQL query to execute against the target database
	 * @return the value from the first column of the single resultant row
	 * @throws SQLException may be thrown due to a mal-formed SQL statement, connectivity problem,
	 *                      or some other issue.
	 * @since 1.0
	 */
	public static Object single(
		DataSource dataSource,
		String sql) throws SQLException
	{
		if (dataSource == null) throw new ArgumentNullException("dataSource");
		if (sql == null) throw new ArgumentNullException("sql");
		if ("".equals(sql)) throw new IllegalArgumentException("sql cannot be empty");

		Object result = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next())
			{
				result = rs.getObject(1);
			}

			// TODO: Fail if there is more than one row in the resultset.
		}
		finally
		{
			DatabaseHelper.release(rs);
			DatabaseHelper.release(ps);
			DatabaseHelper.release(conn);
		}

		return result;
	}

	public static boolean rowExists(
		DataSource dataSource,
		String sql)
	{
		if (dataSource == null) throw new ArgumentNullException("dataSource");
		if (sql == null) throw new ArgumentNullException("sql");
		if ("".equals(sql)) throw new IllegalArgumentException("sql cannot be empty");

		boolean result = false;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();

			result = rs.next();
		}
		catch (SQLException e)
		{
			throw new FaultException(e);
		}
		finally
		{
			try
			{
				DatabaseHelper.release(rs);
				DatabaseHelper.release(ps);
				DatabaseHelper.release(conn);
			}
			catch (SQLException e)
			{
				throw new FaultException(e);
			}
		}

		return result;
	}

	/**
	 * If the supplied Connection reference is non-null, attempts to close that Connection.
	 *
	 * @param conn the Connection to be closed.  Ignored if null is supplied.
	 * @throws SQLException may be thrown due to a state exception, connectivity problem, or some
	 *                      other issue.
	 * @since 1.0
	 */
	public static void release(Connection conn) throws SQLException
	{
		if (conn != null)
		{
			conn.close();
		}
	}

	/**
	 * If the supplied PreparedStatement reference is non-null, attempts to close that PreparedStatement.
	 *
	 * @param ps the PreparedStatement to be closed.  Ignored if null is supplied.
	 * @throws SQLException may be thrown due to a state exception, connectivity problem or some
	 *                      other issue.
	 * @since 1.0
	 */
	public static void release(PreparedStatement ps) throws SQLException
	{
		if (ps != null)
		{
			ps.close();
		}
	}

	/**
	 * If the supplied ResultSet is non-null, attempts to close that ResultSet.
	 *
	 * @param rs the ResultSet to be closed.  Ignored if null is supplied.
	 * @throws SQLException may be thrown due to a state exception, connectiivty problem or some
	 *                      other issue.
	 * @since 1.0
	 */
	public static void release(ResultSet rs) throws SQLException
	{
		if (rs != null)
		{
			rs.close();
		}
	}

	/**
	 * Gets DateTimeOffset to log time, it is timezone aware
	 *
	 * @return time as String
	 * @since 4.0
	 */
	public static DateTime getInstant()
	{
		return new DateTime();
	}

}
