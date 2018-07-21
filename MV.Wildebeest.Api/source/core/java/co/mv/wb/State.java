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

import co.mv.wb.framework.ArgumentNullException;
import co.mv.wb.framework.Util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents an observable and verifiable state of a {@link Resource}/
 *
 * @since 1.0
 */
public interface State
{
	/**
	 * Gets the ID of this State.
	 *
	 * @return the unique ID of this State
	 * @since 1.0
	 */
	UUID getStateId();

	/**
	 * Gets the optional name of this State.
	 *
	 * @return the name of this State, if it has one
	 * @since 1.0
	 */
	Optional<String> getName();

	/**
	 * Gets the set of Assertions that should be used to verify this State.
	 *
	 * @return the set of assertions that should be used to verify this State
	 * @since 1.0
	 */
	List<Assertion> getAssertions();

	/**
	 * Returns the name if the state has one, otherwise returns it's ID.
	 *
	 * @return the name if the State has one, otherwise it's unique ID
	 * @since 1.0
	 */
	String getDisplayName();

	/**
	 * Returns the description if the state has one.
	 *
	 * @return the description if the State has one
	 * @since 1.0
	 */
	Optional<String> getDescription();

	/**
	 * Indicates if the supplied state reference matches either the state's ID or it's name (if set).
	 *
	 * @param stateRef the state reference to check for a match on.
	 * @return boolean value indicating if the reference matches this state.
	 */
	default boolean matchesStateRef(String stateRef)
	{
		boolean result = stateRef == null;

		if (stateRef != null)
		{
			if (Util.isUUID(stateRef))
			{
				UUID stateId = UUID.fromString(stateRef);

				result = this.getStateId().equals(stateId);
			}
			else
			{
				result = this.getName().isPresent() && this.getName().get().equals(stateRef);
			}
		}

		return result;
	}
}
