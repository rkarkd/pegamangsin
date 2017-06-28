package racingHorse;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.ImageIcon;

public class Horse implements Cloneable{
	private int trackNumber;
	private String name = "";
	private int position_X;
	private int position_Y;
	private HorseImageInfo imageInfo;
	private RunningPattern runningPattern;
	private double bettingRate;
	
	private static HorseImageInfo[] imageSet = new HorseImageInfo[7];		//	TODO ���Ŀ� ���� �̹����� ������� ���� �迭
	private static String[] imageNameMapping = new String[7];
	private static String[] horseNameSet = {"���ڵ���", "����", "��ǳ", "���ȯ", "���߸�", "�Ҳɸ�", "���������������", "�÷��ͻ���",
											"������", "����Ϳ�", "����Ʈ�ϴ�", "�׷������", "�׷���Ʈ�긮��", "�ý���", "������ǽ��",
											"��Ƽ��", "���̾����", "��⿡��", "�ҽ�����", "���ͷ�", "����Ǻ�", "������",
											"Ŀ�ú�", "�������־�", "����ŰƼ", "��Ƽ���˷翡Ʈ", "��������", "����ĥ��ĥ", "������",
											"�������������", "�Ƽ���", "���ϳ�", "���κ���뽬", "����ũ������Ű", "���ӽ�P������",
											"����Ʈ�׸���", "���ø���Ų", "�±ǿհ���ǳ", "Ź���ձ�����", "�����α۷θ�", "��",
											"�̽���ũ�ο�", "���̿��̺�", "���̽�Ÿ", "�̸���", "������Ÿ��", "����ؿ俬�����߰�",
											"�����", "����", "�ϴ���", "���о�ٿ�", "���ڰ���", "�������", "������", "���ڶ��",
											"������ǳ", "�����Ŵ�", "�ٺ���", "����̴�", "�ٶ�´��", "�ѵ�", "¥��", "������",
											"���ο", "�Ҹ���", "���ޱ���", "�̰͵������غ�����", "1��������ϴ����Դϴ�", "�Դ��ĸ�����"};
	private static int population = 0;
	private static int population2 = 0;

	public Horse(Horse horse){
		this(horse.trackNumber, horse.name, horse.position_X, horse.position_Y);
	}
	public Horse(int trackNumber, String name, int position_X, int position_Y) {
		this.trackNumber = trackNumber;
//		TODO
//		3. �ߺ��̸� �����ϱ�
//		����� �Ʒ��� �ڵ�� �迭�� ������ ���� �׳� �ҷ����� ������ ����� ������ ���� �̸��� ���� �����.
//		�̸� �ߺ��� ���� ���� HashMap�� �̿��Ѵ�.
//		�ڼ��� ������ 5�� 26���� ���������� �����Ѵ�.
		
		
//		this.name = (name.equals("") ? horseNameSet[new Random().nextInt(horseNameSet.length)] : name);
		this.name = (name.equals("") ? horseNameSet[population++ % horseNameSet.length] : name);	//����
		this.position_X = position_X;
		this.position_Y = position_Y;
		
//		TODO
//		2. ���� �߰�
//		������ ������ �ٸ� ������ ������ �����Ѵ�.
		
		
//		TODO
//		1. �̹��� �߰�
//		�̹����� �Ʒ��� ���� �߰��ϸ� �˴ϴ�.
		
//		imageInfo = imageSet[new Random().nextInt(2)];		//	���� ������ Ȯ�� ���� 1/2
		if(new Random().nextInt(10) < 1){
			int randomImage = population2 % imageSet.length;
			imageInfo = imageSet[randomImage];
			this.name = imageNameMapping[randomImage];
			population2++;
		}else{
			imageInfo = new HorseImageInfo("./src/basicHorse.png", HorseImageInfo.SPRITE_TYPE, 3, 5, 1);
		}
		runningPattern = new RunningPattern(this);
		
//		���� ������� ���� 
		String rank = runningPattern.getRank();
		if(rank.equals("D") || rank.equals("F")){
			bettingRate = 10;
			
		}else if(rank.equals("C-")){
			
			bettingRate = 5.5;
			
		}else if(rank.equals("C")){
			
			bettingRate = 5.2;
		}else if(rank.equals("C+")){
			
			bettingRate = 4.7;
		}else if(rank.equals("B-")){
			
			bettingRate = 4.0;
		}else if(rank.equals("B")){
			bettingRate = 3.8;
			
		}else if(rank.equals("B+")){
			bettingRate = 3.5;
			
		}else if(rank.equals("A-")){
			bettingRate = 2.8;
			
		}else if(rank.equals("A")){
			bettingRate = 2.5;
			
		}else if(rank.equals("A+")){
			bettingRate = 2.2;
			
		}else if(rank.equals("S")){
			bettingRate = 1.9;
			
		}else if(rank.equals("S+")){
			
			bettingRate = 1.6;
		}else if(rank.equals("S++")){
			bettingRate = 1.2;
		}
	}

