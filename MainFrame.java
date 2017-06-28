package racingHorse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	//	���� �����ӿ� ��� �гο� ���� ������ main�Լ��� ������ �ִ�.
	
	public static final int WIDTH = 1500;
	public static final int LANE_SIZE = 60;
	
//	���� ���� TOTAL_HORSE_NUMBER�� ������ �� �ִ�.
	private int TOTAL_HORSE_NUMBER = 6; 										//	������ ��
	private RacingPanel racingPanel = new RacingPanel(TOTAL_HORSE_NUMBER, this);	//	����Ʈ�� �г�
	private CheerUp cheerup = new CheerUp(this);
	private JPanel upPanel = new JPanel();									//	menu + ranking
	private RankingPanel rankingPanel = new RankingPanel(TOTAL_HORSE_NUMBER, this);	//	���� ǥ�� 
	
	private MenuPanel menuPanel = new MenuPanel(racingPanel, this);				// �޴� �г�
	private JPanel southPanel = new JPanel();								// �ǹ� �г�
	
	private AdvertisePanel advertisepanel = new AdvertisePanel(this);			// �����г�
	
	private LogPanel logpanel = new LogPanel(this);

	private static User user = new User("LocalHost", 10000);


	public MainFrame() {
		setTitle("�渶 ����");
		setBounds(200, 50, WIDTH, 500 + LANE_SIZE*TOTAL_HORSE_NUMBER + RacingPanel.TRACK_OFFSET*2);
		setLayout(new BorderLayout());

//		���ʿ� ���� ǥ�� �гΰ� ������ �� �� �ִ� �޴� �гη� �����Ǿ��ִ�.
		upPanel.setLayout(new BorderLayout());
		upPanel.add(rankingPanel, BorderLayout.WEST);
		upPanel.add(menuPanel, BorderLayout.CENTER);
		
		southPanel.setPreferredSize(new Dimension(WIDTH, 150));
		southPanel.setLayout(new GridLayout(1,3 ));
		southPanel.add(logpanel);
		southPanel.add(cheerup);
		southPanel.add(advertisepanel);
		
		
//		TODO
//		�α� �߰��ϱ�, ���� �߰��ϱ�
//		(��ȣ) �� �߰� 

		
//		southPanel.add(logpanel,BorderLayout.WEST);
		

//		southPanel �ȿ� �α׸� ���� �гΰ� ���� ���� �г��� ���� add �ϰ�
//		������ �������־�� �Ѵ�.
//		�α��гΰ� �����г��� ������ class�� �������ִ� ���� �ڵ� ¥�⿡ ���� �� �ִ�.
//		���� : 
//		southPanel.add(logPanel);
//		southPanel.add(advertisePanel);
//		southPanel.setLayout(new GridLayout(2,1));
//		...
		
//		---------------------���� �߰�------------------------
//		cheerup.setLayout(new BorderLayout());
		add(upPanel, BorderLayout.NORTH);
		add(racingPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
//		------------------------------------------------------
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
//		TODO
//		6. Ÿ��Ʋ ȭ�� �߰�
//		Ÿ��Ʋ ȭ���� JFrameȭ�鿡�� ��ư�� ������ ActionPerformed�� 
//		MainFrame frame�� �����Ͽ� ȣ���ϸ� �ȴ�.
		Horse.suffle();
//		MainFrame frame = new MainFrame();
		TitleFrame title = new TitleFrame();
	}
	
	public void printRanking(int rank[]){
		rankingPanel.printRanking(rank);
	}

	public static User getUser() {
		return user;
	}
	
	public void refreshMenuPanel(){
		menuPanel.refreshComboList();
	}

	public void racingResult() {
		menuPanel.racingResult();
		
	}
	
	public int getSelectedHorse(){
		return menuPanel.getSelectedHorse();
	}
	
	public LogPanel getLogPanel(){
		return logpanel;
	}
	
	public RacingPanel getRacingPanel(){
		return racingPanel;
	}
	
	public void setRunningState(int state){
		cheerup.setRunningState(state);
	}
	
	public void setCheeringName(String name){
		cheerup.setHorseName(name);
	}
}

class RacingPanel extends JPanel implements Runnable {
//	���ָ��� �޸��� ���� ǥ���ϱ� ���� JPanel�� paint()�Լ��� Runnable�� run() �Լ��� override �Ͽ� ����Ѵ�. 
	public static final int START_POINT = 10; 			// �������
	public static final int TRACK_LENGTH = 4500;
	public static final int TRACK_OFFSET = 80;
	public static final int FINISH_POINT = TRACK_LENGTH - 120; 		// ��������
	
	private MainFrame mainFrame;
	
	private static Horse[] horses;				//	���ָ��� ������ ������ �ִ� Horse��ü �迭
	private static int[] rank;					//	������ �ű�� ���� �����迭
	private int trackMove = 0;					//	Ʈ���� �󸶳� ���������� ��Ÿ���� ����	
	private boolean horseInfoClickable = true;	//	��⵵�� ���� ������ Ȯ���� �� ������ ����
	
	private String countDown = "";										//	���� ���۽� ������ ī��Ʈ�ٿ� �޼���
	private Font countDownFont = new Font("Dialog", Font.BOLD, 30);		//	ī��Ʈ�ٿ� �޼�����  �۾�ü
	private boolean flag= true;											// ����
	
