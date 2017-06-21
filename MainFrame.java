package racingHorse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
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
	
	public static final int WIDTH = 1200;
	public static final int LANE_SIZE = 60;
	
//	말의 수를 challengerNumber로 수정할 수 있다.
	private int challengerNumber = 6; 										//	참가자 수
	private RacingPanel racingPanel = new RacingPanel(challengerNumber);	//	경주트랙 패널
	private JPanel upPanel = new JPanel();									//	menu + ranking
	private JPanel rankingPanel = new JPanel();								//	순위 표시
	private MenuPanel menuPanel = new MenuPanel(racingPanel);				//	메뉴 패널
	private JPanel broadcastingPanel = new JPanel();						//		
	private static JLabel rankingLabel = new JLabel("순위");					//	순위 표시 
	
	private static User user = new User("LocalUser", 10000);
	
	public MainFrame() {
		setTitle("경마 게임");
		setBounds(600, 100, WIDTH, 500 + LANE_SIZE*challengerNumber);
		
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
		add(upPanel, BorderLayout.NORTH);
		add(racingPanel, BorderLayout.CENTER);
		add(broadcastingPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
	}

	public static void printStatus(int rank[]){
//		순위를 실시간으로 표시하기 위해 racingPanel에서 호출 할 수 있도록 static으로 구현하였다.
//		원래는 JLabel 하나에 여러줄을 삽입할 수 없지만 꼼수로 HTML을 사용하여 여러줄을 넣을 수 있다.
//		<br>마다 줄바꿈이 되며 아래와 같은 양식으로 작성할 수 있다.
//		<HTML> ... <br> ... <br> ... </HTML>
		String str = "<html>순위<br>";
		for(int i = 0; i < rank.length; i++){
			str += (i+1) + "위 : " + (rank[i]+1) + "번마<br>";
		}
		str += "</html>";
		rankingLabel.setText(str);
	}

	public static User getUser() {
		return user;
	}
}

class RacingPanel extends JPanel implements Runnable {
//	경주마가 달리는 것을 표현하기 위해 JPanel의 paint()함수와 Runnable의 run() 함수를 override 하여 사용한다. 
	public static final int START_POINT = 10; 			// 출발지점
	public static final int FINISH_POINT = 980; 		// 도착지점
	
	private Horse[] horses;
	private static int[] rank;

	private String countDown = "";
	private Font countDownFont = new Font("Dialog", Font.BOLD, 30);
	
	public RacingPanel(int challengerNumber) {
		horses = new Horse[challengerNumber];
		rank = new int[challengerNumber];
		for(int i = 0; i < challengerNumber; i++){
			horses[i] = new Horse((i+1)+"번마", START_POINT, i *MainFrame.LANE_SIZE);
		}
//		경주마의 수만큼 크기를 늘린다.
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.LANE_SIZE* challengerNumber));

		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
//		배경 그리기
		for(int i = 0; i < horses.length; i++){
			g.setColor(new Color((i % 2 == 0 ? 183 : 0), (i % 2 == 0 ? 255 : 204), (i % 2 == 0 ? 190 : 19)));
			g.fillRect(0, i *MainFrame.LANE_SIZE , MainFrame.WIDTH, MainFrame.LANE_SIZE);
		}
		int horseSize= MainFrame.LANE_SIZE;	// 말의 크기
		
