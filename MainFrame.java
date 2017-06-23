package racingHorse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	
//	말의 수를 challengerNumber로 수정할 수 있다.
	private int challengerNumber = 6; 										//	참가자 수
	private RacingPanel racingPanel = new RacingPanel(challengerNumber, this);	//	경주트랙 패널
	private JPanel upPanel = new JPanel();									//	menu + ranking
	private JPanel rankingPanel = new JPanel();								//	순위 표시
	private MenuPanel menuPanel = new MenuPanel(racingPanel);				//	메뉴 패널
	private JPanel southPanel = new JPanel();						//		
	private static JLabel rankingLabel = new JLabel("순위");					//	순위 표시 
	
	private static User user = new User("LocalHost", 10000);
	
	public MainFrame() {
		setTitle("경마 게임");
		setBounds(200, 100, WIDTH, 500 + LANE_SIZE*challengerNumber + RacingPanel.TRACK_OFFSET*2);
		setLayout(new BorderLayout());

//		위쪽엔 순위 표시 패널과 배팅을 할 수 있는 메뉴 패널로 구성되어있다.
		upPanel.setLayout(new BorderLayout());
		upPanel.add(rankingPanel, BorderLayout.WEST);
		upPanel.add(menuPanel, BorderLayout.CENTER);

//		rankingPanel은 순위를 표시한다.
		rankingPanel.setPreferredSize(new Dimension(600, 300));
		rankingPanel.add(rankingLabel);
//		순위를 텍스트로 표시하기 위해 rankingLabel을 사용
		rankingLabel.setText("순위");
		rankingLabel.setFont(new Font("Dialog", Font.BOLD, 28));

		southPanel.setPreferredSize(new Dimension(WIDTH, 150));
//		TODO
//		로그 추가하기, 광고 추가하기
//		southPanel 안에 로그를 넣을 패널과 광고를 넣을 패널을 각가 add 하고
//		각각을 설정해주어야 한다.
//		로그패널과 광고패널을 별도의 class로 선언해주는 것이 코드 짜기에 편할 수 있다.
//		예시 : 
//		southPanel.add(logPanel);
//		southPanel.add(advertisePanel);
//		southPanel.setLayout(new GridLayout(2,1));
//		...
		
		add(upPanel, BorderLayout.NORTH);
		add(racingPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
//		TODO
//		6. 타이틀 화면 추가
//		타이틀 화면의 JFrame화면에서 버튼을 누르면 ActionPerformed로 
//		MainFrame frame을 생성하여 호출하면 된다.
		MainFrame frame = new MainFrame();
	}

	public static void printStatus(int rank[]){
//		TODO
//		4. 순위표 깔끔하게
//		현재는 그냥 JLabel 하나만 있어서 깔끔하지 못하다.
//		rankingPanel 안에 Layout 설정을 하고 JLabel을 경주마수만큼 넣어서 순위를 등록할 수 있도록 수정한다.
		
//		순위를 실시간으로 표시하기 위해 racingPanel에서 호출 할 수 있도록 static으로 구현하였다.
//		원래는 JLabel 하나에 여러줄을 삽입할 수 없지만 꼼수로 HTML을 사용하여 여러줄을 넣을 수 있다.
//		<br>마다 줄바꿈이 되며 아래와 같은 양식으로 작성할 수 있다.
//		<HTML> ... <br> ... <br> ... </HTML>
		String str = "<html>순위<br>";
		for(int i = 0; i < rank.length; i++){
			str += (i+1) + "위 : " + (rank[i]+1) + "번 트랙 "+ RacingPanel.getHorses()[rank[i]].getName()+"<br>";
		}
		str += "</html>";
		rankingLabel.setText(str);
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
	
	public RacingPanel(int challengerNumber, MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		horses = new Horse[challengerNumber];
		rank = new int[challengerNumber];
		for(int i = 0; i < challengerNumber; i++){
			horses[i] = new Horse(i, "", START_POINT, i *MainFrame.LANE_SIZE);
		}
//		경주마의 수만큼 크기를 늘린다.
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.LANE_SIZE* challengerNumber + TRACK_OFFSET));
		
		addMouseListener(new MouseAdapter() {
			

			@Override
			public void mouseClicked(MouseEvent me) {
//				TODO
//				3. 중복창 열림 방지
//				창이 열린 상태인지 확인할 수 있는 boolean flag 변수배열을 사용하여
//				한번 열린 번호의 창은 열리지 않도록 수정한다.
				
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
							horseInfoFrame.setVisible(true);
							HorseInfoPanel horseInfoPanel = new HorseInfoPanel(horses[i]);
							horseInfoFrame.add(horseInfoPanel);
							Thread thread = new Thread(horseInfoPanel);
							thread.start();
							horseInfoFrame.addWindowListener(new WindowAdapter() {
								@Override
								public void windowClosing(WindowEvent e) {
									thread.stop();
								}
							});
						}
						
					}
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
			g.setColor(new Color((i % 2 == 0 ? 183 : 0), (i % 2 == 0 ? 255 : 204), (i % 2 == 0 ? 190 : 19)));
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
			if (isAllHorseFinished()) {
//				모든 말이 결승선에 도착하면 MenuPanel에서 결과를 정리한다.
				countDownFont = new Font("Dialog", Font.BOLD, 80);
				countDown = "Finished!";
				repaint();
				try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace();}
				resetHorsePosition();
//				MenuPanel.racingResult();
				mainFrame.racingResult();
//				MainFrame.printStatus(rank);
				mainFrame.refreshMenuPanel();
				countDown = "";
				repaint();
				horseInfoClickable = true;
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
			MainFrame.printStatus(rank);
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

	public static Horse[] getHorses() {
		return horses;
	}

	public static int getStartPoint() {
		return START_POINT;
	}

	public static int getFinishPoint() {
		return FINISH_POINT;
	}

	public static int[] getRank() {
		return rank;
	}

}

class MenuPanel extends JPanel implements ActionListener{
	private RacingPanel racingPanel;
	private String[] horseNames;
	private static JComboBox horseList1;
	private static JComboBox horseList2;
	private static JButton selectButton = new JButton("선택 완료");
	private static JTextField bettingValField = new JTextField();
	private static JLabel amountLabel = new JLabel();
	
//	TODO
//	5. 금액충전
//	금액 충전을 위한 버튼을 만들고 그 버튼의 ActionPerformed 시에
//	static으로 선언되어있는 MainFrame.getUser로() User를 부르고
//	addBalance() 함수로 금액을 충전해준다.
//	예시 )  MainFrame.getUser().addBalance(Integer.parseInt(chargeTestField.getTest().trim()));
//	String 에서 Int로 parsing이 일어나므로 예외처리로 반드시 문자열을 걸러내야 한다.
	
	public MenuPanel(RacingPanel racingPanel){
//		TODO
//		4. 메뉴 패널 깔끔하게 정리
//		레이아웃 설정을 잘 해서 배치를 깔끔하게 하도록 한다.
//		현재는 레이아웃 설정이 안되어 있어서 기본값인 FlowLayout이다.
		this.racingPanel = racingPanel;
		horseNames = new String[racingPanel.getHorses().length + 1];
		horseNames[0] = "선택안함";
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			horseNames[i+1] = racingPanel.getHorses()[i].getName();
		}
		
		bettingValField.setPreferredSize(new Dimension(200, 30));
		horseList1 = new JComboBox(horseNames);
		horseList2 = new JComboBox(horseNames);
		
		setPreferredSize(new Dimension(600,  300));
//		setLayout(new GridLayout(2,3));
		setLayout(new FlowLayout());
		
		add(new JLabel());
		add(horseList1);
		add(horseList2);
		amountLabel.setText("남은 금액 : "+ MainFrame.getUser().getBalance() + "원");
		add(amountLabel);
		add(new JLabel("배팅액 : "));
		add(bettingValField);
		
		add(new JLabel(" "));
		add(selectButton);
		
		selectButton.addActionListener(this);
		bettingValField.addActionListener(this);
	}

	public void refreshComboList(){
		horseNames = new String[racingPanel.getHorses().length + 1];
		horseList1.removeAllItems();
		horseList1.addItem("선택안함");
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			horseList1.addItem(racingPanel.getHorses()[i].getName());
		}
		
		horseList2.removeAllItems();
		horseList2.addItem("선택안함");
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			horseList2.addItem(racingPanel.getHorses()[i].getName());
		}
		
		
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
//			디버그를 위해 선택값이 제대로 설정되었는지 출력
//			이후에 삭제 요망.
			System.out.println(racingPanel.getHorses()[horseList1.getSelectedIndex()-1].getName() + "에 "
					+ Integer.parseInt(bettingValField.getText().trim()) + "원을 걸었습니다.");
			
		}catch(NumberFormatException e1){
//			배팅금액을 parsing하는 과정에서 배팅금액에 숫자가 아닌 문자열이 들어가 파싱이 안되면 예외처리를 한다.
			JOptionPane.showMessageDialog(null, "금액은 숫자만 입력할 수 있습니다.");
			bettingValField.setText("");
			bettingValField.requestFocus();
			return;
		}
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

