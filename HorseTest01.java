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

	JPanel mainPanel = new JPanel();			// �ִϸ��̼�
	JPanel rightPanel = new JPanel();			// �ں��� 
	JPanel leftPanel = new JPanel();			// ���
	JPanel upPanel = new JPanel();				// ���Ӱ��
	JPanel downPanel = new JPanel();			// ����
	JPanel battingPanel = new JPanel();
	JRadioButton one, two, three, four, five, six ;
	ButtonGroup group = new ButtonGroup();
	JButton battingBtn = new JButton("����");
	Image background;

	public HorseTest01(){
		setTitle(" �渶 ����");
		setBounds(0, 0, 1500, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(upPanel,BorderLayout.NORTH);
		add(downPanel,BorderLayout.SOUTH);
		add(leftPanel,BorderLayout.WEST);
		add(rightPanel,BorderLayout.EAST);
//		add(mainPanel,BorderLayout.CENTER);
		
		
		one = new JRadioButton("1�� ��");
		two = new JRadioButton("2�� ��");
		three = new JRadioButton("3�� ��");
		four = new JRadioButton("4�� ��");
		five = new JRadioButton("5�� ��");
		six = new JRadioButton("6�� ��");
		
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
