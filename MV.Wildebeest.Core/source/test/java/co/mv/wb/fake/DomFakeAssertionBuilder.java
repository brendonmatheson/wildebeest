// Wildebeest Migration Framework
// Copyright © 2013 - 2015, Zen Digital Co Inc
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

package co.mv.wb.fake;

import co.mv.wb.Assertion;
import co.mv.wb.service.dom.BaseDomAssertionBuilder;
import java.util.UUID;

public class DomFakeAssertionBuilder extends BaseDomAssertionBuilder
{
	@Override public Assertion build(UUID assertionId, int seqNum)
	{
		String tag = this.getElement().getChildNodes().item(0).getTextContent();
		
		return new FakeAssertion(assertionId, seqNum, tag);
	}
}