	public RacingPanel(int TOTAL_HORSE_NUMBER, MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		horses = new Horse[TOTAL_HORSE_NUMBER];
		rank = new int[TOTAL_HORSE_NUMBER];
		for(int i = 0; i < TOTAL_HORSE_NUMBER; i++){
			horses[i] = new Horse(i, "", START_POINT, i *MainFrame.LANE_SIZE);
		}
//		���ָ��� ����ŭ ũ�⸦ �ø���.
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.LANE_SIZE* TOTAL_HORSE_NUMBER + TRACK_OFFSET));
		
		addMouseListener(new MouseAdapter() {
			

			@Override
			public void mouseClicked(MouseEvent me) {
//				TODO
//				3. �ߺ�â ���� ����
//				â�� ���� �������� Ȯ���� �� �ִ� boolean flag �����迭�� ����Ͽ�
//				�ѹ� ���� ��ȣ�� â�� ������ �ʵ��� �����Ѵ�.
				
				try {
					if(horseInfoClickable){
						int x = me.getX();
						int y = me.getY();
						for(int i =0; i < horses.length ; i++){
							int horseSize= (int)((double)MainFrame.LANE_SIZE / (double)horses[i].getImageInfo().getHeight() * (double)horses[i].getImageInfo().getWidth());
							if( x >= horses[i].getPosition_X() &&
									x <= horses[i].getPosition_X() + horseSize &&
									y >= horses[i].getPosition_Y() + TRACK_OFFSET&&
									y <= horses[i].getPosition_Y() + MainFrame.LANE_SIZE + TRACK_OFFSET){
								JFrame horseInfoFrame = new JFrame();
								horseInfoFrame.setTitle(horses[i].getName());
								horseInfoFrame.setBounds(400 + (10* i), 200, HorseInfoPanel.WIDTH, HorseInfoPanel.HEIGHT);
								horseInfoFrame.setVisible(flag);
								HorseInfoPanel horseInfoPanel;
								horseInfoPanel = new HorseInfoPanel(horses[i].clone());
								
								horseInfoFrame.add(horseInfoPanel);
								Thread thread = new Thread(horseInfoPanel);
								thread.start();
								flag = false;
								horseInfoFrame.addWindowListener(new WindowAdapter() {
									@Override
									public void windowClosing(WindowEvent e) {
										thread.stop();
										flag = true;
									}
								});
							}
							
						}
					}	// if end
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		
//		��� �׸���
//		Ʈ�� ��Ÿ��
		g.setColor(new Color(150, 70, 0));				//	���� ��
		g.fillRect(0, 0, TRACK_LENGTH, TRACK_OFFSET);	//	Ʈ�� �� ���
		g.fillRect(0, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length, TRACK_LENGTH * 2, TRACK_OFFSET*4);	//	Ʈ�� �Ʒ� ���
		g.setColor(Color.WHITE);
		g.fillRect(-trackMove, 20, TRACK_LENGTH * 2, 6);
		for(int i = 0; i < TRACK_LENGTH / 180; i++){
			g.fillRect(100 + i * 180 -trackMove, 20, 6, TRACK_OFFSET - 20);
		}
		
//		g.setColor(new Color(150, 70, 0));				//	���� ��
//		g.fillRect(0, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length, TRACK_LENGTH, TRACK_OFFSET);	//	Ʈ�� �� ���
		
		for(int i = 0; i < horses.length; i++){
//			Ʈ���� �ܵ�
			if(mainFrame.getSelectedHorse() == i){
				g.setColor(Color.PINK);
			}else{
				g.setColor(new Color((i % 2 == 0 ? 183 : 0), (i % 2 == 0 ? 255 : 204), (i % 2 == 0 ? 190 : 19)));
			}
			g.fillRect(0, i *MainFrame.LANE_SIZE + TRACK_OFFSET, TRACK_LENGTH, MainFrame.LANE_SIZE);
//			�ܵ����� Ʈ����ȣ�� �̸�
			g.setColor(Color.GRAY);
			g.setFont(new Font("Dialog", Font.BOLD, 22));
			g.drawString("[" + (i+1)+ "] " + horses[i].getName(), 100 - trackMove, i *MainFrame.LANE_SIZE + TRACK_OFFSET + 33);
		}
		
//		1000pixel���� m ǥ��
		for(int i = 1; i <= TRACK_LENGTH / 1000; i++){
			g.setColor(Color.WHITE);
			g.drawLine(i * 1000 - trackMove, TRACK_OFFSET, i * 1000 - trackMove, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length);
			g.drawString(i + "0m", i * 1000 - trackMove - 13, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length + 30);
		}
//		��¼�
		for(int i = 0; i < horses.length; i++){
			g.setColor(Color.BLACK);
			g.fillRect(FINISH_POINT -trackMove,  i * MainFrame.LANE_SIZE + TRACK_OFFSET ,  MainFrame.LANE_SIZE /2, MainFrame.LANE_SIZE /2);
			g.fillRect(FINISH_POINT+MainFrame.LANE_SIZE /2 -trackMove,  i * MainFrame.LANE_SIZE + MainFrame.LANE_SIZE /2 + TRACK_OFFSET , MainFrame.LANE_SIZE /2, MainFrame.LANE_SIZE /2);
			g.setColor(Color.WHITE);
			g.fillRect(FINISH_POINT -trackMove,  i * MainFrame.LANE_SIZE + MainFrame.LANE_SIZE /2 + TRACK_OFFSET , MainFrame.LANE_SIZE /2, MainFrame.LANE_SIZE /2);
			g.fillRect(FINISH_POINT+MainFrame.LANE_SIZE /2 -trackMove,  i * MainFrame.LANE_SIZE  + TRACK_OFFSET,  MainFrame.LANE_SIZE /2, MainFrame.LANE_SIZE /2);
		}
		
//		g.setColor(Color.WHITE);
//		g.fillRect(0, horses.length *MainFrame.LANE_SIZE + TRACK_OFFSET, MainFrame.WIDTH, horses.length*MainFrame.LANE_SIZE);
		
	
//		�� �׸���
		for(int i = 0; i < horses.length; i++){
			int horseSize= (int)((double)MainFrame.LANE_SIZE / (double)horses[i].getImageInfo().getHeight() * (double)horses[i].getImageInfo().getWidth());
//			�� ������ ��ġ�� �޾ƿͼ� �׸���.
			try{
//				drawImage(img,  dx1,  dx2,  dy2, sx1, sy1, sx2, sy2, observer) : �����̹����� �������� �����쿡 ǥ����
				g.drawImage(horses[i].getImageInfo().getImage(),						// Image
						horses[i].getPosition_X() - trackMove,							// �����쿡 �̹����� ǥ�õ� ���� X ��ǥ
						horses[i].getPosition_Y() + TRACK_OFFSET,						// �����쿡 �̹����� ǥ�õ� ���� Y ��ǥ
						horses[i].getPosition_X() + horseSize -trackMove,				// �����쿡 �̹����� ǥ�õ� �� X ��ǥ = ������ǥ + �� ���� ũ��
						horses[i].getPosition_Y() + MainFrame.LANE_SIZE + TRACK_OFFSET,	// �����쿡 �̹����� ǥ�õ� �� Y ��ǥ = ������ǥ + �� ���� ũ��
						horses[i].getImageInfo().getStartX(),							// �б� ������ ���� �̹����� ���� X ��ǥ
						horses[i].getImageInfo().getStartY(),							// �б� ������ ���� �̹����� ���� Y ��ǥ
						horses[i].getImageInfo().getEndX(),								// �б� ������ ���� �̹����� �� X ��ǥ
						horses[i].getImageInfo().getEndY(),								// �б� ������ ���� �̹����� �� Y ��ǥ
						this);
				
				
			}catch(NullPointerException e){
//				e.printStackTrace();
				System.err.println("�̹��� ������ �ҷ����µ� �����Ͽ����ϴ�.");
			}
		}

		
//		ī��Ʈ�ٿ�
//		g.setColor(Color.GRAY);							//	TODO : ���� �׸���
//		g.setFont(new Font("Dialog", Font.BOLD, 35));	//	���� �׸���
//		g.drawString("3", MainFrame.WIDTH/2 - 50, 31);	//	���� �׸���
		g.setColor(Color.BLACK);
		g.setFont(countDownFont);
		g.drawString(countDown, MainFrame.WIDTH/2 - 180, 80);
	}

	@Override
	public void run() {
//		���յ��� ���� ������ �� �� ������ ����
		horseInfoClickable = false;
//		���� �ʹ� ������ ����ġ�� ���� ���Ѵ�.
		int tooFastOffset = 1;
//		ī��Ʈ�ٿ�
		String[] countDownStr = {"READY", "GET SET!" };
		for(int i = countDownStr.length-1; i >= 0; i--){
			for(int fontSize= 100; fontSize > 50; fontSize--){
				countDown = countDownStr[i] + "";
				countDownFont = new Font("Dialog", Font.BOLD, fontSize);
				repaint();
				try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace();}
			}
		}
		countDownFont = new Font("Dialog", Font.BOLD, 80);
		countDown = "GO!";
		for(int i = 0; i < horses.length; i++){
			rank[i] = i;
		}
		
		long startTime = System.currentTimeMillis();
		while (true) {
//			CountDown �޼��� ������ "GO!" ���ֱ� ���� 1�ʰ� �������� Ȯ���Ѵ�.
			if( System.currentTimeMillis() - startTime > 800){
				countDown = "";
			}
			
//			�����г��� ���������� �ٲ��ش�.
			mainFrame.setRunningState(CheerUp.RUNNING);
			if (isAllHorseFinished()) {
//				��� ���� ��¼��� �����ϸ� MenuPanel���� ����� �����Ѵ�.
				countDownFont = new Font("Dialog", Font.BOLD, 80);
				countDown = "Finished!";
				repaint();
				if(mainFrame.getSelectedHorse() == rank[0]){
					mainFrame.setRunningState(CheerUp.WIN);
				}else{
					mainFrame.setRunningState(CheerUp.LOSE);
				}
				try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace();}
//				MenuPanel.racingResult();
				mainFrame.racingResult();
				resetHorsePosition();
//				MainFrame.printStatus(rank);
				mainFrame.refreshMenuPanel();
				countDown = "";
				repaint();
				horseInfoClickable = true;
				mainFrame.setRunningState(CheerUp.WAITING);
				return;
			}
//			���� �������� ���� ���� ������ �� ���� X��ǥ�� ���� ���� ���� �̵���Ų��.
			for(int i = 0; i < horses.length ; i++){
				if(horses[i].getPosition_X() < FINISH_POINT){
//					horses[i].move(new Random().nextInt(22)+13);
					horses[i].move();
					horses[i].getImageInfo().nextImage();
				}else{
//					horses[i].moveTo(FINISH_POINT);
				}
			}
			
//			Ʈ���� ��� �����̵��� ��� ���� X��ǥ���� Ʈ���� ������ ��ŭ ��ǥ�� ����. 					
//			������ ��¼��� ���̸� ���̻� Ʈ���� �������� �ʴ´�.
			if( trackMove < TRACK_LENGTH - MainFrame.WIDTH){
				trackMove += 18;
//				1���� ȭ���� 65%�� �Ѿ�� Ʈ���� �ӵ��� �� ���� ������´�.
				if(horses[rank[0]].getPosition_X() - trackMove > MainFrame.WIDTH / 100 * 65){
					trackMove += tooFastOffset++;
//					System.out.println("���� ����");
				}
			}

			//	���ָ��� ������ �ǽð����� �ű��.
			for(int i = 0; i < horses.length-1; i++){
				int max = i;
				if(isFinished(horses[rank[i]])){			//	�̹� ����� ����� ���ָ��� ������ �ٲ��� �ʴ´�.
					continue;
				}
				for(int j=i+1; j < horses.length; j++){
					if(horses[rank[i]].getPosition_X() < horses[rank[j]].getPosition_X()){
						int temp = rank[i];						
						rank[i] = rank[j];
						rank[j] = temp;
					}
				}
			}
			
//			�� ���� 1���̸� Ī���� ������ �ٲ��ش�.
			if(rank[0] == mainFrame.getSelectedHorse()){
				mainFrame.setRunningState(CheerUp.WINNING);
			}
			mainFrame.printRanking(rank);
			repaint();
			try { Thread.sleep(35); } catch (InterruptedException e) { e.printStackTrace();}
		}
	}

	private boolean isAllHorseFinished() {
//		��� ���� ��¼��� �����ϸ� ������ ������ �Ѵ�.
		for(int i = 0; i < horses.length ; i++){
			if(horses[i].getPosition_X() < FINISH_POINT){
				return false;
			}
		}
		return true;
	}
	
	public void resetHorsePosition(){
//			��ġ �ʱ�ȭ
		for(int i = 0; i < horses.length ; i++){
//			horses[i].setPosition_X(START_POINT);
			int ypos = horses[i].getPosition_Y();
			horses[i] = new Horse(i, "", START_POINT, ypos);
		}
		trackMove = 0;
	}
	
	public boolean isFinished(Horse horse){
		if(horse == null){
			return false;
		}
		if(horse.getPosition_X() >= FINISH_POINT)
			return true;
		else
			return false;
		
	}

	public Horse[] getHorses() {
		return horses;
	}

	public int[] getRank() {
		return rank;
	}

}

class MenuPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private RacingPanel racingPanel;
	private String[] horseNames;
	private JComboBox horseList1;
	private JComboBox horseList2;
	private JButton selectButton = new JButton("���� �Ϸ� ");
	private JTextField bettingValField = new JTextField();
	private JLabel amountLabel = new JLabel();
	private JButton addButton = new JButton("    ����     ");				// ���� ��ư
	private JComboBox moneychoice;							// ���� �ݾ׼��� �޺�����
	private int chargemoney;
//	TODO
//	5. �ݾ�����
//	�ݾ� ������ ���� ��ư�� ����� �� ��ư�� ActionPerformed �ÿ�
//	static���� ����Ǿ��ִ� MainFrame.getUser��() User�� �θ���
//	addBalance() �Լ��� �ݾ��� �������ش�.
//	���� )  MainFrame.getUser().addBalance(Integer.parseInt(chargeTestField.getTest().trim()));
//	���� )  MainFrame.getUser().addBalance(chargeMoney);
//	setText(MainFrame.getUser().getBalance() + " ��");
//	String ���� Int�� parsing�� �Ͼ�Ƿ� ����ó���� �ݵ�� ���ڿ��� �ɷ����� �Ѵ�.
	
	public MenuPanel(RacingPanel racingPanel, MainFrame mainFrame){
//		TODO
//		4. �޴� �г� ����ϰ� ����
//		���̾ƿ� ������ �� �ؼ� ��ġ�� ����ϰ� �ϵ��� �Ѵ�.
//		����� ���̾ƿ� ������ �ȵǾ� �־ �⺻���� FlowLayout�̴�.
		this.mainFrame = mainFrame;
		this.racingPanel = racingPanel;
		horseNames = new String[racingPanel.getHorses().length + 1];
		horseNames[0] = "���þ���";
		
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			String name = (i+1) + " : " + racingPanel.getHorses()[i].getName();
			String bettingRate = " (���� : " +new DecimalFormat("#.##").format(racingPanel.getHorses()[i].getBettingRate()) + ")";  
			horseNames[i+1] =  name + bettingRate;
		}
		String[] moneyList = {"������ �ݾ�","1000","5000","10000","50000","100000","500000"}; 
		
		bettingValField.setPreferredSize(new Dimension(200, 30));
		horseList1 = new JComboBox(horseNames);
		horseList2 = new JComboBox(horseNames);
