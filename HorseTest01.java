package game2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class HorseTest01 extends JFrame implements Runnable,ItemListener {
	
	Image[] images = new Image[7];
	int w = 88, h = 146, count = 0, xpos = 600;
	
	// 변수 
	static int depositint = 50000;
	static String deposit = depositint + "";						// 자본금 변수
	static String selection;										// 몇 번 마 인지 선택
	static int battingamount;
	static int badangamount;
	int[] badang = new int[6];
	Random random = new Random();
	
	JPanel mainPanel = new JPanel();			// 애니메이션
	JPanel rightPanel = new JPanel();			// 자본금 
	JPanel leftPanel = new JPanel();			// 배당
	JPanel upPanel = new JPanel();				// 게임결과
	JPanel downPanel = new JPanel();			// 하단 배팅
	JPanel battingPanel = new JPanel();			// 하단 배팅			
	JLabel dividend0 = new JLabel("   배당            ");
	JLabel dividend1 = new JLabel();
	JLabel dividend2 = new JLabel();
	JLabel dividend3 = new JLabel();
	JLabel dividend4 = new JLabel();
	JLabel dividend5 = new JLabel();
	JLabel dividend6 = new JLabel();
	JLabel itemlabel = new JLabel(" x번마 선택");
	JPanel depositPanel = new JPanel();				// 우측 패널의 위쪽 패널
	JLabel depositlabel = new JLabel("자본금 ");		// 우측 패널의 라벨
	JLabel depositlabel2 = new JLabel(deposit +" ");	// 우측 패널의 라벨
	
	JLabel battingLabel = new JLabel("      배팅 금액");
	static JTextField battingfield = new JTextField(10);
	
	JRadioButton one, two, three, four, five, six ;
	ButtonGroup group = new ButtonGroup();
	JButton battingBtn = new JButton("배팅");
	Image background;

	JLabel resultlabel = new JLabel("     1등 :");
	

	public HorseTest01(){
		setTitle(" 경마 게임");
		setBounds(0, 0, 1500, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(upPanel,BorderLayout.NORTH);
		add(downPanel,BorderLayout.SOUTH);
		add(leftPanel,BorderLayout.WEST);
		add(rightPanel,BorderLayout.EAST);
		
//		버튼 이름
		one = new JRadioButton("1번 마");
		two = new JRadioButton("2번 마");
		three = new JRadioButton("3번 마");
		four = new JRadioButton("4번 마");
		five = new JRadioButton("5번 마");
		six = new JRadioButton("6번 마");
		
//		 버튼 추가
		group.add(one);
		group.add(two);
		group.add(three);
		group.add(four);
		group.add(five);
		group.add(six);
		
//		 하단 패널 새팅
		downPanel.setLayout(new GridLayout(2, 1));
		downPanel.add(battingPanel);
		battingPanel.add(one);
		battingPanel.add(two);
		battingPanel.add(three);
		battingPanel.add(four);
		battingPanel.add(five);
		battingPanel.add(six);
		battingPanel.add(battingLabel);
		battingPanel.add(battingfield);
		battingPanel.add(itemlabel);
		battingPanel.add(resultlabel);
		itemlabel.setHorizontalAlignment(JLabel.CENTER);
		
		
		downPanel.add(battingBtn);
		
		
//		왼쪽 패널 세팅
		leftPanel.setLayout(new GridLayout(7, 1));
		
		leftPanel.add(dividend0);
		leftPanel.add(dividend1);
		leftPanel.add(dividend2);
		leftPanel.add(dividend3);
		leftPanel.add(dividend4);
		leftPanel.add(dividend5);
		leftPanel.add(dividend6);
		dividend0.setFont(new Font("Dialog", Font.BOLD, 25));
		dividend1.setFont(new Font("Dialog", Font.BOLD, 25));
		dividend2.setFont(new Font("Dialog", Font.BOLD, 25));
		dividend3.setFont(new Font("Dialog", Font.BOLD, 25));
		dividend4.setFont(new Font("Dialog", Font.BOLD, 25));
		dividend5.setFont(new Font("Dialog", Font.BOLD, 25));
		dividend6.setFont(new Font("Dialog", Font.BOLD, 25));
		
		
		
//		 오른쪽 패널 세팅
		rightPanel.setLayout(new GridLayout(2, 1));
		rightPanel.add(depositPanel);
		depositPanel.add(depositlabel);
		rightPanel.add(depositlabel2);
		depositlabel.setPreferredSize(new Dimension(200, 500));
		depositlabel.setFont(new Font("Dialog", Font.BOLD, 40));
		depositlabel2.setFont(new Font("Dialog", Font.BOLD, 40));
		
		
		for(int i=0; i<badang.length; i++){
			badang[i] = random.nextInt(3)+2;
		}
//		dividend1.setText("   1번마 " + badang[0] + " 배");
		dividend1.setText(String.format("   1번마 %d 배", badang[0]));
		dividend2.setText("   2번마 " + badang[1] + " 배");
		dividend3.setText("   3번마 " + badang[2] + " 배");
		dividend4.setText("   4번마 " + badang[3] + " 배");
		dividend5.setText("   5번마 " + badang[4] + " 배");
		dividend6.setText("   6번마 " + badang[5] + " 배");
		
		one.addItemListener(this);
		two.addItemListener(this);
		three.addItemListener(this);
		four.addItemListener(this);
		five.addItemListener(this);
		six.addItemListener(this);
		
		
		
//		JPanel mainPanel = new JPanel(){
//			Image background = Toolkit.getDefaultToolkit().getImage(".\\src\\image\\revisedbackground.png");
//				@Override
//				protected void paintComponent(Graphics g) {
//					g.drawImage(background, 0, 0, this);
//				}
//		};
		MainPanel mainPanel = new MainPanel(this);
		
		add(mainPanel,BorderLayout.CENTER);
//		battingBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				
//			}
//			
//		});
		
		battingBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				battingamount = Integer.parseInt(battingfield.getText().trim());
				Thread thread = new Thread(mainPanel);
				thread.start();
				
			}
		});
		
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		HorseTest01 frame = new HorseTest01();
		Thread thread = new Thread(frame);
		thread.start();
		
	}