	public int move(){
		position_X += runningPattern.run();
		return position_X;
	}
	public int move(int pos) {
		position_X += pos;
		return position_X;
	}

	public int moveTo(int pos) {
		position_X = pos;
		return position_X;
	}

	public int getPosition_X() {
		return position_X;
	}

	public void setPosition_X(int position_X) {
		this.position_X = position_X;
	}

	public int getPosition_Y() {
		return position_Y;
	}

	public void setPosition_Y(int position_Y) {
		this.position_Y = position_Y;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public HorseImageInfo getImageInfo() {
		return imageInfo;
	}
	public int getTrackNumber() {
		return trackNumber;
	}
	public RunningPattern getRunningPattern() {
		return runningPattern;
	}
	public double getBettingRate() {
		return bettingRate;
	}
	public void setBettingRate(double bettingRate) {
		this.bettingRate = bettingRate;
	}
	
//	---------------------------------------------- ����
	public static void suffle() {
		//���̹��� �߰��س����ϴ�.
		imageSet[0] = new HorseImageInfo("./src/basicHorse.png", HorseImageInfo.SPRITE_TYPE, 3, 5, 1);
		imageSet[1] = new HorseImageInfo("./src/childRunner.png", HorseImageInfo.SPRITE_TYPE, 1, 8, 1);			
		imageSet[2] = new HorseImageInfo("./src/rock-running-3.png", HorseImageInfo.SPRITE_TYPE, 1, 3, 1);
		imageSet[3] = new HorseImageInfo("./src/ninja.png", HorseImageInfo.SPRITE_TYPE, 2, 5, 1);
		imageSet[4] = new HorseImageInfo("./src/pegasus.png", HorseImageInfo.SPRITE_TYPE, 3, 4, 1);
		imageSet[5] = new HorseImageInfo("./src/person.png", HorseImageInfo.SPRITE_TYPE, 2, 6, 1);
		imageSet[6] = new HorseImageInfo("./src/soniccd.png", HorseImageInfo.SPRITE_TYPE, 3, 4, 1);
		
		imageNameMapping[0] = "��";
		imageNameMapping[1] = "�����ʱ׸�";
		imageNameMapping[2] = "�ϸ�";
		imageNameMapping[3] = "����";
		imageNameMapping[4] = "�䰡����";
		imageNameMapping[5] = "���";
		imageNameMapping[6] = "�Ҵ�";
		
		
		Random random = new Random();
		for(int i=0; i<100000; i++){
			int r = random.nextInt(horseNameSet.length);
			int r2 = random.nextInt(imageSet.length-1) + 1;
			String temp= horseNameSet[0];
			horseNameSet[0]=horseNameSet[r];
			horseNameSet[r]=temp;
			
			HorseImageInfo temp2 = imageSet[1];
			imageSet[1] = imageSet[r2];
			imageSet[r2] = temp2;
			
			String temp3 =  imageNameMapping[1];
			imageNameMapping[1] = imageNameMapping[r2];
			imageNameMapping[r2] = temp3;
		}
	}
//	-----------------------------------------------------
	protected Horse clone() throws CloneNotSupportedException {
		Horse horse = (Horse) super.clone();
		horse.trackNumber = this.trackNumber;
		horse.name = this.name;
		horse.position_X = this.position_X;
		horse.position_Y = this.position_Y;
		horse.imageInfo = (HorseImageInfo)this.imageInfo.clone();
		horse.runningPattern = (RunningPattern)this.runningPattern.clone();
		horse.bettingRate = this.bettingRate;
		return horse;
	}

	

}

class HorseImageInfo{
//	���� �̹����� �������� ��츦 ����Ͽ� ��üȭ �Ͽ� � �̹����� Ÿ�Ը� �˸� ������ �� �ֵ��� �Ѵ�.
	public static final int MULTI_IMAGE_TYPE = 0;		//	�������� �̹����� ����Ͽ� �Ź� �ٸ� �̸��� �̹��� ������ ����� ���
	public static final int SPRITE_TYPE = 1;			//	�Ѱ��� ��������Ʈ �̹����� ����Ͽ� �׸���ǥ�� �ٲ� ���
	
