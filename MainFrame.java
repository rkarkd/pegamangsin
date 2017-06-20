package kr.koreait.moving;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BtnClass extends JFrame implements ItemListener{

	JCheckBox one,two,three,four,five,six;
	JPanel controlPanel = new JPanel();
	JLabel Label = new JLabel("이곳에 선택한 목록이 나옵니다 ");
	JButton button1 = new JButton("시작");
	
	public BtnClass () {
		
		String[] menu1 = {"단승식(1등맞추기)","복승식(1,2등 맞추기 (순서상관X))","쌍승식(1,2등 맞추기 (순서상관O))"
				,"연승식(1,2등중 하나 맞추는거)","복연승식(3등안에 2마리 맞추기 순서상관X)","삼복승식(1,2,3등 말맞추기 순서상관X)"};
		String name1 = (String) JOptionPane.showInputDialog(button1, "뭘로 걸래?", "#", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("")
				, menu1,"단승식");
		
		
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
		
		add(button1);
		
		Label.setHorizontalAlignment(JLabel.CENTER);
		add(Label,BorderLayout.SOUTH);
		
		one.addItemListener(this);
		two.addItemListener(this);
		three.addItemListener(this);
		four.addItemListener(this);
		five.addItemListener(this);
		six.addItemListener(this);
		
		setVisible(true);
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
	
}
