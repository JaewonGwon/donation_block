package ui;

import java.util.ArrayList;
import java.util.Scanner;

import manager.SynManager;
import vo.Block;
import vo.Giver;
import vo.Institution;
import vo.Receiver;

public class SynUI {
	private Scanner sc = new Scanner(System.in); // Scanner for nextInt()
	private Scanner scl = new Scanner(System.in); // Scanner for nextLine()
	private boolean flag = true;
	private int input;
	SynManager sm = new SynManager();

	public SynUI() {
		while (flag) {
			mainMenu();
			while (true) {
				try {
					System.out.print("메뉴를 선택하세요 >> ");
					input = sc.nextInt();

					break;
				} catch (Exception e) {
					sc.nextLine();
					System.out.println("잘못 입력되었습니다.");
				}
			}
			switch (input) {
			case 1: // 등록
				boolean insertFlag = true;
				while (insertFlag) {
					insertMenu();
					input = sc.nextInt();
					switch (input) { // 회원 등록 ############### 비밀번호 SHA-256 적용해서 DB로 보내기 ##############
					case 1:
						if (sm.insertGiver(insertGiver()) == 1) {
							System.out.println("등록 성공");
						} else {
							System.out.println("등록 실패, 다시 입력해주세요.");
						}
						break;
					case 2:
						if (sm.insertReceiver(insertReceiver()) == 1) {
							System.out.println("등록 성공");
						} else {
							System.out.println("등록 실패, 다시 입력해주세요.");
						}
						break;
					case 3:
						if (sm.insertInst(insertInst()) == 1) {
							System.out.println("등록 성공");
						} else {
							System.out.println("등록 실패, 다시 입력해주세요.");
						}
						break;
					case 4:
						insertFlag = false;
						System.out.println("상위 메뉴로 돌아갑니다.");
						break;
					default:
						System.out.println("잘못 입력되었습니다.");
						break;
					}
				}

				break;
			case 2: // 강제송금 ################### ONLY FOR TEST ACCOUNTS ###########################

				break;
			case 3: // 삭제
				sendMoney();
				break;
			case 4: // 무결성 검사

				break;
			case 5: // 기부 현황
				showChainStatus();
				break;
			case 6: // 캠페인 시작 (블록체인 생성)
				startCampaign();
				break;
			case 7: // 캠페인 종료 (raisedFund 소멸, 체인정보 삭제)
				endCampaign();
				break;
			case 8: // 프로그램 종료
				flag = false;
				break;
			default:
				System.out.println("잘못 입력되었습니다.");
				break;
			}
		}
	}

	public void mainMenu() {
		System.out.println("───────────────-─");
		System.out.println("1. 기부자/클라이언트 등록");
		System.out.println("2. 정보 변경");
		System.out.println("3. 송금");
		System.out.println("4. SYN을 도와주세요! 무결성 검사");
		System.out.println("5. 기부 현황");
		System.out.println("6. 캠페인 시작");
		System.out.println("7. 캠페인 종료");
		System.out.println("8. 프로그램 종료");
		System.out.println("────────────────-");
	}

	public void insertMenu() {
		System.out.println("─────────────");
		System.out.println("1. 기부자 등록");
		System.out.println("2. 클라이언트 등록");
		System.out.println("3. 기관 등록");
		System.out.println("4. 상위 메뉴로 돌아가기");
		System.out.println("─────────────");
	}

	public Giver insertGiver() {
		System.out.print("ID : ");
		String giv_id = scl.nextLine();
		System.out.print("PASSWORD : ");
		String giv_pw = scl.nextLine();
		return new Giver(giv_id, giv_pw, 0);
	}

	public void startCampaign() {
		System.out.println("0을 입력하면 생성 취소");
		System.out.print("대상 클라이언트의 ID : ");
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

	public Receiver insertReceiver() {
		System.out.print("ID : ");
		String receiv_id = scl.nextLine();
		System.out.print("PASSWORD : ");
		String receiv_pw = scl.nextLine();
		System.out.print("관계기관 ID : ");
		String inst_id = scl.nextLine();
		return new Receiver(receiv_id, receiv_pw, 0, inst_id);
	}

	public Institution insertInst() {
		System.out.print("ID : ");
		String id = scl.nextLine();
		System.out.print("PASSWORD : ");
		String pw = scl.nextLine();
		return new Institution(id, pw, 0);
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

	public void sendMoney() {
		int subBalance = 0;
		boolean available = false; // 대상이 없을 경우 활용할 flag
		System.out.println("0을 입력하면 취소");
		System.out.print("보낼 ID : ");
		String giv_id = scl.nextLine();
		if (giv_id.equals("0")) return;
		while (true) {
			System.out.print("클라이언트 ID : ");
			String receiv_id = scl.nextLine();
			if (receiv_id.equals("0")) return;
			for (Block r : sm.searchBlock(receiv_id)) {
				if (r.getReceiv_id().equals(receiv_id)) {
					available = true;
				}
			}
			if (available) {
				int balance = 0;
				while (true) {
					System.out.println("보낼 금액 : ");
					try {
						balance = sc.nextInt();
						if (balance == 0) return;
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

}
