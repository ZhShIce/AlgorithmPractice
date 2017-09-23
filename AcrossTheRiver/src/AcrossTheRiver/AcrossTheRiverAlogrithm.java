package AcrossTheRiver;

import java.util.LinkedList;

/**
 * ���ߣ�ZSH�����ڣ�2017-09-23
 * 
 * ������ʵ���˰��˹�������
 * һ�����ڣ�һ���ְ֣�һ�����裬�����ӣ���Ů��������һ�����죬
 * һ����������һ���ӣ��ְֲ��������˺����ӣ����費�ڰְ��˺�Ů����
 * ���첻�ڻ����˺�һ�����ڣ�ֻ������ְ־���Ὺ����һ��ֻ�ܹ������ˣ�ֻ��һ�Ҵ���
 * 
 * ��һ��״̬Ǩ�Ƶ�����һ��״̬ʹ�þ�����
 * �涨��ʹ�þ�λ����ʾÿ�������״̬ 
 *  ��/�Ұ�---����---�ﷸ---����---ĸ��---����1---����2---Ů��1---Ů��2
 *  �󰶣�0
 *  �Ұ���1
 *  ��ʼ״̬ȫ�����󰶣�0 1111 1111
 *  ����״̬�仯����������״̬
 *  ����״̬ȫ�����Ұ���1 1111 1111
 * */


public class AcrossTheRiverAlogrithm {

	private static int leftBank=1<<8;//�ھ�λΪ����״̬��1��ʾ��
	private static int cap=1<<7;//��8λΪ����
	private static int criminal=1<<6;//��ʾ�ﷸ
	private static int father=1<<5;//��ʾ����
	private static int mother=1<<4;//��ʾĸ��
	private static int son1=1<<3;//��ʾ����1
	private static int son2=1<<2;//����2
	private static int daughter1=1<<1;//Ů��1
	private static int daughter2=1;//Ů��2
	private static String[] people={"û��","Ů��2","Ů��1","����2","����1","����","�ְ�","����","����"};
	private static final int MAX=-1;//���ɴ�
	private static final int LEN=512;//512���Ա�֤�к�9λ������
	private static int[][] matrixPath=new int[LEN][LEN];//·�����󣬴�Ŵ�ĳ����ĳ����״̬���Ƿ�ɴ�

	private static LinkedList<Coordinate> coordinateStatus=new LinkedList<Coordinate>();//��¼�߹������꣬��ĳ�β����е������߲�ͨʱ������ԭ
	private static LinkedList<String> storePreviousStatus=new LinkedList<String>();//����ĳ�ΰ����Ѿ����ڹ���ĳ��״̬
	
	/**
	 * �����ڶ԰����Ǳ˰��жϵ�ǰ���ϵ������Ƿ�������������Ϸ�涨�е�����
	 * */
	private static boolean judgeSatisfyCondition(int x){
	    if(((x&cap)==0)&&((x&criminal)!=0)&&((x&(father+mother+daughter1+daughter2+son1+son2))!=0)){//���첻��ʱ�ﷸ���ܺ���������һ��
	        return false;
	    }
	    if(((x&father)==0)&&((x&mother)!=0)&&((x&(son1+son2))!=0)){//���׵��Ӷ���ʱĸ�ײ��ܺͶ��ӹ�ͬ��һ��
	        return false;
	    }
	    if(((x&mother)==0)&&((x&father)!=0)&&((x&(daughter1+daughter2))!=0)){//ĸ�׵��Ӷ���ʱ���ײ��ܺ�Ů����ͬ��һ��
	            return false;
	    }
	    return true;
	}
	
	/**
	 * �жϵ�ǰ���ϵ�״̬�Ƿ���������
	 * */
	private static boolean judgeSatisfyBoatConditioin(int x){
	    int personCount=0;//�����˵�����
	    int binaryCount=x;//��¼x�ж������м�λΪ1
	    if(x==0){//û�˳˴�
	        return false;
	    }
	    while(binaryCount!=0){
	        personCount++;
	        binaryCount=binaryCount&(binaryCount-1);
	        if(personCount>2){//�����˴���2��
	            return false;
	        }
	    }
	    if(personCount==1){//һ���˳˴�ʱ�����Ƕ�ͯ����
	        if(((x&criminal)!=0)||((x&(son1+son2))!=0)||((x&(daughter1+daughter2))!=0)) {//��ֻ��һ�����ڴ���ʱ������ֻ�Ƿ��ˣ���ֻ�Ƕ��ӻ�ֻ��Ů��
	            return false;
	        }
	    }

	    if((((x&criminal)!=0)&&((x&(son1+son2))!=0))||(((x&criminal)!=0)&&((x&(daughter1+daughter2))!=0))||(((x&(son1+son2))!=0)&&((x&(daughter1+daughter2))!=0))){//�����˲���ֻ��С�������ˣ���Ϊ���ǲ��ܿ���
	              return false;
	    }
	    
	    if((((x&son1)!=0)&&((x&son2)!=0))||(((x&daughter1)!=0)&&((x&daughter2)!=0))){//Ҳ�п������������ӻ�������Ů��
	    	return false;
	    }
	    
	    return judgeSatisfyCondition(x);
	}
	
