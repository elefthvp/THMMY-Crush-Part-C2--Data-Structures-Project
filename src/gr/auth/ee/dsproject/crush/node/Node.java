package gr.auth.ee.dsproject.crush.node85668579;
import java.util.ArrayList;
import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.board.Tile;

public class Node
{

	  //����� ��� ���������� ��� ����������, by default private, ��� ���� ��������������
	   Node parent;
	   ArrayList<Node> children=new ArrayList<Node>();
	   int nodeDepth;
	   int[] nodeMove;
	   Board nodeBoard;
	   double nodeEvaluation;
	   public Node(int nodeDepth,Board nodeBoard){
		   this.nodeDepth=nodeDepth;
		   this.nodeBoard=nodeBoard;
	   }
	   public Node(int nodeDepth,Board nodeBoard,int[] nodeMove,Node parent ){
		   this.nodeDepth=nodeDepth;
		   this.nodeBoard=nodeBoard;
		   this.nodeMove=nodeMove;
		   this.parent=parent;
		   
		   
	   }
	   public void setnodeEvaluation(double nodeEvaluation){
		   this.nodeEvaluation=nodeEvaluation;
	   }
	   public void setchildren(Node child){
		   this.children.add(child);//������ ��� ��� ����� ��� ������� ��� �������� ��� �������
	   }
	   public double getnodeEvaluation(){
		   return nodeEvaluation;
	   }
	   public Node getparent(){
		   return parent;
	   }
	   public Board getnodeBoard(){
		   return nodeBoard;
	   }
	   
		   
	   
	   public int[] getnodeMove(){
		   return nodeMove;
		 
	   }
	   public Node getchild(int i){
		   return children.get(i);
	   }
	   
	   public ArrayList<Node> getchildren(){//EINAI SWSTH AYTH H SYNTAXH?????   P   R  O  S   O  X  H
		   return children;
	   }
	   
	   
	   public double evaluate(){
		   //��� ���������� �� �� ��� ��� ����� ��� ������ ������ ���� ��� ������,�,�� ���������� ����� private ��������� ��� ������ Node
		   double points;
		   points=0;
		   double[] result=new double[2];/*����� �� ������ ��� tiles ��� �������, ������ �� ������� ��������� ��� ������ ����������,
			  �� ������ ��� ���� ��� �� ���������� ��� ��� ����� ������������ ����������� */
			 
			  result=calculateTilesThatWillCrush(nodeBoard);
			  points+=result[0];//������ ��� ������ ��� �� ������ ��� ��������� ��� �� �������
			  if(result[1]==1){
				  points+=2; //���� ��� ������� �� ��� ���������� �������� ������ ��� ���������
			  }
			  //� � � � � � � � �    �������� � ��������� ��� ������ ����, � ��������� �������� ��� �� ��������
			  int height,width,i,color;/*���� ��� ������ �������� ��� ���������,������� ���������� ��� ����� ��� ��� �� ������ ����� ������ ����
			  ������ */
			  int[] colorfrequency=new int[7]; //������� ���������� �������� �������� ��� ��� �� �������,�� ������ �� ����������
			
			  //������ �������� width, height ��� ���� �� �� �� ������ ����� �����
			  if(nodeMove[0]-0>CrushUtilities.NUMBER_OF_PLAYABLE_ROWS-nodeMove[0]){
				  height=CrushUtilities.NUMBER_OF_PLAYABLE_ROWS-nodeMove[0];
			  }else{
				  height=nodeMove[0]-0;
			  }
			  if(nodeMove[1]-0>CrushUtilities.NUMBER_OF_COLUMNS-nodeMove[1]){
				  width=CrushUtilities.NUMBER_OF_COLUMNS-nodeMove[1];
			  }else{
				  width=nodeMove[1]-0;
			  }
			  colorfrequency=sameColorInProximity(width,height,nodeMove[0],nodeMove[1],nodeBoard);
			  color=( parent.getnodeBoard()).giveTileAt(nodeMove[0],nodeMove[1]).getColor();//��� ���� �� ������������ ���� �� �����
			  for(i=0;i<colorfrequency.length;i++){
				  if((i!=color)&&(colorfrequency[i]>4)){
					  points--;
				  }//����� ��� �� ��� ������� ��� ����� �������� 4 ��� ����� ��������, ������ �� points ���� 1 �� �����������.
			  }
			  //��������� ��� ������������� ����� ��� �������
			  //��� �������� � ���������� ��� ������ ���  ����� ������:������ ��� CHAINMOVES
			  Board pinakas;
			  pinakas=CrushUtilities.boardAfterFirstCrush(getparent().getnodeBoard(),getnodeMove());/*���� �� ������ ��� ������ ��� ��� ������ ��� ����-
			  ���������� �� �����.� ������� ��� ������� ����� � ������� ������ ���� ��� ������ ���������,���� ������� ������*/
			  double a=0;
			  do {
				  a=chainmoves(pinakas);
				  points+=a;//� ��������� ��������� ������ ��� ����������� �� �������� ��� ���������,��� ����� �� ���������
				  pinakas=CrushUtilities.boardAfterDeletingNples(pinakas);
			  } while(a!=0);/*���� �� a ��� �������� ������� ������� �� �������� ��� ��� ���� ����� ����� ������ ��� ����� ����������
			  ��� �� ������ chainmoves */
			  return points;
				  
			  
			  }
		  
		  
		      //������� ��� ����������� ��� ������������� ���� MOVEEVALUATION
		  
