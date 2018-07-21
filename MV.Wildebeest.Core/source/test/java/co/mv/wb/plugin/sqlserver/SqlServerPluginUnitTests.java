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

package co.mv.wb.plugin.sqlserver;

import co.mv.wb.Migration;
import co.mv.wb.MigrationFailedException;
import co.mv.wb.MigrationPlugin;
import co.mv.wb.event.LoggingEventSink;
import co.mv.wb.plugin.generaldatabase.BaseDatabasePluginUnitTests;
import co.mv.wb.plugin.generaldatabase.DatabaseFixtureHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Unit tests for plugins as applied to SQL-Server databases.
 *
 * @since 4.0
 */
public class SqlServerPluginUnitTests extends BaseDatabasePluginUnitTests
{
	private static final Logger LOG = LoggerFactory.getLogger(SqlServerPluginUnitTests.class);

	@Override
	@Test
	public void databaseExistsAssertionForExistentDatabase() throws MigrationFailedException
	{
		String databaseName = DatabaseFixtureHelper.databaseName();
		SqlServerDatabaseInstance instance = SqlServerProperties.get().toInstance(databaseName);

		Migration create = new SqlServerCreateDatabaseMigration(
			UUID.randomUUID(),
			null,
			UUID.randomUUID().toString());

		MigrationPlugin createRunner = new SqlServerCreateDatabaseMigrationPlugin();

		Migration drop = new SqlServerDropDatabaseMigration(
			UUID.randomUUID(),
			UUID.randomUUID().toString(),
			null);

		MigrationPlugin dropRunner = new SqlServerDropDatabaseMigrationPlugin();

		this.databaseExistsAssertionForExistentDatabase(
			new LoggingEventSink(LOG),
			instance,
			create,
			createRunner,
			drop,
			dropRunner);
	}

	@Override
	@Test
	public void databaseExistsAssertionForNonExistentDatabase() throws MigrationFailedException
	{
		String databaseName = DatabaseFixtureHelper.databaseName();
		SqlServerDatabaseInstance db = SqlServerProperties.get().toInstance(databaseName);

		this.databaseExistsAssertionForNonExistentDatabase(db);
	}

	@Override
	@Test
	public void databaseDoesNotExistAssertionForExistentDatabase() throws MigrationFailedException
	{
		String databaseName = DatabaseFixtureHelper.databaseName();
		SqlServerDatabaseInstance instance = SqlServerProperties.get().toInstance(databaseName);

		Migration create = new SqlServerCreateDatabaseMigration(
			UUID.randomUUID(),
			null,
			UUID.randomUUID().toString());

		MigrationPlugin createRunner = new SqlServerCreateDatabaseMigrationPlugin();

		Migration drop = new SqlServerDropDatabaseMigration(
			UUID.randomUUID(),
			UUID.randomUUID().toString(),
			null);

		MigrationPlugin dropRunner = new SqlServerDropDatabaseMigrationPlugin();

		this.databaseDoesNotExistAssertionForExistentDatabase(
			new LoggingEventSink(LOG),
			instance,
			create,
			createRunner,
			drop,
			dropRunner);
	}

	@Override
	@Test
	public void databaseDoesNotExistAssertionForNonExistentDatabase() throws MigrationFailedException
	{
		String databaseName = DatabaseFixtureHelper.databaseName();
		SqlServerDatabaseInstance db = SqlServerProperties.get().toInstance(databaseName);

		this.databaseDoesNotExistAssertionForNonExistentDatabase(db);
	}
}
