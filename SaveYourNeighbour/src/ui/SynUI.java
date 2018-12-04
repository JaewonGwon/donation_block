package ui;

import java.util.ArrayList;
import java.util.Scanner;

import manager.SynManager;
import vo.Admin;
import vo.Block;
import vo.Giver;
import vo.Institution;
import vo.Receiver;

public class SynUI {
	public int login_flag = 0; // 0 : Logged_out, 1 : Giver, 2 : Receiver, 3 : Institution, 4 : Admin
	private Scanner sc = new Scanner(System.in); // Scanner for nextInt()
	private Scanner scl = new Scanner(System.in); // Scanner for nextLine()
	private boolean flag = true;
	private String loginID;
	SynManager sm = new SynManager();

	public SynUI() {
		while (flag) {
			if (login_flag == 0) {
				login();
			} else if (login_flag == 1) {
				givMainMenu();
				givMenu(switcher());
			} else if (login_flag == 2) {
				recMainMenu();
				recMenu(switcher());
			} else if (login_flag == 3) {
				instMainMenu();
				instMenu(switcher());
			} else if (login_flag == 4) {
				adminMainMenu();
				adminMenu(switcher());
			}
		}
	}
	
	
	public void startCampaign() {
		System.out.println("0을 입력하면 취소");
		System.out.print("클라이언트 ID : ");
		String receiv_id = scl.nextLine();
		if (receiv_id.equals("0")) {
			return;
		}
		int balance = 0;
		while (true) {
			System.out.print("목표 모금액 : ");
			try {
				balance = sc.nextInt();
				break;
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("잘못 입력되었습니다.");
			}
		}
		if (balance == 0) {
			return;
		}
		Block temp = new Block(receiv_id, balance, "genesis_block", receiv_id);
		sm.insertBlock(temp);
	}

	public void endCampaign() {
		System.out.println("종료할 캠페인이 있는지 확인합니다."); // 종료 기준 : 목표 금액 달성(초과)
		System.out.println("현재 캠페인을 진행하고 있는 클라이언트들은 다음과 같습니다.");
		System.out.println("─────────────────────────────-");
		ArrayList<String> temp = new ArrayList<>();
		temp = sm.receiversOnCampaign(); // 진행중인 캠페인의 receiv_id의 리스트
		for (String s : temp) { // 각 receiv_id의 체인을 검색하여
			int compareBalance = 0;
			ArrayList<Block> tempChain = sm.searchBlock(s);
			for (Block b : tempChain) {
				compareBalance += b.getRaisedFund();
			}
			if ((sm.searchBlock(s).get(0).getRaisedFund() * 2) <= compareBalance) { // 금액을 초과했는지 확인
				int input = 0;
				while (true) {
					System.out.println(s + "님에 대한 캠페인이 종료 가능합니다. 종료하시겠습니까?(1. 캠페인 종료, 9. 취소)");
					try {
						input = sc.nextInt();
						break;
					} catch (Exception e1) {
						sc.nextLine();
						System.out.println("잘못 입력하셨습니다.");
					}
				}
				switch (input) {
				case 1:
					boolean doubleFlag = true;
					boolean needInst = true;
					int totalRaisedFund = (compareBalance - sm.searchBlock(s).get(0).getRaisedFund());
					System.out.println("캠페인 종료 작업을 시작합니다.");
					double com_rate = 0;
					while (doubleFlag) {
						System.out.println("지원 기관에 대한 수수료 비율을 설정하세요.(0.0 ~ 0.05; 최대 5%까지 허용)");
						while (true) {
							try {
								com_rate = sc.nextDouble();
								break;
							} catch (Exception e1) {
								sc.nextLine();
								System.out.println("잘못 입력하셨습니다.");
							}
						}
						if (com_rate > 0.05 || com_rate < 0.0) {
							System.out.println("올바른 수수료 비율을 입력해주세요.");
							break;
						} else {
							try {
								String inst_ = sm.selectInstID(s);
								sm.endInstitution((int) (totalRaisedFund * com_rate), inst_);
							} catch (Exception e) {
								System.out.println("지원 기관이 없습니다. 지원 기관을 설정해주세요.");
								needInst = false;
							}
							doubleFlag = false;
							needInst = true;
							break;
						}

					}
					if (needInst) {
						System.out.println((int) (totalRaisedFund * (1 - com_rate)));
						sm.endReceiver((int) (totalRaisedFund * (1 - com_rate)), s); // receiv_id에 해당하는 balance에 총 모금액을
																						// 입금
						sm.endCampaign(s); // 해당하는 id를 receiv_id로 가지는 체인 내의 모든 블록 삭제
						break;
					}
				case 9:
					break;
				default:

				}
			} else {
				System.out.println(s + "님에 대한 캠페인은 아직 종료가 불가능합니다. 앞으로 "
						+ ((sm.searchBlock(s).get(0).getRaisedFund() * 2) - compareBalance) + "HOPE가 더 필요합니다.");
			}
		}
	}

