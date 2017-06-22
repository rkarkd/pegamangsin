
 package paga;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gyungma extends JFrame implements ActionListener{
 int member = 6;

 JButton bt = new JButton("베팅");              // 베팅하기 버튼
 JLabel btmy = new JLabel("베팅금액 : ");       // 베팅 금액 글씨
 TextField bt_money = new TextField(10);        // 베팅 금액
 JLabel hc = new JLabel("말 선택 : ");          // 말 선택 글씨
 Choice combo;                                 // 말 보기 상자
 JButton start = new JButton("시작");           // 시작 버튼
 ScreenPanel screenpanel = new ScreenPanel();      // 스크린
 ResultPanel resultpanel = new ResultPanel();      // 결과
 JPanel buttompanel = new JPanel();                // 베팅 버튼 & 금액 / 말 선택 버튼/말 보기 선택/ 시작 버튼
 Label money = new Label("현재 베팅 금액 : ");
 JLabel result = new JLabel("실시간 순위표");

 
 
 public Gyungma() {
  
  setTitle("경마게임");
  setBounds(0, 0, 1100, 630);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setLayout(new BorderLayout());
  
//      상단 부분
  
  screenpanel.setBackground(Color.BLACK);
  resultpanel.setBackground(Color.white);
  resultpanel.setPreferredSize(new Dimension(300, 540));
  resultpanel.add(result);
  resultpanel.add(money);
  
  
  result.setFont(new Font("Dialog", Font.BOLD, 35));
  money.setFont(new Font("Dialog", Font.BOLD, 13));
  money.setAlignment((int) BOTTOM_ALIGNMENT);
  add(screenpanel,BorderLayout.CENTER);
  add(resultpanel,BorderLayout.EAST);
  
//      하단 부분
  
  combo = new Choice();
  combo.add("선택해 주세요");
  combo.add(" 1번 마 ");
  combo.add(" 2번 마 ");
  combo.add(" 3번 마 ");
  combo.add(" 4번 마 ");
  combo.add(" 5번 마 ");
  combo.add(" 6번 마 ");
  
  buttompanel.add(btmy);
  buttompanel.add(bt_money);
  buttompanel.add(hc);
  buttompanel.add(combo);
  buttompanel.add(bt);
  buttompanel.add(start);
  buttompanel.setPreferredSize(new Dimension(1100, 50));
  buttompanel.setBackground(Color.MAGENTA);
  add(buttompanel,BorderLayout.SOUTH);
  
// 버튼의 action 값
  bt.addActionListener(this);
  start.addActionListener(this);
  
  
//   결과 값 출력
  setVisible(true);
  
 }
 
 
 
 @Override
 public void actionPerformed(ActionEvent e) {
  
  switch (e.getActionCommand()) {
  case "베팅":
   String item = bt_money.getText();
   
   if(item.trim().length() >0 && combo.getSelectedIndex() >0 ) {
    
    JOptionPane.showMessageDialog(bt, "베팅 완료.");
   }else if(item.trim().length() >0 && combo.getSelectedIndex() ==0){
    JOptionPane.showMessageDialog(combo, "말 선택을 해주세요");
   }else if(item.trim().length() == 0 && combo.getSelectedIndex() > 0){
    JOptionPane.showMessageDialog(bt_money,"베팅 금액을 입력해주세요");
   }else {
    JOptionPane.showMessageDialog(bt, "말 선택과 베팅 금액을 입력해주세요"); 
   }
   
   break;
   
  case "시작":
	  String item1 = bt_money.getText();
	  Thread thread = new Thread(screenpanel);
	  if(item1.trim().length() >0 && combo.getSelectedIndex() >0 ) {
		    
		  thread.start();
		   }else if(item1.trim().length() >0 && combo.getSelectedIndex() ==0){
		    JOptionPane.showMessageDialog(start,"베팅이 완료 되었는지 확인해주세요");
		   }else if(item1.trim().length() == 0 && combo.getSelectedIndex() > 0){
		    JOptionPane.showMessageDialog(start,"베팅이 완료 되었는지 확인해주세요");
		   }else {
		    JOptionPane.showMessageDialog(start, "베팅이 완료 되었는지 확인해주세요"); 
		   }
   break;
  }
  
  
  
  
 }
 
 
 public static void main(String[] args) {
  Gyungma frame = new Gyungma();
 }
 
 
}


class ScreenPanel extends JPanel implements Runnable{
 int xpos = 0;
 
 @Override
 public void paint(Graphics g) {
  g.fillRect(0, 0, 800, 540);
  for(int i= 0; i<6; i++ ) {   // 배경색
   g.setColor(Color.GREEN);
   if(i%2 == 0 ) {
   g.setColor(new Color(92, 163, 60));
   }
   g.fillRect(0, i*90, 800, 90*(i +1));
   }
  for(int j = 0; j < 6; j++ ) {  // 원의 색깔 
   g.setColor(Color.pink);
   g.fillOval(0 +xpos,90*j, 60, 90);
   }
  
 }
   
 @Override
 public void run() {
	 String number[] = {"1","2","3","4","5","6"};
	Random random = new Random();
  while(true) {
   if(xpos >800-100) {
    return;
   }
   for(int i = 0; i<1000; i++ ){
	   int r = random.nextInt(5);
	   String temp = number[0];
	   number[0] = number[r];
	   number[r] = temp;
   }
   xpos += r;
   try {
    Thread.sleep(10);
   } catch (InterruptedException e) {
    
    e.printStackTrace();
   }
   repaint();
   
  }
  
 }
 
 
}

class ResultPanel extends JPanel {
 
 

}

 

 

 

 

 

 

 

 
