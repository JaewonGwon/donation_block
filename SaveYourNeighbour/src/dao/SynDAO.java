package dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import vo.Admin;
import vo.Block;
import vo.Giver;
import vo.Institution;
import vo.Receiver;

public class SynDAO {

	private SqlSessionFactory factory = MybatisConfig.getSqlSessionFactory();

	public int insertGiver(Giver g) {

		int result = 0;
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		try {
			result = mapper.insertGiver(g);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}

		return result;
	}

	public ArrayList<Giver> selectGiver() {
		ArrayList<Giver> gList = new ArrayList<>();

		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		gList = mapper.selectGiver();
		session.close();

		return gList;
	}

	public int insertBlock(Block b) {
		int result = 0;

		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		result = mapper.insertBlock(b);
		
		
		session.commit();


		return result;

	}

	public ArrayList<Block> selectBlock() {
		ArrayList<Block> bList = new ArrayList<>();

		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		bList = mapper.selectBlock();
		session.close();

		return bList;
	}

	public int insertInst(Institution i) {
		int result = 0;

		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		try {
			result = mapper.insertInst(i);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return result;

	}

	public int insertReceiver(Receiver r) {
		int result = 0;

		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		try {
			result = mapper.insertReceiver(r);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return result;

	}

	public ArrayList<Block> searchBlock(String receiv_id) {
		ArrayList<Block> result = new ArrayList<>();

		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		try {
			result = mapper.searchBlock(receiv_id);
			session.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();

			}
		}

		return result;

	}
	
	public ArrayList<Block> searchGiverBlock(String giv_id) {
		ArrayList<Block> result = new ArrayList<>();

		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);

		try {
			result = mapper.searchBlock(giv_id);
			session.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();

			}
		}

		return result;

	}

	
	public int subBalance(int balance, String giv_id) {
		int result = 0;
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			result = mapper.subBalance(balance, giv_id);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return result;
	}
	
	public int endReceiver(int balance, String receiv_id) {
		int result = 0;
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			result = mapper.endReceiver(balance, receiv_id);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return result;
	}
	
	public int endInstitution(int balance, String inst_id) {
		int result = 0;
		
		SqlSession session = factory.openSession();
		Mapper mp = session.getMapper(Mapper.class);
		
		try {
			mp.endInstitution(balance, inst_id);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return result;
	}
	
	public ArrayList<String> receiversOnCampaign() {
		ArrayList<String> tempList = new ArrayList<>();
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			tempList = mapper.receiversOnCampaign();
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return tempList;
	}
	
	public int endCampaign (String receiv_id) {
		int result = 0;
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			result = mapper.endCampaign(receiv_id);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();	
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return result;
	}
	
	public ArrayList<Receiver> selectReceiver() {
		ArrayList<Receiver> tempList = new ArrayList<>();
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			tempList = mapper.selectReceiver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return tempList;
	}
	
	public String selectInstID(String receiv_id) {
		
		String result = "";
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			result = mapper.selectInstID(receiv_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return result;
		
				
	}
	
	public ArrayList<Institution> selectInst() {
		ArrayList<Institution> iList = new ArrayList<>();
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			iList = mapper.selectInst();
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return iList;
		
		
	}
	
	public ArrayList<Admin> selectAdmin() {
		ArrayList<Admin> admin = new ArrayList<>();
		
		SqlSession session = factory.openSession();
		Mapper mapper = session.getMapper(Mapper.class);
		
		try {
			admin = mapper.selectAdmin();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session!=null) {
				session.close();
			}
		}
		
		return admin;
	}
}
