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
		System.out.println("0�� �Է��ϸ� ���");
		System.out.print("Ŭ���̾�Ʈ ID : ");
		String receiv_id = scl.nextLine();
		if (receiv_id.equals("0")) {
			return;
		}
		int balance = 0;
		while (true) {
			System.out.print("��ǥ ��ݾ� : ");
			try {
				balance = sc.nextInt();
				break;
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("�߸� �ԷµǾ����ϴ�.");
			}
		}
		if (balance == 0) {
			return;
		}
		Block temp = new Block(receiv_id, balance, "genesis_block", receiv_id);
		sm.insertBlock(temp);
	}

	public void endCampaign() {
		System.out.println("������ ķ������ �ִ��� Ȯ���մϴ�."); // ���� ���� : ��ǥ �ݾ� �޼�(�ʰ�)
		System.out.println("���� ķ������ �����ϰ� �ִ� Ŭ���̾�Ʈ���� ������ �����ϴ�.");
		System.out.println("����������������������������������������������������������-");
		ArrayList<String> temp = new ArrayList<>();
		temp = sm.receiversOnCampaign(); // �������� ķ������ receiv_id�� ����Ʈ
		for (String s : temp) { // �� receiv_id�� ü���� �˻��Ͽ�
			int compareBalance = 0;
			ArrayList<Block> tempChain = sm.searchBlock(s);
			for (Block b : tempChain) {
				compareBalance += b.getRaisedFund();
			}
			if ((sm.searchBlock(s).get(0).getRaisedFund() * 2) <= compareBalance) { // �ݾ��� �ʰ��ߴ��� Ȯ��
				int input = 0;
				while (true) {
					System.out.println(s + "�Կ� ���� ķ������ ���� �����մϴ�. �����Ͻðڽ��ϱ�?(1. ķ���� ����, 9. ���)");
					try {
						input = sc.nextInt();
						break;
					} catch (Exception e1) {
						sc.nextLine();
						System.out.println("�߸� �Է��ϼ̽��ϴ�.");
					}
				}
				switch (input) {
				case 1:
					boolean doubleFlag = true;
					boolean needInst = true;
					int totalRaisedFund = (compareBalance - sm.searchBlock(s).get(0).getRaisedFund());
					System.out.println("ķ���� ���� �۾��� �����մϴ�.");
					double com_rate = 0;
					while (doubleFlag) {
						System.out.println("���� ����� ���� ������ ������ �����ϼ���.(0.0 ~ 0.05; �ִ� 5%���� ���)");
						while (true) {
							try {
								com_rate = sc.nextDouble();
								break;
							} catch (Exception e1) {
								sc.nextLine();
								System.out.println("�߸� �Է��ϼ̽��ϴ�.");
							}
						}
						if (com_rate > 0.05 || com_rate < 0.0) {
							System.out.println("�ùٸ� ������ ������ �Է����ּ���.");
							break;
						} else {
							try {
								String inst_ = sm.selectInstID(s);
								sm.endInstitution((int) (totalRaisedFund * com_rate), inst_);
							} catch (Exception e) {
								System.out.println("���� ����� �����ϴ�. ���� ����� �������ּ���.");
								needInst = false;
							}
							doubleFlag = false;
							needInst = true;
							break;
						}

					}
					if (needInst) {
						System.out.println((int) (totalRaisedFund * (1 - com_rate)));
						sm.endReceiver((int) (totalRaisedFund * (1 - com_rate)), s); // receiv_id�� �ش��ϴ� balance�� �� ��ݾ���
																						// �Ա�
						sm.endCampaign(s); // �ش��ϴ� id�� receiv_id�� ������ ü�� ���� ��� ��� ����
						break;
					}
				case 9:
					break;
				default:

				}
			} else {
				System.out.println(s + "�Կ� ���� ķ������ ���� ���ᰡ �Ұ����մϴ�. ������ "
						+ ((sm.searchBlock(s).get(0).getRaisedFund() * 2) - compareBalance) + "HOPE�� �� �ʿ��մϴ�.");
			}
		}
	}

	public void showChainStatus() { // receiv_id�� �Է¹޾� genesis_block�� ������ ü�ο� ��ϵ� ��� ��ξ��� ���
		int total_bal = 0;

		System.out.print("ķ���� ID : ");
		String receiv_id = scl.nextLine();
		if (sm.searchBlock(receiv_id).size() == 0) {

			System.out.println("�ش� ���̵�� �˻��Ǵ� ķ������ �����ϴ�.");

		} else {
			for (Block b : sm.searchBlock(receiv_id)) { // ���� selectBlock �޼���� ��� ����� �ҷ��� ��
				if (b.getGiv_id() != "genesis_block") {
					total_bal += b.getRaisedFund();
				}
			}

			System.out.println(receiv_id + " ķ������ ���� ��ݾ��� " + total_bal + "HOPE �Դϴ�.");
		}
	}

	public void showCampaigns() {
		ArrayList<String> temp = new ArrayList<>();
		temp = sm.receiversOnCampaign(); // �������� ķ������ receiv_id�� ����Ʈ
		for (String s : temp) { // �� receiv_id�� ü���� �˻��Ͽ�
			int compareBalance = 0;
			ArrayList<Block> tempChain = sm.searchBlock(s);
			for (Block b : tempChain) {
				compareBalance += b.getRaisedFund();
			}
			if ((sm.searchBlock(s).get(0).getRaisedFund() * 2) <= compareBalance) { // �ݾ��� �ʰ��ߴ��� Ȯ��
				System.out.println(s + "�Կ� ���� ķ������ ��ǥ�� �޼��߽��ϴ�!");
			} else {
				System.out.println(s + "�Կ� ���� ķ������ ���� ������ �� �ʿ��մϴ�. ������ "
						+ ((sm.searchBlock(s).get(0).getRaisedFund() * 2) - compareBalance) + "HOPE�� �� �ʿ��մϴ�!");
			}
		}
	}

	public void sendMoney() {
		int subBalance = 0;
		boolean available = false; // ����� ���� ��� Ȱ���� flag
		System.out.println("0�� �Է��ϸ� ��ҵ˴ϴ�.");
		System.out.print("���� ID : ");
		String giv_id = scl.nextLine();
		if (giv_id.equals("0"))
			return;
		while (true) {
			System.out.print("Ŭ���̾�Ʈ ID : ");
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
					System.out.print("���� �ݾ� : ");
					try {
						balance = sc.nextInt();
						if (balance == 0)
							return;
						break;
					} catch (Exception e) {
						sc.nextLine();
						System.out.println("�߸� �Է��ϼ̽��ϴ�.");
					}
				}
				for (Giver g : sm.selectGiver()) { // giv_id�� balance���� ���� ����
					if (g.getGiv_id().equals(giv_id)) {
						if (g.getBalance() < balance) { // ���� ���� giv_id�� balance���� ū�� Ȯ��
							System.out.println("�ܰ� �����մϴ�.");
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
				System.out.println("�������� ������ �߸��Ǿ��ų�, ���� ���۵��� ���� ķ�����Դϴ�.");
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
		boolean available = false; // ����� ���� ��� Ȱ���� flag
		System.out.println("0�� �Է��ϸ� ��ҵ˴ϴ�.");
		System.out.println("����Ͻ� Ŭ���̾�Ʈ�� ID�� �Է����ּ���.");
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
					System.out.print("���� �ݾ� : ");
					try {
						balance = sc.nextInt();
						if (balance == 0)
							return;
						break;
					} catch (Exception e) {
						sc.nextLine();
						System.out.println("�߸� �Է��ϼ̽��ϴ�.");
					}
				}
				for (Giver g : sm.selectGiver()) { // giv_id�� balance���� ���� ����
					if (g.getGiv_id().equals(loginID)) {
						if (g.getBalance() < balance) { // ���� ���� giv_id�� balance���� ū�� Ȯ��
							System.out.println("�ܰ� �����մϴ�.");
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
				System.out.println("�������� ������ �߸��Ǿ��ų�, ���� ���۵��� ���� ķ�����Դϴ�.");
			}
		}
	}

	public void addBlock(String receiv_id, String giv_id, int balance) {
		ArrayList<Block> tempList = new ArrayList<>();
		tempList = sm.searchBlock(receiv_id); // receiv_id�� �˻��� ����Ʈ; ķ���� ���� ��ϵ��� �˻��Ͽ� ����Ʈ�� ����;
												// �ݵ�� Timestamp ���� ASC ������ ��

		if (tempList.size() > 0) { // �˻��� ����� genesis block�� �����ų�, Ȥ�� �������� ü������ Ȯ��
//			System.out.println(tempList.get(tempList.size()-1));
//			System.out.println(tempList.get(tempList.size()-1).getHash());
//			System.out.println(tempList.size());
			sm.insertBlock(new Block(tempList.get(tempList.size() - 1).getHash(), balance, giv_id, receiv_id));
		} else {
			System.out.println("�������� ������ �߸��Ǿ��ų�, ���� ���۵��� ���� ķ�����Դϴ�.");
		}

	}

	public void login() {
		boolean flag = true;
		while (flag) {
			Block b = new Block(); // Block ��ü�� SHA-256 �޼��� Ȱ���� ����..
			System.out.println("��������������������������������");
			System.out.println(" Save. Your. Neighbour.  S.Y.N  ");
			System.out.println("��������������������������������");
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
				System.out.println("��������������������������������");
				System.out.println("�Է��Ͻ� ������ ���ο� ������ ����ðڽ��ϱ�? (1. ��  2. �ƴϿ�)");
				if (sc.nextInt() == 1) {
					switch (newAccountTypeMenu()) {
					case 1:
						System.out.println("��������������������������������");
						System.out.println("��й�ȣ�� �ٽ� �Է����ּ���.");
						System.out.print("PW : ");
						String checkPWg = b.applySha256(scl.nextLine());
						if (checkPWg.equals(hashedPW)) {
							sm.insertGiver(new Giver(loginID, checkPWg, 1000000));
							System.out.println("������ �Ϸ�Ǿ����ϴ�. ȯ���մϴ�!");
							System.out.println("��������������������������������");
							login_flag = 1;
							flag = false;
						} else {
							System.out.println("��й�ȣ�� ���� �ʽ��ϴ�.");
						}
						break;
					case 2:
						System.out.println("��������������������������������");
						System.out.println("��й�ȣ�� �ٽ� �Է����ּ���.");
						System.out.print("PW : ");
						String checkPWr = b.applySha256(scl.nextLine());
						if (checkPWr.equals(hashedPW)) {
							System.out.println("���� �ް� ��� ����� ID�� �Է����ּ���.");
							String newInstID = scl.nextLine();
							sm.insertReceiver(new Receiver(loginID, checkPWr, 0, newInstID));
							System.out.println("������ �Ϸ�Ǿ����ϴ�. ȯ���մϴ�!");
							login_flag = 2;
							flag = false;
						} else {
							System.out.println("��й�ȣ�� ���� �ʽ��ϴ�.");
						}
						break;
					case 3:
						System.out.println("��������������������������������");
						System.out.println("��й�ȣ�� �ٽ� �Է����ּ���.");
						System.out.print("PW : ");
						String checkPWi = b.applySha256(scl.nextLine());
						if (checkPWi.equals(hashedPW)) {
							sm.insertInst(new Institution(loginID, checkPWi, 0));
							System.out.println("������ �Ϸ�Ǿ����ϴ�. ȯ���մϴ�!");
							login_flag = 3;
							flag = false;
						} else {
							System.out.println("��й�ȣ�� ���� �ʽ��ϴ�.");
						}
						break;
					}
				}
				break;
			}
		}
	}

	public int newAccountTypeMenu() {
		System.out.println("��������������������������������");
		System.out.println("���Ͻô� ���� Ÿ���� �����ϼ���");
		System.out.println("1. �����   2. Ŭ���̾�Ʈ   3. ���");
		return sc.nextInt();
	}

	public int switcher() {
		while (true) {
			System.out.println("ȸ�� ���� : " + login_flag);
			System.out.println("�α��� ID : " + loginID);
			try {
				System.out.print("�޴��� �����ϼ��� >> ");
				return sc.nextInt();
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("�߸� �ԷµǾ����ϴ�.");
			}
		}
	}

	public void givMainMenu() {
		System.out.println("��������������������������������");
		System.out.println("1. ��� �ϱ�");
		System.out.println("2. ��� ��Ȳ");
		System.out.println("3. ���� Ȯ��");
		System.out.println("4. SYN�� �����ּ���! ���Ἲ �˻�");
		System.out.println("9. �α׾ƿ�");
		System.out.println("0. ���α׷� ����");
		System.out.println("��������������������������������");
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
			System.out.println(loginID + "���� ���� �ܾ��� " + showBalance() + "HOPE �Դϴ�.");
			break;
		case 4:
			sm.chainValid();
			break;
		case 9:
			System.out.println("�α׾ƿ� �Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("���α׷��� �����Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}

	public void recMainMenu() {
		System.out.println("��������������������������������");
		System.out.println("1. ���� Ȯ��");
		System.out.println("2. ��� ��Ȳ");
		System.out.println("3. SYN�� �����ּ���! ���Ἲ �˻�");
		System.out.println("9. �α׾ƿ�");
		System.out.println("0. ���α׷� ����");
		System.out.println("��������������������������������");
	}
	
	public void recMenu(int i) {
		switch (i) {
		case 1:
			System.out.println(loginID + "���� ���� �ܾ��� " + showBalance() + "HOPE �Դϴ�.");
			break;
		case 2:
			showCampaigns();
			break;
		case 3:
			sm.chainValid();
			break;
		case 9:
			System.out.println("�α׾ƿ� �Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("���α׷��� �����Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}

	public void instMainMenu() {
		System.out.println("��������������������������������");
		System.out.println("1. ���� Ȯ��");
		System.out.println("2. Ŭ���̾�Ʈ ��Ȳ");
		System.out.println("3. SYN�� �����ּ���! ���Ἲ �˻�");
		System.out.println("9. �α׾ƿ�");
		System.out.println("0. ���α׷� ����");
		System.out.println("��������������������������������");
	}
	public void instMenu(int i) {
		switch (i) {
		case 1:
			System.out.println(loginID + "���� ���� �ܾ��� " + showBalance() + "HOPE �Դϴ�.");
			break;
		case 2:
			showCampaigns();
			break;
		case 3:
			sm.chainValid();
			break;
		case 9:
			System.out.println("�α׾ƿ� �Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("���α׷��� �����Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}
	public void adminMainMenu() {
		System.out.println("��������������������������������");
		System.out.println("1. ���� �۱� ##### ONLY FOR TEST");
		System.out.println("2. ķ���� ���Ἲ �˻�");
		System.out.println("3. ��� ��Ȳ(��ü)");
		System.out.println("4. ��� ��Ȳ(ID)");
		System.out.println("5. ķ���� ����");
		System.out.println("6. ķ���� ����");
		System.out.println("9. �α׾ƿ�");
		System.out.println("0. ���α׷� ����");
		System.out.println("��������������������������������");
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
			System.out.println("�α׾ƿ� �Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				login_flag = 0;
			break;
		case 0:
			System.out.println("���α׷��� �����Ͻðڽ��ϱ�? (1. ��   2. �ƴϿ�)");
			if (sc.nextInt() == 1)
				flag = false;
			break;
		default:
			break;

		}
	}
}
