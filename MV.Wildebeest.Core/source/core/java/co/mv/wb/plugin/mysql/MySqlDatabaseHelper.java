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

package co.mv.wb.plugin.mysql;

import co.mv.wb.FaultException;
import co.mv.wb.framework.ArgumentNullException;
import co.mv.wb.framework.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Functional helper methods for working with MySQL databases.
 *
 * @since 1.0
 */
public class MySqlDatabaseHelper
{
	/**
	 * Returns a boolean flag indicating whether or not a table exists for the database described by the supplied
	 * instance.
	 *
	 * @param instance  the MySqlDatabaseInstance to check.
	 * @param tableName the name of the table to check for.
	 * @return a boolean flag indicating whether or not the specified table exists.
	 * @since 1.0
	 */
	public static boolean tableExists(
		MySqlDatabaseInstance instance,
		String tableName)
	{
		if (instance == null) throw new ArgumentNullException("instance");
		if (tableName == null) throw new ArgumentNullException("tableName");
		if ("".equals(tableName)) throw new IllegalArgumentException("tableName cannot be empty");

		StringBuilder query = new StringBuilder();
		query
			.append("SELECT TABLE_NAME FROM TABLES ")
			.append("WHERE ")
			.append("TABLE_SCHEMA = ? AND ")
			.append("TABLE_NAME = ?;");

		boolean result = false;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			conn = instance.getAdminDataSource().getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, instance.getDatabaseName());
			ps.setString(2, tableName);
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
}
