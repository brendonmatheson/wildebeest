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

package co.mv.wb.fixture.xmlbuilder;

import co.mv.wb.framework.ArgumentNullException;

import java.util.Optional;
import java.util.UUID;

/**
 * Creates &lt;migration&gt;'s as part of the fluent API for creating XML fixtures for unit tests.
 *
 * @since 4.0
 */
public class MigrationBuilder
{
	private final ResourceXmlBuilder builder;
	private final ResourceBuilder resource;
	private final String type;
	private final UUID migrationId;
	private final String fromState;
	private final String toState;
	private String innerXml;

	public MigrationBuilder(
		ResourceXmlBuilder builder,
		ResourceBuilder resource,
		String type,
		UUID migrationId,
		String fromState,
		String toState)
	{
		this(
			builder,
			resource,
			type,
			migrationId,
			fromState,
			toState,
			"");
	}

	private MigrationBuilder(
		ResourceXmlBuilder builder,
		ResourceBuilder resource,
		String type,
		UUID migrationId,
		String fromState,
		String toState,
		String innerXml)
	{
		if (builder == null) throw new ArgumentNullException("builder");
		if (resource == null) throw new ArgumentNullException("resource");
		if (type == null) throw new ArgumentNullException("type");
		if (migrationId == null) throw new ArgumentNullException("migrationId");

		this.builder = builder;
		this.resource = resource;
		this.type = type;
		this.migrationId = migrationId;
		this.fromState = fromState;
		this.toState = toState;
		this.innerXml = innerXml;
	}

	public ResourceBuilder getResource()
	{
		return this.resource;
	}

	public String getType()
	{
		return this.type;
	}

	public UUID getMigrationId()
	{
		return this.migrationId;
	}

	public Optional<String> getFromState()
	{
		return Optional.ofNullable(this.fromState);
	}

	public Optional<String> getToState()
	{
		return Optional.ofNullable(this.toState);
	}

	public String getInnerXml()
	{
		return this.innerXml;
	}

	public MigrationBuilder withInnerXml(String innerXml)
	{
		if (innerXml == null) throw new ArgumentNullException("innerXml");

		this.innerXml = innerXml;

		return this;
	}

	public ResourceBuilder resource()
	{
		return this.getResource();
	}

	public MigrationBuilder migration(
		String type,
		UUID migrationId,
		String fromState,
		String toState)
	{
		return this.getResource().migration(type, migrationId, fromState, toState);
	}

	public String render()
	{
		return this.builder.build();
	}
}
