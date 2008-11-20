package atunit.example.subjects;

import com.google.inject.Inject;

public class GuiceUserManager extends UserManagerImpl {

	@Inject
	public GuiceUserManager(UserDao dao, Logger log) {
		super(dao, log);
		log.debug("GuiceUserManager instantiated");
	}

}
