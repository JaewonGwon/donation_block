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
					System.out.print("�޴��� �����ϼ��� >> ");
					input = sc.nextInt();

					break;
				} catch (Exception e) {
					sc.nextLine();
					System.out.println("�߸� �ԷµǾ����ϴ�.");
				}
			}
			switch (input) {
			case 1: // ���
				boolean insertFlag = true;
				while (insertFlag) {
					insertMenu();
					input = sc.nextInt();
					switch (input) { // ȸ�� ��� ############### ��й�ȣ SHA-256 �����ؼ� DB�� ������ ##############
					case 1:
						if (sm.insertGiver(insertGiver()) == 1) {
							System.out.println("��� ����");
						} else {
							System.out.println("��� ����, �ٽ� �Է����ּ���.");
						}
						break;
					case 2:
						if (sm.insertReceiver(insertReceiver()) == 1) {
							System.out.println("��� ����");
						} else {
							System.out.println("��� ����, �ٽ� �Է����ּ���.");
						}
						break;
					case 3:
						if (sm.insertInst(insertInst()) == 1) {
							System.out.println("��� ����");
						} else {
							System.out.println("��� ����, �ٽ� �Է����ּ���.");
						}
						break;
					case 4:
						insertFlag = false;
						System.out.println("���� �޴��� ���ư��ϴ�.");
						break;
					default:
						System.out.println("�߸� �ԷµǾ����ϴ�.");
						break;
					}
				}

				break;
			case 2: // �����۱� ################### ONLY FOR TEST ACCOUNTS ###########################

				break;
			case 3: // ����
				sendMoney();
				break;
			case 4: // ���Ἲ �˻�

				break;
			case 5: // ��� ��Ȳ
				showChainStatus();
				break;
			case 6: // ķ���� ���� (���ü�� ����)
				startCampaign();
				break;
			case 7: // ķ���� ���� (raisedFund �Ҹ�, ü������ ����)
				endCampaign();
				break;
			case 8: // ���α׷� ����
				flag = false;
				break;
			default:
				System.out.println("�߸� �ԷµǾ����ϴ�.");
				break;
			}
		}
	}

	public void mainMenu() {
		System.out.println("������������������������������-��");
		System.out.println("1. �����/Ŭ���̾�Ʈ ���");
		System.out.println("2. ���� ����");
		System.out.println("3. �۱�");
		System.out.println("4. SYN�� �����ּ���! ���Ἲ �˻�");
		System.out.println("5. ��� ��Ȳ");
		System.out.println("6. ķ���� ����");
		System.out.println("7. ķ���� ����");
		System.out.println("8. ���α׷� ����");
		System.out.println("��������������������������������-");
	}

	public void insertMenu() {
		System.out.println("��������������������������");
		System.out.println("1. ����� ���");
		System.out.println("2. Ŭ���̾�Ʈ ���");
		System.out.println("3. ��� ���");
		System.out.println("4. ���� �޴��� ���ư���");
		System.out.println("��������������������������");
	}

	public Giver insertGiver() {
		System.out.print("ID : ");
		String giv_id = scl.nextLine();
		System.out.print("PASSWORD : ");
		String giv_pw = scl.nextLine();
		return new Giver(giv_id, giv_pw, 0);
	}

	public void startCampaign() {
		System.out.println("0�� �Է��ϸ� ���� ���");
		System.out.print("��� Ŭ���̾�Ʈ�� ID : ");
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

	public Receiver insertReceiver() {
		System.out.print("ID : ");
		String receiv_id = scl.nextLine();
		System.out.print("PASSWORD : ");
		String receiv_pw = scl.nextLine();
		System.out.print("������ ID : ");
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

	public void sendMoney() {
		int subBalance = 0;
		boolean available = false; // ����� ���� ��� Ȱ���� flag
		System.out.println("0�� �Է��ϸ� ���");
		System.out.print("���� ID : ");
		String giv_id = scl.nextLine();
		if (giv_id.equals("0")) return;
		while (true) {
			System.out.print("Ŭ���̾�Ʈ ID : ");
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
					System.out.println("���� �ݾ� : ");
					try {
						balance = sc.nextInt();
						if (balance == 0) return;
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

}
