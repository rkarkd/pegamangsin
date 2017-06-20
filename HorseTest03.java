package game;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.temporal.JulianFields;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HorseTest03 extends JPanel implements Runnable, MouseListener {

	static JButton bettingBtn = new JButton("베팅!!"); 
	static JPanel box = new JPanel();
	static JPanel bettingPanel = new JPanel();
	static Checkbox one, two, three, four, five, six;
	static CheckboxGroup select = new CheckboxGroup();
	
	Image bg, horse;
	int deposit;
	int w = 1612/ 16, h = 96;
	int count = 0;	
	int xpos = 50;				// 
	public HorseTest03() {
		bg = Toolkit.getDefaultToolkit().getImage("./src/image/background2.png");
		horse = Toolkit.getDefaultToolkit().getImage("./src/image/ani4.png");
	}
	
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Run! Run!");
		frame.setBounds(0, 0, 1800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		HorseTest03 animation = new HorseTest03();
		frame.add(animation);
		Thread thread = new Thread(animation);
		thread.start();
		one = new Checkbox("1번 말", true, select);
		two = new Checkbox("2번 말", false, select);
		three = new Checkbox("3번 말", false, select);
		four = new Checkbox("4번 말", false, select);
		five = new Checkbox("5번 말", false, select);
		six = new Checkbox("6번 말", false, select);
		box.add(one);
		box.add(two);
		box.add(three);
		box.add(four);
		box.add(five);
		box.add(six);
//		bettingBtn.setName("베 팅 ! ! ");
		
		bettingBtn.addMouseListener(animation);
		frame.add(bettingPanel, BorderLayout.SOUTH);
		bettingPanel.setLayout(new GridLayout(2, 1));
		bettingPanel.add(box);
		bettingPanel.add(bettingBtn);
		
		frame.setVisible(true);
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(bg, 0, 0, this);
		g.drawImage(horse, xpos, 50, w+xpos, h+50, w*(count%16), 0, w*(count%16+1), h, this);
	}


	@Override
	public void run() {
		while(true) {
			
			try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
			count++;
			xpos++;
			if(xpos >= 1612) {
				xpos = -w;
			}
			repaint();
			
		}
	}



	@Override
	public void mouseClicked(MouseEvent e) {
//		Object obj = e.getSource();
//		if(((JButton)obj).getName() != null && ((JButton)obj).getName().equals("베팅!!")){
		JOptionPane.showMessageDialog(this , "잔금 : ");
			
//		}
	}


	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}



	
	
}
