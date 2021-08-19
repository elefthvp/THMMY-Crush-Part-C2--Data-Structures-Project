# THMMY-Crush-Part-C2--Data-Structures-Project
<p align=justify> The objective of this project is to choose the next move of our player in a Candy Crush game, using the Java package developed by the course instruction team. In contrast to the two previous projects, this final version also includes the evaluation of the current available moves in the longterm. More specifically, not only are the available moves weighed based on their immediate effects, but the opponent's next available moves and their possible impact on our score are also evaluated. Thus, this algorithm "predicts the future". <br></p>
<p align=justify> To implement this complex analysis, we use a tree-like structure with a minimum depth of two (our current move and the opponent's available moves after it). <br></p>
<p align=justify> The students were required to implement the Node Class that structures the tree as well as fill in the process using which the player "MinMax Player" opts for their next move. <br></p>

## Dependencies 
- gr.auth.ee.dsproject.crush package;
- java.util package