//		2�� ������ ���̻� ������� �ʴ´�.
		horseList2.setVisible(false);
		moneychoice = new JComboBox(moneyList);
		
//		setPreferredSize(new Dimension(600, 300));
		setLayout(new GridLayout(3,1));
//		setLayout(new FlowLayout());
		
//		JPanel moneymenupanel = new JPanel(new GridLayout(1,3));
//		�ݾ��޺�����,���� ��ư, �����ݾ� ��
		JPanel moneymenupanel = new JPanel();
//		moneymenupanel.setPreferredSize(new Dimension(600, 100));
		moneymenupanel.add(moneychoice);
		moneymenupanel.add(addButton);
		addButton.setSize(new Dimension(200, 100));
		moneymenupanel.add(amountLabel);
		amountLabel.setSize(200, 100);
		amountLabel.setText("���� �ݾ� : "+ MainFrame.getUser().getBalance() + "��");
//		
//		JPanel horsemenupanel = new JPanel(new GridLayout(1,2));
//		�� ���� �޺����� 1,2
		JPanel horsemenupanel = new JPanel();
//		horsemenupanel.setPreferredSize(new Dimension(500, 100)); 
		horsemenupanel.add(horseList1);
		horsemenupanel.add(horseList2);
		
		
//		JPanel bettingpanel = new JPanel(new GridLayout(1, 4));
//		���� ��, �ݾ� �Է� �ؽ�Ʈ�ʵ�, ���ÿϷ� ��ư
		JPanel bettingpanel = new JPanel();
		bettingpanel.add(new JLabel("���þ� : "));
		bettingpanel.add(bettingValField);
		bettingpanel.add(new JLabel(" "));
		bettingpanel.add(selectButton);
		
//		menupanel�� �� �г��� �߰�		
		add(moneymenupanel);
		add(horsemenupanel);
		add(bettingpanel);
		
		selectButton.addActionListener(this);
		bettingValField.addActionListener(this);
		
		addButton.addActionListener(new ActionListener() {
//			ó�� �ݾ��� 0������ �����ϰ� �����ݾ׼����ϰ� ������ư ���������� �������� �س����ϴ�.
//			�׸��� �ݾ��� ���þȵǾ������� ������ �ݾ� �����϶�� �޽��� ����ݴϴ�.
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(moneychoice.getSelectedIndex()){
				case 0:
					JOptionPane.showMessageDialog(moneychoice, "������ �ݾ��� �������ּ���!!!!");
					break;
				case 1:
					chargemoney = 1000;
					break;
				case 2:
					chargemoney = 5000;
					break;
				case 3:
					chargemoney = 10000;
					break;
				case 4:
					chargemoney = 50000;
					break;
				case 5:
					chargemoney = 100000;
					break;
				case 6:
					chargemoney = 500000;
					break;
				}
				MainFrame.getUser().addBalance(chargemoney);
				amountLabel.setText("���� �ݾ� : "+ MainFrame.getUser().getBalance() + "��");
				chargemoney = 0;
			}
		});
		
		horseList1.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				Horse[] horses = mainFrame.getRacingPanel().getHorses();
				mainFrame.getRacingPanel().repaint();
				mainFrame.setRunningState(CheerUp.READY);
				try{
					mainFrame.setCheeringName(horses[horseList1.getSelectedIndex() - 1].getName());
				}catch(ArrayIndexOutOfBoundsException e1){
//					e1.printStackTrace();
				}
				try{
					if(!bettingValField.getText().trim().equals("")){
						mainFrame.getLogPanel().setBetResult((int)(horses[horseList1.getSelectedIndex() - 1].getBettingRate() * Integer.parseInt(bettingValField.getText().trim())));
					}
				}catch(NumberFormatException e1){
//					e1.printStackTrace();
				}
			}
		});
		
		bettingValField.addFocusListener(new FocusListener() {
			Horse[] horses = mainFrame.getRacingPanel().getHorses();
			
			@Override
			public void focusLost(FocusEvent e) {
				Horse[] horses = mainFrame.getRacingPanel().getHorses();
				mainFrame.getRacingPanel().repaint();
				mainFrame.setRunningState(CheerUp.READY);
				try{
					mainFrame.setCheeringName(horses[horseList1.getSelectedIndex() - 1].getName());
				}catch(ArrayIndexOutOfBoundsException e1){
//					e1.printStackTrace();
				}
				try{
					if(!bettingValField.getText().trim().equals("")){
						mainFrame.getLogPanel().setBetResult((int)(horses[horseList1.getSelectedIndex() - 1].getBettingRate() * Integer.parseInt(bettingValField.getText().trim())));
					}
				}catch(NumberFormatException e1){
//					e1.printStackTrace();
				}catch(ArrayIndexOutOfBoundsException e1){
					mainFrame.setRunningState(CheerUp.WAITING);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				mainFrame.getLogPanel().setBetResult(0);
			}
		});
		
		
	}
	
	public int getSelectedHorse() {
		return (horseList1.getSelectedIndex() - 1);
	}

	public void refreshComboList(){
//		horseNames = new String[racingPanel.getHorses().length + 1];
		horseList1.removeAllItems();
		horseList1.addItem("���þ���   ");
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			String name = (i+1) + " : " + racingPanel.getHorses()[i].getName();
			String bettingRate = " (���� : " +new DecimalFormat("#.##").format(racingPanel.getHorses()[i].getBettingRate()) + ")";  
			horseNames[i+1] =  name + bettingRate;
			horseList1.addItem(horseNames[i+1]);
		}
		