//		배팅결과 및 순익분배
		double bettingRate = RacingPanel.getHorses()[horseList1.getSelectedIndex()-1].getBettingRate();
		int bettingResult = 0;
		int winner = RacingPanel.getRank()[0];
		String resultStr = "우승은 " + (winner+1) + "번 트랙의 말 " + RacingPanel.getHorses()[winner].getName()+" 입니다.";
		if(RacingPanel.getRank()[0]+1 == horseList1.getSelectedIndex()){
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
			bettingRate = RacingPanel.getHorses()[horseList2.getSelectedIndex()-1].getBettingRate();
			if(RacingPanel.getRank()[1]+1 == horseList2.getSelectedIndex()){
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
		amountLabel.setText("남은 금액 : "+ MainFrame.getUser().getBalance() + "원");
		
//		파산일 경우 에러메세지와 게밍 종료
		if(MainFrame.getUser().getBalance() <= 0){
//			
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
	
	public HorseInfoPanel(Horse horse) {
		this.horse = horse;
		setLayout(new GridLayout(1, 2));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.WHITE);
		setVisible(true);
		
		add(new JPanel(){	});
		add(infoPanel);
		
		Font font = new Font("Dialog", Font.PLAIN, 14);
		infoPanel.setLayout(new GridLayout(5,1));
		number.setText("번호 : " + horse.getTrackNumber() + "번");
		name.setText("이름 : " + horse.getName());
		speed.setText("스피드 : " + horse.getRunningPattern().getAvgSpeed() * 2 + " km/h");
		rank.setText("등급 : " + horse.getRunningPattern().getRank());
		style.setText("스타일 : " + horse.getRunningPattern().getPatternName());
		
		number.setFont(font);
		name.setFont(font);
		speed.setFont(font);
		rank.setFont(font);
		rank.setFont(font);
		
		infoPanel.add(name);
		infoPanel.add(speed);
		infoPanel.add(rank);
		infoPanel.add(style);
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
