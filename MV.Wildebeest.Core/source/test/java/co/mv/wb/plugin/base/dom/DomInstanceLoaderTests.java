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

package co.mv.wb.plugin.base.dom;

import co.mv.wb.Instance;
import co.mv.wb.InstanceBuilder;
import co.mv.wb.LoaderFault;
import co.mv.wb.PluginBuildException;
import co.mv.wb.TestUtils;
import co.mv.wb.plugin.postgresql.PostgreSqlDatabaseInstance;
import co.mv.wb.plugin.postgresql.dom.PostgreSqlDatabaseDomInstanceBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DomInstanceLoaderTests
{
	/**
	 * Use the DomInstanceLoader to load the fixture test/app/PostgreSqlDatabase/staging.wbinstance.xml which is a valid
	 * {@link co.mv.wb.plugin.postgresql.PostgreSqlDatabaseInstance} and verify that configuration was loaded correctly.
	 *
	 * @since 4.0
	 */
	@Test
	public void loadInstance_validPostgreSqlInstance_succeeds() throws LoaderFault, PluginBuildException
	{
		// Setup
		Map<String, InstanceBuilder> instanceBuilders = new HashMap<>();
		instanceBuilders.put("co.mv.wb.PostgreSqlDatabase", new PostgreSqlDatabaseDomInstanceBuilder());

		String instanceXml = TestUtils.readAllText("PostgreSqlDatabase/staging.wbinstance.xml");

		DomInstanceLoader loader = new DomInstanceLoader(
			instanceBuilders,
			instanceXml);

		// Execute
		Instance instance = loader.load();

		// Verify
		// TODO: Verify that Instance is actually a PostgreSqlDatabaseInstance
		// Assert.assertTrue(...);

		PostgreSqlDatabaseInstance instanceT = (PostgreSqlDatabaseInstance)instance;

		// TODO: Verify the properties of the PostgreSqlDatabaseInstance
		Assert.assertEquals("instance.hostName", "127.0.0.1", instanceT.getHostName());

		// TODO: port
		// TODO: adminUsername
		// TODO: etc
	}
}
