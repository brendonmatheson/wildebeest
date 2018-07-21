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

package co.mv.wb.fixture;

import co.mv.wb.FileLoadException;
import co.mv.wb.Instance;
import co.mv.wb.InvalidReferenceException;
import co.mv.wb.LoaderFault;
import co.mv.wb.PluginBuildException;
import co.mv.wb.Resource;
import co.mv.wb.WildebeestApi;
import co.mv.wb.XmlValidationException;
import co.mv.wb.framework.ArgumentNullException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Fluent-style builder for creating Mockito-based mocks of {@link WildebeestApi}.
 *
 * @since 4.0
 */
public class WildebeestApiMockBuilder
{
	private WildebeestApi wildebeestApi;

	public WildebeestApiMockBuilder()
	{
		wildebeestApi = mock(WildebeestApi.class);
	}

	public WildebeestApiMockBuilder loadResourceReturns(Resource resource)
	{
		if (resource == null) throw new ArgumentNullException("resource");

		try
		{
			when(wildebeestApi.loadResource(any())).thenReturn(resource);
		}
		catch (FileLoadException e)
		{
			e.printStackTrace();
		}
		catch (LoaderFault loaderFault)
		{
			loaderFault.printStackTrace();
		}
		catch (PluginBuildException e)
		{
			e.printStackTrace();
		}
		catch (XmlValidationException e)
		{
			e.printStackTrace();
		}
		catch (InvalidReferenceException e)
		{
			e.printStackTrace();
		}

		return this;
	}

	public WildebeestApiMockBuilder loadInstanceReturns(Instance instance)
	{
		if (instance == null) throw new ArgumentNullException("instance");

		try
		{
			when(wildebeestApi.loadInstance(any())).thenReturn(instance);
		}
		catch (FileLoadException | PluginBuildException | LoaderFault | XmlValidationException e)
		{
			e.printStackTrace();
		}

		return this;
	}

	public WildebeestApi get()
	{
		return wildebeestApi;
	}
}
