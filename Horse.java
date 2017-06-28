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
	
	private static HorseImageInfo[] imageSet = new HorseImageInfo[7];		//	TODO 차후에 랜덤 이미지를 갖고오기 위한 배열
	private static String[] imageNameMapping = new String[7];
	private static String[] horseNameSet = {"날쌘돌이", "번개", "태풍", "김길환", "날쌩마", "불꽃마", "히어로즈오브더스톰", "플러터샤이",
											"갑돌이", "어번와우", "샤우트하니", "그랜드비전", "그레이트브리지", "시스터", "쉬즈파퓰러",
											"솔티유", "파이어러츠댄서", "장기에프", "불스아이", "닥터런", "계명의빛", "노던디바",
											"커시브", "더리얼주얼", "비잔키티", "장티유알루에트", "강공알파", "보잉칠사칠", "지빠귀",
											"프린세스버블검", "아서스", "송하나", "레인보우대쉬", "마이크와조스키", "제임스P설리반",
											"라이트닝맥퀸", "맥컬리컬킨", "태권왕강태풍", "탁구왕김제빵", "리턴인글로리", "빅스",
											"미스터크로우", "케이웨이브", "케이스타", "미르온", "강남스타일", "사랑해요연예가중계",
											"사랑이", "똘이", "하늘이", "더밀어붙여", "가자가자", "가장먼저", "간다이", "강자뜬다",
											"기절초풍", "나가신다", "다비켜", "대박이다", "바라는대로", "뿌듯", "짜릿", "빛나요",
											"새로운나", "소리쳐", "어쭈구리", "이것도너프해보시지", "1위를계승하는중입니다", "입닥쳐말포이"};
	private static int population = 0;
	private static int population2 = 0;

	public Horse(Horse horse){
		this(horse.trackNumber, horse.name, horse.position_X, horse.position_Y);
	}
	public Horse(int trackNumber, String name, int position_X, int position_Y) {
		this.trackNumber = trackNumber;
//		TODO
//		3. 중복이름 방지하기
//		현재는 아래의 코드로 배열중 랜덤한 값을 그냥 불러오기 때문에 재수가 없으면 같은 이름의 말이 생긴다.
//		이름 중복을 막기 위해 HashMap을 이용한다.
//		자세한 내용은 5월 26일자 수업내용을 참고한다.
		
		
//		this.name = (name.equals("") ? horseNameSet[new Random().nextInt(horseNameSet.length)] : name);
		this.name = (name.equals("") ? horseNameSet[population++ % horseNameSet.length] : name);	//현도
		this.position_X = position_X;
		this.position_Y = position_Y;
		
//		TODO
//		2. 배당률 추가
//		배당률은 말마다 다른 배당률을 갖도록 세팅한다.
		
		
//		TODO
//		1. 이미지 추가
//		이미지를 아래와 같이 추가하면 됩니다.
		
//		imageInfo = imageSet[new Random().nextInt(2)];		//	말이 정해질 확률 각가 1/2
		if(new Random().nextInt(10) < 1){
			int randomImage = population2 % imageSet.length;
			imageInfo = imageSet[randomImage];
			this.name = imageNameMapping[randomImage];
			population2++;
		}else{
			imageInfo = new HorseImageInfo("./src/basicHorse.png", HorseImageInfo.SPRITE_TYPE, 3, 5, 1);
		}
		runningPattern = new RunningPattern(this);
		
//		배당률 등급으로 고정 
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
	
//	---------------------------------------------- 현도
	public static void suffle() {
		//말이미지 추가해놨습니다.
		imageSet[0] = new HorseImageInfo("./src/basicHorse.png", HorseImageInfo.SPRITE_TYPE, 3, 5, 1);
		imageSet[1] = new HorseImageInfo("./src/childRunner.png", HorseImageInfo.SPRITE_TYPE, 1, 8, 1);			
		imageSet[2] = new HorseImageInfo("./src/rock-running-3.png", HorseImageInfo.SPRITE_TYPE, 1, 3, 1);
		imageSet[3] = new HorseImageInfo("./src/ninja.png", HorseImageInfo.SPRITE_TYPE, 2, 5, 1);
		imageSet[4] = new HorseImageInfo("./src/pegasus.png", HorseImageInfo.SPRITE_TYPE, 3, 4, 1);
		imageSet[5] = new HorseImageInfo("./src/person.png", HorseImageInfo.SPRITE_TYPE, 2, 6, 1);
		imageSet[6] = new HorseImageInfo("./src/soniccd.png", HorseImageInfo.SPRITE_TYPE, 3, 4, 1);
		
		imageNameMapping[0] = "말";
		imageNameMapping[1] = "스콜필그림";
		imageNameMapping[2] = "록맨";
		imageNameMapping[3] = "닌자";
		imageNameMapping[4] = "페가수스";
		imageNameMapping[5] = "사람";
		imageNameMapping[6] = "소닉";
		
		
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
//	말의 이미지가 여러개일 경우를 고려하여 객체화 하여 어떤 이미지든 타입만 알면 수정할 수 있도록 한다.
	public static final int MULTI_IMAGE_TYPE = 0;		//	여러개의 이미지를 사용하여 매번 다른 이름의 이미지 파일을 사용할 경우
	public static final int SPRITE_TYPE = 1;			//	한개의 스프라이트 이미지를 사용하여 그림좌표만 바뀔 경우
	
	private String fileName;				//	파일 명
	private Image image;
	private int imageType;					//	MULTI_IMAGE_TYPE(0) 또는 SPRITE_TYPE(1)
	private int imageNumber;				//	MULTI_IMAGE_TYPE인 경우 이미지의 장수를 적는다.
	private int originalWidth;				//	원본 파일의 가로 크기
	private int originalHeight;				//	원본 파일의 세로 크기
	private int width;						//	잘라서 불러올 이미지의 가로 크기
	private int height;						//	잘라서 불러올 이미지의 세로 크기
	private int row;						//	스프라이트 이미지 가로 줄 수
	private int col;						//	스프라이트 이미지 세로 줄 수
	private int imageCount = 0;				//	몇번쩨 이미지를 불러올 것인지 카운트
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
		Image img = new ImageIcon(fileName).getImage();			// 가로세로 크기 측정을 위해 사용 -> 가로세로가 0이면 Fail
		image =  Toolkit.getDefaultToolkit().getImage(fileName);
		this.originalWidth = img.getWidth(null);
		this.originalHeight = img.getHeight(null);
		width = originalWidth / col;					//	출력할 이미지의 가로 크기 = 전체 가로 / 세로줄 수  
		height = originalHeight / row;					//	출력할 미이지의 세로 크기 = 전체 세로 / 가로줄 수
		resetImage();									//	이미지 초기화 -> 꼭 안해도 됌
//		imageCount = new Random().nextInt(row * col);	//	이미지 랜덤선택 -> 꼭 안해도 됌
	}
	
	public HorseImageInfo nextImage(){
		if(imageType == SPRITE_TYPE){
			imageCount = (imageCount+1) % (col * row);
//			스프라이트 타입일 경우 fileName은 변하지 않고 시작좌표와 끝좌표만 바뀐다.
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
//		imageCount를 초기화 하여 처음 사진으로 되돌린다.
//		사실 필수적인 작업은 아니나 이미지에 따라서 첫 이미지가 필요한 경우를 고려하여 함수제작
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
			str += "뒷심 있고";
		}
		if(max > 38){
			str += "폭발적인 ";
		}else if(min > 15){
			str += "안정적인 ";
		}else{
			str += "균형 잡힌";
		}
		str += "스타일";
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