//		horseList2.removeAllItems();
//		horseList2.addItem("���þ���    ");
//		for(int i = 0; i < racingPanel.getHorses().length; i++){
//			horseList2.addItem(racingPanel.getHorses()[i].getName());
//		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		TODO : 1���� 2���� �ߺ��� ���ָ��� ���� ���ϵ��� �����ؾ� ��.
		
		try{
//			���ָ��� �������� ������ �����޼���
			if(horseList1.getSelectedIndex() == 0){
				JOptionPane.showMessageDialog(null, "���ָ��� �������� �ʾҽ��ϴ�.");
				return;
			}
//			���ñݾ��� ������ �����޼���
			if(bettingValField.getText().trim().equals("")){
				JOptionPane.showMessageDialog(null, "���� �ݾ��� �����Ͽ� �ֽʽÿ�.");
				bettingValField.setText("");
				bettingValField.requestFocus();
				return;
			}
//			�ݾ��� 0���� �۰ų� ������ �����޼���
			if( bettingValField.getText().trim().equals("0") || Integer.parseInt(bettingValField.getText().trim()) < 0){
				JOptionPane.showMessageDialog(null, "���ñݾ��� 0�� ���� Ŀ�� �մϴ�.");
				bettingValField.setText("");
				bettingValField.requestFocus();
				return;
			}
//			�ݾ� �ʰ�
			if(Integer.parseInt(bettingValField.getText().trim()) > MainFrame.getUser().getBalance()){
				JOptionPane.showMessageDialog(null, "���� �ݾ��� ������� �ܾ׺��� �����ϴ�.");
				bettingValField.setText(MainFrame.getUser().getBalance()+ "");
				bettingValField.requestFocus();
				return;
			}
			
			
		}catch(NumberFormatException e1){
//			���ñݾ��� parsing�ϴ� �������� ���ñݾ׿� ���ڰ� �ƴ� ���ڿ��� �� �Ľ��� �ȵǸ� ����ó���� �Ѵ�.
			JOptionPane.showMessageDialog(null, "�ݾ��� ���ڸ� �Է��� �� �ֽ��ϴ�.");
			bettingValField.setText("");
			bettingValField.requestFocus();
			return;
		}try{
			mainFrame.getLogPanel().setBetResult((int)(racingPanel.getHorses()[horseList1.getSelectedIndex() - 1].getBettingRate() * Integer.parseInt(bettingValField.getText().trim())));
		}catch(NumberFormatException e1){
			System.out.println("NumberFormatException");
			mainFrame.getLogPanel().setBetResult(0);
		}
		mainFrame.setRunningState(CheerUp.READY);
		mainFrame.setCheeringName(racingPanel.getHorses()[horseList1.getSelectedIndex() - 1].getName());
		Thread thread = new Thread(racingPanel);
		thread.start();
		
		selectButton.setEnabled(false);
		horseList1.setEnabled(false);
		horseList2.setEnabled(false);
		bettingValField.setEnabled(false);
			
	}

	
	
	public void racingResult(){
//		�޴����� ������ ������ ��� ������ �ֱ� ������ ��� ó���� �ٽ� �޾ƿ´�.
//		1. ���ð�� ��� -> ��ŷ ����� ���� ���ñݾ��� user ���� ����Ѵ�.
//			1-1. ���� �� -> ������ ���� ������ ���߾����� ���ñݾ׸�ŭ �����ش�.
//		2. ��ư Ȱ��ȭ -> ���� ������ ������ ���� ���� ��Ȱ��ȭ �� ��ư�� �޺��ڽ����� ��� ���� �� �ٽ� Ȱ��ȭ �Ѵ�.
//		3. user�� �ܾ� ��ǥ�� -> ���� ����� ���� �ܾ��� �ٽ� refresh�Ѵ�.
//		4. �Ļ��� ��� �����޼��� ��°� ���� ����
							
		
		double bettingRate = racingPanel.getHorses()[horseList1.getSelectedIndex()-1].getBettingRate();			//����
		int moneyToGet;
		moneyToGet= (int)(Integer.parseInt(bettingValField.getText().trim()) * (int)bettingRate);
		
		
//		���ð�� �� ���ͺй�
		int bettingResult = 0;				//�������ι޴� �ݾ�
		mainFrame.getLogPanel().setBetResult(moneyToGet);
		int winner = racingPanel.getRank()[0];			//1��� �˷��ش�. 
		String resultStr = "����� " + (winner+1) + "�� Ʈ���� �� " + racingPanel.getHorses()[winner].getName()+" �Դϴ�.";
		if(racingPanel.getRank()[0] == horseList1.getSelectedIndex() - 1){
//			1���� ���� ���߸� ���÷���ŭ �����ش�.
//			���� �ӽ÷� ���÷��� 100%�� �����س��´�.
			bettingResult = (int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate);
			resultStr += "\n�����մϴ�! 1����  ���߼̽��ϴ�!";
			
		}
		else{
			bettingResult = -1 * (int)(Integer.parseInt(bettingValField.getText().trim()) );
			resultStr += "\n�ƽ��Ե� 1����  ������ ���ϼ̽��ϴ�.";
		}
		
		if(horseList2.getSelectedIndex() != 0){
//			2���� ���� �������� ��� �߰� ����
			bettingRate = racingPanel.getHorses()[horseList2.getSelectedIndex()-1].getBettingRate();
			if(racingPanel.getRank()[1]+1 == horseList2.getSelectedIndex()){
//			2���� ���� ���߸� ���÷���ŭ �����ش�.
//			���� �ӽ÷� ���÷��� 100%�� �����س��´�.
				bettingResult += (int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate);
				resultStr += "\n2����  ���߼̽��ϴ�!";
			}
			else{
				bettingResult += -1 * (int)(Integer.parseInt(bettingValField.getText().trim()));
				resultStr += "\n2����  ������ ���ϼ̽��ϴ�.";
			}
		}
//		TODO : 1,000 ó�� õ�� �ڸ����� , ���.
		MainFrame.getUser().addBalance(bettingResult);
		resultStr += "\n�� ������ " + bettingResult + "�� �ܰ�� " + MainFrame.getUser().getBalance()+"�� �Դϴ�.";
		JOptionPane.showMessageDialog(null, resultStr);
		
//		User�� �ܾ� ��ǥ��
//		���ñݾ� 
		
		
//		(��ȣ)	���� �����Ѵ�. 
		if(bettingResult > 0){
			mainFrame.getLogPanel().addTotalIncome(bettingResult);
		}else{
			mainFrame.getLogPanel().addTotalLoss(-1 * bettingResult);
		}
		amountLabel.setText("���� �ݾ� : "+ MainFrame.getUser().getBalance() + "��");				//���� ���� �� 
		
//		�Ļ��� ��� �����޼����� �Թ� ����
		if(MainFrame.getUser().getBalance() <= 0){
			mainFrame.setRunningState(CheerUp.LOSE);
			JOptionPane.showMessageDialog(null, "�Ļ��Ͽ����ϴ�.");
			JFrame frame = new JFrame("���� �Ѱ� �µ���");
			frame.setBounds(700, 300, HangangPanel.WIDHT, HangangPanel.HEIGHT);
			frame.add(new HangangPanel());
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
//		��ư Ȱ��ȭ
		selectButton.setEnabled(true);
		horseList1.setEnabled(true);
		horseList2.setEnabled(true);
		bettingValField.setEnabled(true);
		bettingValField.setText("");
	}

	public JTextField getBettingValField() {		//logpanel���� �� ���ð� (��ȣ)
		return bettingValField;
	}
}

