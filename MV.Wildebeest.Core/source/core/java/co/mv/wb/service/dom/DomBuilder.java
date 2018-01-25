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

package co.mv.wb.service.dom;

import org.w3c.dom.Element;

/**
 * Provides the core definition for all builder implementations (AssertionBuilder, InstanceBuilder, etc) that are based
 * on DOM parsing of XML representations.
 * 
 * @author                                      Brendon Matheson
 * @since                                       1.0
 */
public interface DomBuilder
{
	/**
	 * Sets the root DOM {@link org.w3c.dom.Element} that represents the item to be built by this builder.
	 * 
	 * @param       element                     the root Element for the item to be built
	 * @since                                   1.0
	 */
	void setElement(Element element);
	
	/**
	 * Resets the builder back to an initialized state.
	 * 
	 * @since                                   1.0
	 */
	void reset();
}