	private static void calcuateMatrixPath(){
		 for(int i=0;i<LEN;i++){//����i�ǳ������ӱ����������Ǳ���״̬
			 for(int j=0;j<LEN;j++){//����j�Ƕ԰����԰�״̬
				 if(i==j){//�Լ����Լ���״̬���ô�
					 
					 matrixPath[i][j] = 0;//�������Լ����Լ�·��Ϊ0
		            
				 }else if((((i&leftBank)^(j&leftBank))!=0) && //�жϵ�ǰi��j�Ƿ�����Եİ�
						 judgeSatisfyCondition(~j) && //��jȡ����ʾת�ƺ��󰶵�״̬
						 judgeSatisfyCondition(~i) && //��iȡ����ʾת�Ժ��Ұ���״̬
						 judgeSatisfyCondition(i) && //��ʾת��ǰ��״̬
						 judgeSatisfyCondition(j)){ //��ʾת�ƺ��Ұ�״̬
					 		
				 		int cutLow8BigOf_i=i%256;//��ȡ��8λ
	                    int cutLow8BitOf_j=j%256;
	                    //int boatStatus=((~cutLow8BigOf_i)^cutLow8BitOf_j)&255;//��¼�����ƶ����ϵ�״̬ ��Ϊ���з��������Խ�ȡ��8λ
	                    int boatStatus=(cutLow8BigOf_i&cutLow8BitOf_j)&255;//��¼�����ƶ����ϵ�״̬ ��Ϊ���з��������Խ�ȡ��8λ
	                    int boatStatusDire=(i&256)|boatStatus;//��õ�ǰ�ھ�λ״̬λ�����Ǵ��󰶵��Ұ����Ǵ��Ұ����󰶣�0��ʾ���󰶵��Ұ���1�෴
	                    //��ʱ���������ݴ�i��j����������
	                    int tempI=i;
	                    int tempJ=j;
	                    
	                    if(judgeSatisfyBoatConditioin(boatStatus) &&//�жϴ��ϵ�״̬�Ƿ���������
	                    		(((((~tempI)&511)|boatStatus)&511)==(tempJ&511))){//�����tempI��tempJ�����򱾰�״̬��tempIȡ����ʹ���״̬�������ͻ�õ��԰���tempJ��״̬
	                    	
	                    	matrixPath[i][j]=boatStatusDire;//��״̬iͨ�����ϵ�״̬boatStatus�õ�j״̬������״̬�����˵ھ�λ����λ�����������Ǵӱ������԰����Ƿ�������
	                    
	                    }else{//�������򲻿ɴ�
	                            matrixPath[i][j]=MAX;   
	                    }    
				 }else{
		                matrixPath[i][j]=MAX;
		            }
			 }
		 } 
	}
	
