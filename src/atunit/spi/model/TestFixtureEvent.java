package atunit.spi.model;


public interface TestFixtureEvent {

	public static enum Stage {
		BEFORE_INSTANTIATION
	}
	
	public Stage getStage();
	public TestFixture getTestFixture();
	
}
