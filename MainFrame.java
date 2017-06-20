package racingHorse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	public static final int WIDTH = 1200;
	public static final int LANE_SIZE = 60;
	
	private int challengerNumber = 6; // ������ ��
	private RacingPanel racingPanel = new RacingPanel(challengerNumber);
	private JPanel upPanel = new JPanel();
	private JPanel rankingPanel = new JPanel();
	private MenuPanel menuPanel = new MenuPanel(racingPanel);
	private JPanel broadcastingPanel = new JPanel();
	private JLabel rankingLabel = new JLabel("����");
	
	public MainFrame() {
		setTitle("�渶 ����");
		setBounds(600, 100, WIDTH, 500 + LANE_SIZE*challengerNumber);
		
		setLayout(new BorderLayout());
		
		upPanel.setLayout(new BorderLayout());
		upPanel.add(rankingPanel, BorderLayout.WEST);
		upPanel.add(menuPanel, BorderLayout.CENTER);
		
		rankingPanel.setPreferredSize(new Dimension(600, 300));
		rankingPanel.add(rankingLabel);
		rankingLabel.setText("����");
		
		add(upPanel, BorderLayout.NORTH);
		add(racingPanel, BorderLayout.CENTER);
		add(broadcastingPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		JFrame frame_2 = new JFrame("title");
		frame_2.setVisible(true);
		frame_2.setBounds(200, 500, 400, 500);
		
	}

	public void printStatus(){
		
	}
}

class RacingPanel extends JPanel implements Runnable {

	public static final int START_POINT = 0; // �������
	public static final int FINISH_POINT = 850; // ��������
	
	private Horse[] horses;

	public RacingPanel(int challengerNumber) {
		horses = new Horse[challengerNumber];
		for(int i = 0; i < challengerNumber; i++){
			horses[i] = new Horse((i+1)+"����", START_POINT, i *MainFrame.LANE_SIZE);
		}
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.LANE_SIZE* challengerNumber));
		

		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
//		��� �׸���
		for(int i = 0; i < horses.length; i++){
			g.setColor(new Color((i % 2 == 0 ? 183 : 0), (i % 2 == 0 ? 255 : 204), (i % 2 == 0 ? 190 : 19)));
			g.fillRect(0, i *MainFrame.LANE_SIZE , MainFrame.WIDTH, (i+1)*MainFrame.LANE_SIZE);
		}
		g.setColor(new Color(255,255,255));
		g.fillRect(0,  (horses.length) *MainFrame.LANE_SIZE , MainFrame.WIDTH, (horses.length)*MainFrame.LANE_SIZE);
		
		
//		�� �׸���
		int horseSize= 60;
		g.setColor(Color.PINK);
		for(int i = 0; i < horses.length; i++){
//			�� ������ ��ġ�� �޾ƿͼ� �׸���.
			try{
				g.fillOval(horses[i].getPosition_X(), i *MainFrame.LANE_SIZE, 60, MainFrame.LANE_SIZE);
			}catch(NullPointerException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			if (isAllHorseFinished()) {
				break;
			}
			for(int i = 0; i < horses.length ; i++){
				if(horses[i].getPosition_X() <= FINISH_POINT){
					horses[i].move(new Random().nextInt(6)+1);
				}else{
					
				}
			}
			repaint();
			try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace();}
		}
	}

	private boolean isAllHorseFinished() {
		for(int i = 0; i < horses.length ; i++){
			if(horses[i].getPosition_X() < FINISH_POINT){
				return false;
			}
		}
		for(int i = 0; i < horses.length ; i++){
//			��ġ �ʱ�ȭ
			horses[i].setPosition_X(START_POINT);
		}
		return true;
	}

	public Horse[] getHorses() {
		return horses;
	}

}

class MenuPanel extends JPanel implements ActionListener{
//	private String[] horseNames = {"1����", "2����", "3����", "4����", "5����", "6����"};
//	private JComboBox horseList1 = new JComboBox(horseNames);
//	private JComboBox horseList2 = new JComboBox(horseNames);
	private RacingPanel racingPanel;
	private String[] horseNames;
	private JComboBox horseList1;
	private JComboBox horseList2;
	private JButton selectButton = new JButton("���� �Ϸ�");
	
	public MenuPanel(RacingPanel racingPanel){
		this.racingPanel = racingPanel;
		horseNames = new String[racingPanel.getHorses().length + 1];
		horseNames[0] = "���þ���";
		for(int i = 0; i < racingPanel.getHorses().length; i++){
			horseNames[i+1] = racingPanel.getHorses()[i].getName();
		}
		
		horseList1 = new JComboBox(horseNames);
		horseList2 = new JComboBox(horseNames);
		
		setPreferredSize(new Dimension(600,  300));
		setLayout(new GridLayout(2,3));
		
		add(horseList1);
		add(horseList2);
		add(new JLabel("���� �ݾ� : 1000��"));
		add(new JLabel("���þ� : 100��"));
		add(new JLabel(" "));
		add(selectButton);
		
		selectButton.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		Thread thread = new Thread(racingPanel);
		thread.start();
//			selectButton.setEnabled(false);
//			JOptionPane.showMessageDialog(null, horseList1.getSelectedItem() + "�� �����ϼ̽��ϴ�.");
			
	}
}
