package gr.auth.ee.dsproject.crush.player;

import gr.auth.ee.dsproject.crush.board.Board;

import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.defplayers.AbstractPlayer;
import gr.auth.ee.dsproject.crush.node85668579.Node;

import java.util.ArrayList;



public class MinMaxPlayer implements AbstractPlayer
{
  // TODO Fill the class code.

  int score;
  int id;
  String name;

  public MinMaxPlayer (Integer pid)
  {
    id = pid;
    score = 0;
  }

  public String getName ()
  {

    return "MinMax";

  }

  public int getId ()
  {
    return id;
  }

  public void setScore (int score)
  {
    this.score = score;
  }

  public int getScore ()
  {
    return score;
  }

  public void setId (int id)
  {
    this.id = id;
  }

  public void setName (String name)
  {
    this.name = name;
  }

  public int[] getNextMove (ArrayList<int[]> availableMoves, Board board)
  {
    // TODO Fill the code
	  Board A;
		A=CrushUtilities.cloneBoard(board);
		Node root=new Node(0,A); //etsi i root()?
		createMySubTree(root,1); //problima edw

        int indexBest = 0;

        indexBest=chooseMove(root);//ki edw

        int[] bestMove = availableMoves.get(indexBest);

        return CrushUtilities.calculateNextMove(bestMove);
    
  }

  private void createMySubTree (Node parent, int depth)
  {
    // TODO Fill the code

	    ArrayList<int[]> moves=new ArrayList<int[]>();
	    moves=CrushUtilities.getAvailableMoves(parent.getnodeBoard());
	    int i;
	    Board temp;
	    
	    for(i=0;i<moves.size();i++){
	    	temp=CrushUtilities.boardAfterFirstMove(parent.getnodeBoard(),moves.get(i));
	    	Node child=new Node(depth,temp,moves.get(i),parent);
	    	child.setnodeEvaluation(child.evaluate());
	    	parent.setchildren(child);
	    	createOpponentSubTree(child,depth+1);
	    }

  }

  private void createOpponentSubTree (Node parent, int depth)
  {
    // TODO Fill the code

	    Board A;
	    A=CrushUtilities.boardAfterFullMove(parent.getparent().getnodeBoard(),parent.getnodeMove());
	    ArrayList<int[]> moves =new ArrayList<int[]>(); //an vgalei provlima tha kanw copy paste tin apo panw seira
	    moves=CrushUtilities.getAvailableMoves(A);
	    Board temp;
	    int i;
	    for(i=0;i<moves.size();i++){
	    	temp=CrushUtilities.boardAfterFirstMove(A,moves.get(i));
	    	Node child=new Node(depth,temp,moves.get(i),parent);
	    	child.setnodeEvaluation(-child.evaluate()+parent.getnodeEvaluation());
	    	parent.setchildren(child);
	    	
	    		
	    	
	    }
	    

  }

  private int chooseMove (Node root)
  {

    // TODO Fill the code
	  {  /*για καθε παιδι (επιπεδο 1):εξεταζω τα παιδιά του (εγγόνια), επιλέγω την μικρότερη τιμή και την δίνω σαν τιμή του παιδιού.
		  έπειτα, απ όλες τις τιμές των παιδιών (επίπεδο 1) επιλέγω τη μεγαλύτερη και επιστρέφω τον δείκτη του, μιας που αυτή είναι η καλύτερη
		  κίνηση. */
			  int i,j; //δείκτες επανάληψης
			  double min;
			  for(i=0;i<root.getchildren().size();i++){
				  min=root.getchild(i).getchild(0).getnodeEvaluation();
				  for(j=1;j<root.getchild(i).getchildren().size();j++){
					  if(root.getchild(i).getchild(j).getnodeEvaluation()>min){
						  min=root.getchild(i).getchild(j).getnodeEvaluation();
					  }
				  }
				  root.getchild(i).setnodeEvaluation(min);/*βαζω την αξια καθε μελους του επιπεδου 1 ιση με την μικροτερη απ τα παιδια του
				  (εγγονια του root) */
			  }
			  int pos=0;
			  double max=root.getchild(0).getnodeEvaluation();
			  for(i=1;i<root.getchildren().size();i++){
				  if(root.getchild(i).getnodeEvaluation()>max){
					  max=root.getchild(i).getnodeEvaluation();
					  pos=i;
					  }
			  }
			  return pos;
			  
		    
		  }
  }

}