class HorseInfoPanel extends JPanel implements Runnable{

	public static final int WIDTH = 600;
	public static final int HEIGHT = 380;
	private Horse horse;
	private JPanel infoPanel = new JPanel();
	private JLabel number = new JLabel("");
	private JLabel name = new JLabel("");
	private JLabel speed = new JLabel("");
	private JLabel rank = new JLabel("");
	private JLabel style = new JLabel("");
	private JLabel bettingRate = new JLabel("");
	
	public HorseInfoPanel(Object horse){
		this((Horse)horse);
	}
	public HorseInfoPanel(Horse horse) {
		this.horse = horse;
		setLayout(new GridLayout(1, 2));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.WHITE);
		setVisible(true);
		
		add(new JPanel(){	});
		add(infoPanel);
		
		Font font = new Font("Dialog", Font.PLAIN, 14);
		infoPanel.setLayout(new GridLayout(6,1));
		number.setText("��ȣ : " + horse.getTrackNumber() + "��");
		name.setText("�̸� : " + horse.getName());
		speed.setText("���ǵ� : " + horse.getRunningPattern().getAvgSpeed() * 2 + " km/h");
		rank.setText("��� : " + horse.getRunningPattern().getRank());
		style.setText("��Ÿ�� : " + horse.getRunningPattern().getPatternName());
		bettingRate.setText("���� : " + new DecimalFormat("#.##").format(horse.getBettingRate()));
		
		number.setFont(font);
		name.setFont(font);
		speed.setFont(font);
		rank.setFont(font);
		rank.setFont(font);
		bettingRate.setFont(font);
		
		infoPanel.add(name);
		infoPanel.add(speed);
		infoPanel.add(rank);
		infoPanel.add(style);
		infoPanel.add(bettingRate);
	}
	
	@Override
	public void run() {
		while(true){
			try { 
				repaint();
				horse.getImageInfo().nextImage();
				Thread.sleep(75); 
			} catch (InterruptedException e) { e.printStackTrace();}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int horseSize= (int)((double)(WIDTH/2-20) / (double)horse.getImageInfo().getWidth() * (double)horse.getImageInfo().getHeight());
		g.setColor(Color.CYAN);
		g.drawImage(horse.getImageInfo().getImage(),						// Image
				0, HEIGHT/2 - horseSize/2 - 50 , WIDTH/2-20, HEIGHT/2 + horseSize/2 - 50,
				horse.getImageInfo().getStartX(),							// �б� ������ ���� �̹����� ���� X ��ǥ
				horse.getImageInfo().getStartY(),							// �б� ������ ���� �̹����� ���� Y ��ǥ
				horse.getImageInfo().getEndX(),								// �б� ������ ���� �̹����� �� X ��ǥ
				horse.getImageInfo().getEndY(),								// �б� ������ ���� �̹����� �� Y ��ǥ
				this);
	}
}

class HangangPanel extends JPanel implements Runnable{

	public static final int WIDHT = 600;
	public static final int HEIGHT = 400;
	private Image bg = Toolkit.getDefaultToolkit().getImage("./src/HangangBackground.jpg");
	
