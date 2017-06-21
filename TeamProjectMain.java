
package kr.koreait.teamProject;
import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TeamProjectMain extends JFrame implements ActionListener{
	
	JPanel bettingPanel = new JPanel(new GridLayout(11,1));
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel eastPanel = new JPanel(new GridLayout(2, 1));
	static JPanel horsePanel = new JPanel();
	JPanel centerPanel = new JPanel();
	JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 100, 0));
	JButton startButton = new JButton("시작");
	JButton resetButton = new JButton("초기화");
	JButton bettingButton = new JButton("배팅");
	
	JButton horsewin_1 = new JButton("1번말 : 1.85배"); 
	JButton horsewin_2 = new JButton("2번말 : 2.00배"); 
	JButton horsewin_3 = new JButton("3번말 : 1.90배"); 
	JButton horsewin_4 = new JButton("4번말 : 2.50배"); 
	JButton horsewin_5 = new JButton("5번말 : 2.10배"); 
	JButton horsewin_6 = new JButton("6번말 : 1.35배"); 
	JButton horsewin_7 = new JButton("7번말 : 1.83배"); 
	JButton horsewin_8 = new JButton("8번말 : 3.90배"); 
	JButton horsewin_9 = new JButton("9번말 : 5.00배"); 
	JButton horsewin_10 = new JButton("10번말 : 2.70배"); 
	
	JLabel bettingLabel = new JLabel("1등 말 고르기");
	
	JLabel label = new JLabel("현재 금액 : 50000");
	
	JTextField bettingField = new JTextField();
	
	public TeamProjectMain() {
		
		setTitle("경마게임");
		setBounds(50,50,1300,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		horsePanel.setPreferredSize(new Dimension(850, 400));
		bettingPanel.setPreferredSize(new Dimension(850, 400));
		eastPanel.setPreferredSize(new Dimension(400, 700));
		
		horsePanel.setBackground(Color.GREEN);
		bettingPanel.setBackground(Color.black);
		panel2.setBackground(Color.RED);
		panel3.setBackground(Color.YELLOW);
		centerPanel.add(horsePanel);
		
		bettingLabel.setHorizontalAlignment(JLabel.CENTER);
		bettingLabel.setFont(new Font("궁서체", Font.BOLD, 20));
		bettingLabel.setForeground(Color.WHITE);
		
		bettingPanel.add(bettingLabel);
		
		bettingPanel.add(horsewin_1);
		bettingPanel.add(horsewin_2);
		bettingPanel.add(horsewin_3);
		bettingPanel.add(horsewin_4);
		bettingPanel.add(horsewin_5);
		bettingPanel.add(horsewin_6);
		bettingPanel.add(horsewin_7);
		bettingPanel.add(horsewin_8);
		bettingPanel.add(horsewin_9);
		bettingPanel.add(horsewin_10);
		
		
		
		buttonPanel.add(startButton);
		startButton.addActionListener(this);
		buttonPanel.add(resetButton);
		resetButton.addActionListener(this);
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
		eastPanel.add(panel2);
		panel3.add(label);
		eastPanel.add(panel3);
		add(eastPanel, BorderLayout.EAST);
		
		
		
		
		
		setVisible(true);
		
		
		
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		
		TeamProjectMain frame = new TeamProjectMain();
		
//		runningHorse runningHorse = new runningHorse();
		
//		movingHorse.setVisible(true);
		
		
		
		
//		frame.add(runningHorse);
//		horsePanel.add(runningHorse);
//		frame.setVisible(true);
//		Thread thread = new Thread(runningHorse);
//		thread.start();
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "시작"){
			JFrame frame = new JFrame("sam");
			runningHorse runningHorse = new runningHorse();
			frame.setBounds(400, 200, 1000, 700);
			frame.add(runningHorse);
			Thread thread = new Thread(runningHorse);
			thread.start();
			frame.setVisible(true);
			
			
		}else if(e.getActionCommand() == "초기화"){
			
		}else if(e.getActionCommand() == "배팅"){
			
		}
	}
	
}

class runningHorse extends JPanel implements Runnable{
	
	
	Image[] image = new Image[10];
	int [] pos_x = new int[10];
	int w = 100, h = 70, count = 0, xpos = 50;
	int rank = 1;
	
	public static final int START_POINT = 0; 
	public static final int FINISH_POINT = 800;
	
	public runningHorse() {
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
			
//			for(int i = 0; i < pos_x.length ; i++){
//				if(pos_x[i] == FINISH_POINT){
//					System.out.println(i);
//					break;
//				}
//			}
			
			
			if(pos_x[0] >= FINISH_POINT){
				System.out.println("!~!~");
			}
			
			
			if(pos_x[0] == FINISH_POINT && pos_x[1] == FINISH_POINT && pos_x[2] == FINISH_POINT && pos_x[3] == FINISH_POINT && 
					pos_x[4] == FINISH_POINT && pos_x[5] == FINISH_POINT && pos_x[6] == FINISH_POINT && pos_x[7] == FINISH_POINT && 
					pos_x[8] == FINISH_POINT && pos_x[9] == FINISH_POINT){
				break;
			}
			
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			repaint();
		}
		
	}
	
	public boolean isFinished(){
		if(horse == null){
			return false;
		}
		if(horse. >= FINISH_POINT)
			return true;
		else
			return false;
		
	}
	
	
}
