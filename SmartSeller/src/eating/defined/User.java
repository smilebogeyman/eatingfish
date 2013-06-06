package eating.defined;
import java.io.Serializable;

public class User implements Serializable{
	private String userName;
	private String password;
	private String operation;
	public User(){
		
	}
	public User(String u, String p, String o){
		this.userName = u;
		this.password = p;
		this.operation = o;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}


}
