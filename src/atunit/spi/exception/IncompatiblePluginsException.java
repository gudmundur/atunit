package atunit.spi.exception;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Join;

import atunit.spi.plugin.AtUnitPlugin;

public class IncompatiblePluginsException extends InvalidTestException {

	private static final long serialVersionUID = 1L;

	public IncompatiblePluginsException(Class<? extends AtUnitPlugin>... classes ) {
		super(join(classes) + " are incompatible");
	}
	
	private static String join(Class<?>[] classes) {
		int s = classes.length;
		if ( s < 2 ) {
			throw new IllegalArgumentException("Must specify at least two incompatible plugin classes");
		}
		
		List<Class<?>> list = Arrays.asList(classes);
		return Join.join(", ", list.subList(0, list.size()-1)) + (s > 2 ? "," : "") + " and " + list.get(list.size()-1);
	}
}
