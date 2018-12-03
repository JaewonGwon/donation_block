package vo;

public class Giver {

	private String giv_id;
	private String giv_pw;
	private int balance;

	public Giver(String giv_id, String giv_pw, int balance) {
		super();
		this.giv_id = giv_id;
		this.giv_pw = giv_pw;
		this.balance = balance;
	}

	public Giver() {

	}

	public String getGiv_id() {
		return giv_id;
	}

	public void setGiv_id(String giv_id) {
		this.giv_id = giv_id;
	}

	public String getGiv_pw() {
		return giv_pw;
	}

	public void setGiv_pw(String giv_pw) {
		this.giv_pw = giv_pw;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Giver [giv_id=" + giv_id + ", giv_pw=" + giv_pw + ", balance=" + balance + "]";
	}

	
}