	private String fileName;				//	���� ��
	private Image image;
	private int imageType;					//	MULTI_IMAGE_TYPE(0) �Ǵ� SPRITE_TYPE(1)
	private int imageNumber;				//	MULTI_IMAGE_TYPE�� ��� �̹����� ����� ���´�.
	private int originalWidth;				//	���� ������ ���� ũ��
	private int originalHeight;				//	���� ������ ���� ũ��
	private int width;						//	�߶� �ҷ��� �̹����� ���� ũ��
	private int height;						//	�߶� �ҷ��� �̹����� ���� ũ��
	private int row;						//	��������Ʈ �̹��� ���� �� ��
	private int col;						//	��������Ʈ �̹��� ���� �� ��
	private int imageCount = 0;				//	����� �̹����� �ҷ��� ������ ī��Ʈ
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	public HorseImageInfo(String fileName, int imageType, int row, int col, int imageNumber) {
		this.fileName = fileName;
		this.imageType = imageType;
		this.row = row;
		this.col = col;
		this.imageNumber = imageNumber;
		Image img = new ImageIcon(fileName).getImage();			// ���μ��� ũ�� ������ ���� ��� -> ���μ��ΰ� 0�̸� Fail
		image =  Toolkit.getDefaultToolkit().getImage(fileName);
		this.originalWidth = img.getWidth(null);
		this.originalHeight = img.getHeight(null);
		width = originalWidth / col;					//	����� �̹����� ���� ũ�� = ��ü ���� / ������ ��  
		height = originalHeight / row;					//	����� �������� ���� ũ�� = ��ü ���� / ������ ��
		resetImage();									//	�̹��� �ʱ�ȭ -> �� ���ص� ��
//		imageCount = new Random().nextInt(row * col);	//	�̹��� �������� -> �� ���ص� ��
	}
	
	public HorseImageInfo nextImage(){
		if(imageType == SPRITE_TYPE){
			imageCount = (imageCount+1) % (col * row);
//			��������Ʈ Ÿ���� ��� fileName�� ������ �ʰ� ������ǥ�� ����ǥ�� �ٲ��.
			startX = (imageCount % col) * width;
			startY = (imageCount / col) * height;
			endX = ((imageCount % col) + 1) * width;
			endY = ((imageCount / col) + 1) * height;
		}else if(imageType == MULTI_IMAGE_TYPE){
			imageCount = (imageCount+1) % imageNumber;
			Toolkit.getDefaultToolkit().getImage(String.format(fileName, imageCount));
			startX = 0;
			startY = 0;
			endX = width;
			endY = height;
		}
		return this;
	}
	
