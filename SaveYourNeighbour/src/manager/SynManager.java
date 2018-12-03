package manager;

import java.util.ArrayList;

import dao.SynDAO;
import vo.Block;
import vo.Giver;
import vo.Institution;
import vo.Receiver;

public class SynManager {
	SynDAO dao = new SynDAO();
	ArrayList<Giver> gList = new ArrayList<>();
	ArrayList<Receiver> rList = new ArrayList<>();
	ArrayList<Block> bList = new ArrayList<>();
	ArrayList<String> sList = new ArrayList<>();

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
	
	public String selectInstID (String receiv_id) {
		
		String result = "";
		
		result = dao.selectInstID(receiv_id);
		
		
		return result;
	}
}
