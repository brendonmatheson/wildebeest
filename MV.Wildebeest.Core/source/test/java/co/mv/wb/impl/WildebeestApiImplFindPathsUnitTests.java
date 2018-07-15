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

package co.mv.wb.impl;

import co.mv.wb.Migration;
import co.mv.wb.Resource;
import co.mv.wb.State;
import co.mv.wb.fixture.TestContext_SimpleFakeResource;
import co.mv.wb.fixture.TestContext_SimpleFakeResource_Builder;
import co.mv.wb.plugin.base.ImmutableState;
import co.mv.wb.plugin.fake.SetTagMigration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests to verify the {@link WildebeestApiImpl#findPaths(Resource, Optional, Optional)}} and
 * {@link WildebeestApiImpl#findPaths(Resource, Optional, Optional)} methods.
 *
 * @since 4.0
 */
public class WildebeestApiImplFindPathsUnitTests
{

	/**
	 * Refer to image FindPathsTestReferences/single_path.png
	 */
	@Test
	public void findPathWithSinglePathFromSourceToTarget()
	{
		TestContext_SimpleFakeResource context = TestContext_SimpleFakeResource_Builder
			.create()
			.withDefaultTarget("state4")
			.getResourceAndInstanceOnly();

		context.resource.getStates().addAll(createDummyStates(5));

		State s1 = context.resource.getStates().get(0);
		State s2 = context.resource.getStates().get(1);
		State s3 = context.resource.getStates().get(2);
		State s4 = context.resource.getStates().get(3);
		State s5 = context.resource.getStates().get(4);

		SetTagMigration m12 = createMigration(Optional.of(s1), Optional.of(s2), "state 1 -> state 2");
		SetTagMigration m15 = createMigration(Optional.of(s1), Optional.of(s5), "state 1 -> state 5");
		SetTagMigration m23 = createMigration(Optional.of(s2), Optional.of(s3), "state 2 -> state 3");
		SetTagMigration m34 = createMigration(Optional.of(s3), Optional.of(s4), "state 3 -> state 4");

		context.resource.getMigrations().add(m12);
		context.resource.getMigrations().add(m15);
		context.resource.getMigrations().add(m23);
		context.resource.getMigrations().add(m34);

		Optional<UUID> fromStateId = Optional.of(s1.getStateId());
		Optional<UUID> targetStateId = Optional.of(s4.getStateId());

		List<List<Migration>> paths = WildebeestApiImpl.findPaths(context.resource, fromStateId, targetStateId);
		assertTrue(paths.size() == 1);

		List<Migration> expectedMigrations = Arrays.asList(m12, m23, m34);
		assertEquals(paths.get(0), expectedMigrations);
	}

	/**
	 * Refer to image FindPathsTestReferences/basic_multiple_paths.png
	 */
	@Test
	public void findPathWithMultiplePathsFromSourceToTarget()
	{
		TestContext_SimpleFakeResource context = TestContext_SimpleFakeResource_Builder
			.create()
			.withDefaultTarget("state5")
			.getResourceAndInstanceOnly();

		context.resource.getStates().addAll(createDummyStates(5));
		State s1 = context.resource.getStates().get(0);
		State s2 = context.resource.getStates().get(1);
		State s3 = context.resource.getStates().get(2);
		State s4 = context.resource.getStates().get(3);
		State s5 = context.resource.getStates().get(4);

		//migrations
		SetTagMigration m12 = createMigration(Optional.of(s1), Optional.of(s2), "state 1 -> state 2");
		SetTagMigration m23 = createMigration(Optional.of(s2), Optional.of(s3), "state 2 -> state 3");
		SetTagMigration m24 = createMigration(Optional.of(s2), Optional.of(s4), "state 2 -> state 4");
		SetTagMigration m35 = createMigration(Optional.of(s3), Optional.of(s5), "state 3 -> state 5");
		SetTagMigration m45 = createMigration(Optional.of(s4), Optional.of(s5), "state 4 -> state 5");

		context.resource.getMigrations().add(m12);
		context.resource.getMigrations().add(m23);
		context.resource.getMigrations().add(m24);
		context.resource.getMigrations().add(m35);
		context.resource.getMigrations().add(m45);

		Optional<UUID> fromStateId = Optional.of(s1.getStateId());
		Optional<UUID> targetStateId = Optional.of(s5.getStateId());

		List<List<Migration>> paths = WildebeestApiImpl.findPaths(context.resource, fromStateId, targetStateId);

		assertTrue(paths.size() == 2);

		List<Migration> expectedMigrationSet1 = Arrays.asList(m12, m23, m35);
		List<Migration> expectedMigrationSet2 = Arrays.asList(m12, m24, m45);
		assertEquals(paths.get(0), expectedMigrationSet1);
		assertEquals(paths.get(1), expectedMigrationSet2);
	}


	/**
	 * Refer to image FindPathsTestReferences/non_existent_multiple_paths.png
	 */
	@Test
	public void findPathWithMultiplePathsFromNonExistentSourceToExistingTarget()
	{
		TestContext_SimpleFakeResource context = TestContext_SimpleFakeResource_Builder
			.create()
			.withDefaultTarget("state3")
			.getResourceAndInstanceOnly();

		context.resource.getStates().addAll(createDummyStates(4));
		State s1 = context.resource.getStates().get(0);
		State s2 = context.resource.getStates().get(1);
		State s3 = context.resource.getStates().get(2);
		State s4 = context.resource.getStates().get(3);

		//migrations
		SetTagMigration m_1 = createMigration(Optional.empty(), Optional.of(s1), "state_ -> state 1");
		SetTagMigration m13 = createMigration(Optional.of(s1), Optional.of(s3), "state 1 -> state 3");
		SetTagMigration m34 = createMigration(Optional.of(s3), Optional.of(s4), "state 3 -> state 4");
		SetTagMigration m_2 = createMigration(Optional.empty(), Optional.of(s2), "state_ -> state 2");
		SetTagMigration m23 = createMigration(Optional.of(s2), Optional.of(s3), "state 2 -> state 3");

		context.resource.getMigrations().add(m_1);
		context.resource.getMigrations().add(m13);
		context.resource.getMigrations().add(m34);
		context.resource.getMigrations().add(m_2);
		context.resource.getMigrations().add(m23);

		Optional<UUID> fromStateId = Optional.empty();
		Optional<UUID> targetStateId = Optional.of(s4.getStateId());

		List<List<Migration>> paths = WildebeestApiImpl.findPaths(context.resource, fromStateId, targetStateId);

		assertTrue(paths.size() == 2);

		List<Migration> expectedMigrationSet1 = Arrays.asList(m_1, m13, m34);
		List<Migration> expectedMigrationSet2 = Arrays.asList(m_2, m23, m34);
		assertEquals(paths.get(0), expectedMigrationSet1);
		assertEquals(paths.get(1), expectedMigrationSet2);
	}

	/**
	 * Refer to image FindPathsTestReferences/state_to_non_existent_state.png
	 */
	@Test
	public void findPathWithMultiplePathsFromSourceStateToNonExistentState()
	{
		TestContext_SimpleFakeResource context = TestContext_SimpleFakeResource_Builder
			.create()
			.withDefaultTarget("state_")
			.getResourceAndInstanceOnly();

		context.resource.getStates().addAll(createDummyStates(4));

		State s1 = context.resource.getStates().get(0);
		State s2 = context.resource.getStates().get(1);
		State s3 = context.resource.getStates().get(2);
		State s4 = context.resource.getStates().get(3);

		//migrations
		SetTagMigration m12 = createMigration(Optional.of(s1), Optional.of(s2), "state 1 -> state 2");
		SetTagMigration m23 = createMigration(Optional.of(s2), Optional.of(s3), "state 2 -> state 3");
		SetTagMigration m3_ = createMigration(Optional.of(s3), Optional.empty(), "state 3 -> state_");
		SetTagMigration m24 = createMigration(Optional.of(s2), Optional.of(s4), "state 2 -> state 4");
		SetTagMigration m4_ = createMigration(Optional.of(s4), Optional.empty(), "state 4 -> state_");

		context.resource.getMigrations().add(m12);
		context.resource.getMigrations().add(m23);
		context.resource.getMigrations().add(m3_);
		context.resource.getMigrations().add(m24);
		context.resource.getMigrations().add(m4_);

		Optional<UUID> fromStateId = Optional.of(s1.getStateId());
		Optional<UUID> targetStateId = Optional.empty();

		List<List<Migration>> paths = WildebeestApiImpl.findPaths(context.resource, fromStateId, targetStateId);
		assertTrue(paths.size() == 2);

		List<Migration> expectedMigrationsSet1 = Arrays.asList(m12, m23, m3_);
		List<Migration> expectedMigrationsSet2 = Arrays.asList(m12, m24, m4_);
		assertEquals(paths.get(0), expectedMigrationsSet1);
		assertEquals(paths.get(1), expectedMigrationsSet2);
	}

	/**
	 * Refer to image FindPathsTestReferences/no_path_found.png
	 */
	@Test
	public void findPathWithNoPathFromSourceToTarget()
	{
		TestContext_SimpleFakeResource context = TestContext_SimpleFakeResource_Builder
			.create()
			.withDefaultTarget("state3")
			.getResourceAndInstanceOnly();

		context.resource.getStates().addAll(createDummyStates(4));

		State s1 = context.resource.getStates().get(0);
		State s2 = context.resource.getStates().get(1);
		State s3 = context.resource.getStates().get(2);
		State s4 = context.resource.getStates().get(3);

		SetTagMigration m_1 = createMigration(Optional.empty(), Optional.of(s1), "state_ -> state 1");
		SetTagMigration m12 = createMigration(Optional.of(s1), Optional.of(s2), "state 1 -> state 2");
		SetTagMigration m24 = createMigration(Optional.of(s2), Optional.of(s4), "state 2 -> state 4");
		SetTagMigration m_3 = createMigration(Optional.empty(), Optional.of(s3), "state_ -> state 3");
		SetTagMigration m34 = createMigration(Optional.of(s3), Optional.of(s4), "state 3 -> state 4");

		context.resource.getMigrations().add(m_1);
		context.resource.getMigrations().add(m12);
		context.resource.getMigrations().add(m24);
		context.resource.getMigrations().add(m_3);
		context.resource.getMigrations().add(m34);

		Optional<UUID> currentStateId = Optional.of(s1.getStateId());
		Optional<UUID> targetStateId = Optional.of(s3.getStateId());

		List<List<Migration>> paths = WildebeestApiImpl.findPaths(context.resource, currentStateId, targetStateId);

		assertTrue(paths.size() == 0);
	}

	/**
	 * Refer to image FindPathsTestReferences/circular_example.png
	 */
	@Test
	public void findPathCircular()
	{
		TestContext_SimpleFakeResource context = TestContext_SimpleFakeResource_Builder
			.create()
			.withDefaultTarget("state5")
			.getResourceAndInstanceOnly();

		context.resource.getStates().addAll(createDummyStates(5));
		State s1 = context.resource.getStates().get(0);
		State s2 = context.resource.getStates().get(1);
		State s3 = context.resource.getStates().get(2);
		State s4 = context.resource.getStates().get(3);
		State s5 = context.resource.getStates().get(4);

		SetTagMigration m12 = createMigration(Optional.of(s1), Optional.of(s2), "state 1 -> state 2");
		SetTagMigration m23 = createMigration(Optional.of(s2), Optional.of(s3), "state 2 -> state 3");
		SetTagMigration m34 = createMigration(Optional.of(s3), Optional.of(s4), "state 3 -> state 4");
		SetTagMigration m42 = createMigration(Optional.of(s4), Optional.of(s2), "state 4 -> state 2");
		SetTagMigration m35 = createMigration(Optional.of(s3), Optional.of(s5), "state 3 -> state 5");

		context.resource.getMigrations().add(m12);
		context.resource.getMigrations().add(m23);
		context.resource.getMigrations().add(m34);
		context.resource.getMigrations().add(m42);
		context.resource.getMigrations().add(m35);

		Optional<UUID> fromStateId = Optional.of(s1.getStateId());
		Optional<UUID> targetStateId = Optional.of(s5.getStateId());

		List<List<Migration>> paths = WildebeestApiImpl.findPaths(context.resource, fromStateId, targetStateId);
		assertTrue(paths.size() == 1);

		List<Migration> expectedMigrationsSet1 = Arrays.asList(m12, m23, m35);
		assertEquals(paths.get(0), expectedMigrationsSet1);
	}

	private List<State> createDummyStates(int nStates)
	{
		List<State> states = new ArrayList<>();
		for (int i = 1; i <= nStates; i++)
		{
			states.add(new ImmutableState(
				UUID.randomUUID(),
				Optional.of("state" + i)));
		}

		return states;
	}

	private SetTagMigration createMigration(
		Optional<State> fromState,
		Optional<State> toState,
		String tag)
	{
		Optional<String> fromStateUUID;
		if (fromState.isPresent())
		{
			fromStateUUID = Optional.of(fromState.get().getStateId().toString());
		}
		else
		{
			fromStateUUID = Optional.empty();
		}

		Optional<String> toStateUUID;
		if (toState.isPresent())
		{
			toStateUUID = Optional.of(toState.get().getStateId().toString());
		}
		else
		{
			toStateUUID = Optional.empty();
		}

		return new SetTagMigration(UUID.randomUUID(), fromStateUUID, toStateUUID, tag);
	}
}