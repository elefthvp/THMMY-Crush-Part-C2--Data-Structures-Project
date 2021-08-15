package gr.auth.ee.dsproject.crush.node85668579;
import java.util.ArrayList;
import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.board.Tile;

public class Node
{

	  //ορίζω τις μεταβλητές της συνάρτησης, by default private, δεν βαζω προσδιοριστικό
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
		   this.children.add(child);//δηλαδη παω στη λιστα των παιδιων και προσθετω ενα παιδακι
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
		   //δεν χρειαζεται πχ να βρω την μορφη του πινακα αμεσως μετα την κινηση,ο,τι χρειαζομαι ειναι private μεταβλητη της κλασης Node
		   double points;
		   points=0;
		   double[] result=new double[2];/*πρώτα το πλήθος των tiles που φεύγουν, επειτα αν φευγουν οριζοντια και κάθετα ταυτόχρονα,
			  θα δεχτεί την τιμή που θα επιστραφεί απο την πρώτη υποσυνάρτηση αξιολόγησης */
			 
			  result=calculateTilesThatWillCrush(nodeBoard);
			  points+=result[0];//πόντοι που παίρνω όσο το πλήθος των πλακακίων που θα σκάσουν
			  if(result[1]==1){
				  points+=2; //δινω δύο πόντους αν έχω ταυτόχρονη διαγραφή κάθετα και οριζόντια
			  }
			  //Τ Ε Λ Ε Υ Τ Α Ι Ο    κριτήριο η κατάσταση του πίνακα μετά, η συχνότητα χρωμάτων που θα προκύψει
			  int height,width,i,color;/*ύψος και πλάτος περιοχής που ελέγχεται,δείκτης επανάληψης και χρώμα που δεν θα ελέγξω γιατι ανήκει στην
			  κίνηση */
			  int[] colorfrequency=new int[7]; //πινακας συχνοτητων χρωματων μεγεθους οσο και τα χρωματα,θα δεχτει το αποτελεσμα
			
