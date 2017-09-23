package AcrossTheRiver;

import java.util.LinkedList;

/**
 * 作者：ZSH，日期：2017-09-23
 * 
 * 本程序实现了八人国人问题
 * 一家六口，一个爸爸，一个妈妈，俩儿子，俩女儿，还有一个警察，
 * 一个坏蛋，过一条河，爸爸不在妈妈伤害儿子，妈妈不在爸爸伤害女儿，
 * 警察不在坏蛋伤害一家六口，只有妈妈爸爸警察会开船，一次只能过两个人，只有一艘船。
 * 
 * 将一种状态迁移到另外一种状态使用矩阵存放
 * 规定：使用九位来表示每个人物的状态 
 *  左岸/右岸---警察---罪犯---父亲---母亲---儿子1---儿子2---女儿1---女儿2
 *  左岸：0
 *  右岸：1
 *  初始状态全部在左岸：0 1111 1111
 *  经过状态变化到如下最终状态
 *  最终状态全部在右岸：1 1111 1111
 * */


public class AcrossTheRiverAlogrithm {

	private static int leftBank=1<<8;//第九位为岸的状态，1表示左岸
	private static int cap=1<<7;//第8位为警察
	private static int criminal=1<<6;//表示罪犯
	private static int father=1<<5;//表示父亲
	private static int mother=1<<4;//表示母亲
	private static int son1=1<<3;//表示儿子1
	private static int son2=1<<2;//儿子2
	private static int daughter1=1<<1;//女儿1
	private static int daughter2=1;//女儿2
	private static String[] people={"没人","女儿2","女儿1","儿子2","儿子1","妈妈","爸爸","犯人","警察"};
	private static final int MAX=-1;//不可达
	private static final int LEN=512;//512可以保证有后9位二进制
	private static int[][] matrixPath=new int[LEN][LEN];//路径矩阵，存放从某岸到某岸的状态及是否可达

	private static LinkedList<Coordinate> coordinateStatus=new LinkedList<Coordinate>();//记录走过的坐标，在某次操作中的坐标走不通时用来还原
	private static LinkedList<String> storePreviousStatus=new LinkedList<String>();//保存某次岸边已经处于过的某中状态
	
	/**
	 * 不管在对岸还是彼岸判断当前岸上的人物是否满足条件，游戏规定中的条件
	 * */
	private static boolean judgeSatisfyCondition(int x){
	    if(((x&cap)==0)&&((x&criminal)!=0)&&((x&(father+mother+daughter1+daughter2+son1+son2))!=0)){//警察不在时罪犯不能和其他人在一起
	        return false;
	    }
	    if(((x&father)==0)&&((x&mother)!=0)&&((x&(son1+son2))!=0)){//父亲到河对面时母亲不能和儿子共同在一起
	        return false;
	    }
	    if(((x&mother)==0)&&((x&father)!=0)&&((x&(daughter1+daughter2))!=0)){//母亲到河对面时父亲不能和女儿共同在一起
	            return false;
	    }
	    return true;
	}
	
	/**
	 * 判断当前船上的状态是否满足条件
	 * */
	private static boolean judgeSatisfyBoatConditioin(int x){
	    int personCount=0;//船上人的数量
	    int binaryCount=x;//记录x中二进制有几位为1
	    if(x==0){//没人乘船
	        return false;
	    }
	    while(binaryCount!=0){
	        personCount++;
	        binaryCount=binaryCount&(binaryCount-1);
	        if(personCount>2){//船上人大于2人
	            return false;
	        }
	    }
	    if(personCount==1){//一个人乘船时不能是儿童或犯人
	        if(((x&criminal)!=0)||((x&(son1+son2))!=0)||((x&(daughter1+daughter2))!=0)) {//当只有一个人在船上时，不能只是犯人，或只是儿子或只是女儿
	            return false;
	        }
	    }

	    if((((x&criminal)!=0)&&((x&(son1+son2))!=0))||(((x&criminal)!=0)&&((x&(daughter1+daughter2))!=0))||(((x&(son1+son2))!=0)&&((x&(daughter1+daughter2))!=0))){//船上人不能只有小孩儿或犯人，因为他们不能开船
	              return false;
	    }
	    
	    if((((x&son1)!=0)&&((x&son2)!=0))||(((x&daughter1)!=0)&&((x&daughter2)!=0))){//也有可能是两个儿子或是两个女儿
	    	return false;
	    }
	    
	    return judgeSatisfyCondition(x);
	}
	