	public void showChainStatus() { // receiv_id를 입력받아 genesis_block을 제외한 체인에 기록된 모든 기부액을 출력
		int total_bal = 0;

		System.out.print("캠페인 ID : ");
		String receiv_id = scl.nextLine();
		if (sm.searchBlock(receiv_id).size() == 0) {

			System.out.println("해당 아이디로 검색되는 캠페인이 없습니다.");

		} else {
			for (Block b : sm.searchBlock(receiv_id)) { // 먼저 selectBlock 메서드로 모든 블락을 불러온 뒤
				if (b.getGiv_id() != "genesis_block") {
					total_bal += b.getRaisedFund();
				}
			}

			System.out.println(receiv_id + " 캠페인의 현재 모금액은 " + total_bal + "HOPE 입니다.");
		}
	}

	public void showCampaigns() {
		ArrayList<String> temp = new ArrayList<>();
		temp = sm.receiversOnCampaign(); // 진행중인 캠페인의 receiv_id의 리스트
		for (String s : temp) { // 각 receiv_id의 체인을 검색하여
			int compareBalance = 0;
			ArrayList<Block> tempChain = sm.searchBlock(s);
			for (Block b : tempChain) {
				compareBalance += b.getRaisedFund();
			}
			if ((sm.searchBlock(s).get(0).getRaisedFund() * 2) <= compareBalance) { // 금액을 초과했는지 확인
				System.out.println(s + "님에 대한 캠페인이 목표를 달성했습니다!");
			} else {
				System.out.println(s + "님에 대한 캠페인은 아직 도움이 더 필요합니다. 앞으로 "
						+ ((sm.searchBlock(s).get(0).getRaisedFund() * 2) - compareBalance) + "HOPE가 더 필요합니다!");
			}
		}
	}

	public void sendMoney() {
		int subBalance = 0;
		boolean available = false; // 대상이 없을 경우 활용할 flag
		System.out.println("0을 입력하면 취소됩니다.");
		System.out.print("보낼 ID : ");
		String giv_id = scl.nextLine();
		if (giv_id.equals("0"))
			return;
		while (true) {
			System.out.print("클라이언트 ID : ");
			String receiv_id = scl.nextLine();
			if (receiv_id.equals("0"))
				return;
			for (Block r : sm.searchBlock(receiv_id)) {
				if (r.getReceiv_id().equals(receiv_id)) {
					available = true;
				}
			}
			if (available) {
				int balance = 0;
				while (true) {
					System.out.print("보낼 금액 : ");
					try {
						balance = sc.nextInt();
						if (balance == 0)
							return;
						break;
					} catch (Exception e) {
						sc.nextLine();
						System.out.println("잘못 입력하셨습니다.");
					}
				}
				for (Giver g : sm.selectGiver()) { // giv_id의 balance값을 먼저 감소
					if (g.getGiv_id().equals(giv_id)) {
						if (g.getBalance() < balance) { // 받은 값이 giv_id의 balance보다 큰지 확인
							System.out.println("잔고가 부족합니다.");
							return;
						}
						subBalance = g.getBalance() - balance;
						sm.subBalance(subBalance, giv_id);
						break;
					}
				}
				addBlock(receiv_id, giv_id, balance);
				break;
			} else {
				System.out.println("수취인의 정보가 잘못되었거나, 아직 시작되지 않은 캠페인입니다.");
			}
		}

	}

	public int showBalance() {
		switch (login_flag) {
		case 1:
			for (Giver g : sm.selectGiver()) {
				if (g.getGiv_id().equals(loginID))
					return g.getBalance();
			}
			break;
		case 2:
			for (Receiver r : sm.selectReceiver()) {
				if (r.getReceiv_id().equals(loginID))
					return r.getBalance();
			}
			break;
		case 3:
			for (Institution i : sm.selectInst()) {
				if (i.getInst_id().equals(loginID))
					return i.getBalance();
			}
			break;
		}
		return 0;
	}

