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

package co.mv.wb.framework;

/**
 * Indicates that an invalid value was passed in for a parameter.
 *
 * @since 4.0
 */
public class ArgumentException extends RuntimeException
{
	/**
	 * Constructs a new ArgumentException for the specified parameter.
	 *
	 * @param paramName the parameter for which a null value was supplied.
	 * @since 4.0
	 */
	public ArgumentException(
		String paramName,
		String message)
	{
		super(String.format("paramName: %s; %s", paramName, message));
	}
}