	/**
	 * �ڽ����õľ�����������·��
	 * @param matrixPath ֮ǰ�����õľ���
	 * @param fromLeftBank ���ӱ���ʲô״̬���ʼ��˭Ҫ���԰���
	 * @param toRightBank ���ն԰�����˭
	 * */
	private static void searchThePath(int[][] matrixPath,int fromLeftBank,int toRightBank){//���ѵ�Ŀ�����󰶵��Ұ���״̬��1111 1111 ------>0000 0000
		int row=fromLeftBank;
		int destination=toRightBank;
	
		if((matrixPath==null)||(judgeSatisfyCondition(fromLeftBank)==false)||(judgeSatisfyCondition(toRightBank)==false)){//���ڲ�����������ֱ�ӷ���
		    return;
		}
		
		boolean flag=false;//Ϊtrue��ʾ�ڵ�ǰ�����У��ӱ������ҵ���һ�����ʵ������꣨Ҫ����İ�״̬����Ϊfalse��ʾû���ҵ�

		while(row!=toRightBank){//�ӱ���״̬û�е���԰�״̬��һֱ�ң��ӵ�ǰ��״̬��row�����԰�״̬��column��
		    for(int column=0;column<LEN;column++){//�ֱ��ҵ��԰���column��״̬�Ƿ�������������
		        if((matrixPath[row][column]!=-1)&&(matrixPath[row][column]!=0)){//���ҵ��˼�¼��ÿһ�γɹ��������
		        	if(!storePreviousStatus.contains(""+column)){//�������������������꣨�԰�״̬����ǰû�г��ֹ���ô��������
		            Coordinate coord=new Coordinate(row,column,matrixPath[row][column]);
		            storePreviousStatus.addLast(""+column);//��ʾ��״̬�Ѿ����ֹ�
		        	coordinateStatus.addLast(coord);//��¼�³ɹ�ͨ����״̬
		        	row=column;//Ϊ����һ������һ���е�������׼��
		        	flag=true;//��ʾ�ҵ��˺���ĵ�
		        	break;
		        }else{
		        	flag=false;//��ʾ��row��column״̬������
		        }
		     }
		   }
		    if(flag==false){//��һ���ж�û���ҵ�����·������ô�ͷ��ص�ԭ���Ľڵ�����������ڵ���·������Ϊ�����for���ҵ�����״̬����break����
		    	if((coordinateStatus.size()==0)){//��û�б����·������ô�ʹӱ���״̬fromLeftBank������
		    		System.out.println("�յ�");
		    		row=fromLeftBank;
		    		continue;
		    	}
		    	row=coordinateStatus.getLast().getRow();//��������ֵ�Ǿ��˻ص���һ�ε�״̬��������һ��״̬������
		    	coordinateStatus.removeLast();
		    }
		}	
	}
	
	/**
	 * ��ӡ·��
	 * */
	private static void printThePath(){
		while(coordinateStatus.size()>0){
			Coordinate coord=coordinateStatus.getFirst();//�ӵ�һ��״̬��ʼ��ӡ
			coordinateStatus.removeFirst();
			int from = coord.getRow();
			int destinate = coord.getColumn();
			int boatStatus = coord.getBoatStatus();
			int fromTo=(boatStatus&256)==0?0:1;//��õ�9λ״ֵ̬��1��ʾ���Ұ�����
			String fromNumToPeople=NumToPeople(from^boatStatus);//������Ϊ����ʾʱ�ܰ��Ѿ����԰��Ļ����뿪����������ʾ����ʾ����
			String destinateNumToPeople=NumToPeople(destinate);
			String onBoatPeople=NumToPeople(boatStatus)+"���Ŵ����԰�";
			if(fromTo==0){//��ʾ�����󰶣���δ������Ұ���
				System.out.println( fromNumToPeople+" ------> "+onBoatPeople+"------> "+destinateNumToPeople);
				System.out.println();
			}else{//�������Ұ�
				System.out.println( destinateNumToPeople+"<------ "+onBoatPeople+" <------ "+fromNumToPeople);
				System.out.println();
			}
		}
	}
	
	/**
	 * @param status�ǰ����Ǵ��ϵ�״̬
	 * @return ״̬Ϊת��Ϊ������ƴ�Ӻõ��ַ���
	 * ��������״̬λת��Ϊ��Ӧ���ˣ�ƴ�ӳ��ַ���
	 * */
	public static String NumToPeople(int status){
		StringBuilder groupOfPeople=new StringBuilder();//����״̬λ1ʱ��Ӧ����
		String leftBracket="[";
		String rightBracket="]";
		int count=1;
		int fromTo=status&256;//��õ�ǰ���Ǹ���
		
		while(count<=8){
			if((status&1)!=0){
				groupOfPeople.append(people[count]+",");
			}
			status=status>>>1;
			count++;
		}

		if(groupOfPeople.length()==0){
			
			groupOfPeople.append("����");
		
		}else{
			
			groupOfPeople.deleteCharAt(groupOfPeople.length()-1);//�����һ��,����ɾ��
		
		}
		
		return leftBracket+groupOfPeople.toString()+rightBracket;
	}

	
	public static void main(String[] args){
		calcuateMatrixPath();//����·������
	    searchThePath(matrixPath,255,511);//�Ҵ���0-1111-1111 �� �Ұ�״̬��1-1111-1111��·��
	    System.out.println("--------------------------------------------��������------------------------------------");
	    System.out.println();
	    printThePath();//���ҵ���·����ӡ����
	    System.out.println("-------------------------------------------ȫ������--------------------------------------");
	}

}
