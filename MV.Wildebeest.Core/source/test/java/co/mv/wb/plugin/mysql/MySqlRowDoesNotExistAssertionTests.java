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

import co.mv.wb.AssertionFailedException;
import co.mv.wb.AssertionResponse;
import co.mv.wb.plugin.generaldatabase.RowDoesNotExistAssertion;
import co.mv.wb.plugin.generaldatabase.RowDoesNotExistAssertionPlugin;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MySqlRowDoesNotExistAssertionTests
{
	@Test
	public void applyForNonExistentRowSucceds() throws AssertionFailedException
	{
		// Setup
		MySqlProperties mySqlProperties = MySqlProperties.get();

		String databaseName = MySqlUtil.createDatabase(
			mySqlProperties,
			"stm_test",
			MySqlElementFixtures.productCatalogueDatabase());

		MySqlDatabaseInstance instance = new MySqlDatabaseInstance(
			mySqlProperties.getHostName(),
			mySqlProperties.getPort(),
			mySqlProperties.getUsername(),
			mySqlProperties.getPassword(),
			databaseName,
			null);

		RowDoesNotExistAssertion assertion = new RowDoesNotExistAssertion(
			UUID.randomUUID(),
			"HW ProductType Exists",
			0,
			"SELECT * FROM ProductType WHERE ProductTypeCode = 'HW';");

		RowDoesNotExistAssertionPlugin plugin = new RowDoesNotExistAssertionPlugin();

		// Execute
		AssertionResponse response = null;
		try
		{
			response = plugin.perform(
				assertion,
				instance);
		}
		finally
		{
			MySqlUtil.dropDatabase(mySqlProperties, "stm_test");
		}

		// Verify
		assertNotNull("response", response);
		assertEquals("response.result", true, response.getResult());
		assertEquals("response.message", "Row does not exist, as expected", response.getMessage());
	}

	@Test
	public void applyForExistentRowFails()
	{
		// Setup
		MySqlProperties mySqlProperties = MySqlProperties.get();

		String databaseName = MySqlUtil.createDatabase(
			mySqlProperties,
			"stm_test",
			MySqlElementFixtures.productCatalogueDatabase() +
				MySqlElementFixtures.productTypeRows());

		MySqlDatabaseInstance instance = new MySqlDatabaseInstance(
			mySqlProperties.getHostName(),
			mySqlProperties.getPort(),
			mySqlProperties.getUsername(),
			mySqlProperties.getPassword(),
			databaseName,
			null);

		RowDoesNotExistAssertion assertion = new RowDoesNotExistAssertion(
			UUID.randomUUID(),
			"HW ProductType Exists",
			0,
			"SELECT * FROM ProductType WHERE ProductTypeCode = 'HW';");

		RowDoesNotExistAssertionPlugin plugin = new RowDoesNotExistAssertionPlugin();

		// Execute
		AssertionResponse response = null;
		try
		{
			response = plugin.perform(
				assertion,
				instance);
		}
		finally
		{
			MySqlUtil.dropDatabase(mySqlProperties, databaseName);
		}

		// Verify
		assertNotNull("response", response);
		assertEquals("response.result", false, response.getResult());
		assertEquals("response.message", "Expected to find no rows but found 1", response.getMessage());
	}

	@Ignore
	@Test
	public void applyForNonExistentTableFails()
	{
		throw new UnsupportedOperationException();
	}
}
