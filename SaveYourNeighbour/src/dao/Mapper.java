package dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import vo.Admin;
import vo.Block;
import vo.FCamp;
import vo.Giver;
import vo.Institution;
import vo.Receiver;

public interface Mapper {
	public int insertGiver(Giver g);
	public int insertBlock(Block b);
	public int insertInst(Institution i);
	public int insertReceiver(Receiver r);
	public int insertFCamp(FCamp f);
	
	public ArrayList<Receiver> selectReceiver();
	public ArrayList<Giver> selectGiver();
	public ArrayList<Block> selectBlock();
	public ArrayList<Institution> selectInst();
	public ArrayList<Admin> selectAdmin();
	public ArrayList<FCamp> selectFCamp();
	
	public ArrayList<Block> searchBlock(String receiv_id);
	public ArrayList<Block> searchGiverBlock(String giv_id);
	public String selectInstID(String receiv_id);
	
	public int subBalance(@Param ("balance") int balance, @Param ("giv_id") String giv_id);
	public int endReceiver(@Param ("balance") int balance, @Param ("receiv_id") String receiv_id);
	public int endInstitution(@Param ("balance") int balance, @Param ("inst_id") String inst_id);
	public int endCampaign(String receiv_id);
	public ArrayList<String> receiversOnCampaign();
}
