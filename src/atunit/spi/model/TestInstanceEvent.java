package atunit.spi.model;


public interface TestInstanceEvent {
	
	public static enum Stage {
		BETWEEN_INSTANTIATION_AND_BEFORES,
		BETWEEN_BEFORES_AND_TEST_METHOD,
		BETWEEN_TEST_METHOD_AND_AFTERS,
		AFTER_AFTERS
	}
	
	public Stage getStage();
	public Object getTestInstance();
	public TestFixture getTestFixture();

}
