package kr.koreait.moving;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Horse extends JPanel implements Runnable,ActionListener{


	Image img ,img2; 			//말사진을 넣을 이미지 변수
	
	int w= 133, h= 135;		//말 1마리의 폭과 높이
	int count = 0;		//이미지의 출력 순서 변경에 사용할 변수
	int xpos =50;		// 처음 시작위치
	int w1=400,h1=248;
	
	static JPanel panel = new JPanel();
	
	JButton button = new JButton("Start");
	JButton button2 = new JButton("Reset");
	
	
	
	
	public Horse() {
		String filename = "C:\\Users\\Administrator\\Desktop\\경마하는법/132544164CC712F10A178F.jpg";
		img = Toolkit.getDefaultToolkit().getImage(filename);
		String filename2 = "C:\\Users\\Administrator\\Desktop\\경마하는법/말.png";
		img2 = Toolkit.getDefaultToolkit().getImage(filename2);
		
//		String[] menu1 = {"단승식(1등맞추기)","복승식(1,2등 맞추기 (순서상관X))","쌍승식(1,2등 맞추기 (순서상관O))"
//				,"연승식(1,2등중 하나 맞추는거)","복연승식(3등안에 2마리 맞추기 순서상관X)","삼복승식(1,2,3등 말맞추기 순서상관X)"};
//		String name1 = (String) JOptionPane.showInputDialog(button1, "뭘로 걸래?", "#", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("")
//				, menu1,"단승식");
//		
//		String[] menu = {"1번마", "2번마","3번마","4번마","5번마","6번마"};
//		String name = (String) JOptionPane.showInputDialog(button1, "누구 걸래?", "#", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("")
//				, menu,"1번마");
//		
//		String menu2[]={"100원","1000원","10000원","10만원"};
//		String name2 = (String) JOptionPane.showInputDialog(button1, "얼마 걸래?", "#", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("")
//				, menu2,"10000원");
			
			
			
	}
	
	public static void main(String[] args) {
		
		Horse horse = new Horse();
		
		Frame frame = new Frame("말달리자");
		frame.setBounds(0,0,1200,800);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
			
		});
		
		frame.add(panel);
		frame.add(horse);
		
		frame.setVisible(true);
	}


	@Override
	public void paint(Graphics g) {
		
		g.setColor(Color.white);
//		g.drawImage(img, 10, 20, this);
		
		g.fillRect(0, 0, 800, 800);
		g.drawImage(img, xpos, h*1-50, w*2-50+xpos, h*2-50 ,  w*(count%12), 0, w*(count%12+1), h,this);
		g.drawImage(img2, xpos+100, h1*1-50+200, w1*2-50+xpos, h1*2-50 ,  w1*(count%12), 0, w1*(count%12+1), h1,this);
		
	}

	@Override
	public void run() {
//		말을 달리게 하는 스레드
		while(true){
//			for(int i = 0; i<4;i++){
//				count ++;
//			}
			try { Thread.sleep(50); } catch (InterruptedException e) {	e.printStackTrace(); }
			count++;
			if(count==4){
				w+=w;
			}

			xpos++;
			if(xpos>=720){
				xpos=-w;
				
			}
			
			repaint();
			
						}
		
		
	}
		
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Start")){
			Thread thread = new Thread();
			thread.start();
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
}
