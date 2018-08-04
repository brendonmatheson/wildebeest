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

package co.mv.wb.cli;

import picocli.CommandLine;

/**
 * Command definition for JumpState.
 *
 * @since 4.0
 */
@CommandLine.Command(name = "jumpstate",
	description = " Tell Wildebeest that a resource instance is in a particular state",
	subcommands = CommandLine.HelpCommand.class)
public class JumpStateCommand extends SharedCommands
{
	@CommandLine.Option(names = {"-t", "--target-state"}, description = "Target state")
	String targetState;
}
