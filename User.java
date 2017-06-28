package racingHorse;

public class User {

	private String name = "¹«¸í¾¾";
	private int balance = 1000;
	
	public User(){ }
	public User(String name, int balance) {
		this.name = name;
		this.balance = balance;
	}
	
	public String getName() {
		return name;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public void addBalance(int val) {
		balance += val;
	}
	
}
