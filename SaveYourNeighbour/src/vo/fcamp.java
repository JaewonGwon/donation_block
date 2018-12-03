package vo;

public class fcamp {
	private int x;
	private int y;
	private String address;

	public fcamp(int x, int y, String address) {
		super();
		this.x = x;
		this.y = y;
		this.address = address;
	}

	public fcamp() {

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
