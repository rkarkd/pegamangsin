
package kr.koreait.teamProject;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TeamProjectMain extends JFrame implements ActionListener{
	
	int totalmoney = 0;
	int chargemoney = 0;
	JPanel bettingPanel = new JPanel(new GridLayout(11,1));
	JPanel eastPanelup = new JPanel();
	JPanel eastPanelDown = new JPanel();
	JPanel eastPanel = new JPanel(new GridLayout(2, 1));
	static JPanel horsePanel = new JPanel(new GridLayout(11, 1));
	JPanel centerPanel = new JPanel();
	JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 70, 0));
	JButton startButton = new JButton("경기 시작");
	JButton resetButton = new JButton("초기화");
	JButton bettingButton = new JButton("배팅");
	JButton chargeButton = new JButton("충전");
	Choice selectHorse = new Choice();
	
	JLabel[] horsewin = new JLabel[10];			//말 배당을 띄울 라벨
	
	JLabel[] horseResult = new JLabel[10];		//말 등수를 띄울 라벨
	
	JLabel bettingLabel = new JLabel("====================1등 말 고르기====================");
	
	Choice selectMoney = new Choice();
	JLabel nowMoney = new JLabel("현재 금액 : " + totalmoney + "원");
	
	
	JTextField bettingField = new JTextField();		//배팅할 금액을 치는 텍스트필드
	
	public TeamProjectMain() {
		
		setTitle("경마게임");
		setBounds(30,30,1300,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for(int i = 0; i < 10; i++){	// horsewin라벨을 초기화
			horsewin[i] = new JLabel(i + "번 마 : ");
		}
		
		for(int i = 0; i < 10; i++){	// horseResult라벨을 초기화
			horseResult[i] = new JLabel(i + "번 마 : ");
		}
		
		for(int i = 0; i< 10 ; i++){	// selectHorse콤보상자에 말 추가
			selectHorse.add((i+1) + "번 마        ");
		}
		
		selectMoney.add("         5,000         ");
		selectMoney.add("         10,000         "); // selectMoney콤보상자에 말 추가
		selectMoney.add("         30,000         ");
		selectMoney.add("         50,000         ");
		selectMoney.add("         100,000         ");
		selectMoney.add("         200,000         ");
		selectMoney.add("         300,000         ");
		selectMoney.add("         500,000         ");
		
		
		
		horsePanel.setPreferredSize(new Dimension(880, 400));
		bettingPanel.setPreferredSize(new Dimension(880, 420));
		eastPanel.setPreferredSize(new Dimension(400, 700));
		
		horsePanel.setBackground(Color.GREEN);
		bettingPanel.setBackground(Color.black);
		eastPanelup.setBackground(Color.RED);
		eastPanelDown.setBackground(Color.YELLOW);
		centerPanel.add(horsePanel);
		
		
		bettingPanel.add(bettingLabel);
		bettingLabel.setHorizontalAlignment(JLabel.CENTER);
		bettingLabel.setFont(new Font("궁서체", Font.BOLD, 20));
		bettingLabel.setForeground(Color.WHITE);
		
		resetBettingRate();
		
		resetHorseResult();
		
		
		
		buttonPanel.add(startButton);
		startButton.addActionListener(this);
		buttonPanel.add(resetButton);
		resetButton.addActionListener(this);
		buttonPanel.add(selectHorse);
		bettingField.setText("배팅할 금액을 입력해주세요");
		bettingField.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(bettingField.getText().equals("배팅할 금액을 입력해주세요")){
					bettingField.setText("");
				}
			}
		});
		buttonPanel.add(bettingField);
		buttonPanel.add(bettingButton);
		bettingButton.addActionListener(this);
		add(buttonPanel, BorderLayout.SOUTH);
		centerPanel.add(bettingPanel);
		add(centerPanel);
		
		
		
		eastPanelDown.add(selectMoney);
		eastPanelDown.add(chargeButton);
		chargeButton.addActionListener(this);
		
		eastPanel.add(eastPanelup);
		eastPanelDown.add(nowMoney);
		eastPanel.add(eastPanelDown);
		add(eastPanel, BorderLayout.EAST);
		
		
		
		
		
		setVisible(true);
		
		
		
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		
		TeamProjectMain frame = new TeamProjectMain();
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "경기 시작"){
			JFrame frame = new JFrame("sam");
			runningHorse runningHorse = new runningHorse(this);
			frame.setBounds(400, 200, 1000, 700);
			frame.add(runningHorse);
			Thread thread = new Thread(runningHorse);
			thread.start();
			frame.setVisible(true);
			
		}else if(e.getActionCommand() == "초기화"){
			
		}else if(e.getActionCommand() == "배팅"){
			if(totalmoney >= Integer.parseInt(bettingField.getText().trim())){
				totalmoney -= Integer.parseInt(bettingField.getText().trim());
				String str = "현재금액 : " + totalmoney + "원";
				nowMoney.setText(str);
			}else {
				JOptionPane.showMessageDialog(null, "배팅할 금액이 현재금액을 초과합니다.");
			}
			
			
		}else if(e.getActionCommand() == "충전"){
			
			switch(selectMoney.getSelectedIndex()){
			case 0:
				chargemoney = 5000;
				break;
			case 1:
				chargemoney = 10000;
				break;
			case 2:
				chargemoney = 30000;
				break;
			case 3:
				chargemoney = 50000;
				break;
			case 4:
				chargemoney = 100000;
				break;
			case 5:
				chargemoney = 200000;
				break;
			case 6:
				chargemoney = 300000;
				break;
			default:
				chargemoney = 500000;
			}
			
			totalmoney += chargemoney;
			String str = "현재금액 : " + totalmoney + "원";
			nowMoney.setText(str);
			chargemoney = 0;
			
		}
	}
	public void resetBettingRate(){
		for(int i=0 ; i<10 ; i++) {
			int a = new Random().nextInt(4)+1;
			int b = new Random().nextInt(9);
			int c = new Random().nextInt(9);
			horsewin[i].setForeground(Color.WHITE);
			horsewin[i].setHorizontalAlignment(JLabel.CENTER);
			horsewin[i].setFont(new Font("궁서체", Font.BOLD, 20));
			horsewin[i].setText((i+1) + "번말  " + a + "." + b + c +"배");
			bettingPanel.add(horsewin[i]);
		}
	}
	public void resetHorseResult(){
		for(int i=0 ; i<10 ; i++) {
			horseResult[i].setForeground(Color.WHITE);
			horseResult[i].setHorizontalAlignment(JLabel.CENTER);
			horseResult[i].setFont(new Font("궁서체", Font.BOLD, 20));
			horseResult[i].setText((i+1) + "등 말   :" + (runningHorse.rank[i]+1) + "번  마!!!!");
			horsePanel.add(horseResult[i]);
		}
	}
	
}

