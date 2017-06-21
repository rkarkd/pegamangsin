package kr.koreait.moving;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//		돈이 계산된 것을 표시하는 프레임을 만든다. 
public class Money {

	JLabel label1 = new JLabel("초기 자본 : ");
	JLabel label2 = new JLabel("현재 자본 : ");
	JLabel label3 = new JLabel("잃은 돈 : ");
	JLabel label4 = new JLabel("얻은 돈 : ");
	JPanel panel = new JPanel(new GridLayout(2, 2));
	JTextArea area = new JTextArea();
	
	public Money() {
		Frame frame = new Frame("돈의 역사");
		frame.setBounds(200, 200, 500, 500);
		

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			frame.dispose();
			}
		});
		
		frame.add(panel);
		
		panel.add(label1);
		panel.add(label2);
		panel.add(label3);
		panel.add(label4);
		
		
		frame.setVisible(true);
		

		
	}
	public static void main(String[] args) {
		Money money = new Money();
		
		
	}
	
}
