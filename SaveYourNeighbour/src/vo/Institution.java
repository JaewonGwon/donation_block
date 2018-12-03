package vo;

public class Institution {

	private String inst_id;
	private String inst_pw;
	private int balance;

	public Institution(String inst_id, String inst_pw, int balance) {
		super();
		this.inst_id = inst_id;
		this.inst_pw = inst_pw;
		this.balance = balance;
		}

	public Institution() {

	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public String getInst_pw() {
		return inst_pw;
	}

	public void setInst_pw(String inst_pw) {
		this.inst_pw = inst_pw;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}


}
