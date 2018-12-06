package vo;

public class FCamp {
	private String FCamp_ID;
	private String receiv_id;
	private int receiv_balance;

	public FCamp(String receiv_id, int receiv_balance) {
		super();
		FCamp_ID = null;
		this.receiv_id = receiv_id;
		this.receiv_balance = receiv_balance;
	}

	public FCamp() {

	}

	public String getFCamp_ID() {
		return FCamp_ID;
	}

	public void setFCamp_ID(String fCamp_ID) {
		FCamp_ID = fCamp_ID;
	}

	public String getReceiv_id() {
		return receiv_id;
	}

	public void setReceiv_id(String receiv_id) {
		this.receiv_id = receiv_id;
	}

	public int getReceiv_balance() {
		return receiv_balance;
	}

	public void setReceiv_balance(int receiv_balance) {
		this.receiv_balance = receiv_balance;
	}

	@Override
	public String toString() {
		return "[" + FCamp_ID + "회]" + "클라이언트 ID : " + receiv_id + "  모금액 : " + receiv_balance + "HOPE";
	}

}
