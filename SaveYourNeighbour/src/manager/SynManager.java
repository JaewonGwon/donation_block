package manager;

import java.util.ArrayList;

import dao.SynDAO;
import vo.Admin;
import vo.Block;
import vo.FCamp;
import vo.Giver;
import vo.Institution;
import vo.Receiver;

public class SynManager {
	SynDAO dao = new SynDAO();
	ArrayList<Giver> gList = new ArrayList<>();
	ArrayList<Receiver> rList = new ArrayList<>();
	ArrayList<Block> bList = new ArrayList<>();
	ArrayList<Institution> iList = new ArrayList<>();
	ArrayList<String> sList = new ArrayList<>();
	ArrayList<Admin> aList = new ArrayList<>();
	ArrayList<FCamp> fList = new ArrayList<>();

	public int insertGiver(Giver g) {

		int result = 0;

		result = dao.insertGiver(g);

		return result;

	}

	public ArrayList<Giver> selectGiver() {

		gList = dao.selectGiver();

		return gList;

	}

	public int insertBlock(Block b) {

		int result = 0;

		result = dao.insertBlock(b);

		return result;

	}

	public ArrayList<Block> selectBlock() {

		bList = dao.selectBlock();

		return bList;
	}

	public int insertInst(Institution i) {

		int result = 0;

		result = dao.insertInst(i);

		return result;

	}

	public int insertReceiver(Receiver r) {

		int result = 0;

		result = dao.insertReceiver(r);

		return result;

	}

	public ArrayList<Block> searchBlock(String receiv_id) {

		bList = dao.searchBlock(receiv_id);

		return bList;
	}

	public ArrayList<Block> searchGiverBlock(String giv_id) {

		bList = dao.searchBlock(giv_id);

		return bList;
	}

	public int subBalance(int balance, String giv_id) {

		int result = 0;

		result = dao.subBalance(balance, giv_id);

		return result;

	}

	public int endInstitution(int balance, String inst_id) {

		int result = 0;

		result = dao.endInstitution(balance, inst_id);

		return result;

	}

	public int endReceiver(int balance, String receiv_id) {

		int result = 0;

		result = dao.endReceiver(balance, receiv_id);

		return result;
	}

	public ArrayList<String> receiversOnCampaign() {

		sList = dao.receiversOnCampaign();

		return sList;
	}

	public int endCampaign(String receiv_id) {

		int result = 0;

		result = dao.endCampaign(receiv_id);

		return result;

	}

	public ArrayList<Receiver> selectReceiver() {

		rList = dao.selectReceiver();

		return rList;
	}

	public String selectInstID(String receiv_id) {

		String result = "";

		result = dao.selectInstID(receiv_id);

		return result;
	}

	public boolean chainValid() {
		ArrayList<Block> testChain = new ArrayList<>();
		testChain = selectBlock();

		System.out.println();
		for (int i = 1; i < testChain.size(); i++) {
			if (!testChain.get(i).getPreviousHash().equals(testChain.get(i - 1).getHash())) {
				System.out.println("캠페인 무결성 검사 결과 : 체인 연결 오류 발생");
				return false;
			}

		}

		for (int i = 0; i < testChain.size(); i++) {
			if (!testChain.get(i).calculateHash().equals(testChain.get(i).getHash())) {
				System.out.println("캠페인 무결성 검사 결과 : 보증 내용 오류 발생");
				return false;
			}
		}

		System.out.println("캠페인 무결성 검사 결과 : 합격");
		return true;
	}

	public ArrayList<Institution> selectInst() {

		iList = dao.selectInst();

		return iList;
	}

	public ArrayList<Admin> selectAdmin() {

		aList = dao.selectAdmin();

		return aList;

	}

	public int findLoginType(String loginID) {

		for (Giver g : selectGiver()) {
			if (g.getGiv_id().equals(loginID)) {
				return 1;
			}
		}

		for (Receiver r : selectReceiver()) {
			if (r.getReceiv_id().equals(loginID)) {
				return 2;
			}
		}

		for (Institution i : selectInst()) {
			if (i.getInst_id().equals(loginID)) {
				return 3;
			}
		}

		for (Admin a : selectAdmin()) {
			if (a.getAdmin_id().equals(loginID)) {
				return 4;
			}
		}

		System.out.println("해당하는 정보를 찾을 수 없습니다.");
		return 0;
	}

	public int checkValidateGiver(Giver g) {

		for (Giver giv : selectGiver()) {
			if (giv.getGiv_id().equals(g.getGiv_id())) {
				if (giv.getGiv_pw().equals(g.getGiv_pw())) {
					return 1;
				}
			}
		}

		System.out.println("해당하는 정보를 찾을 수 없습니다.");
		return 0;
	}

	public int checkValidateReceiver(Receiver r) {

		for (Receiver rec : selectReceiver()) {
			if (rec.getReceiv_id().equals(r.getReceiv_id())) {
				if (rec.getReceiv_pw().equals(r.getReceiv_pw())) {
					return 2;
				}
			}
		}

		System.out.println("해당하는 정보를 찾을 수 없습니다.");
		return 0;
	}

	public int checkValidateInst(Institution i) {

		for (Institution inst : selectInst()) {
			if (inst.getInst_id().equals(i.getInst_id())) {
				if (inst.getInst_pw().equals(i.getInst_pw())) {
					return 3;
				}
			}
		}

		System.out.println("해당하는 정보를 찾을 수 없습니다.");
		return 0;
	}

	public int checkValidateAdmin(Admin a) {

		for (Admin admin : selectAdmin()) {
			if (admin.getAdmin_id().equals(a.getAdmin_id())) {
				if (admin.getAdmin_pw().equals(a.getAdmin_pw())) {
					return 4;
				}
			}
		}

		System.out.println("해당하는 정보를 찾을 수 없습니다.");
		return 0;
	}

	public boolean checkValidateNull(String id, String pw) {
		// id 혹은 비밀번호가 공란으로 입력되었는지 확인하는 메소드. pw의 경우 String ""의 해쉬코드값.
		boolean result = true;

		if (id.equals("") || pw.equals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")) {
			result = false;
		}

		return result;
	}

	public ArrayList<FCamp> selectFCamp() {

		fList = dao.selectFCamp();

		return fList;
	}

	public int insertFCamp(FCamp f) {
		int result = 0;

		result = dao.insertFCamp(f);

		return result;
	}

	public boolean rememberNoKorean(String input) {

		char c;

		for (int i = 0; i < input.length(); i++) {

			c = input.charAt(i);

			if (c >= 0x61 && c <= 0x7A) {
			} else if (c >= 0x30 && c <= 0x39) {
			} else {
				return false;
			}

		}

		return true;

	}
}