//	@Override
//	public void paint(Graphics g) {
//		g.setColor(Color.WHITE);
//		g.fillRect(0, 0, 720, 320);
//		g.drawImage(images[6 - count % 6], xpos, 50, this);
//	}

	@Override
	public void run() {
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		System.out.println(((JRadioButton)obj).getText());
		JRadioButton item = (JRadioButton)obj;
		itemlabel.setText(item.getText()+ (item.isSelected() ? " 선택" : " 선택"  ));
		
		selection = item.getText();
		
//		if(itemlabel.getText().equals())
//		if(MainPanel.first.equals(item.isSelected())){
//			deposit += badang[0]*(int)battingfield;
//			depositint = depositint + 10000;
//		}else{
//			depositint = depositint - 10000;
//		}
		
		
	}
}

class MainPanel extends JPanel implements Runnable{
	HorseTest01 frame;
	Random random = new Random();
	Image background = Toolkit.getDefaultToolkit().getImage(".\\src\\image\\revisedbackground.png");
	Image pinkhorse1 = Toolkit.getDefaultToolkit().getImage("./src/image/Horse/horse-run-05.png");
	Image bluhehorse1 = Toolkit.getDefaultToolkit().getImage("./src/image/Horse Ice/ice-horse-run-03.png");
	Image pinkhorse2 = Toolkit.getDefaultToolkit().getImage("./src/image/Horse/horse-run-01.png");
	Image bluhehorse2 = Toolkit.getDefaultToolkit().getImage("./src/image/Horse Ice/ice-horse-run-02.png");
	Image pinkhorse3 = Toolkit.getDefaultToolkit().getImage("./src/image/Horse/horse-run-01.png");
	Image bluhehorse3 = Toolkit.getDefaultToolkit().getImage("./src/image/Horse Ice/ice-horse-run-01.png");
	int w1 = 0, w2 = 0, w3 = 0, w4 = 0, w5 = 0, w6 = 0;
	int finishline = 1000, count = 0;
	int[] rank = new int[6];
	static String first;
	public MainPanel(HorseTest01 frame){
		this.frame = frame;
//		Thread thread = new Thread(this);
//		thread.start();
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, this);
		g.drawImage(pinkhorse1, w1, 0, this);
		g.drawImage(bluhehorse1, w2, 80, this);
		g.drawImage(pinkhorse2, w3, 160, this);
		g.drawImage(bluhehorse2, w4, 240, this);
		g.drawImage(pinkhorse3, w5, 320, this);
		g.drawImage(bluhehorse3, w6, 400, this);
	}
	@Override
	public void run() {
		int[] badang = new int[6];
		while(true){
			try { Thread.sleep(random.nextInt(200));} catch (InterruptedException e) { e.printStackTrace();	}

			for(int i=0; i<badang.length; i++){
				badang[i] = random.nextInt(3)+2;
			}
			
			if(w1 < finishline){
				w1 += random.nextInt(50);
				if(w1 >= finishline){
					count++;
					rank[count-1]=count;
					if(count==1){
						first = "1번 마";
					}
				}
			}
			if(w2 < finishline){
				w2 += random.nextInt(50);
				if(w2 >= finishline){
					count++;
					rank[count-1]=count;
					if(count==1){
						first = "2번 마";
					}
				}
			}
			if(w3 < finishline){
				w3 += random.nextInt(50);
				if(w3 >= finishline){
					count++;
					rank[count-1]=count;
					if(count==1){
						first = "3번 마";
					}
				}
			}
			if(w4 < finishline){
				w4 += random.nextInt(50);
				if(w4 >= finishline){
					count++;
					rank[count-1]=count;
					if(count==1){
						first = "4번 마";
					}
				}
			}
			if(w5 < finishline){
				w5 += random.nextInt(50);
				if(w5 >= finishline){
					count++;
					rank[count-1]=count;
					if(count==1){
						first = "5번 마";
					}
				}
			}
			if(w6 < finishline){
				w6 += random.nextInt(50);
				if(w6 >= finishline){
					count++;
					rank[count-1]=count;
					if(count==1){
						first = "6번 마";
					}
				}
			}
			
			
			
			if(w1>=finishline && w2>=finishline && w3>=finishline && w4>=finishline && w5>=finishline && w6>=finishline ){
				w1 = 0; w2 = 0; w3 = 0; w4 = 0; w5 = 0; w6 = 0; 
				count = 0;
//				for(int i=0; i<6; i++){
//					System.out.println(rank[i]);
//				}
//				System.out.println(first + "");
				frame.resultlabel.setText("  1등 : " +first);
				frame.depositlabel2.setText("파산");
				frame.dividend1.setText("   1번마 " + badang[0] + " 배");
				frame.dividend2.setText("   2번마 " + badang[1] + " 배");
				frame.dividend3.setText("   3번마 " + badang[2] + " 배");
				frame.dividend4.setText("   4번마 " + badang[3] + " 배");
				frame.dividend5.setText("   5번마 " + badang[4] + " 배");
				frame.dividend6.setText("   6번마 " + badang[5] + " 배");
				System.out.println(HorseTest01.depositint);
				
				// 자본금 계산
				switch(HorseTest01.selection){
				case "1번 마" : HorseTest01.badangamount = badang[0]; break;
				case "2번 마" : HorseTest01.badangamount = badang[1]; break;
				case "3번 마" : HorseTest01.badangamount = badang[2]; break;
				case "4번 마" : HorseTest01.badangamount = badang[3]; break;
				case "5번 마" : HorseTest01.badangamount = badang[4]; break;
				case "6번 마" : HorseTest01.badangamount = badang[5]; break;
				}
				int k = HorseTest01.badangamount;
				if(HorseTest01.selection.equals(first)){
					int i = HorseTest01.depositint;
					int j = HorseTest01.battingamount;
					HorseTest01.depositint = i - j + (k*j);
//					System.out.println("맞췄다.");
				} else {
					int i = HorseTest01.depositint;
					int j = HorseTest01.battingamount;
					HorseTest01.depositint = i - j;
//					System.out.println("틀렸네 한강 각");
				}

				// 자본금 초기화
				frame.depositlabel2.setText(HorseTest01.depositint+"");
				
				if(HorseTest01.depositint<=0){
					JOptionPane.showMessageDialog(frame, "한강으로 가세요^^");
					System.exit(0);
				}
				
				
				break;
			}
			
		
			
			repaint();
			
		}
		
	}


	
	// 피니쉬라인을 다 넘으면 종료라는 시점
	
	
}




// 	값을 넘겨준다 	1. 스태틱 2. 오브젝트 값을 생성자에 실어서 내보낸다








































