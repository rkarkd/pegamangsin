package game2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class HorseTest01 extends JFrame {

	JPanel mainPanel = new JPanel();			// 애니메이션
	JPanel rightPanel = new JPanel();			// 자본금 
	JPanel leftPanel = new JPanel();			// 배당
	JPanel upPanel = new JPanel();				// 게임결과
	JPanel downPanel = new JPanel();			// 배팅
	JPanel battingPanel = new JPanel();
	JRadioButton one, two, three, four, five, six ;
	ButtonGroup group = new ButtonGroup();
	JButton battingBtn = new JButton("배팅");
	Image background;

	public HorseTest01(){
		setTitle(" 경마 게임");
		setBounds(0, 0, 1500, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(upPanel,BorderLayout.NORTH);
		add(downPanel,BorderLayout.SOUTH);
		add(leftPanel,BorderLayout.WEST);
		add(rightPanel,BorderLayout.EAST);
//		add(mainPanel,BorderLayout.CENTER);
		
		
		one = new JRadioButton("1번 마");
		two = new JRadioButton("2번 마");
		three = new JRadioButton("3번 마");
		four = new JRadioButton("4번 마");
		five = new JRadioButton("5번 마");
		six = new JRadioButton("6번 마");
		
		group.add(one);
		group.add(two);
		group.add(three);
		group.add(four);
		group.add(five);
		group.add(six);
		
		
		
		downPanel.setLayout(new GridLayout(2, 1));
		downPanel.add(battingPanel);
		battingPanel.add(one);
		battingPanel.add(two);
		battingPanel.add(three);
		battingPanel.add(four);
		battingPanel.add(five);
		battingPanel.add(six);
		downPanel.add(battingBtn);
		
		JPanel mainPanel2 = new JPanel(){
			Image background = Toolkit.getDefaultToolkit().getImage(".\\src\\image\\revisedbackground.png");
				@Override
				protected void paintComponent(Graphics g) {
					g.drawImage(background, 150, 30, this);
				}
		};
		
		add(mainPanel2,BorderLayout.CENTER);
		
		
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		HorseTest01 frame = new HorseTest01();
		
	}
}