	private static void calcuateMatrixPath(){
		 for(int i=0;i<LEN;i++){//坐标i是出发：从本岸出发，是本岸状态
			 for(int j=0;j<LEN;j++){//坐标j是对岸：对岸状态
				 if(i==j){//自己到自己的状态不用船
					 
					 matrixPath[i][j] = 0;//矩阵中自己到自己路径为0
		            
				 }else if((((i&leftBank)^(j&leftBank))!=0) && //判断当前i和j是否处于相对的岸
						 judgeSatisfyCondition(~j) && //对j取反表示转移后左岸的状态
						 judgeSatisfyCondition(~i) && //对i取反表示转以后右岸的状态
						 judgeSatisfyCondition(i) && //表示转移前左岸状态
						 judgeSatisfyCondition(j)){ //表示转移后右岸状态
					 		
				 		int cutLow8BigOf_i=i%256;//截取低8位
	                    int cutLow8BitOf_j=j%256;
	                    //int boatStatus=((~cutLow8BigOf_i)^cutLow8BitOf_j)&255;//记录本次移动船上的状态 因为是有符号数所以截取低8位
	                    int boatStatus=(cutLow8BigOf_i&cutLow8BitOf_j)&255;//记录本次移动船上的状态 因为是有符号数所以截取低8位
	                    int boatStatusDire=(i&256)|boatStatus;//获得当前第九位状态位，船是从左岸到右岸还是从右岸到左岸，0表示从左岸到右岸，1相反
	                    //临时定义两个暂存i和j的两个变量
	                    int tempI=i;
	                    int tempJ=j;
	                    
	                    if(judgeSatisfyBoatConditioin(boatStatus) &&//判断船上的状态是否满足条件
	                    		(((((~tempI)&511)|boatStatus)&511)==(tempJ&511))){//如果从tempI到tempJ合理，则本岸状态：tempI取反后和船上状态相或操作就会得到对岸：tempJ的状态
	                    	
	                    	matrixPath[i][j]=boatStatusDire;//从状态i通过船上的状态boatStatus得到j状态，穿上状态保存了第九位符号位，即：方向是从本岸到对岸还是反过来，
	                    
	                    }else{//不满足则不可达
	                            matrixPath[i][j]=MAX;   
	                    }    
				 }else{
		                matrixPath[i][j]=MAX;
		            }
			 }
		 } 
	}
	