		     double[] calculateTilesThatWillCrush(Board boardaftermove){
		    	 int i,j,k,z,color,count;//������� �����������,����� ��� ������,�������� ����� ���������
		    	 boolean flag=false;//������ ��� �� �� ������� ��� ��� �������� arraylist ������ �������.
		    	 double VerHor=0;//�� ���������� ���������� �� ��� ���� ��������� � ������ (0) � ��� �� ��� (1)
		    	 //arraylist ��� ���������� �� �������� ��� �� ������
		    	 ArrayList <Tile> coordinates=new ArrayList <Tile>();//���������� ��� tiles ��� ������������
		    	 //��������� ������
		    	 for(i=0;i<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS;i++){  
		    		 color=boardaftermove.giveTileAt(i, 0).getColor();
		    		 count=1;
		    		 for(j=0;j<CrushUtilities.NUMBER_OF_COLUMNS-1;j++){
		    			 if(boardaftermove.giveTileAt(i,j+1).getColor()==color){
		    				 count++;
		    			 }else{
		    				 color=boardaftermove.giveTileAt(i,j+1).getColor();
		    				 if(count>=3){
		    					 for(k=j;k>j-count;k--){
		    						 coordinates.add(boardaftermove.giveTileAt(i,k));
		    					 }
		    				 }
		    				 count=1;
		    			 }
		    		 }
		    	 }
		    	 //������ ������
		    	for(j=0;j<CrushUtilities.NUMBER_OF_COLUMNS;j++){
		    		color=boardaftermove.giveTileAt(0,j).getColor();
		    		count=1;
		    		for(i=0;i<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS-1;i++){
		    			if(boardaftermove.giveTileAt(i+1,j).getColor()==color){
		    				count++;
		    			}else{
		    				color=boardaftermove.giveTileAt(i+1, j).getColor();
		    				if(count>=3){
		    					for(k=i;k>i-count;k--){
		    						flag=false; //��� ���� �������� ��� �� ������ �� ��� ������� ������� ��� ��� ������� ���� flag=0
		    						for(z=0;z<coordinates.size();z++){
		    							if(coordinates.get(z).getX()==i && coordinates.get(z).getY()==j){
		    								flag=true;
		    								VerHor=1;//������� ���� ��� ��� ���������� ����� ������-���������
		    								
		    							}
		    						}
		    						if(flag==false){
		    							coordinates.add(boardaftermove.giveTileAt(k, j));
		    							
		    						}//�������� ��� tile ��� ��� �������
		    						
		    		 
		    		 
		    		 
		    						 
		    					 }
		    				 }
		    				count=1;
		    				 
		    			 }
		    		 }
		    	 }
		    	double[] result=new double[2];
	 	    	result[0]=coordinates.size(); //�������� ��������� ��� int ��� ���������� �� size �� double
		    	result[1]=VerHor;
		        return result;
		        
		    	 
		    	 
			  
			  
			  
			  
			   
			
			  
			  
		    

		  }
		     
		     
		     
		     
		  int[] sameColorInProximity(int width,int height,int x,int y,Board A){/*����� � ���������� �������� �������� ��� ���� �� 
		  ����������� ��� ������� �� ���������� ����� ����� ��� ���� ��� ������.��� ������� ����������� ������ �� ������� ���� �������
		  ������ � ������� */
			  int[] colorfrequency=new int [7];//������� ����������
			  int i,j;//������� ����������
			  //������� ������� ����� ���� �� �� 0 ��� �������� ��� ������
			  for(i=0;i<colorfrequency.length;i++){
				  colorfrequency[i]=0;
			  }
			  for(i=x-height;i<x+height;i++){
				  for(j=y-width;j<y+width;j++){
					  colorfrequency[A.giveTileAt(i, j).getColor()]++;
					  
				  }
			  }
			  return colorfrequency;
		  }
		  
