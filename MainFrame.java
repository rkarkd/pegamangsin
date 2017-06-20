package racingHorse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	//	메인 프레임에 모든 패널에 관한 정보와 main함수를 가지고 있다.
	
	public static final int WIDTH = 1200;
	public static final int LANE_SIZE = 60;
	
	private int challengerNumber = 6; 										//	참가자 수
	private RacingPanel racingPanel = new RacingPanel(challengerNumber);	//	경주트랙 패널
	private JPanel upPanel = new JPanel();									//	menu + ranking
	private JPanel rankingPanel = new JPanel();								//	순위 표시
	private MenuPanel menuPanel = new MenuPanel(racingPanel);				//	메뉴 패널
	private JPanel broadcastingPanel = new JPanel();						//		
	private static JLabel rankingLabel = new JLabel("순위");					//	순위 표시 
	
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
		rankingLabel.setFont(new Font("Dialog", Font.BOLD, 30));
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
		String str = "<html>순위<br>";
		for(int i = 0; i < rank.length; i++){
			str += (i+1) + "위 : " + (rank[i]+1) + "번마<br>";
		}
		str += "</html>";
		rankingLabel.setText(str);
	}
}

class RacingPanel extends JPanel implements Runnable {
//	경주마가 달리는 것을 표현하기 위해 JPanel의 paint()함수와 Runnable의 run() 함수를 override 하여 사용한다. 
	public static final int START_POINT = 0; 			// 출발지점
	public static final int FINISH_POINT = 900; 		// 도착지점
	
	private Horse[] horses;
	private static int[] rank;

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
				g.fillOval(horses[i].getPosition_X(), i *MainFrame.LANE_SIZE, 60, MainFrame.LANE_SIZE);
			}catch(NullPointerException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {

		for(int i = 0; i < horses.length; i++){
			rank[i] = i;
		}
		
		while (true) {
			if (isAllHorseFinished()) {
				MenuPanel.setEnableButton(true);
				break;
			}
			for(int i = 0; i < horses.length ; i++){
				if(horses[i].getPosition_X() < FINISH_POINT){
					horses[i].move(new Random().nextInt(6)+1);
				}else{
					horses[i].moveTo(FINISH_POINT);
				}
			}

			//			경주마의 순위를 실시간으로 매긴다.
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
			for(int i=0; i < rank.length; i++){
				System.out.println("rank[" + i +"]" + " = " + rank[i] + " pos : " + horses[rank[i]].getPosition_X());
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
	private JComboBox horseList1;
	private JComboBox horseList2;
	private static JButton selectButton = new JButton("선택 완료");
	
	public MenuPanel(RacingPanel racingPanel){
		this.racingPanel = racingPanel;
		horseNames = new String[racingPanel.getHorses().length + 1];
		horseNames[0] = "선택안함";
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			horseNames[i+1] = racingPanel.getHorses()[i].getName();
		}
		
		horseList1 = new JComboBox(horseNames);
		horseList2 = new JComboBox(horseNames);
		
		setPreferredSize(new Dimension(600,  300));
		setLayout(new GridLayout(2,3));
		
		add(horseList1);
		add(horseList2);
		add(new JLabel("남은 금액 : 1000원"));
		add(new JLabel("배팅액 : 100원"));
		add(new JLabel(" "));
		add(selectButton);
		
		selectButton.addActionListener(this);
	}

	public static void setEnableButton(boolean b){
		selectButton.setEnabled(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Thread thread = new Thread(racingPanel);
		thread.start();
		selectButton.setEnabled(false);
//			JOptionPane.showMessageDialog(null, horseList1.getSelectedItem() + "를 선택하셨습니다.");
			
	}
}
