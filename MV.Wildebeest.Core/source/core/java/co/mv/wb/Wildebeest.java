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

package co.mv.wb;

import co.mv.wb.event.EventSink;
import co.mv.wb.framework.ArgumentNullException;
import co.mv.wb.impl.WildebeestApiBuilder;
import co.mv.wb.plugin.composite.ExternalResourceMigrationPlugin;
import co.mv.wb.plugin.generaldatabase.AnsiSqlCreateDatabaseMigrationPlugin;
import co.mv.wb.plugin.generaldatabase.AnsiSqlDropDatabaseMigrationPlugin;
import co.mv.wb.plugin.generaldatabase.SqlScriptMigrationPlugin;
import co.mv.wb.plugin.mysql.MySqlCreateDatabaseMigrationPlugin;
import co.mv.wb.plugin.mysql.MySqlDatabaseResourcePlugin;
import co.mv.wb.plugin.mysql.MySqlDropDatabaseMigrationPlugin;
import co.mv.wb.plugin.postgresql.PostgreSqlDatabaseResourcePlugin;
import co.mv.wb.plugin.sqlserver.SqlServerCreateDatabaseMigrationPlugin;
import co.mv.wb.plugin.sqlserver.SqlServerCreateSchemaMigrationPlugin;
import co.mv.wb.plugin.sqlserver.SqlServerDatabaseResourcePlugin;
import co.mv.wb.plugin.sqlserver.SqlServerDropDatabaseMigrationPlugin;
import co.mv.wb.plugin.sqlserver.SqlServerDropSchemaMigrationPlugin;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Global definitions and functions for Wildebeest.
 *
 * @since 4.0
 */
public class Wildebeest
{

	//
	// PluginGroup
	//

	public static final PluginGroup CompositeResourcePluginGroup = new PluginGroup(
		"co.mv.wb:Composite",
		"Composite Resource",
		"Works with higher-order resources composed of multiple other Wildebeest resources");
	public static final PluginGroup GeneralDatabasePluginGroup = new PluginGroup(
		"co.mv.wb:GeneralDatabase",
		"General Database",
		"Plugins that can be used for most relational database management systems");
	public static final PluginGroup MySqlPluginGroup = new PluginGroup(
		"co.mv.wb:MySqlDatabase",
		"MySQL",
		"Plugins for MySQL database resources");
	public static final PluginGroup PostgreSqlPluginGroup = new PluginGroup(
		"co.mv.wb:PostgreSqlDatabase",
		"PostgreSQL",
		"Plugins for PostgreSQL database resources");
	public static final PluginGroup SqlServerPluginGroup = new PluginGroup(
		"co.mv.wb:SqlServerDatabase",
		"SQL Server",
		"Plugins for SQL Server database resources");

	public static List<PluginGroup> getPluginGroups()
	{
		return Arrays.asList(
			Wildebeest.CompositeResourcePluginGroup,
			Wildebeest.GeneralDatabasePluginGroup,
			Wildebeest.MySqlPluginGroup,
			Wildebeest.PostgreSqlPluginGroup,
			Wildebeest.SqlServerPluginGroup);
	}

	//
	// ResourceType
	//

	public static final ResourceType MySqlDatabase = new ResourceType(
		"co.mv.wb.MySqlDatabase",
		"MySQL Database");
	public static final ResourceType PostgreSqlDatabase = new ResourceType(
		"co.mv.wb.PostgreSqlDatabase",
		"PostgreSQL Database");
	public static final ResourceType SqlServerDatabase = new ResourceType(
		"co.mv.wb.SqlServerDatabase",
		"SQL Server Database");

	public static Map<ResourceType, ResourcePlugin> getResourcePlugins()
	{
		Map<ResourceType, ResourcePlugin> result = new HashMap<>();

		result.put(Wildebeest.MySqlDatabase, new MySqlDatabaseResourcePlugin());
		result.put(Wildebeest.PostgreSqlDatabase, new PostgreSqlDatabaseResourcePlugin());
		result.put(Wildebeest.SqlServerDatabase, new SqlServerDatabaseResourcePlugin());

		return result;
	}

	//
	// Migration
	//

	public static List<MigrationPlugin> getMigrationPlugins(
		WildebeestApi wildebeestApi)
	{
		if (wildebeestApi == null) throw new ArgumentNullException("wildebeestApi");

		List<MigrationPlugin> result = new ArrayList<>();

		// ansisql
		result.add(new AnsiSqlCreateDatabaseMigrationPlugin());
		result.add(new AnsiSqlDropDatabaseMigrationPlugin());

		// composite
		result.add(new ExternalResourceMigrationPlugin(wildebeestApi));

		// database
		result.add(new SqlScriptMigrationPlugin());

		// mysql
		result.add(new MySqlCreateDatabaseMigrationPlugin());
		result.add(new MySqlDropDatabaseMigrationPlugin());

		// sqlserver
		result.add(new SqlServerCreateDatabaseMigrationPlugin());
		result.add(new SqlServerCreateSchemaMigrationPlugin());
		result.add(new SqlServerDropDatabaseMigrationPlugin());
		result.add(new SqlServerDropSchemaMigrationPlugin());

		return result;
	}

	//
	// Assertion
	//

	public static List<AssertionType> findAssertionTypes()
	{
		Reflections reflections = new Reflections("co.mv.wb");

		return reflections
			.getTypesAnnotatedWith(AssertionType.class)
			.stream()
			.map(x -> x.getAnnotation(AssertionType.class))
			.collect(Collectors.toList());
	}

	//
	// Services
	//

	public static WildebeestApiBuilder wildebeestApi(
		EventSink eventSink)
	{
		if (eventSink == null) throw new ArgumentNullException("eventSink");

		return WildebeestApiBuilder.create(eventSink);
	}

	//
	// Global Functions
	//

	/**
	 * Attempts to find the {@link State} matching the supplied stateRef reference in the supplied {@link Resource}.
	 *
	 * @param resource the Resource in which to search for the State
	 * @param stateRef the reference to the State to search for.  May be the ID or the name of the State.
	 * @return the State that matches the supplied stateRef reference.
	 */
	public static State findState(
		Resource resource,
		String stateRef) throws InvalidReferenceException
	{
		if (resource == null) throw new ArgumentNullException("resource");
		if (stateRef == null) throw new ArgumentNullException("stateRef");

		Optional<State> result = resource
			.getStates().stream()
			.filter(s -> s.matchesStateRef(stateRef))
			.findFirst();

		if (!result.isPresent())
		{
			throw InvalidReferenceException.oneReference(
				EntityType.State,
				stateRef);
		}

		return result.get();
	}
}
