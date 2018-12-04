package vo;

import java.security.MessageDigest;
import java.util.Date;

public class Block {
	private String hash;
	private String previousHash;
	private String receiv_id; //어떤 캠페인인지 확인하기 위한 변수
	private String giv_id; //Transaction의 송금자를 확인하기 위한 변수
	private int raisedFund; //data
	private long timeStamp;
	
	public Block(String previousHash, int raisedFund, String giv_id, String receiv_id) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.raisedFund = raisedFund;
		this.receiv_id = receiv_id;
		this.giv_id = giv_id;
		this.hash = calculateHash();
	}
	
	public Block() {
		
	}
	
	public String calculateHash() {
		String calculatedhash = applySha256(
				previousHash +
				Long.toString(timeStamp)
				+ Integer.toString(raisedFund) +
				receiv_id + giv_id
				);
		return calculatedhash;
	}
	
	public String applySha256(String input) {	// SHA-256 알고리즘으로 String input의 HashCode 생성, String의 형태로 출력.
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("MS949"));
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getReceiv_id() {
		return receiv_id;
	}

	public void setReceiv_id(String receiv_id) {
		this.receiv_id = receiv_id;
	}
	
	public String getGiv_id() {
		return giv_id;
	}

	public void setGiv_id(String giv_id) {
		this.giv_id = giv_id;
	}

	public int getRaisedFund() {
		return raisedFund;
	}

	public void setRaisedFund(int raisedFund) {
		this.raisedFund = raisedFund;
	}

	
	
	
	@Override
	public String toString() {
		return "Block [hash=" + hash + ", previousHash=" + previousHash + ", receiv_id=" + receiv_id + ", raisedFund="
				+ raisedFund + ", timeStamp=" + timeStamp + "]";
	}


	
	
}
