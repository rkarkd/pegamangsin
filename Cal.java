package kr.koreait.moving;

public class Cal {

//		돈을 계산하는 메소드를 만들어야겠다
	
	String acno ;		//계좌 번호
	String name ; 		// 참가자 이름
	
	int fmoney = 50000;		//초기 자본금
	int lostmoney;			// 잃은 돈 (총 배팅 금액)
	int getmoney;			// 얻은 돈 (1판)
	int batting; 			// 배팅금액 = 잃은돈 (1판)
	int nowmoney;  			// 현재 금액
	int totalget; 			// 총 얻은돈
	
	
	public Cal (String acno, String name, int fmoney) {
		
		this.acno = acno;
		this.name = name;
		this.fmoney = fmoney;
		BtnClass btnClass = new BtnClass();
		
		
	}
	
	
	public int Lost () {		
		lostmoney +=batting;
		
		return lostmoney;
		
	}

	public int Now () {
		nowmoney = fmoney-lostmoney+getmoney;
		return nowmoney;
		
	}
	
	public int Get () {
		totalget += getmoney;
		return totalget;
	}
	
	
	
	
	
	
}