	/**
	 * 在建立好的矩阵上面搜索路径
	 * @param matrixPath 之前建立好的矩阵
	 * @param fromLeftBank 欲从本岸什么状态（最开始有谁要到对岸）
	 * @param toRightBank 最终对岸都有谁
	 * */
	private static void searchThePath(int[][] matrixPath,int fromLeftBank,int toRightBank){//所搜的目标是左岸到右岸的状态：1111 1111 ------>0000 0000
		int row=fromLeftBank;
		int destination=toRightBank;
	
		if((matrixPath==null)||(judgeSatisfyCondition(fromLeftBank)==false)||(judgeSatisfyCondition(toRightBank)==false)){//对于不符合条件的直接返回
		    return;
		}
		
		boolean flag=false;//为true表示在当前矩阵行（从本岸）找到了一个合适的列坐标（要到达的岸状态），为false表示没有找到

		while(row!=toRightBank){//从本岸状态没有到达对岸状态就一直找，从当前岸状态（row）到对岸状态（column）
		    for(int column=0;column<LEN;column++){//分别找到对岸（column）状态是否有满足条件的
		        if((matrixPath[row][column]!=-1)&&(matrixPath[row][column]!=0)){//若找到了记录下每一次成功的坐标点
		        	if(!storePreviousStatus.contains(""+column)){//如果这个满足条件的坐标（对岸状态）以前没有出现过那么满足条件
		            Coordinate coord=new Coordinate(row,column,matrixPath[row][column]);
		            storePreviousStatus.addLast(""+column);//表示此状态已经出现过
		        	coordinateStatus.addLast(coord);//记录下成功通过的状态
		        	row=column;//为到下一行找下一个列的坐标做准备
		        	flag=true;//表示找到了合理的点
		        	break;
		        }else{
		        	flag=false;//表示从row到column状态不合适
		        }
		     }
		   }
		    if(flag==false){//若一行中都没有找到合适路径，那么就返回到原来的节点继续从其他节点找路径，因为上面的for在找到合适状态后是break操作
		    	if((coordinateStatus.size()==0)){//若没有保存的路径了那么就从本岸状态fromLeftBank从新找
		    		System.out.println("空的");
		    		row=fromLeftBank;
		    		continue;
		    	}
		    	row=coordinateStatus.getLast().getRow();//容器中有值那就退回到上一次的状态继续从上一个状态往下找
		    	coordinateStatus.removeLast();
		    }
		}	
	}
	
	/**
	 * 打印路径
	 * */
	private static void printThePath(){
		while(coordinateStatus.size()>0){
			Coordinate coord=coordinateStatus.getFirst();//从第一个状态开始打印
			coordinateStatus.removeFirst();
			int from = coord.getRow();
			int destinate = coord.getColumn();
			int boatStatus = coord.getBoatStatus();
			int fromTo=(boatStatus&256)==0?0:1;//获得第9位状态值：1表示从右岸到左岸
			String fromNumToPeople=NumToPeople(from^boatStatus);//异或操作为了显示时能把已经到对岸的或是离开本岸的人显示或不显示出来
			String destinateNumToPeople=NumToPeople(destinate);
			String onBoatPeople=NumToPeople(boatStatus)+"坐着船到对岸";
			if(fromTo==0){//表示船在左岸，这次从左岸往右岸开
				System.out.println( fromNumToPeople+" ------> "+onBoatPeople+"------> "+destinateNumToPeople);
				System.out.println();
			}else{//否则船在右岸
				System.out.println( destinateNumToPeople+"<------ "+onBoatPeople+" <------ "+fromNumToPeople);
				System.out.println();
			}
		}
	}
	
	/**
	 * @param status是岸或是船上的状态
	 * @return 状态为转换为人物后的拼接好的字符串
	 * 本函数将状态位转换为对应的人，拼接成字符串
	 * */
	public static String NumToPeople(int status){
		StringBuilder groupOfPeople=new StringBuilder();//保存状态位1时对应的人
		String leftBracket="[";
		String rightBracket="]";
		int count=1;
		int fromTo=status&256;//获得当前在那个岸
		
		while(count<=8){
			if((status&1)!=0){
				groupOfPeople.append(people[count]+",");
			}
			status=status>>>1;
			count++;
		}

		if(groupOfPeople.length()==0){
			
			groupOfPeople.append("岸空");
		
		}else{
			
			groupOfPeople.deleteCharAt(groupOfPeople.length()-1);//最后有一个,符号删掉
		
		}
		
		return leftBracket+groupOfPeople.toString()+rightBracket;
	}

	
	public static void main(String[] args){
		calcuateMatrixPath();//构造路径矩阵
	    searchThePath(matrixPath,255,511);//找从左岸0-1111-1111 到 右岸状态：1-1111-1111的路径
	    System.out.println("--------------------------------------------过岸过程------------------------------------");
	    System.out.println();
	    printThePath();//将找到的路径打印出来
	    System.out.println("-------------------------------------------全部过完--------------------------------------");
	}

}
