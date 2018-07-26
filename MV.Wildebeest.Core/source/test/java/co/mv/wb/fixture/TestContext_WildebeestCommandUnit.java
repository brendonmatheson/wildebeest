package co.mv.wb.fixture;

import co.mv.wb.Instance;
import co.mv.wb.Resource;
import co.mv.wb.WildebeestApi;
import co.mv.wb.cli.WildebeestCommand;

import java.io.PrintStream;

/**
 * A test context providing an instance of WildebeestCommand with a mocked WildebeestApi configured to return a fake
 * Resource from loadResource and a FakeInstance from loadInstance.
 *
 * @since 4.0
 */
public class TestContext_WildebeestCommandUnit
{
	public final PrintStream output;
	public final Resource fakeResource;
	public final Instance fakeInstance;
	public final WildebeestApi wildebeestApi;
	public final WildebeestCommand wildebeestCommand;

	public static TestContext_WildebeestCommandUnit create()
	{
		return new TestContext_WildebeestCommandUnit();
	}

	private TestContext_WildebeestCommandUnit()
	{
		this.output = System.out;

		this.fakeResource = Fixtures.fakeResource();
		this.fakeInstance = Fixtures.fakeInstance();

		this.wildebeestApi = Fixtures
			.wildebeestApi()
			.loadResourceReturns(fakeResource)
			.loadInstanceReturns(fakeInstance)
			.get();

		this.wildebeestCommand = new WildebeestCommand(
			output,
			wildebeestApi);
	}
}