			  //ευρεση μεγιστου width, height που ομως να μη με βγαζει εκτος οριων
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
			  color=( parent.getnodeBoard()).giveTileAt(nodeMove[0],nodeMove[1]).getColor();//δεν θελω να προσμετρηθει αυτο το χρωμα
			  for(i=0;i<colorfrequency.length;i++){
				  if((i!=color)&&(colorfrequency[i]>4)){
					  points--;
				  }//θεωρω οτι αν στη περιοχη που ορισα υπαρχουν 4 του ιδιου χρωματος, μειωνω το points κατα 1 ως μειονεκτημα.
			  }
			  //ΕΠΙΣΤΡΟΦΗ ΤΗΣ ΕΚΤΙΜΗΣΘΕΙΣΑΣ ΑΞΙΑΣ ΤΗΣ ΚΙΝΗΣΗΣ
			  //ΕΔΩ ΕΓΚΕΙΤΑΙ Η ΑΝΑΒΑΘΜΙΣΗ ΠΟΥ ΕΧΟΥΜΕ ΣΤΟ  ΤΡΙΤΟ ΣΚΕΛΟΣ:ΠΟΝΤΟΙ ΑΠΟ CHAINMOVES
			  Board pinakas;
			  pinakas=CrushUtilities.boardAfterFirstCrush(getparent().getnodeBoard(),getnodeMove());/*δινω το ταμπλο του γονιου και την κινηση που αντι-
			  προσωπευει το παιδι.ο πινακας του παιδιου ειναι ο πινακας αμεσως μετα την αλλαγη πλακακιου,πριν αλλαξει τιποτα*/
			  double a=0;
			  do {
				  a=chainmoves(pinakas);
				  points+=a;//η μεταβλητη συνολικων ποντων που χρησιμοποιω σε ολοκληρη την συναρτηση,την οποια θα επιστρεψω
				  pinakas=CrushUtilities.boardAfterDeletingNples(pinakas);
			  } while(a!=0);/*οταν το a δεν αποφερει καθολου ποντους θα σημαινει οτι δεν εχει γινει καμια αλλαγη και εχουν εξαντληθει
			  όλα τα πιθανά chainmoves */
			  return points;
				  
			  
			  }
		  
		  
		      //ΟΡΙΣΜΟΣ ΤΩΝ ΣΥΝΑΡΤΗΣΕΩΝ ΠΟΥ ΧΡΗΣΙΜΟΠΟΙΗΣΑ ΣΤΗΝ MOVEEVALUATION
		  
		     double[] calculateTilesThatWillCrush(Board boardaftermove){
		    	 int i,j,k,z,color,count;//δείκτες επανάληψεις,χρώμα που ελέγχω,μετρητής ίδιων πλακακίων
		    	 boolean flag=false;//σημαία για το αν υπάρχει ήδη στο παρακάτω arraylist κάποιο πλακάκι.
		    	 double VerHor=0;//θα επιστραφεί δείχνοντας αν έχω μόνο οριζοντια ή κάθετα (0) ή και τα δύο (1)
		    	 //arraylist που αποθηκευει τα κουτακια που θα φυγουν
		    	 ArrayList <Tile> coordinates=new ArrayList <Tile>();//αποθήκευση των tiles που διαγράφονται
		    	 //Οριζοντια Σαρωση
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
		    	 //Κάθετη Σάρωση
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
		    						flag=false; //για καθε τουβλακι που θα ελεγξω αν ηδη υπαρχει υποθετω οτι δεν υπαρχει μεσω flag=0
		    						for(z=0;z<coordinates.size();z++){
		    							if(coordinates.get(z).getX()==i && coordinates.get(z).getY()==j){
		    								flag=true;
		    								VerHor=1;//υπαρχει εστω και ενα ταυτοχρονο διπλο καθετο-οριζοντιο
		    								
		    							}
		    						}
		    						if(flag==false){
		    							coordinates.add(boardaftermove.giveTileAt(k, j));
		    							
		    						}//προσθεση του tile αμα δεν υπαρχει
		    						
		    		 
		    		 
		    		 
		    						 
		    					 }
		    				 }
		    				count=1;
		    				 
		    			 }
		    		 }
		    	 }
		    	double[] result=new double[2];
	 	    	result[0]=coordinates.size(); //αυτοματη μετατροπη του int που επιστρεφει το size σε double
		    	result[1]=VerHor;
		        return result;
		        
		    	 
		    	 
			  
			  
			  
			  
			   
			
			  
			  
		    

		  }
		     
		     
		     
		     
		  int[] sameColorInProximity(int width,int height,int x,int y,Board A){/*αυτος ο αλγοριθμος συμφερει καλυτερα για οταν το 
		  τετραγωνακι που σκοπευω να μετακινησω ειναι καπου στο μεσο του πινακα.Για ακραιες περιπτωσεις μπορει να σαρωσει μονο τμηματα
		  σειρας η γραμμης */
			  int[] colorfrequency=new int [7];//πίνακας συχνοτήτων
			  int i,j;//δείκτες επανάληψης
			  //αποδοση αρχικης τιμης ισης με το 0 στα στοιχεια του πινακα
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
			  int i,j,k,z,color,count;//δείκτες επανάληψεις,χρώμα που ελέγχω,μετρητής ίδιων πλακακίων
	 	      boolean flag=false;//σημαία για το αν υπάρχει ήδη στο παρακάτω arraylist κάποιο πλακάκι.
	 	      
	 	 //arraylist που αποθηκευει τα κουτακια που θα φυγουν
	 	      ArrayList <Tile> coordinates=new ArrayList <Tile>();//αποθήκευση των tiles που διαγράφονται
	 	 //Οριζοντια Σαρωση
	 	      for(i=0;i<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS;i++){  
	 		     color=boardaftermove.giveTileAt(i, 0).getColor();
	 		     count=1;
	 		     for(j=0;j<CrushUtilities.NUMBER_OF_COLUMNS-1;j++){
	 			    if(boardaftermove.giveTileAt(i,j+1).getColor()==color){
	 			    	if(color!=(-1)){
	 				      count++;
	 			    	}/*για καποιο χρώμα ακόμη και αν αυτό είναι το -1.λαμβανω ομως υποψιν τα ιδια ζαχαρωτα απο αποψη μετρησης μόνο
	 			    	όταν έχουν ορισμένο χρώμα, διάφορο του -1.αυτό το μικρό τρικ γίνεται για να μην μπλέξω πολύ τον αλγόριθμο με τμήματα-γρίφους*/
	 			    	
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
	 	 //Κάθετη Σάρωση
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
	 						flag=false; //για καθε τουβλακι που θα ελεγξω αν ηδη υπαρχει υποθετω οτι δεν υπαρχει μεσω flag=0
	 						for(z=0;z<coordinates.size();z++){
	 							if(coordinates.get(z).getX()==i && coordinates.get(z).getY()==j){
	 								flag=true;
	 								
	 								
	 							}
	 						}
	 						if(flag==false){
	 							coordinates.add(boardaftermove.giveTileAt(k, j));
	 							
	 						}//προσθεση του tile αμα δεν υπαρχει
	 						
	 		 
	 		 
	 		 
	 						 
	 					}
	 				 }
	 				count=1;
	 				 
	 			 }
	 		 }
	 	 }
	 	      double result=coordinates.size(); //αυτοματη μετατροπη απο int σε double
	 	      return result;
			  
			  
			  
		  }
		  

		

	   }


	   