//		결승선
		for(int i = 0; i < horses.length; i++){
			g.setColor(Color.BLACK);
			g.fillRect(FINISH_POINT,  i * MainFrame.LANE_SIZE ,  horseSize/2, horseSize/2);
			g.fillRect(FINISH_POINT+horseSize/2,  i * MainFrame.LANE_SIZE + horseSize/2 , horseSize/2, horseSize/2);
			g.setColor(Color.WHITE);
			g.fillRect(FINISH_POINT,  i * MainFrame.LANE_SIZE + horseSize/2 , horseSize/2, horseSize/2);
			g.fillRect(FINISH_POINT+horseSize/2,  i * MainFrame.LANE_SIZE ,  horseSize/2, horseSize/2);
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(0, horses.length *MainFrame.LANE_SIZE , MainFrame.WIDTH, horses.length*MainFrame.LANE_SIZE);
		
		
//		말 그리기
		g.setColor(Color.PINK);
		for(int i = 0; i < horses.length; i++){
//			말 각각의 위치를 받아와서 그린다.
			try{
				g.fillOval(horses[i].getPosition_X(), horses[i].getPosition_Y(), 60, MainFrame.LANE_SIZE);
			}catch(NullPointerException e){
				e.printStackTrace();
			}
		}
		
//		카운트다운
//		g.setColor(Color.GRAY);							//	글자 그림자
//		g.setFont(new Font("Dialog", Font.BOLD, 35));	//	글자 그림자
//		g.drawString("3", MainFrame.WIDTH/2 - 50, 31);	//	글자 그림자
		g.setColor(Color.BLACK);
		g.setFont(countDownFont);
		g.drawString(countDown, MainFrame.WIDTH/2 - 80, 80);
	}

	@Override
	public void run() {
//		카운트다운
		
		for(int i = 3; i > 0; i--){
			for(int fontSize= 100; fontSize > 50; fontSize--){
				countDown = i + "";
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
				MenuPanel.racingResult();
				break;
			}
//			아직 도착하지 않은 말이 있으면 각 말의 X좌표에 값을 더해 말을 이동시킨다.
			for(int i = 0; i < horses.length ; i++){
				if(horses[i].getPosition_X() < FINISH_POINT){
					horses[i].move(new Random().nextInt(6)+1);
				}else{
					horses[i].moveTo(FINISH_POINT);
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
			try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace();}
		}
	}

	private boolean isAllHorseFinished() {
//		모든 말이 결승선에 도착하면 게임을 끝내야 한다.
		for(int i = 0; i < horses.length ; i++){
			if(horses[i].getPosition_X() < FINISH_POINT){
				return false;
			}
		}
		for(int i = 0; i < horses.length ; i++){
//			위치 초기화
			horses[i].setPosition_X(START_POINT);
		}
		return true;
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
	
	public MenuPanel(RacingPanel racingPanel){
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
				return;
			}
//			금액이 0보다 작거나 같으면 에러메세지
			if( bettingValField.getText().trim().equals("0") || Integer.parseInt(bettingValField.getText().trim()) < 0){
				JOptionPane.showMessageDialog(null, "배팅금액은 0원 보다 커야 합니다.");
				return;
			}
//			금액 초과
			if(Integer.parseInt(bettingValField.getText().trim()) > MainFrame.getUser().getBalance()){
				JOptionPane.showMessageDialog(null, "배팅 금액이 사용자의 잔액보다 많습니다.");
				bettingValField.setText(MainFrame.getUser().getBalance()+ "");
				return;
			}
//			디버그를 위해 선택값이 제대로 설정되었는지 출력
//			이후에 삭제 요망.
			System.out.println(racingPanel.getHorses()[horseList1.getSelectedIndex()-1].getName() + "에 "
					+ Integer.parseInt(bettingValField.getText().trim()) + "원을 걸었습니다.");
			
		}catch(NumberFormatException e1){
//			배팅금액을 parsing하는 과정에서 배팅금액에 숫자가 아닌 문자열이 들어가 파싱이 안되면 예외처리를 한다.
			JOptionPane.showMessageDialog(null, "금액은 숫자만 입력할 수 있습니다.");
			return;
		}
		Thread thread = new Thread(racingPanel);
		thread.start();
		
		selectButton.setEnabled(false);
		horseList1.setEnabled(false);
		horseList2.setEnabled(false);
		bettingValField.setEnabled(false);
			
	}
	
	public static void racingResult(){
//		메뉴에서 배팅의 정보를 모두 가지고 있기 때문에 결과 처리를 다시 받아온다.
//		1. 배팅결과 배분 -> 랭킹 결과에 따라 배팅금액을 user 에게 배분한다.
//			1-1. 순위 비교 -> 배팅한 말의 순위를 맞추었으면 배팅금액만큼 돌려준다.
//		2. 버튼 활성화 -> 이중 쓰레드 실행을 막기 위해 비활성화 한 버튼과 콤보박스들을 경기 종료 후 다시 활성화 한다.
//		3. user의 잔액 재표시 -> 배팅 결과에 따른 잔액을 다시 refresh한다.
//		4. 파산일 경우 에러메세지 출력과 게임 종료

//		배팅결과 및 순익분배
		double bettingRate = 1;	
		if(RacingPanel.getRank()[0]+1 == horseList1.getSelectedIndex()){
//			1위의 말을 맞추면 배팅률만큼 돌려준다.
//			현재 임시로 배팅률은 100%로 설정해놓는다.
			MainFrame.getUser().addBalance((int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate));
		}
		else{
			MainFrame.getUser().addBalance(-1 * (int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate));
		}
		
		if(horseList2.getSelectedIndex() != 0){
//			2위의 말에 배팅했을 경우 추가 배팅
			if(RacingPanel.getRank()[1]+1 == horseList2.getSelectedIndex()){
//			2위의 말을 맞추면 배팅률만큼 돌려준다.
//			현재 임시로 배팅률은 100%로 설정해놓는다.
				MainFrame.getUser().addBalance((int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate));
			}
			else{
				MainFrame.getUser().addBalance(-1 * (int)(Integer.parseInt(bettingValField.getText().trim()) * bettingRate));
			}
		}
		
//		버튼 활성화
		selectButton.setEnabled(true);
		horseList1.setEnabled(true);
		horseList2.setEnabled(true);
		bettingValField.setEnabled(true);
		
//		User의 잔액 재표시
		amountLabel.setText("남은 금액 : "+ MainFrame.getUser().getBalance() + "원");
		
//		파산일 경우 에러메세지와 게밍 종료
		if(MainFrame.getUser().getBalance() <= 0){
//			TODO : 한상수온  표시
			JOptionPane.showMessageDialog(null, "파산하였습니다.");
			System.exit(0);
		}
		
		
	}
}