	public void raiseMoney() {
		int subBalance = 0;
		boolean available = false; // 대상이 없을 경우 활용할 flag
		System.out.println("0을 입력하면 취소됩니다.");
		System.out.println("기부하실 클라이언트의 ID를 입력해주세요.");
		while (true) {
			System.out.print("ID : ");
			String receiv_id = scl.nextLine();
			if (receiv_id.equals("0"))
				return;
			for (Block r : sm.searchBlock(receiv_id)) {
				if (r.getReceiv_id().equals(receiv_id)) {
					available = true;
				}
			}
			if (available) {
				int balance = 0;
				while (true) {
					System.out.print("보낼 금액 : ");
					try {
						balance = sc.nextInt();
						if (balance == 0)
							return;
						break;
					} catch (Exception e) {
						sc.nextLine();
						System.out.println("잘못 입력하셨습니다.");
					}
				}
				for (Giver g : sm.selectGiver()) { // giv_id의 balance값을 먼저 감소
					if (g.getGiv_id().equals(loginID)) {
						if (g.getBalance() < balance) { // 받은 값이 giv_id의 balance보다 큰지 확인
							System.out.println("잔고가 부족합니다.");
							return;
						}
						subBalance = g.getBalance() - balance;
						sm.subBalance(subBalance, loginID);
						break;
					}
				}
				addBlock(receiv_id, loginID, balance);
				break;
			} else {
				System.out.println("수취인의 정보가 잘못되었거나, 아직 시작되지 않은 캠페인입니다.");
			}
		}
	}

	public void addBlock(String receiv_id, String giv_id, int balance) {
		ArrayList<Block> tempList = new ArrayList<>();
		tempList = sm.searchBlock(receiv_id); // receiv_id로 검색된 리스트; 캠페인 내의 블록들을 검색하여 리스트로 만듬;
												// 반드시 Timestamp 기준 ASC 정렬할 것

		if (tempList.size() > 0) { // 검색된 블록이 genesis block을 가졌거나, 혹은 진행중인 체인인지 확인
//			System.out.println(tempList.get(tempList.size()-1));
//			System.out.println(tempList.get(tempList.size()-1).getHash());
//			System.out.println(tempList.size());
			sm.insertBlock(new Block(tempList.get(tempList.size() - 1).getHash(), balance, giv_id, receiv_id));
		} else {
			System.out.println("수취인의 정보가 잘못되었거나, 아직 시작되지 않은 캠페인입니다.");
		}

	}

	public void login() {
		boolean flag = true;
		while (flag) {
			Block b = new Block(); // Block 객체의 SHA-256 메서드 활용을 위해..
			System.out.println("────────────────");
			System.out.println(" Save. Your. Neighbour.  S.Y.N  ");
			System.out.println("────────────────");
			System.out.print("ID : ");
			loginID = scl.nextLine();
			System.out.print("PW : ");
			String hashedPW = b.applySha256(scl.nextLine());
			System.out.println(hashedPW);
			switch (sm.findLoginType(loginID)) {
			case 1:
				Giver g = new Giver(loginID, hashedPW, 0);
				login_flag = sm.checkValidateGiver(g);
				if (login_flag != 0) {
					flag = false;
				}
				break;
			case 2:
				Receiver r = new Receiver(loginID, hashedPW, 0, "0");
				login_flag = sm.checkValidateReceiver(r);
				if (login_flag != 0) {
					flag = false;
				}
				break;
			case 3:
				Institution i = new Institution(loginID, hashedPW, 0);
				login_flag = sm.checkValidateInst(i);
				if (login_flag != 0) {
					flag = false;
				}
				break;
			case 4:
				Admin a = new Admin(loginID, hashedPW);
				login_flag = sm.checkValidateAdmin(a);
				if (login_flag != 0) {
					flag = false;
				}
				break;
			case 0:
				System.out.println("────────────────");
				System.out.println("입력하신 값으로 새로운 계정을 만드시겠습니까? (1. 예  2. 아니오)");
				if (sc.nextInt() == 1) {
					switch (newAccountTypeMenu()) {
					case 1:
						System.out.println("────────────────");
						System.out.println("비밀번호를 다시 입력해주세요.");
						System.out.print("PW : ");
						String checkPWg = b.applySha256(scl.nextLine());
						if (checkPWg.equals(hashedPW)) {
							sm.insertGiver(new Giver(loginID, checkPWg, 1000000));
							System.out.println("가입이 완료되었습니다. 환영합니다!");
							System.out.println("────────────────");
							login_flag = 1;
							flag = false;
						} else {
							System.out.println("비밀번호가 맞지 않습니다.");
						}
						break;
					case 2:
						System.out.println("────────────────");
						System.out.println("비밀번호를 다시 입력해주세요.");
						System.out.print("PW : ");
						String checkPWr = b.applySha256(scl.nextLine());
						if (checkPWr.equals(hashedPW)) {
							System.out.println("지원 받고 계신 기관의 ID를 입력해주세요.");
							String newInstID = scl.nextLine();
							sm.insertReceiver(new Receiver(loginID, checkPWr, 0, newInstID));
							System.out.println("가입이 완료되었습니다. 환영합니다!");
							login_flag = 2;
							flag = false;
						} else {
							System.out.println("비밀번호가 맞지 않습니다.");
						}
						break;
					case 3:
						System.out.println("────────────────");
						System.out.println("비밀번호를 다시 입력해주세요.");
						System.out.print("PW : ");
						String checkPWi = b.applySha256(scl.nextLine());
						if (checkPWi.equals(hashedPW)) {
							sm.insertInst(new Institution(loginID, checkPWi, 0));
							System.out.println("가입이 완료되었습니다. 환영합니다!");
							login_flag = 3;
							flag = false;
						} else {
							System.out.println("비밀번호가 맞지 않습니다.");
						}
						break;
					}
				}
				break;
			}
		}
	}

