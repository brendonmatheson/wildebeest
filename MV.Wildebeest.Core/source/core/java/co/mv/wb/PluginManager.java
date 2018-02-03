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

import java.util.List;

/**
 * An internal service for managing and accessing plugins.
 *
 * @author                                      Brendon Matheson
 * @since                                       4.0
 */
public interface PluginManager
{
	/**
	 * Returns a list of MigrationTypeInfo's representing all available migrations.
	 *
	 * @return                                  a list of MigrationTypeInfo's representing all available migrations.
	 * @since                                   4.0
	 */
	List<MigrationTypeInfo> getMigrationTypeInfos();

	/**
	 * Looks up the MigrationPlugin for the supplied MigrationType URI.
	 *
	 * @param       uri                         the URI identifying the MigrationType of interest.
	 * @return                                  the MigrationPlugin for the supplied MigrationType URI.
	 */
	MigrationPlugin getMigrationPlugin(String uri);
}
