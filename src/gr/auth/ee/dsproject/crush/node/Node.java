package gr.auth.ee.dsproject.crush.node85668579;
import java.util.ArrayList;
import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.board.Tile;

public class Node
{

	  //define class variables, private by default
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
		   this.children.add(child); //add another child to the already existing children list
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
	   
	   public ArrayList<Node> getchildren(){
		   return children;
	   }
	   
	   
	   public double evaluate(){
		   double points;
		   points=0;
		   double[] result=new double[2];//first the number of  tiles crushed,  then if the crush is horizontal or vertical at the same time
			  result=calculateTilesThatWillCrush(nodeBoard);
			  points+=result[0]; //acquired points are equal to the number of tiles crushed
			  if(result[1]==1){
				  points+=2; //plus two points if the crush is spontaneously horizontal and vertical
			  }
			  // last points evaluation metric: the post-crush board
			  int height,width,i,color; /* height and width of the area being evaluated, and the colour that won't be taken into consideration since it belongs to the
		   	  colour that just crushed */
		   
			  int[] colorfrequency=new int[7]; //colour frequency array, has a length equal to the number of available colours
			
			  //max width, height within Board limits
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
			  color=( parent.getnodeBoard()).giveTileAt(nodeMove[0],nodeMove[1]).getColor(); //this colour is not taken into consideration
			  for(i=0;i<colorfrequency.length;i++){
				  if((i!=color)&&(colorfrequency[i]>4)){
					  points--;
				  }//if there are 4 tiles of the same colour in the defined area, the points are decreased as this is a drawback
			  }
			  //return evaluated move value
		   
			  //calculate points from chainmoves
			  Board pinakas;
			  pinakas=CrushUtilities.boardAfterFirstCrush(getparent().getnodeBoard(),getnodeMove()); /* pass the parent's board and the child-move. The child's board is the board right
			  after the move before anything else changes. */
			
			  double a=0;
			  do {
				  a=chainmoves(pinakas);
				  points+=a;	//the total points variable that returns
				  pinakas=CrushUtilities.boardAfterDeletingNples(pinakas);
			  } while(a!=0);	/* when a doesn't provoke any positive change in points, all possible chainmoves have been checked */
			  return points;
			  }
		  
		  
		      //definition of functions used within the evaluate() function 
		  
		     	 double[] calculateTilesThatWillCrush(Board boardaftermove){
		    	 int i,j,k,z,color,count;
		    	 boolean flag=false; //flag that shows whether a tile already exists in the list
		    	 double VerHor=0;  //returns 0 for only vertical/horizontal or 1 for a sponataneous crush
		    	 //arraylist where about-to-crush tiles are saved 
		    	 ArrayList <Tile> coordinates=new ArrayList <Tile>();
				 
		    	 //Horizontal Scan
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
		    	 //Vertical Scan
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
		    						flag=false; //initial hypothesis for every tile checked is that it doesn't already exist in the horizonta list		    						for(z=0;z<coordinates.size();z++){
		    							if(coordinates.get(z).getX()==i && coordinates.get(z).getY()==j){
		    								flag=true;
		    								VerHor=1; //at least one spontaneous horizontal-vertical crush exists 
		    								
		    							}
		    						}
		    						if(flag==false){
		    							coordinates.add(boardaftermove.giveTileAt(k, j));
		    							
		    						}//add tile if it doesn't already exist
		    						
							 }
		    				 }
		    				count=1;
		    				 
		    			 }
		    		 }
		    	 }
		    	double[] result=new double[2];
	 	    	result[0]=coordinates.size(); //convert int size to double
		    	result[1]=VerHor;
		        return result;
		  }
		    
		     
		     
		  int[] sameColorInProximity(int width,int height,int x,int y,Board A){/* this algorithm returns better results when the tile to be moves is in the central area 
		  rather than the marginal one */
			  int[] colorfrequency=new int [7]; //frequency matrix
			  int i,j;
			  //initialize to zero
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
			  int i,j,k,z,color,count;
	 	      	  boolean flag=false;
			  ArrayList <Tile> coordinates=new ArrayList <Tile>();
			  for(i=0;i<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS;i++){  
	 		     	color=boardaftermove.giveTileAt(i, 0).getColor();
	 		     	count=1;
	 		     	for(j=0;j<CrushUtilities.NUMBER_OF_COLUMNS-1;j++){
	 			    if(boardaftermove.giveTileAt(i,j+1).getColor()==color){
	 			    	if(color!=(-1)){
	 				      count++;
	 			    	}
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
								flag=false; 
								for(z=0;z<coordinates.size();z++){
									if(coordinates.get(z).getX()==i && coordinates.get(z).getY()==j){
										flag=true;
									}
								}
								if(flag==false){
									coordinates.add(boardaftermove.giveTileAt(k, j));

								}

							}
						 }
						count=1;

					 }
				    }
			     }
	 	      double result=coordinates.size();
	 	      return result;
			  
			  
	}
}