		  double chainmoves(Board boardaftermove){
			  int i,j,k,z,color,count;//������� �����������,����� ��� ������,�������� ����� ���������
	 	      boolean flag=false;//������ ��� �� �� ������� ��� ��� �������� arraylist ������ �������.
	 	      
	 	 //arraylist ��� ���������� �� �������� ��� �� ������
	 	      ArrayList <Tile> coordinates=new ArrayList <Tile>();//���������� ��� tiles ��� ������������
	 	 //��������� ������
	 	      for(i=0;i<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS;i++){  
	 		     color=boardaftermove.giveTileAt(i, 0).getColor();
	 		     count=1;
	 		     for(j=0;j<CrushUtilities.NUMBER_OF_COLUMNS-1;j++){
	 			    if(boardaftermove.giveTileAt(i,j+1).getColor()==color){
	 			    	if(color!=(-1)){
	 				      count++;
	 			    	}/*��� ������ ����� ����� ��� �� ���� ����� �� -1.������� ���� ������ �� ���� �������� ��� ����� �������� ����
	 			    	���� ����� �������� �����, ������� ��� -1.���� �� ����� ���� ������� ��� �� ��� ������ ���� ��� ��������� �� �������-�������*/
	 			    	
	 			    }else{
	 				   color=boardaftermove.giveTileAt(i,j+1).getColor();
	 				   if(count>=3){
	 					 for(k=j;k>j-count;k--){
	 						 coordinates.add(boardaftermove.giveTileAt(i,k));
	 					 }
	 				   }
	 				   count=1;
	 			    }
	 		     }
	 	      }
	 	 //������ ������
	 	      for(j=0;j<CrushUtilities.NUMBER_OF_COLUMNS;j++){
	 		    color=boardaftermove.giveTileAt(0,j).getColor();
	 		    count=1;
	 		    for(i=0;i<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS-1;i++){
	 			   if(boardaftermove.giveTileAt(i+1,j).getColor()==color){
	 				count++;
	 			   }else{
	 				  color=boardaftermove.giveTileAt(i+1, j).getColor();
	 				  if(count>=3){
	 					for(k=i;k>i-count;k--){
	 						flag=false; //��� ���� �������� ��� �� ������ �� ��� ������� ������� ��� ��� ������� ���� flag=0
	 						for(z=0;z<coordinates.size();z++){
	 							if(coordinates.get(z).getX()==i && coordinates.get(z).getY()==j){
	 								flag=true;
	 								
	 								
	 							}
	 						}
	 						if(flag==false){
	 							coordinates.add(boardaftermove.giveTileAt(k, j));
	 							
	 						}//�������� ��� tile ��� ��� �������
	 						
	 		 
	 		 
	 		 
	 						 
	 					}
	 				 }
	 				count=1;
	 				 
	 			 }
	 		 }
	 	 }
	 	      double result=coordinates.size(); //�������� ��������� ��� int �� double
	 	      return result;
			  
			  
			  
		  }
		  

		

	   }


	   



