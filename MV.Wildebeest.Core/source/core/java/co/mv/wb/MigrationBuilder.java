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

import java.io.File;
import java.util.UUID;


/**
 * A MigrationBuilder is a factory component for building instances of {@link Migration}s.  The build method takes the
 * common properties of a Migration as parameters.
 * <p>
 * It's expected that MigrationBuilder's will typically be stateful, with additional properties or configuration
 * information being supplied to them as properties.  The reset() method should be implemented to clear such additional
 * state and restore the MigrationBuilder to a clean state ready to be re-used to build another Migration instance.  The
 * framework will always call reset() before using an MigrationBuilder.
 *
 * @since 1.0
 */
public interface MigrationBuilder
{
	/**
	 * Builds a new {@link Migration}.
	 *
	 * @param migrationId the ID for the new {@link Migration}.
	 * @param fromState   the source state for the new {@link Migration} or null to migrate from
	 *                    the non-existent state.
	 * @param toState     the target state for the new {@link Migration} or null to migrate to
	 *                    the non-existent state.
	 * @param baseDir     the base directory to use for resolving relative paths.
	 * @return the new {@link Migration} instance.
	 * @throws PluginBuildException if the plugin fails to build.
	 * @since 1.0
	 */
	Migration build(
		UUID migrationId,
		String fromState,
		String toState,
		File baseDir) throws
		PluginBuildException;

	/**
	 * Resets the MigrationBuilder, making it ready to build a new instance.
	 *
	 * @since 1.0
	 */
	void reset();
}
