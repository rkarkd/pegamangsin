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
	//	메인 프레임에 모든 패널에 관한 정보와 main함수를 가지고 있다.
	
	public static final int WIDTH = 1500;
	public static final int LANE_SIZE = 60;
	
//	말의 수를 TOTAL_HORSE_NUMBER로 수정할 수 있다.
	private int TOTAL_HORSE_NUMBER = 6; 										//	참가자 수
	private RacingPanel racingPanel = new RacingPanel(TOTAL_HORSE_NUMBER, this);	//	경주트랙 패널
	private CheerUp cheerup = new CheerUp(this);
	private JPanel upPanel = new JPanel();									//	menu + ranking
	private RankingPanel rankingPanel = new RankingPanel(TOTAL_HORSE_NUMBER, this);	//	순위 표시 
	
	private MenuPanel menuPanel = new MenuPanel(racingPanel, this);				// 메뉴 패널
	private JPanel southPanel = new JPanel();								// 맨밑 패널
	
	private AdvertisePanel advertisepanel = new AdvertisePanel(this);			// 광고패널
	
	private LogPanel logpanel = new LogPanel(this);

	private static User user = new User("LocalHost", 10000);


	public MainFrame() {
		setTitle("경마 게임");
		setBounds(200, 50, WIDTH, 500 + LANE_SIZE*TOTAL_HORSE_NUMBER + RacingPanel.TRACK_OFFSET*2);
		setLayout(new BorderLayout());

//		위쪽엔 순위 표시 패널과 배팅을 할 수 있는 메뉴 패널로 구성되어있다.
		upPanel.setLayout(new BorderLayout());
		upPanel.add(rankingPanel, BorderLayout.WEST);
		upPanel.add(menuPanel, BorderLayout.CENTER);
		
		southPanel.setPreferredSize(new Dimension(WIDTH, 150));
		southPanel.setLayout(new GridLayout(1,3 ));
		southPanel.add(logpanel);
		southPanel.add(cheerup);
		southPanel.add(advertisepanel);
		
		
//		TODO
//		로그 추가하기, 광고 추가하기
//		(세호) 라벨 추가 

		
//		southPanel.add(logpanel,BorderLayout.WEST);
		

//		southPanel 안에 로그를 넣을 패널과 광고를 넣을 패널을 각가 add 하고
//		각각을 설정해주어야 한다.
//		로그패널과 광고패널을 별도의 class로 선언해주는 것이 코드 짜기에 편할 수 있다.
//		예시 : 
//		southPanel.add(logPanel);
//		southPanel.add(advertisePanel);
//		southPanel.setLayout(new GridLayout(2,1));
//		...
		
//		---------------------현도 추가------------------------
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
//		6. 타이틀 화면 추가
//		타이틀 화면의 JFrame화면에서 버튼을 누르면 ActionPerformed로 
//		MainFrame frame을 생성하여 호출하면 된다.
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
//	경주마가 달리는 것을 표현하기 위해 JPanel의 paint()함수와 Runnable의 run() 함수를 override 하여 사용한다. 
	public static final int START_POINT = 10; 			// 출발지점
	public static final int TRACK_LENGTH = 4500;
	public static final int TRACK_OFFSET = 80;
	public static final int FINISH_POINT = TRACK_LENGTH - 120; 		// 도착지점
	
	private MainFrame mainFrame;
	
	private static Horse[] horses;				//	경주마의 정보를 가지고 있는 Horse객체 배열
	private static int[] rank;					//	순위를 매기기 위한 정수배열
	private int trackMove = 0;					//	트랙이 얼마나 움직였는지 나타내는 정수	
	private boolean horseInfoClickable = true;	//	경기도중 말의 정보를 확인할 수 없도록 설정
	
	private String countDown = "";										//	게임 시작시 나오는 카운트다운 메세지
	private Font countDownFont = new Font("Dialog", Font.BOLD, 30);		//	카운트다운 메세지의  글씨체
	private boolean flag= true;											// 현도
	
	public RacingPanel(int TOTAL_HORSE_NUMBER, MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		horses = new Horse[TOTAL_HORSE_NUMBER];
		rank = new int[TOTAL_HORSE_NUMBER];
		for(int i = 0; i < TOTAL_HORSE_NUMBER; i++){
			horses[i] = new Horse(i, "", START_POINT, i *MainFrame.LANE_SIZE);
		}
//		경주마의 수만큼 크기를 늘린다.
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.LANE_SIZE* TOTAL_HORSE_NUMBER + TRACK_OFFSET));
		
		addMouseListener(new MouseAdapter() {
			

			@Override
			public void mouseClicked(MouseEvent me) {
//				TODO
//				3. 중복창 열림 방지
//				창이 열린 상태인지 확인할 수 있는 boolean flag 변수배열을 사용하여
//				한번 열린 번호의 창은 열리지 않도록 수정한다.
				
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
		
//		배경 그리기
//		트랙 울타리
		g.setColor(new Color(150, 70, 0));				//	갈색 흙
		g.fillRect(0, 0, TRACK_LENGTH, TRACK_OFFSET);	//	트랙 위 흙길
		g.fillRect(0, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length, TRACK_LENGTH * 2, TRACK_OFFSET*4);	//	트랙 아래 흙길
		g.setColor(Color.WHITE);
		g.fillRect(-trackMove, 20, TRACK_LENGTH * 2, 6);
		for(int i = 0; i < TRACK_LENGTH / 180; i++){
			g.fillRect(100 + i * 180 -trackMove, 20, 6, TRACK_OFFSET - 20);
		}
		
//		g.setColor(new Color(150, 70, 0));				//	갈색 흙
//		g.fillRect(0, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length, TRACK_LENGTH, TRACK_OFFSET);	//	트랙 밖 흙길
		
		for(int i = 0; i < horses.length; i++){
//			트랙의 잔디
			if(mainFrame.getSelectedHorse() == i){
				g.setColor(Color.PINK);
			}else{
				g.setColor(new Color((i % 2 == 0 ? 183 : 0), (i % 2 == 0 ? 255 : 204), (i % 2 == 0 ? 190 : 19)));
			}
			g.fillRect(0, i *MainFrame.LANE_SIZE + TRACK_OFFSET, TRACK_LENGTH, MainFrame.LANE_SIZE);
//			잔디위에 트랙번호와 이름
			g.setColor(Color.GRAY);
			g.setFont(new Font("Dialog", Font.BOLD, 22));
			g.drawString("[" + (i+1)+ "] " + horses[i].getName(), 100 - trackMove, i *MainFrame.LANE_SIZE + TRACK_OFFSET + 33);
		}
		
//		1000pixel마다 m 표시
		for(int i = 1; i <= TRACK_LENGTH / 1000; i++){
			g.setColor(Color.WHITE);
			g.drawLine(i * 1000 - trackMove, TRACK_OFFSET, i * 1000 - trackMove, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length);
			g.drawString(i + "0m", i * 1000 - trackMove - 13, TRACK_OFFSET + MainFrame.LANE_SIZE * horses.length + 30);
		}
//		결승선
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
		
	
//		말 그리기
		for(int i = 0; i < horses.length; i++){
			int horseSize= (int)((double)MainFrame.LANE_SIZE / (double)horses[i].getImageInfo().getHeight() * (double)horses[i].getImageInfo().getWidth());
//			말 각각의 위치를 받아와서 그린다.
			try{
//				drawImage(img,  dx1,  dx2,  dy2, sx1, sy1, sx2, sy2, observer) : 원본이미지를 변형시켜 윈도우에 표시함
				g.drawImage(horses[i].getImageInfo().getImage(),						// Image
						horses[i].getPosition_X() - trackMove,							// 윈도우에 이미지가 표시될 시작 X 좌표
						horses[i].getPosition_Y() + TRACK_OFFSET,						// 윈도우에 이미지가 표시될 시작 Y 좌표
						horses[i].getPosition_X() + horseSize -trackMove,				// 윈도우에 이미지가 표시될 끝 X 좌표 = 시작좌표 + 말 가로 크기
						horses[i].getPosition_Y() + MainFrame.LANE_SIZE + TRACK_OFFSET,	// 윈도우에 이미지가 표시될 끝 Y 좌표 = 시작좌표 + 말 세로 크기
						horses[i].getImageInfo().getStartX(),							// 읽기 시작할 원본 이미지의 시작 X 좌표
						horses[i].getImageInfo().getStartY(),							// 읽기 시작할 원본 이미지의 시작 Y 좌표
						horses[i].getImageInfo().getEndX(),								// 읽기 시작할 원본 이미지의 끝 X 좌표
						horses[i].getImageInfo().getEndY(),								// 읽기 시작할 원본 이미지의 끝 Y 좌표
						this);
				
				
			}catch(NullPointerException e){
//				e.printStackTrace();
				System.err.println("이미지 파일을 불러오는데 실패하였습니다.");
			}
		}

		
//		카운트다운
//		g.setColor(Color.GRAY);							//	TODO : 글자 그림자
//		g.setFont(new Font("Dialog", Font.BOLD, 35));	//	글자 그림자
//		g.drawString("3", MainFrame.WIDTH/2 - 50, 31);	//	글자 그림자
		g.setColor(Color.BLACK);
		g.setFont(countDownFont);
		g.drawString(countDown, MainFrame.WIDTH/2 - 180, 80);
	}

	@Override
	public void run() {
//		시합도중 말의 정보를 열 수 없도록 수정
		horseInfoClickable = false;
//		말이 너무 빠르면 보정치를 점점 더한다.
		int tooFastOffset = 1;
//		카운트다운
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
//			CountDown 메세지 마지막 "GO!" 없애기 위해 1초가 지났는지 확인한다.
			if( System.currentTimeMillis() - startTime > 800){
				countDown = "";
			}
			
//			응원패널의 응원문구를 바꿔준다.
			mainFrame.setRunningState(CheerUp.RUNNING);
			if (isAllHorseFinished()) {
//				모든 말이 결승선에 도착하면 MenuPanel에서 결과를 정리한다.
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
//			아직 도착하지 않은 말이 있으면 각 말의 X좌표에 값을 더해 말을 이동시킨다.
			for(int i = 0; i < horses.length ; i++){
				if(horses[i].getPosition_X() < FINISH_POINT){
//					horses[i].move(new Random().nextInt(22)+13);
					horses[i].move();
					horses[i].getImageInfo().nextImage();
				}else{
//					horses[i].moveTo(FINISH_POINT);
				}
			}
			
//			트랙이 살살 움직이도록 모든 말의 X좌표에서 트랙이 움직인 만큼 좌표를 뺀다. 					
//			마지막 결승선이 보이면 더이상 트랙은 움직이지 않는다.
			if( trackMove < TRACK_LENGTH - MainFrame.WIDTH){
				trackMove += 18;
//				1등이 화면의 65%를 넘어가면 트랙이 속도를 더 높혀 따라잡는다.
				if(horses[rank[0]].getPosition_X() - trackMove > MainFrame.WIDTH / 100 * 65){
					trackMove += tooFastOffset++;
//					System.out.println("보정 시작");
				}
			}

			//	경주마의 순위를 실시간으로 매긴다.
			for(int i = 0; i < horses.length-1; i++){
				int max = i;
				if(isFinished(horses[rank[i]])){			//	이미 결승을 통과한 경주마의 순위는 바뀌지 않는다.
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
			
//			내 말이 1등이면 칭찬의 문구로 바꿔준다.
			if(rank[0] == mainFrame.getSelectedHorse()){
				mainFrame.setRunningState(CheerUp.WINNING);
			}
			mainFrame.printRanking(rank);
			repaint();
			try { Thread.sleep(35); } catch (InterruptedException e) { e.printStackTrace();}
		}
	}

	private boolean isAllHorseFinished() {
//		모든 말이 결승선에 도착하면 게임을 끝내야 한다.
		for(int i = 0; i < horses.length ; i++){
			if(horses[i].getPosition_X() < FINISH_POINT){
				return false;
			}
		}
		return true;
	}
	
	public void resetHorsePosition(){
//			위치 초기화
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
	private JButton selectButton = new JButton("선택 완료 ");
	private JTextField bettingValField = new JTextField();
	private JLabel amountLabel = new JLabel();
	private JButton addButton = new JButton("    충전     ");				// 충전 버튼
	private JComboBox moneychoice;							// 충전 금액선택 콤보상자
	private int chargemoney;
//	TODO
//	5. 금액충전
//	금액 충전을 위한 버튼을 만들고 그 버튼의 ActionPerformed 시에
//	static으로 선언되어있는 MainFrame.getUser로() User를 부르고
//	addBalance() 함수로 금액을 충전해준다.
//	예시 )  MainFrame.getUser().addBalance(Integer.parseInt(chargeTestField.getTest().trim()));
//	예시 )  MainFrame.getUser().addBalance(chargeMoney);
//	setText(MainFrame.getUser().getBalance() + " 원");
//	String 에서 Int로 parsing이 일어나므로 예외처리로 반드시 문자열을 걸러내야 한다.
	
	public MenuPanel(RacingPanel racingPanel, MainFrame mainFrame){
//		TODO
//		4. 메뉴 패널 깔끔하게 정리
//		레이아웃 설정을 잘 해서 배치를 깔끔하게 하도록 한다.
//		현재는 레이아웃 설정이 안되어 있어서 기본값인 FlowLayout이다.
		this.mainFrame = mainFrame;
		this.racingPanel = racingPanel;
		horseNames = new String[racingPanel.getHorses().length + 1];
		horseNames[0] = "선택안함";
		
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			String name = (i+1) + " : " + racingPanel.getHorses()[i].getName();
			String bettingRate = " (배당률 : " +new DecimalFormat("#.##").format(racingPanel.getHorses()[i].getBettingRate()) + ")";  
			horseNames[i+1] =  name + bettingRate;
		}
		String[] moneyList = {"충전할 금액","1000","5000","10000","50000","100000","500000"}; 
		
		bettingValField.setPreferredSize(new Dimension(200, 30));
		horseList1 = new JComboBox(horseNames);
		horseList2 = new JComboBox(horseNames);
//		2위 배팅은 더이상 사용하지 않는다.
		horseList2.setVisible(false);
		moneychoice = new JComboBox(moneyList);
		
//		setPreferredSize(new Dimension(600, 300));
		setLayout(new GridLayout(3,1));
//		setLayout(new FlowLayout());
		
//		JPanel moneymenupanel = new JPanel(new GridLayout(1,3));
//		금액콤보상자,충전 버튼, 남은금액 라벨
		JPanel moneymenupanel = new JPanel();
//		moneymenupanel.setPreferredSize(new Dimension(600, 100));
		moneymenupanel.add(moneychoice);
		moneymenupanel.add(addButton);
		addButton.setSize(new Dimension(200, 100));
		moneymenupanel.add(amountLabel);
		amountLabel.setSize(200, 100);
		amountLabel.setText("남은 금액 : "+ MainFrame.getUser().getBalance() + "원");
//		
//		JPanel horsemenupanel = new JPanel(new GridLayout(1,2));
//		말 선택 콤보상자 1,2
		JPanel horsemenupanel = new JPanel();
//		horsemenupanel.setPreferredSize(new Dimension(500, 100)); 
		horsemenupanel.add(horseList1);
		horsemenupanel.add(horseList2);
		
		
//		JPanel bettingpanel = new JPanel(new GridLayout(1, 4));
//		베팅 라벨, 금액 입력 텍스트필드, 선택완료 버튼
		JPanel bettingpanel = new JPanel();
		bettingpanel.add(new JLabel("배팅액 : "));
		bettingpanel.add(bettingValField);
		bettingpanel.add(new JLabel(" "));
		bettingpanel.add(selectButton);
		
//		menupanel에 각 패널을 추가		
		add(moneymenupanel);
		add(horsemenupanel);
		add(bettingpanel);
		
		selectButton.addActionListener(this);
		bettingValField.addActionListener(this);
		
		addButton.addActionListener(new ActionListener() {
//			처음 금액은 0원으로 시작하고 충전금액선택하고 충전버튼 누를때마다 더해지게 해놨습니다.
//			그리고 금액이 선택안되었을때는 충전할 금액 선택하라고 메시지 띄워줍니다.
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(moneychoice.getSelectedIndex()){
				case 0:
					JOptionPane.showMessageDialog(moneychoice, "충전할 금액을 선택해주세요!!!!");
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
				amountLabel.setText("남은 금액 : "+ MainFrame.getUser().getBalance() + "원");
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
		horseList1.addItem("선택안함   ");
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			String name = (i+1) + " : " + racingPanel.getHorses()[i].getName();
			String bettingRate = " (배당률 : " +new DecimalFormat("#.##").format(racingPanel.getHorses()[i].getBettingRate()) + ")";  
			horseNames[i+1] =  name + bettingRate;
			horseList1.addItem(horseNames[i+1]);
		}
		
//		horseList2.removeAllItems();
//		horseList2.addItem("선택안함    ");
//		for(int i = 0; i < racingPanel.getHorses().length; i++){
//			horseList2.addItem(racingPanel.getHorses()[i].getName());
//		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		TODO : 1위와 2위에 중복된 경주마를 걸지 못하도록 설정해야 함.
		
		try{
//			경주마를 선택하지 않으면 에러메세지
			if(horseList1.getSelectedIndex() == 0){
				JOptionPane.showMessageDialog(null, "경주마를 선택하지 않았습니다.");
				return;
			}
//			배팅금액이 없으면 에러메세지
			if(bettingValField.getText().trim().equals("")){
				JOptionPane.showMessageDialog(null, "배팅 금액을 설정하여 주십시오.");
				bettingValField.setText("");
				bettingValField.requestFocus();
				return;
			}
//			금액이 0보다 작거나 같으면 에러메세지
			if( bettingValField.getText().trim().equals("0") || Integer.parseInt(bettingValField.getText().trim()) < 0){
				JOptionPane.showMessageDialog(null, "배팅금액은 0원 보다 커야 합니다.");
				bettingValField.setText("");
				bettingValField.requestFocus();
				return;
			}
//			금액 초과
			if(Integer.parseInt(bettingValField.getText().trim()) > MainFrame.getUser().getBalance()){
				JOptionPane.showMessageDialog(null, "배팅 금액이 사용자의 잔액보다 많습니다.");
				bettingValField.setText(MainFrame.getUser().getBalance()+ "");
				bettingValField.requestFocus();
				return;
			}
			
			
		}catch(NumberFormatException e1){
//			배팅금액을 parsing하는 과정에서 배팅금액에 숫자가 아닌 문자열이 들어가 파싱이 안되면 예외처리를 한다.
			JOptionPane.showMessageDialog(null, "금액은 숫자만 입력할 수 있습니다.");
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
//		메뉴에서 배팅의 정보를 모두 가지고 있기 때문에 결과 처리를 다시 받아온다.
//		1. 배팅결과 배분 -> 랭킹 결과에 따라 배팅금액을 user 에게 배분한다.
//			1-1. 순위 비교 -> 배팅한 말의 순위를 맞추었으면 배팅금액만큼 돌려준다.
//		2. 버튼 활성화 -> 이중 쓰레드 실행을 막기 위해 비활성화 한 버튼과 콤보박스들을 경기 종료 후 다시 활성화 한다.
//		3. user의 잔액 재표시 -> 배팅 결과에 따른 잔액을 다시 refresh한다.
//		4. 파산일 경우 에러메세지 출력과 게임 종료
							
		
		double bettingRate = racingPanel.getHorses()[horseList1.getSelectedIndex()-1].getBettingRate();			//배당률
		int moneyToGet;
		moneyToGet= (int)(Integer.parseInt(bettingValField.getText().trim()) * (int)bettingRate);
		
		
//		배팅결과 및 순익분배
		int bettingResult = 0;				//배팅으로받는 금액
		mainFrame.getLogPanel().setBetResult(moneyToGet);
		int winner = racingPanel.getRank()[0];			//1등말을 알려준다. 
		String resultStr = "우승은 " + (winner+1) + "번 트랙의 말 " + racingPanel.getHorses()[winner].getName()+" 입니다.";
		if(racingPanel.getRank()[0] == horseList1.getSelectedIndex() - 1){
//			1위의 말을 맞추면 배팅률만큼 돌려준다.
//			현재 임시로 배팅률은 100%로 설정해놓는다.
			bettingResult = (int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate);
			resultStr += "\n축하합니다! 1등을  맞추셨습니다!";
			
		}
		else{
			bettingResult = -1 * (int)(Integer.parseInt(bettingValField.getText().trim()) );
			resultStr += "\n아쉽게도 1등을  맞추지 못하셨습니다.";
		}
		
		if(horseList2.getSelectedIndex() != 0){
//			2위의 말에 배팅했을 경우 추가 배팅
			bettingRate = racingPanel.getHorses()[horseList2.getSelectedIndex()-1].getBettingRate();
			if(racingPanel.getRank()[1]+1 == horseList2.getSelectedIndex()){
//			2위의 말을 맞추면 배팅률만큼 돌려준다.
//			현재 임시로 배팅률은 100%로 설정해놓는다.
				bettingResult += (int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate);
				resultStr += "\n2등을  맞추셨습니다!";
			}
			else{
				bettingResult += -1 * (int)(Integer.parseInt(bettingValField.getText().trim()));
				resultStr += "\n2등을  맞추지 못하셨습니다.";
			}
		}
//		TODO : 1,000 처럼 천의 자리마다 , 찍기.
		MainFrame.getUser().addBalance(bettingResult);
		resultStr += "\n총 수입은 " + bettingResult + "원 잔고는 " + MainFrame.getUser().getBalance()+"원 입니다.";
		JOptionPane.showMessageDialog(null, resultStr);
		
//		User의 잔액 재표시
//		배팅금액 
		
		
//		(세호)	라벨을 수정한다. 
		if(bettingResult > 0){
			mainFrame.getLogPanel().addTotalIncome(bettingResult);
		}else{
			mainFrame.getLogPanel().addTotalLoss(-1 * bettingResult);
		}
		amountLabel.setText("남은 금액 : "+ MainFrame.getUser().getBalance() + "원");				//현재 남은 돈 
		
//		파산일 경우 에러메세지와 게밍 종료
		if(MainFrame.getUser().getBalance() <= 0){
			mainFrame.setRunningState(CheerUp.LOSE);
			JOptionPane.showMessageDialog(null, "파산하였습니다.");
			JFrame frame = new JFrame("지금 한강 온도는");
			frame.setBounds(700, 300, HangangPanel.WIDHT, HangangPanel.HEIGHT);
			frame.add(new HangangPanel());
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
//		버튼 활성화
		selectButton.setEnabled(true);
		horseList1.setEnabled(true);
		horseList2.setEnabled(true);
		bettingValField.setEnabled(true);
		bettingValField.setText("");
	}

	public JTextField getBettingValField() {		//logpanel에서 쓸 배팅값 (세호)
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
		number.setText("번호 : " + horse.getTrackNumber() + "번");
		name.setText("이름 : " + horse.getName());
		speed.setText("스피드 : " + horse.getRunningPattern().getAvgSpeed() * 2 + " km/h");
		rank.setText("등급 : " + horse.getRunningPattern().getRank());
		style.setText("스타일 : " + horse.getRunningPattern().getPatternName());
		bettingRate.setText("배당률 : " + new DecimalFormat("#.##").format(horse.getBettingRate()));
		
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
				horse.getImageInfo().getStartX(),							// 읽기 시작할 원본 이미지의 시작 X 좌표
				horse.getImageInfo().getStartY(),							// 읽기 시작할 원본 이미지의 시작 Y 좌표
				horse.getImageInfo().getEndX(),								// 읽기 시작할 원본 이미지의 끝 X 좌표
				horse.getImageInfo().getEndY(),								// 읽기 시작할 원본 이미지의 끝 Y 좌표
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
	private String tempString = thermo_temp + "℃ 입니다.";
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
		g.drawString("지금 한강 수온은" , 168, 101);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Dialog", Font.BOLD, 32));
		g.drawString(tempString, 188, 138);
//		시간
		g.setFont(new Font("Dialog", Font.PLAIN, 16));
		g.drawString(tempTime, 190, 190);
		
//		온도계
		g.setColor(Color.WHITE);
		g.fillOval(100, 250 , 50, 50);
		g.fillRect(113, 80, 25, 200);
//		빨간색 수은		
		g.setColor(Color.RED);
		g.fillOval(106, 257 , 40, 40);
		g.fillRect(119, thermo_ypos, 12, thermo_length);
//		눈금
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
			tempString = String.format("%.2f℃ 입니다.", thermo_temp); 
			repaint();
			try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		tempTime = HangangTempGetter.getHangangTime() + " 기준";
		repaint();
	}
		
	
}

//-------------------------------------------------------현도
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
	private String[] wait = {"대기중", "말 고르는중", "시작해 보실까?", "이번엔 꼭 따고 말꺼야"};
	private String[] ready = {" 너로 정했다!", " 너라면 해낼거야!", " 너만 믿는다!"};
	private String[] running = {" 힘내!!"," 조금만 더 힘내!!"," 젖먹던 힘좀 내봐!"," 더 빨리!"," 조금만 더 빨리!"," 달려!!"};
	private String[] winning = {" 잘한다!", " 니가 1등이야", " 이대로만 가!", " 곧 우승이야!"};
	private String[] win = {"해냈다!", "땄다!", "얼씨구 좋구나!", "존나좋군?", "앗싸 좋구나!"};
	private String[] lose = {"안돼", "망했어요", "으음 오늘 한강 따숩나?", "다 잃었어!"}; 
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
//				달리고 있는 경우는 계속 말을 바꿔준다.
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

	

//	광고패널
//	남주
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
	private JLabel rankingTitleLabel = new JLabel("순위");			//	순위 제목 라벨 
	private MainFrame mainFrame;
//	남주
//	private static JLabel[] rankshowLabel;
//	객체 배열 선언법
//	클래스[] 배열이름 = new 클래스[숫자];
//	배열이름[i] = new 클래스();
	private JLabel[] rankshowLabel;		//	1위랭킹 순위 수정
	private JPanel TitlePanel = new JPanel();					// 순위 패널
	private JPanel TitleMenuPanel = new JPanel();				// 등수표시  패널
	
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
			rankshowLabel[i] = new JLabel((i+1) + "위 : ");
			TitleMenuPanel.add(rankshowLabel[i]);
			rankshowLabel[i].setFont(new Font("Dialog", Font.BOLD, 20));
		}
		
//		랭킹 순위 라벨

		rankingTitleLabel.setHorizontalAlignment(JLabel.CENTER);
//		순위를 텍스트로 표시하기 위해 rankingLabel을 사용
//		순위를 각 라벨로 나뉘어 글씨 크기 조정
//		남주
		rankingTitleLabel.setFont(new Font("Dialog", Font.BOLD, 28));
	}
	
	public void printRanking(int[] rank){
//		TODO
//		4. 순위표 깔끔하게
//		현재는 그냥 JLabel 하나만 있어서 깔끔하지 못하다.
//		rankingPanel 안에 Layout 설정을 하고 JLabel을 경주마수만큼 넣어서 순위를 등록할 수 있도록 수정한다.
		
//		순위를 실시간으로 표시하기 위해 racingPanel에서 호출 할 수 있도록 static으로 구현하였다.
//		원래는 JLabel 하나에 여러줄을 삽입할 수 없지만 꼼수로 HTML을 사용하여 여러줄을 넣을 수 있다.
//		<br>마다 줄바꿈이 되며 아래와 같은 양식으로 작성할 수 있다.
//		<HTML> ... <br> ... <br> ... </HTML>
		
//		남주
		String strTitle = "순위";
		String[] str = new String[rank.length];
		for(int i = 0; i < rank.length; i++){	
			str[i] = i+1 + "위 : " + mainFrame.getRacingPanel().getHorses()[rank[i]].getName() + "\n";

			rankshowLabel[i].setText(str[i]);
		}
		
	}

}


class LogPanel extends JPanel{

	private int totalIncome = 0;
	private int totalLoss = 0;
	
	private JPanel logMainPanel = new JPanel();
	private JLabel betResultLabel = new JLabel("0원");
	private JLabel totalSuccessLabel = new JLabel(totalIncome + "원");		
	private JLabel totalFailLabel = new JLabel(totalLoss + "원");
	private DecimalFormat df = new DecimalFormat("#,##0");
	
	public LogPanel(MainFrame mainFrame) {
		
		setLayout(new BorderLayout());
		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(100, 10));
		add(blankPanel, BorderLayout.WEST);
		add(logMainPanel, BorderLayout.CENTER);
		
		
		logMainPanel.setLayout(new GridLayout(3, 2));
		logMainPanel.add(new JLabel("배당금 (성공시) : "));
		logMainPanel.add(betResultLabel);
		logMainPanel.add(new JLabel("딴 총액 : "));
		logMainPanel.add(totalSuccessLabel);
		logMainPanel.add(new JLabel("잃은 총액 : "));
		logMainPanel.add(totalFailLabel);
	}
	
	public void setBetResult(int val){
		betResultLabel.setText(df.format(val) + "원");
	}
	public void setBetResult(String str){
		betResultLabel.setText(str);
	}
	public void addTotalIncome(int val){
		totalIncome += val;
		totalSuccessLabel.setText(df.format(totalIncome) + "원");
	}
	public void addTotalLoss(int val){
		totalLoss += val;
		totalFailLabel.setText(df.format(totalLoss) + "원");
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