// Wildebeest Migration Framework
// Copyright 2013, Zen Digital Co Inc
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

package co.zd.wb.model.database;

import co.zd.wb.model.AssertionResponse;
import co.zd.wb.model.mysql.MySqlDatabaseInstance;
import co.zd.wb.model.mysql.MySqlElementFixtures;
import co.zd.wb.model.mysql.MySqlProperties;
import co.zd.helium.fixture.MySqlDatabaseFixture;
import java.util.UUID;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class RowExistsAssertionTests
{
	@Test public void applyForNonExistentRowFails()
	{
		
		//
		// Fixture Setup
		//
		
		MySqlProperties mySqlProperties = MySqlProperties.get();
		
		MySqlDatabaseFixture f = new MySqlDatabaseFixture(
			mySqlProperties.getHostName(),
			mySqlProperties.getPort(),
			mySqlProperties.getUsername(),
			mySqlProperties.getPassword(),
			"stm_test",
			MySqlElementFixtures.productCatalogueDatabase());
		f.setUp();
		
		MySqlDatabaseInstance instance = new MySqlDatabaseInstance(
			mySqlProperties.getHostName(),
			mySqlProperties.getPort(),
			mySqlProperties.getUsername(),
			mySqlProperties.getPassword(),
			f.getDatabaseName(),
			null);
		
		RowExistsAssertion assertion = new RowExistsAssertion(
			UUID.randomUUID(),
			"ProductType HW Exists",
			0,
			"SELECT * FROM ProductType WHERE ProductTypeCode = 'HW';");
		
		//
		// Execute
		//

		AssertionResponse response = null;
		try
		{
			response = assertion.apply(instance);
		}
		finally
		{
			f.tearDown();
		}
		
		//
		// Assert Results
		//
		
		Assert.assertNotNull("response", response);
		Assert.assertEquals("response.result", false, response.getResult());
		Assert.assertEquals("response.message", "Expected to find exactly one row, but found 0", response.getMessage());
		
	}
	
	@Test public void applyForExistentRowSucceeds()
	{
		
		//
		// Fixture Setup
		//
		
		MySqlProperties mySqlProperties = MySqlProperties.get();
		
		MySqlDatabaseFixture f = new MySqlDatabaseFixture(
			mySqlProperties.getHostName(),
			mySqlProperties.getPort(),
			mySqlProperties.getUsername(),
			mySqlProperties.getPassword(),
			"stm_test",
			MySqlElementFixtures.productCatalogueDatabase() +
			MySqlElementFixtures.productTypeRows());
		f.setUp();
		
		MySqlDatabaseInstance instance = new MySqlDatabaseInstance(
			mySqlProperties.getHostName(),
			mySqlProperties.getPort(),
			mySqlProperties.getUsername(),
			mySqlProperties.getPassword(),
			f.getDatabaseName(),
			null);
		
		RowExistsAssertion assertion = new RowExistsAssertion(
			UUID.randomUUID(),
			"ProductType HW Exists",
			0,
			"SELECT * FROM ProductType WHERE ProductTypeCode = 'HW';");
		
		//
		// Execute
		//

		AssertionResponse response = null;
		try
		{
			response = assertion.apply(instance);
		}
		finally
		{
			f.tearDown();
		}
		
		//
		// Assert Results
		//
		
		Assert.assertNotNull("response", response);
		Assert.assertEquals("response.result", true, response.getResult());
		Assert.assertEquals("response.message", "Exactly one row exists, as expected", response.getMessage());
		
	}
	
	@Ignore @Test public void applyForNonExistentTableFails()
	{
		throw new UnsupportedOperationException();
	}
}