	private int thermo_ypos = 260;		//	260 -> 160
	private int thermo_length = 0;		// 	0 -> 100
	private double thermo_temp = 0;		//	0 -> getHangangTemp()
	private String tempString = thermo_temp + "�� �Դϴ�.";
	private String tempTime = "";
	public HangangPanel() {
		
		Thread thread = new Thread(this);
		thread.start();
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(bg, 0,0, WIDHT, HEIGHT, this);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Dialog", Font.BOLD, 32));
		g.drawString("���� �Ѱ� ������" , 168, 101);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Dialog", Font.BOLD, 32));
		g.drawString(tempString, 188, 138);
//		�ð�
		g.setFont(new Font("Dialog", Font.PLAIN, 16));
		g.drawString(tempTime, 190, 190);
		
//		�µ���
		g.setColor(Color.WHITE);
		g.fillOval(100, 250 , 50, 50);
		g.fillRect(113, 80, 25, 200);
//		������ ����		
		g.setColor(Color.RED);
		g.fillOval(106, 257 , 40, 40);
		g.fillRect(119, thermo_ypos, 12, thermo_length);
//		����
		g.setColor(Color.BLACK);
		for(int i=1; i < 28; i++){
			g.fillRect(113, 80 + i * 6, 7, 2);
		}
	}
	@Override
	public void run() {
		for(int temp = 1; temp <= 100; temp++){
			thermo_temp = (double)HangangTempGetter.getHangangTemp() / 100 * temp;
			thermo_ypos--;
			thermo_length++;
			tempString = String.format("%.2f�� �Դϴ�.", thermo_temp); 
			repaint();
			try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		tempTime = HangangTempGetter.getHangangTime() + " ����";
		repaint();
	}
		
	
}

//-------------------------------------------------------����
class CheerUp extends JPanel implements Runnable {
	
	private MainFrame frame;
	private JLabel cheeruplabel;

	public static final int WAITING = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int WINNING = 3;
	public static final int WIN = 4;
	public static final int LOSE = 5;
	
	private int cheerUpState = WAITING;
	
	private Random random = new Random();
	private String[] wait = {"�����", "�� ������", "������ ���Ǳ�?", "�̹��� �� ���� ������"};
	private String[] ready = {" �ʷ� ���ߴ�!", " �ʶ�� �س��ž�!", " �ʸ� �ϴ´�!"};
	private String[] running = {" ����!!"," ���ݸ� �� ����!!"," ���Դ� ���� ����!"," �� ����!"," ���ݸ� �� ����!"," �޷�!!"};
	private String[] winning = {" ���Ѵ�!", " �ϰ� 1���̾�", " �̴�θ� ��!", " �� ����̾�!"};
	private String[] win = {"�س´�!", "����!", "�󾾱� ������!", "��������?", "�ѽ� ������!"};
	private String[] lose = {"�ȵ�", "���߾��", "���� ���� �Ѱ� ������?", "�� �Ҿ���!"}; 
	private Thread thread;
	private String horseName = "";
	private boolean nameChangeFlag = true;
	
	
	public CheerUp(MainFrame frame){
		this.frame = frame;
//		setPreferredSize(new Dimension(50, 50));
		cheeruplabel = new JLabel("");
		add(cheeruplabel);
		thread = new Thread(this);
		thread.start();
		cheeruplabel.setHorizontalAlignment(JLabel.CENTER);
		cheeruplabel.setFont(new Font("Dialog", Font.BOLD, 20));
	}
	
	@Override
	public void run() {
		int pastState = -1;
		String pastName = "";
		String str = "";
		while(true){
			int duration = 100;
			setBackground(Color.white);
			switch(cheerUpState){
				case WAITING:
					if(pastState != cheerUpState)  str = wait[new Random().nextInt(wait.length)]; break;
				case READY:
					if(pastState != cheerUpState || nameChangeFlag)  str = horseName + ready[new Random().nextInt(ready.length)]; break;
				case WINNING:
					if(pastState != cheerUpState)  str = horseName + winning[new Random().nextInt(winning.length)]; break;
				case WIN:
					if(pastState != cheerUpState)  str = win[new Random().nextInt(win.length)]; break;
				case LOSE:
					if(pastState != cheerUpState)  str = lose[new Random().nextInt(lose.length)]; break;
//				�޸��� �ִ� ���� ��� ���� �ٲ��ش�.
				case RUNNING:
					str = horseName + running[new Random().nextInt(running.length)];
					int r = random.nextInt(100) + 156;
					int g = random.nextInt(100) + 156;
					int b = random.nextInt(100) + 156;
					setBackground(new Color(r, g, b));
					duration = 1500;
					break;
			}
			pastState = cheerUpState;
			nameChangeFlag = false;
			cheeruplabel.setHorizontalAlignment(JLabel.CENTER);
			cheeruplabel.setText(str);
			try {	Thread.sleep(duration);	} catch (InterruptedException e) {	e.printStackTrace(); }

		}
	}
	
	public void setHorseName(String horseName){
		this.horseName = horseName;
		nameChangeFlag = true;
	}
	
	public void setRunningState(int state){
		cheerUpState = state;
	}
}

	

//	�����г�
//	����
class AdvertisePanel extends JPanel implements Runnable{
	MainFrame mainFrame;
	Image[] images = new Image[4];
	int w = 494, h = 150;
	int count = 0;
	String filename = "";
	Thread thread = new Thread(this);
	public AdvertisePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		for(int i = 0; i<images.length; i++) {
			filename = String.format("./src/pag%02d.jpg", i+1);
			images[i] = Toolkit.getDefaultToolkit().getImage(filename);
		}
		thread.start();
	}
	
	
	@Override
	public void paint(Graphics g) {
//		super.paint(g);
		g.drawImage(images[count % 4], 0, 0, this);
	}
	
