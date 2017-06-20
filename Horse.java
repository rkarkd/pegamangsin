package racingHorse;

import java.util.ArrayList;

public class Horse {
	private String name = "";
	private int position_X;
	private int position_Y;

	public Horse(String name, int position_X, int position_Y) {
		this.name = name;
		this.position_X = position_X;
		this.position_Y = position_Y;
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

}