package atunit.example.subjects;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class GuiceUser extends User {

	@Inject
	public GuiceUser(@Named("user.id") Integer id, @Named("user.name") String username) {
		super(id, username);
	}

}
