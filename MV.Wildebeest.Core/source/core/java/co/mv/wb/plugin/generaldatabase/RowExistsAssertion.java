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

package co.mv.wb.plugin.generaldatabase;

import co.mv.wb.Assertion;
import co.mv.wb.AssertionFaultException;
import co.mv.wb.AssertionResponse;
import co.mv.wb.Instance;
import co.mv.wb.MigrationType;
import co.mv.wb.ModelExtensions;
import co.mv.wb.ResourceType;
import co.mv.wb.Wildebeest;
import co.mv.wb.framework.ArgumentNullException;
import co.mv.wb.framework.DatabaseHelper;
import co.mv.wb.plugin.base.BaseAssertion;
import co.mv.wb.plugin.base.ImmutableAssertionResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * An {@link Assertion} that verifies that a given SQL query yields a single row.
 *
 * @since 1.0
 */
@MigrationType(
	pluginGroupUri = "co.mv.wb:GeneralDatabase",
	uri = "co.mv.wb.generaldatabase:RowExists",
	description = "Asserts that a query results in exactly one row.",
	example =
		"<assertion\n" +
			"    type=\"RowExists\"\n" +
			"    id=\"c1ea9cfb-bbf5-4262-8512-4bc13ebb05a4\"\n" +
			"    name=\"ProductType HW exists\">\n" +
			"    <sql><![CDATA[\n" +
			"        SELECT * FROM ProductType WHERE ProductTypeCode = 'HW';\n" +
			"    ]]></sql>\n" +
			"</assertion> "
)
public class RowExistsAssertion extends BaseAssertion implements Assertion
{
	private final String description;
	private final String sql;

	/**
	 * Creates a new RowDoesNotExistAssertion.
	 *
	 * @param assertionId the ID of the assertion
	 * @param description the description of the query that is being asserted
	 * @param seqNum      the ordinal index of the assertion within it's containing set
	 * @param sql         the query to be evaluated
	 */
	public RowExistsAssertion(
		UUID assertionId,
		String description,
		int seqNum,
		String sql)
	{
		super(assertionId, seqNum);

		if (description == null) throw new ArgumentNullException("description");
		if (sql == null) throw new ArgumentNullException("sql");

		this.description = description;
		this.sql = sql;
	}

	@Override public String getDescription()
	{
		return this.description;
	}

	@Override public List<ResourceType> getApplicableTypes()
	{
		return Arrays.asList(
			Wildebeest.MySqlDatabase,
			Wildebeest.PostgreSqlDatabase,
			Wildebeest.SqlServerDatabase);
	}

	@Override public AssertionResponse perform(Instance instance)
	{
		if (instance == null) throw new ArgumentNullException("instance");

		DatabaseInstance db = ModelExtensions.as(instance, DatabaseInstance.class);
		if (db == null)
		{
			throw new IllegalArgumentException("instance must be a DatabaseInstance");
		}

		AssertionResponse result = null;

		DataSource ds = db.getAppDataSource();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			try
			{
				conn = ds.getConnection();
				ps = conn.prepareStatement(this.sql);
				rs = ps.executeQuery();

				int rowCount = 0;
				while (rs.next())
				{
					rowCount++;
				}

				if (rowCount == 1)
				{
					result = new ImmutableAssertionResponse(true, "Exactly one row exists, as expected");
				}
				else
				{
					result = new ImmutableAssertionResponse(
						false,
						String.format("Expected to find exactly one row, but found %d", rowCount));
				}
			}
			finally
			{
				DatabaseHelper.release(rs);
				DatabaseHelper.release(ps);
				DatabaseHelper.release(conn);
			}
		}
		catch (SQLException e)
		{
			throw new AssertionFaultException(this.getAssertionId(), e);
		}

		return result;
	}
}
