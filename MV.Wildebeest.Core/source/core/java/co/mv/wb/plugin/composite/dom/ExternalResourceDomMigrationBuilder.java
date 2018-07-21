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

package co.mv.wb.plugin.composite.dom;

import co.mv.wb.MessageList;
import co.mv.wb.Migration;
import co.mv.wb.MigrationBuilder;
import co.mv.wb.PluginBuildException;
import co.mv.wb.V;
import co.mv.wb.framework.ArgumentNullException;
import co.mv.wb.plugin.base.dom.BaseDomMigrationBuilder;
import co.mv.wb.plugin.composite.ExternalResourceMigration;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

/**
 * A {@link MigrationBuilder} that builds a {@link ExternalResourceMigration} from a DOM {@link org.w3c.dom.Element}.
 *
 * @since 4.0
 */
public class ExternalResourceDomMigrationBuilder extends BaseDomMigrationBuilder
{
	/**
	 * Constructs a new ExternalResourceDomMigrationBuilder instance.
	 *
	 * @since 4.0
	 */
	public ExternalResourceDomMigrationBuilder()
	{
	}

	@Override
	public Migration build(
		UUID migrationId,
		String fromState,
		String toState,
		File baseDir) throws
		PluginBuildException
	{
		if (migrationId == null) throw new ArgumentNullException("migrationId");
		if (baseDir == null) throw new ArgumentNullException("baseDir");

		Migration result;

		Optional<String> filename = this.tryGetString("filename");
		Optional<String> target = this.tryGetString("target");

		// Validation
		MessageList messages = new MessageList();
		if (!filename.isPresent())
		{
			messages.addMessage(V.elementMissing(migrationId, "filename", ExternalResourceMigration.class));
		}
		if (!target.isPresent())
		{
			messages.addMessage(V.elementMissing(migrationId, "target", ExternalResourceMigration.class));
		}

		if (messages.size() > 0)
		{
			throw new PluginBuildException(messages);
		}

		result = new ExternalResourceMigration(
			migrationId,
			fromState,
			toState,
			baseDir,
			filename.get(),
			target.orElse(null));

		return result;
	}
}
