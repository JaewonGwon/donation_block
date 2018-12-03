package vo;

public class Receiver {

	private String receiv_id;
	private String receiv_pw;
	private int balance;
//	private Location location;
	private String inst_id;
	
	public Receiver(String receiv_id, String receiv_pw, int balance, String inst_id) {
		super();
		this.receiv_id = receiv_id;
		this.receiv_pw = receiv_pw;
		this.balance = balance;
//		this.location = location;
		this.inst_id = inst_id;
	}
	
	public Receiver() {
		
	}

	public String getReceiv_id() {
		return receiv_id;
	}

	public void setReceiv_id(String receiv_id) {
		this.receiv_id = receiv_id;
	}

	public String getReceiv_pw() {
		return receiv_pw;
	}

	public void setReceiv_pw(String receiv_pw) {
		this.receiv_pw = receiv_pw;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

//	public Location getLocation() {
//		return location;
//	}
//
//	public void setLocation(Location location) {
//		this.location = location;
//	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	
	
	
}
