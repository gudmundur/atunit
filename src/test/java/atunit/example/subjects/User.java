package atunit.example.subjects;

public class User {
	
	private Integer id;
	private String username;
	
	public User() {
	}
	
	public User(Integer id, String username) {
		this.id = id;
		this.username = username;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	

}
