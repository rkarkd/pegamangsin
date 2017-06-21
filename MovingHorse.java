package kr.koreait.teamProject;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;

public class MovingHorse extends JPanel implements Runnable{

	Image[] image = new Image[15];
	int w = 100, h = 70, count = 0, xpos = 50;
	
	public static final int START_POINT = 0; 
	public static final int FINISH_POINT = 800;
	
	public MovingHorse() {
		String filename = "";
		for(int i=0 ; i<image.length ; i++){
		filename = String.format("./src/horse/%02d.png", i);
		
		image[i] = Toolkit.getDefaultToolkit().getImage(filename);
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
		for(int i = 1 ; i<=11; i++){
			g.drawImage(image[count], xpos, 50*i+50, w+xpos, h+50*i+30, 0, 0, w, h, this);
			
			g.drawLine(0, 50*i+52, 1000, 50*i+52);
		}
		
		g.drawLine(FINISH_POINT, 0, FINISH_POINT, 700);
		
	}
	
	
	
	@Override
	public void run() {
		while(true){
			count = ++count%15;
			xpos++;
			if(xpos == FINISH_POINT){
				break;
			}
			try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
			repaint();
		}
		
	}

}
