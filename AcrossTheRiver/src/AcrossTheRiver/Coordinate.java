package AcrossTheRiver;

public class Coordinate {//(row,column,matrixPath[row][column]);
	private int row;//矩阵中的行坐标（状态）,出发地（本岸）
	private int column;//矩阵中的列坐标（状态），目的地（对岸）
	private int boatStatus;//船上的状态（都谁在船上）
	
	public Coordinate(int row,int column,int boatStatus){
		this.row=row;
		this.column=column;
		this.boatStatus=boatStatus;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getBoatStatus() {
		return boatStatus;
	}

	public void setBoatStatus(int boatStatus) {
		this.boatStatus = boatStatus;
	}
	
}