	public void resetImage(){
//		imageCount�� �ʱ�ȭ �Ͽ� ó�� �������� �ǵ�����.
//		��� �ʼ����� �۾��� �ƴϳ� �̹����� ���� ù �̹����� �ʿ��� ��츦 ����Ͽ� �Լ�����
		imageCount = 0;
		startX = 0;
		startY = 0;
		endX = width;
		endY = height;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public Image getImage() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getTotal(){
		return row * col;
	}

	@Override
	public String toString() {
		return fileName;
	}

	protected Object clone() throws CloneNotSupportedException {
		HorseImageInfo clone = new HorseImageInfo(fileName, imageType, row, col, imageNumber);
		clone.image = this.image;
		return clone;
		
	}

}

class RunningPattern{
	private String patternName = "";
	private int max = 35;
	private int min = 13;
	private int r = 22;
	private double avgSpeed;
	private Horse horse = null;
	private String rank = "D";
	private int lastSpurt = 0;
	
	public RunningPattern(Horse horse){
		this(new Random().nextInt(17) + 5);
		this.horse = horse;
	}
	public RunningPattern(int min){
		this.max = 46 + (new Random().nextInt(3)) - min;
		this.min = min;
		this.r = max - min;
		this.lastSpurt = new Random().nextInt(3);
		simulate();
		setRank();
		setName();
	}
	

	public RunningPattern(String patternName, int max, int min, int r, double avgSpeed, Horse horse, String rank,
			int lastSpurt) {
		super();
		this.patternName = patternName;
		this.max = max;
		this.min = min;
		this.r = r;
		this.avgSpeed = avgSpeed;
		this.horse = horse;
		this.rank = rank;
		this.lastSpurt = lastSpurt;
	}
	public int run(){
		double current_pos = (horse == null ? 0 : (double)horse.getPosition_X());
		int result = (new Random().nextInt(r)+min);
		if(lastSpurt > 0 && current_pos >= (double)RacingPanel.TRACK_LENGTH * 0.8){
			result += new Random().nextInt(lastSpurt);
		}
		return result;
	}
	
	public void simulate(){
		double total = 0;
		for(int i = 0; i < 1000; i++){
			total += (double)run();
		}
		total /= 1000;
		this.avgSpeed = total;
	}
	
	public void setRank(){
		if(avgSpeed > 25){
			rank = "S++";
		}else if(avgSpeed > 24.5){
			rank = "S+";
		}else if(avgSpeed > 24.2){
			rank = "S";
		}else if(avgSpeed > 24.0){
			rank = "A+";
		}else if(avgSpeed > 23.8){
			rank = "A";
		}else if(avgSpeed > 23.6){
			rank = "A-";
		}else if(avgSpeed > 23.2){
			rank = "B+";
		}else if(avgSpeed > 23.0){
			rank = "B";
		}else if(avgSpeed > 22.8){
			rank = "B-";
		}else if(avgSpeed > 22.6){
			rank = "C+";
		}else if(avgSpeed > 22.3){
			rank = "C";
		}else if(avgSpeed > 22.2){
			rank = "C-";
		}else if(avgSpeed > 24.0){
			rank = "D";
		}else{
			rank = "F";
		}
	}
	public void setName(){
		String str = "";
		if(lastSpurt > 1){
			str += "�޽� �ְ�";
		}
		if(max > 38){
			str += "�������� ";
		}else if(min > 15){
			str += "�������� ";
		}else{
			str += "���� ����";
		}
		str += "��Ÿ��";
		patternName = str;
	}
	
	public double getAvgSpeed(){
		return this.avgSpeed;
	}
	public String getPatternName() {
		return patternName;
	}
	public int getMax() {
		return max;
	}
	public int getMin() {
		return min;
	}
	public String getRank() {
		return rank;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		RunningPattern clone = new RunningPattern(patternName, max, min, r, avgSpeed, horse, rank, lastSpurt);
		return clone;
	}
	
	

}