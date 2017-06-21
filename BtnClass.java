package kr.koreait.moving;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BtnClass extends JFrame implements ItemListener,ActionListener{

	JCheckBox one,two,three,four,five,six;						//말 마리 
	JPanel controlPanel = new JPanel();
	JLabel Label = new JLabel("이곳에 선택한 목록이 나옵니다 "); 		//몇번말을 골랐나 표시해주는 거
	JButton button1 = new JButton("시작");					//말을 달리게하는 버튼
	JButton button2 = new JButton("역사를 본다."); 			//나의 돈의 역사
	JLabel Label2 = new JLabel("현재금액 : 50000"); 		//예시
	JPanel centerpanel = new JPanel(new GridLayout(1, 2));	//버튼을 넣을 패널
	
	public BtnClass () {
		
		String[] menu1 = {"단승식(1등맞추기)","복승식(1,2등 맞추기 (순서상관X))","쌍승식(1,2등 맞추기 (순서상관O))"
				,"연승식(1,2등중 하나 맞추는거)","복연승식(3등안에 2마리 맞추기 순서상관X)","삼복승식(1,2,3등 말맞추기 순서상관X)"};
		String name1 = (String) JOptionPane.showInputDialog(button1, "뭘로 걸래?", "#", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("")
				, menu1,"단승식");
		String menu2[]={"100원","1천원","5천원","1만원","3만원","5만원","10만원"};
		String name2 = (String) JOptionPane.showInputDialog(button1, "얼마 걸래?", "#", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("")
				, menu2,"10000원");
		
		
		
		setTitle("item");
		setBounds(200,200,500,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		one = new JCheckBox("1번마",false);
		two = new JCheckBox("2번마",false);
		three = new JCheckBox("3번마",false);
		four = new JCheckBox("4번마",false);
		five = new JCheckBox("5번마",false);
		six = new JCheckBox("6번마",false);
		controlPanel.add(one);
		controlPanel.add(two);
		controlPanel.add(three);
		controlPanel.add(four);
		controlPanel.add(five);
		controlPanel.add(six);
		
		add(controlPanel,BorderLayout.NORTH);
		add(Label2,BorderLayout.WEST);
		
		add(centerpanel,BorderLayout.CENTER);
		centerpanel.add(button1);
		centerpanel.add(button2);
		
		Label.setHorizontalAlignment(JLabel.CENTER);
		add(Label,BorderLayout.SOUTH);
		
		one.addItemListener(this);
		two.addItemListener(this);
		three.addItemListener(this);
		four.addItemListener(this);
		five.addItemListener(this);
		six.addItemListener(this);
		
		button1.addActionListener(this);
		button2.addActionListener(this);
		
		setVisible(true);
		
//		String acno = "111";
//		String name = "아무개";
//		int fmoney = 500000;
//		Cal cal  = new Cal(acno, name, fmoney);
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		
		BtnClass frame = new BtnClass();
		
		
	}



	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		
//		System.out.println(((JCheckBox)obj).getText());
		JCheckBox item= ((JCheckBox)obj);
//		isSelected() : 체크상자가 선택된 상태면 true 해제된 상태면 false를 리턴한다. 
		Label.setText(item.getText()+(item.isSelected() ? "선택" : "해제"));
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("시작")){
			System.out.println("눌럿다.");
			Frame frame = new Frame();
			frame.setBounds(0, 0, 1700, 1200);
			
			Project2 project2 = new Project2();
			frame.add(project2);
			
			Thread thread = new Thread(project2);
			thread.start();
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					frame.dispose();
				}
				
			});
			frame.setVisible(true);
			
		}
		
		if(e.getActionCommand().equals("역사를 본다.")){
			
			
		}
		
	}
	
}
