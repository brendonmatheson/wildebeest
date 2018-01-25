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

package co.mv.wb.service;

import co.mv.wb.Resource;
import java.io.File;

/**
 * An ResourceLoader is responsible for the overall deserialization of a descriptor from some persistent representation
 * to an {@link Resource} object.
 * 
 * @author                                      Brendon Matheson
 * @since                                       1.0
 */
public interface ResourceLoader
{
	/**
	 * Loads an Resource according to the configuration of this ResourceLoader.
	 * 
	 * @param       baseDir                     the base directory for relative paths.
	 * @return                                  the deserialized Resource.
	 * @throws      MessagesException           if any user-resolvable errors occurred during deserialization.
	 * @since                                   1.0
	 */
	Resource load(File baseDir) throws MessagesException;
}