	@Override
	public void run() {
		int temp = count;
		while(true) {
			Horse[] horses = mainFrame.getRacingPanel().getHorses();
			if(horses[0].getPosition_X() < RacingPanel.FINISH_POINT - 4000  ) {
				count = 0;
			} else if (horses[0].getPosition_X() < RacingPanel.FINISH_POINT - 3000 ) {
				count = 1;
			} else if(horses[0].getPosition_X() < RacingPanel.FINISH_POINT - 2000) {
				count = 2;
				
			} else if(horses[0].getPosition_X() < RacingPanel.FINISH_POINT - 1000){
				count = 3;				
			}else {
				count = 4;
			}
			repaint();
			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
}


class RankingPanel extends JPanel{
	private JLabel rankingTitleLabel = new JLabel("����");			//	���� ���� �� 
	private MainFrame mainFrame;
//	����
//	private static JLabel[] rankshowLabel;
//	��ü �迭 �����
//	Ŭ����[] �迭�̸� = new Ŭ����[����];
//	�迭�̸�[i] = new Ŭ����();
	private JLabel[] rankshowLabel;		//	1����ŷ ���� ����
	private JPanel TitlePanel = new JPanel();					// ���� �г�
	private JPanel TitleMenuPanel = new JPanel();				// ���ǥ��  �г�
	
	public RankingPanel(int TOTAL_HORSE_NUMBER, MainFrame mainFrame){
		this.mainFrame = mainFrame;
		setPreferredSize(new Dimension(600, 300));
		setLayout(new BorderLayout());
		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(100, 10));
		add(blankPanel, BorderLayout.WEST);
		add(TitlePanel, BorderLayout.NORTH);
		add(TitleMenuPanel, BorderLayout.CENTER);
//		TitlePanel.setLayout();
		TitleMenuPanel.setLayout(new GridLayout(3, 2));
		
		TitlePanel.add(rankingTitleLabel);

		rankshowLabel = new JLabel[TOTAL_HORSE_NUMBER];
		for(int i = 0; i < rankshowLabel.length; i++){
			rankshowLabel[i] = new JLabel((i+1) + "�� : ");
			TitleMenuPanel.add(rankshowLabel[i]);
			rankshowLabel[i].setFont(new Font("Dialog", Font.BOLD, 20));
		}
		
//		��ŷ ���� ��

		rankingTitleLabel.setHorizontalAlignment(JLabel.CENTER);
//		������ �ؽ�Ʈ�� ǥ���ϱ� ���� rankingLabel�� ���
//		������ �� �󺧷� ������ �۾� ũ�� ����
//		����
		rankingTitleLabel.setFont(new Font("Dialog", Font.BOLD, 28));
	}
	
	public void printRanking(int[] rank){
//		TODO
//		4. ����ǥ ����ϰ�
//		����� �׳� JLabel �ϳ��� �־ ������� ���ϴ�.
//		rankingPanel �ȿ� Layout ������ �ϰ� JLabel�� ���ָ�����ŭ �־ ������ ����� �� �ֵ��� �����Ѵ�.
		
//		������ �ǽð����� ǥ���ϱ� ���� racingPanel���� ȣ�� �� �� �ֵ��� static���� �����Ͽ���.
//		������ JLabel �ϳ��� �������� ������ �� ������ �ļ��� HTML�� ����Ͽ� �������� ���� �� �ִ�.
//		<br>���� �ٹٲ��� �Ǹ� �Ʒ��� ���� ������� �ۼ��� �� �ִ�.
//		<HTML> ... <br> ... <br> ... </HTML>
		
//		����
		String strTitle = "����";
		String[] str = new String[rank.length];
		for(int i = 0; i < rank.length; i++){	
			str[i] = i+1 + "�� : " + mainFrame.getRacingPanel().getHorses()[rank[i]].getName() + "\n";

			rankshowLabel[i].setText(str[i]);
		}
		
	}

}


class LogPanel extends JPanel{

	private int totalIncome = 0;
	private int totalLoss = 0;
	
	private JPanel logMainPanel = new JPanel();
	private JLabel betResultLabel = new JLabel("0��");
	private JLabel totalSuccessLabel = new JLabel(totalIncome + "��");		
	private JLabel totalFailLabel = new JLabel(totalLoss + "��");
	private DecimalFormat df = new DecimalFormat("#,##0");
	
	public LogPanel(MainFrame mainFrame) {
		
		setLayout(new BorderLayout());
		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(100, 10));
		add(blankPanel, BorderLayout.WEST);
		add(logMainPanel, BorderLayout.CENTER);
		
		
		logMainPanel.setLayout(new GridLayout(3, 2));
		logMainPanel.add(new JLabel("���� (������) : "));
		logMainPanel.add(betResultLabel);
		logMainPanel.add(new JLabel("�� �Ѿ� : "));
		logMainPanel.add(totalSuccessLabel);
		logMainPanel.add(new JLabel("���� �Ѿ� : "));
		logMainPanel.add(totalFailLabel);
	}
	
	public void setBetResult(int val){
		betResultLabel.setText(df.format(val) + "��");
	}
	public void setBetResult(String str){
		betResultLabel.setText(str);
	}
	public void addTotalIncome(int val){
		totalIncome += val;
		totalSuccessLabel.setText(df.format(totalIncome) + "��");
	}
	public void addTotalLoss(int val){
		totalLoss += val;
		totalFailLabel.setText(df.format(totalLoss) + "��");
	}

}

class TitleFrame extends JFrame{
	private TitlePanel basePanel = new TitlePanel(this);
	
	public TitleFrame(){
		setLayout(new BorderLayout());
		
		add(basePanel, BorderLayout.CENTER);
		
		setBounds(200,100, 1212, 728);
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class TitlePanel extends JPanel{
	private Image background = Toolkit.getDefaultToolkit().getImage("./src/title.jpg"); 
	private JPanel upPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel westPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JPanel downPanel = new JPanel();
	private JButton button = new JButton("Press Start");
	private TitleFrame titleFrame;
	public TitlePanel(TitleFrame titleFrame) {
//		downPanel.setBackground(Color.PINK);
//		upPanel.setBackground(Color.BLACK);
//		westPanel.setBackground(Color.BLUE);
		
		this.titleFrame = titleFrame;
		setLayout(new BorderLayout());
		add(upPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
		add(downPanel, BorderLayout.SOUTH);
		
		upPanel.setPreferredSize(new Dimension(1212, 398));
		westPanel.setPreferredSize(new Dimension(400, 200));
		eastPanel.setPreferredSize(new Dimension(400, 200));
		downPanel.setPreferredSize(new Dimension(1212, 200));
		centerPanel.setLayout(new GridLayout(1, 1));
		centerPanel.add(button);
		button.setFont(new Font("Dialog", Font.BOLD, 30));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame();
				titleFrame.setVisible(false);
			}
		});
		repaint();
		
		
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(background, 0, 0, this);
		centerPanel.repaint();
	}
	
	
}