class runningHorse extends JPanel implements Runnable{
	
	TeamProjectMain frame;
	Image[] image = new Image[10];			//말 이미지가 들어가는 배열
	static int [] pos_x = new int[10];		//말이 들어가는 배열
	static int [] rank = new int[10];		//등수가 매겨지는 배열
	int w = 100, h = 70, count = 0, xpos = 50;
	static int ranking = 1;
	int a = 0;
	int b = 0;
	int c = 0;
	
	
	public static final int START_POINT = 0; 
	public static final int FINISH_POINT = 800;
	
	public runningHorse(TeamProjectMain frame)  {
		this.frame = frame;
		String filename = "";
		for(int i=0 ; i<image.length ; i++){
			filename = String.format("./src/horse/%02d.png", i);
			
			image[i] = Toolkit.getDefaultToolkit().getImage(filename);
		}
		for(int i=0 ; i<10 ; i++){
			pos_x[i] = START_POINT;
		}
	}
	
	public static void main(String[] args) {
		
		Frame frame = new Frame("AnimationTest02");
		frame.setBounds(400, 200, 1000, 700);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);				
			}
		});
		
		MovingHorse animation = new MovingHorse();
		frame.add(animation);
		Thread thread = new Thread(animation);
		thread.start();
		
		frame.setVisible(true);
		
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(0, 0, 1000, 700);
		g.setColor(Color.black);
		for(int i = 1 ; i<=9; i++){
			g.drawImage(image[count], pos_x[i-1], 50*i+50, w+pos_x[i-1], h+50*i+30, 0, 0, w, h, this);
			
			g.drawLine(0, 50*i+52, 1000, 50*i+52);
		}
		
		g.drawLine(FINISH_POINT, 0, FINISH_POINT, 700);
		
	}
	
	
	
	@Override
	public void run() {
		for(int i = 0; i < 10; i++){
			rank[i] = i;
		}
		while(true){
			
			count = ++count%9;
			xpos++;
			
			for(int i = 0; i < image.length ; i++){
				if(pos_x[i] < FINISH_POINT){
					pos_x[i] += (new Random().nextInt(10)+1);
				} else {
					pos_x[i] = FINISH_POINT;
				}
				
			}
			
			
			
//			경주마의 순위를 실시간으로 매긴다.
			for(int i = 0; i < pos_x.length-1; i++){
				if(isFinished(pos_x[rank[i]])){			//	이미 결승을 통과한 경주마의 순위는 바뀌지 않는다.
					continue;
				}
				for(int j=i+1; j < pos_x.length; j++){
					if(pos_x[rank[i]] < pos_x[rank[j]]){
						int temp = rank[i];						
						rank[i] = rank[j];
						rank[j] = temp;
					}
				}
			}
			
			if(pos_x[0] == FINISH_POINT && pos_x[1] == FINISH_POINT && pos_x[2] == FINISH_POINT && pos_x[3] == FINISH_POINT && 
					pos_x[4] == FINISH_POINT && pos_x[5] == FINISH_POINT && pos_x[6] == FINISH_POINT && pos_x[7] == FINISH_POINT && 
					pos_x[8] == FINISH_POINT && pos_x[9] == FINISH_POINT){
				frame.resetBettingRate();
				frame.resetHorseResult();
				break;
			}
			
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			repaint();
		}
		
	}
	
	public boolean isFinished(int horse){
		if(horse >= FINISH_POINT)
			return true;
		else
			return false;
		
	}
	
	
	
}