	public int newAccountTypeMenu() {
		System.out.println("────────────────");
		System.out.println("원하시는 계정 타입을 선택하세요");
		System.out.println("1. 기부자   2. 클라이언트   3. 기관");
		return sc.nextInt();
	}

	public int switcher() {
		while (true) {
			System.out.println("회원 정보 : " + login_flag);
			System.out.println("로그인 ID : " + loginID);
			try {
				System.out.print("메뉴를 선택하세요 >> ");
				return sc.nextInt();
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("잘못 입력되었습니다.");
			}
		}
	}

	public void givMainMenu() {
		System.out.println("────────────────");
		System.out.println("1. 기부 하기");
		System.out.println("2. 기부 현황");
		System.out.println("3. 계정 확인");
		System.out.println("4. SYN을 도와주세요! 무결성 검사");
		System.out.println("9. 로그아웃");
		System.out.println("0. 프로그램 종료");
		System.out.println("────────────────");
	}

	public void givMenu(int i) {
		switch (i) {
		case 1:
			raiseMoney();
			break;
		case 2:
			showCampaigns();
			break;
		case 3:
			System.out.println(loginID + "님의 계정 잔액은 " + showBalance() + "HOPE 입니다.");
			break;
		case 4:
			sm.chainValid();
			break;
		case 9:
			System.out.println("로그아웃 하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("프로그램을 종료하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}

	public void recMainMenu() {
		System.out.println("────────────────");
		System.out.println("1. 계정 확인");
		System.out.println("2. 기부 현황");
		System.out.println("3. SYN을 도와주세요! 무결성 검사");
		System.out.println("9. 로그아웃");
		System.out.println("0. 프로그램 종료");
		System.out.println("────────────────");
	}
	
	public void recMenu(int i) {
		switch (i) {
		case 1:
			System.out.println(loginID + "님의 계정 잔액은 " + showBalance() + "HOPE 입니다.");
			break;
		case 2:
			showCampaigns();
			break;
		case 3:
			sm.chainValid();
			break;
		case 9:
			System.out.println("로그아웃 하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("프로그램을 종료하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}

	public void instMainMenu() {
		System.out.println("────────────────");
		System.out.println("1. 계정 확인");
		System.out.println("2. 클라이언트 현황");
		System.out.println("3. SYN을 도와주세요! 무결성 검사");
		System.out.println("9. 로그아웃");
		System.out.println("0. 프로그램 종료");
		System.out.println("────────────────");
	}
	public void instMenu(int i) {
		switch (i) {
		case 1:
			System.out.println(loginID + "님의 계정 잔액은 " + showBalance() + "HOPE 입니다.");
			break;
		case 2:
			showCampaigns();
			break;
		case 3:
			sm.chainValid();
			break;
		case 9:
			System.out.println("로그아웃 하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("프로그램을 종료하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}
	public void adminMainMenu() {
		System.out.println("────────────────");
		System.out.println("1. 강제 송금 ##### ONLY FOR TEST");
		System.out.println("2. 캠페인 무결성 검사");
		System.out.println("3. 기부 현황(전체)");
		System.out.println("4. 기부 현황(ID)");
		System.out.println("5. 캠페인 시작");
		System.out.println("6. 캠페인 종료");
		System.out.println("9. 로그아웃");
		System.out.println("0. 프로그램 종료");
		System.out.println("────────────────");
	}
	
	public void adminMenu(int i) {
		switch (i) {
		case 1:
			sendMoney();
			break;
		case 2:
			sm.chainValid();
			break;
		case 3:
			showCampaigns();
			break;
		case 4:
			showChainStatus();
			break;
		case 5:
			startCampaign();
			break;
		case 6:
			endCampaign();
			break;
		case 9:
			System.out.println("로그아웃 하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("프로그램을 종료하시겠습니까? (1. 예   2. 아니오)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}
}
