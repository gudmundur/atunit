package atunit.example.subjects;

public class UserManagerImpl implements UserManager {

	private UserDao dao;
	final Logger log;
	
	public UserManagerImpl(UserDao dao, Logger log) {
		this.dao = dao;
		this.log = log;
	}
	
	public UserManagerImpl(Logger log) {
		this.log = log;
	}
	
	public void setUserDao(UserDao dao) {
		this.dao = dao;
	}
	
	public User getUser(Integer id) {
		log.debug("user " + id + " retrieved");
		return dao.load(id);
	}

}
