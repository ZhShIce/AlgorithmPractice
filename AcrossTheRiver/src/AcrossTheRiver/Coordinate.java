package AcrossTheRiver;

public class Coordinate {//(row,column,matrixPath[row][column]);
	private int row;//�����е������꣨״̬��,�����أ�������
	private int column;//�����е������꣨״̬����Ŀ�ĵأ��԰���
	private int boatStatus;//���ϵ�״̬����˭�ڴ��ϣ�
	
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
