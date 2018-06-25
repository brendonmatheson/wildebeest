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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Utility methods for use within the test suite.  Some of these methods could graduate to the product Utils class but
 * need to be made more robust first.
 *
 * @since 4.0
 */
public class TestUtils
{
	/**
	 * Read all text from the file at the specified path.
	 *
	 * @param filename the path to the file to read
	 * @return the content of the file as a String
	 * @since 4.0
	 */
	public static String readAllText(String filename)
	{
		if (filename == null) throw new ArgumentNullException("filename");

		final String result;

		try
		{
			result = new String(Files.readAllBytes(new File(filename).toPath()));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		return result;
	}
}
