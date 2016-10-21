/* Assignment: Connect 4
 * Name: Nima && Ray
 * Date:
 * Teacher: Mr. A
 */ 

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class connect4C extends JFrame implements ActionListener{
  
//create frame
  static connect4C frame;
  
//------------CREATE STATIC VARIABLES-----------//  
  
  static Timer timer;
  
  
//INTEGERS
  static int mode;
  static int max;
  static int columnClicked;
  static int dropheight=-1;
  static int spacecount=0;
  static int player = 1;
  static int totaldepth = 7;
  static int first=1;
  static int count=1;
  
//INTEGER ARRAYS
  static int[][] grid = new int[6][7];
  static int[] best=new int[2];
  static int[] fitness=new int[7];
  static int[] colheights= new int[7];
  
//BOOLEAN    
  static boolean playervsAI=true;
  static boolean win = false;
  static boolean playclicked = false;
  static boolean computer = false;
  
//RANDOM
  static Random rand = new Random();
  
  
//-----------CREATE GUI VARIALBES-----------//
  
//JPANELS
  JPanel left = new JPanel();
  JPanel gamegrid = new JPanel();
  JPanel top = new JPanel();
  JPanel display = new JPanel();
  JPanel bottom = new JPanel();
  
//RADIOBUTTONS  
  JRadioButton  pvp = new JRadioButton ("Player vs. Player");
  JRadioButton  pvai = new JRadioButton  ("Player vs. Computer");
  
//JBUTTONS  
  static JButton[] column = new JButton[7];
  JButton help = new JButton("HOW TO PLAY");
  JButton play = new JButton ("PLAY");
  static JButton[][] guigrid = new JButton[6][7];
  
//JLABEL  
  JLabel title = new JLabel ("CONNECT 4");
  
  
//-------------CREATE CONSTRUCTOR-----------//  
  public connect4C(){
    
//CREATE THE FRAME
    setTitle ("Connect 4");
    setSize (650,600);
    setVisible(true);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    setContentPane(new JLabel(new ImageIcon("background.png")));
    setLayout(new FlowLayout());
    setResizable(false);
    
//ADD LAYOUTS TO THE DESIRED JPANEL   
    left.setLayout (new BoxLayout(left, BoxLayout.Y_AXIS));
    display.setLayout (new BoxLayout(display, BoxLayout.Y_AXIS));
    GridLayout board = new GridLayout(8,7);
    gamegrid.setLayout(board);
    
//SET THE FONT SIZE OF THE JLABEL "TITLE"    
    title.setFont(new Font("Sans Serif", Font.BOLD, 20));
    
//ADD THE TITLE TO THE TOP PANEL AND CENTER THE TITLE   
    top.add(title);
    pvp.setAlignmentX(Component.CENTER_ALIGNMENT);
    
//ADD THE GUI OBJECTS TO THE LEFT PANEL AND CENTER THE OBJECTS    
    left. add (pvp);
    left.add (new JLabel(" "));
    pvai.setAlignmentX(Component.CENTER_ALIGNMENT);
    left.add (pvai);
    left.add (new JLabel(" "));
    play.setAlignmentX(Component.CENTER_ALIGNMENT);
    left.add (play);
    left.add (new JLabel(" "));
    help.setAlignmentX(Component.CENTER_ALIGNMENT);
    left.add (help);
    
//ADD THE LEFT PANEL TO THE BOTTOM PANEL    
    bottom.add (left);
    bottom.add (new JLabel ("     "));
    
//ADD THE COLUMN BUTTON TO THE GAMEGRID PANEL    
    for (int z = 0; z < 7; z++) {
      column [z] = new JButton ("");
      column[z].setPreferredSize(new Dimension (60,60));
      try {
        
        //ADD AN IMAGE TO THE COLUMN BUTTON
        Image img1 = ImageIO.read(getClass().getResource("arrow.png"));
        column[z].setIcon(new ImageIcon(img1));
      } 
      
      catch (IOException ex) {
      }
      
      gamegrid. add (column[z]);
      
    }
    
//CREATE A BLANK ROW TO SEPARATE THE GRID AND THE COLUMN BUTTONS    
    for (int i = 0; i < 7; i++) {
      gamegrid.add (new JLabel ("  "));
    }
    
//ADD THE GUIGRID BUTTONS TO THE GAMEGRID PANLE    
    for (int i = 0; i < 6; i ++) {
      for (int a = 0; a < 7; a++) {
        guigrid[i][a] = new JButton ("");
        guigrid[i][a].setPreferredSize(new Dimension (60,60));
        guigrid[i][a].setBackground (Color.WHITE);
        gamegrid. add (guigrid[i][a]);
      }
    }
    
//ADD THE GAMEGRID PANEL TO THE BOTTOM PANEL    
    bottom.add (gamegrid);
    
//ADD THE TOP AND THE BOTTOM PANEL TO THE DISPLAY PANEL    
    display.add(top);
    display.add(bottom);
    
//ADD THE DISPLAY PANEL TO THE FRAME    
    add(display);
    
//SET THE DEFAULT CONDITIONS FOR THE RADIOBUTTONS    
    pvp.setMnemonic(KeyEvent.VK_B);
    pvp.addActionListener(this);
    pvp.setSelected(false);
    pvai.setMnemonic(KeyEvent.VK_B);
    pvai.addActionListener(this);
    pvai.setSelected(true);
    
//ADD ACTIONLISTENER TO THE PLAY BUTTON AND THE HELP BUTTON    
    play.addActionListener(this);
    help.addActionListener(this);
    top.setOpaque(false);
    left.setOpaque(false);
    bottom.setOpaque(false);
    display.setOpaque(false);
    gamegrid.setOpaque(false);
    pvp.setOpaque(false);
    pvai.setOpaque(false);
  }
  
//-----------THIS METHOD IS CALLED WHEN THE USER PERFORMS AN ACTION------------//  
  public void actionPerformed(ActionEvent event) {
    
//IF THE USER CLICKS THE PVP RADIOBUTTON ENTER THIS IF STATEMENT
    if (event.getSource() == pvp) {
      if (playervsAI) {
        playervsAI =false;
        pvp.setSelected(true);
        pvai.setSelected(false);
      }
    }
    
//IF THE USER CLICKS THE PVAI RADIOBUTTON ENTER THIS IF STATEMENT
    if (event.getSource() == pvai) {
      if (!playervsAI) {
        playervsAI = true;
        pvp.setSelected(false);
        pvai.setSelected(true);
      }
    }
    
//IF THE USER CLICKS THE PLAY BUTTON ENTER THIS IF STATEMENT
    if (event.getSource() == play) {
      int n=0;
      //IF THE USER HAS CLICKED THIS BUTTON BEFORE ENTER THIS IF STATEMENT      
      if (playclicked) {
        
        //ASK THE USER IF HE/SHE WANTS TO RESET THE GAMEBOARD
        Object[] options = {"Yes", "No"};
        n = JOptionPane.showOptionDialog(frame,"Would you like to restart the game? ","A Silly Question", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        
        //IF THE USER WISHES TO RESET THE BOARD ENTER THIS IF STATEMENT AND RESET ALL VARIABLES        
        if (n == 0) {
          totaldepth = 7;
          
            for (int b = 0; b< 6; b++) {
              for (int a = 0; a < 7; a++) {
                grid[b][a] = 0;
                guigrid[b][a].setIcon(null);
                guigrid[b][a].setBackground(Color.WHITE);
                column[a].removeActionListener(this);
                colheights[a]=0;
              
            }
            
          }
            win = false;
          player = 1;
          count=1;
          spacecount=0;
          
        }
      }
      
      //ENABLE THE COLUMN BUTTONS      
      for (int i = 0; i < 7; i++ ) {
        column[i].setEnabled(true);
      }
      
//------ADD ACTIONLISTENERS TO ALL THE COLUMN BUTTONS-----//     
      if (!playclicked) {
        column[0].addActionListener(new ActionListener(){ //column[0] button
          @Override
          public void actionPerformed(ActionEvent arg) {
            columnClicked = 0;
            new Worker().execute(); //Call the worker
          }
        });
        column[1].addActionListener(new ActionListener(){ //column[1] button
          @Override
          public void actionPerformed(ActionEvent arg) {
            columnClicked = 1;
            new Worker().execute(); //Call the worker
          }
        });
        column[2].addActionListener(new ActionListener(){ //column[2] button
          @Override
          public void actionPerformed(ActionEvent arg) {
            columnClicked = 2;
            new Worker().execute(); //Call the worker
          }
        });
        column[3].addActionListener(new ActionListener(){ //column[3] button
          @Override
          public void actionPerformed(ActionEvent arg) {
            columnClicked = 3;
            new Worker().execute(); //Call the worker
          }
        });
        column[4].addActionListener(new ActionListener(){ //column[4] button
          @Override
          public void actionPerformed(ActionEvent arg) {
            columnClicked = 4;
            new Worker().execute(); //Call the worker
          }
        });
        column[5].addActionListener(new ActionListener(){ //column[5] button
          @Override
          public void actionPerformed(ActionEvent arg) {
            columnClicked = 5;
            new Worker().execute(); //Call the worker
          }
        });
        column[6].addActionListener(new ActionListener(){ //column[6] button
          @Override
          public void actionPerformed(ActionEvent arg) {
            columnClicked = 6;
            new Worker().execute(); //Call the worker
          }
        });
      }
      
      //SET THE DEFAULT MODE TO 0      
      mode = 0;
      
      //IF THE PVAI RADIO BUTTON IS SELECTED ENTER THIS IF STATEMENT      
      if (playervsAI && n==0) {
        
        //SET THE MODE TO 1        
        mode = 1;
        
        //ASK THE USER IF HE/SHE WOULD LIKE TO GO FIRST        
        Object[] options = {"Yes, please","No, thanks"};
        int a = JOptionPane.showOptionDialog(frame,"Would you like to go first?","A Silly Question",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        
        //IF THE USER WISHES TO GO SECOND        
        if (a == 1) {
          drop (3,3,grid);
          player = 3;
          first = 3;
          //CALL THE AIDELAY METHOD          
          AIdelay(0,3,"Player2.png",5);         
          player = 1;
          spacecount++;
        }
      }
      playclicked = true; //PLAY BUTTON HAS BEEN CLICKED ATLEAST ONCE
    }
    
//IF THE HELP BUTTON WAS CLICKED ENTER THIS IF STATEMENT    
    if (event.getSource() == help) {
      JOptionPane.showMessageDialog(frame, "The objective of the game is to connect four\n of one's own discs of the same color next to\n each other vertically, horizontally, or diagonally\n before your opponent.");
    }
  }
  
//-------------------MAIN METHOD-----------------//  
  public static void main (String[] args) {
    frame = new connect4C();
  }
  
//-------------------WHEN THE USER/AI DROPS A PIECE------------------//  
  public static int drop(int col, int player, int[][] grid){
    for (int j = 6-1; j >=0 ; j--) {
      if (grid[j][col] == 0) {
        grid[j][col] = player;
        return j;
      }
    }
    return -1;
  }
  
//------------------CODERINO METHOD----------------//  
  public static void coderino(int col, int dropheight, int player, int[][] grid, int mode) {
    if(pointcheck(dropheight,col,player,4,grid)!=-1){
      JOptionPane.showMessageDialog(frame, "Player "+ player +" WINS!");
      win=true;
    }
    
  }
  
//--------------CHECK SURROUNDING SPACES FOR A SIMILAR PIECE------------//  
  public static int pointcheck(int x, int y, int player, int nums, int[][] grid){
    
    for(int i=x-1;i<=x+1;i++){
      for(int j=y-1;j<=y+1;j++){            //nested for loop running in a 9x9 grid with current point as center
        if((i!=x || j!=y)){                 //excludes center point
          if(i>=0&&i<6&&j>=0&&j<7){         //makes sure it is not out of bounds
            if(grid[i][j]==player){         //checks if the values around the current point are the current player's
              check(x,y,i-x,j-y,player,grid);                    //if it finds a match, run the check method on that point in both directions
              check(x,y,x-i,y-j,player,grid);
              if(count>=nums){              //if the count is greater than or equal to the nums in a row it is looking for, it returns which player won.
                count=1;
                return player;
              }
              count=1;
            }
          }
        }
      }
    }
    return -1;
  }
  
//--------------METHOD THAT CONTINUES TO CHECK IN THE SAME DIRECTION--------------//  
  public static void check(int a, int b, int x, int y,int player, int[][] grid){
    //This method keeps counting in the given direction recursively until it hits something that is not a match, then stops
    if(a+x>=0&&b+y>=0&&a+x<6&&b+y<7&&grid[a+x][b+y]==player){
      count++;
      check(a+x, b+y, x,y,player,grid);
    }
  }
  
  
//----------------------METHOD THAT DISPLAYS THE GAMEBOARD---------------------//  
  public static void showgrid(int[][] grid){
    for (int i = 0; i< 6; i++) {
      for (int a = 0; a < 7; a++) {
        System.out.print(grid[i][a]+"  ");
      }
      System.out.println();
    }
  }
  
//----------------------METHOD THAT CHECKS FOR THREE IN A ROW---------------------//  
  
  public static int threecheck(int x, int y, int player,int otherplayer, int[][] grid){
    int exitcount=0;
    int count=1;
    int threecount=0;
    boolean spacehit=false;
    int [] spacepoint={-1,-1};
    
    //In below code, i-x serves as the direction in the positive and x-i serves as direction in the negative,
    //k is the magnitude or how far in that direction it compares to
    //The code runs on the points topleft, topright, and to the left of the point it is checking or origin, doing comparisons in each direction 
    //up to 3 units away from the origin in the positive and negative directions
    
    for(int i=x-1;i<=x+1;i++){
      for(int j=y-1;j<=y+1;j++){            
        if(y!=j){                           //ignore vertical threes as they are immediately blocked
          for(int k=1;k<4;k++){             
            if(x+((i-x)*k)>=0&&y+((j-y)*k)>=0&&x+((i-x)*k)<6&&y+((j-y)*k)<7){  //check boundaries
              if(grid[x+((i-x)*k)][y+((j-y)*k)]==player){                      //if the piece is the same as that of the origin, the count increases
                count++;
              }else if(grid[x+((i-x)*k)][y+((j-y)*k)]==otherplayer){           //if it hits the other player the check in that direction immediately ends
                k=4;
              }else if(!spacehit){                                             //the coordinates of the first open space are recorded in spacepoint
                spacepoint[0]=x+((i-x)*k);
                spacepoint[1]=y+((j-y)*k);
                spacehit=true;
              }
            }
          }
          
          //same as above for loop but in opposite direction
          
          for(int k=1;k<4;k++){
            if(x+((x-i)*k)>=0&&y+((y-j)*k)>=0&&x+((x-i)*k)<6&&y+((y-j)*k)<7){
              if(grid[x+((x-i)*k)][y+((y-j)*k)]==player){
                count++;
              }else if(grid[x+((x-i)*k)][y+((y-j)*k)]==otherplayer){
                k=4;
              }else if(!spacehit){
                spacepoint[0]=x+((x-i)*k);
                spacepoint[1]=y+((y-j)*k);
                spacehit=true;
              }
            }
          }
        }
        
        if(count==3&&spacepoint[0]>=0&&spacepoint[0]+colheights[spacepoint[1]]!=5){  //if it has counted three in a row, and it isn't immediately blockable,
          if(spacepoint[0]%2==0){                                //the open space is even,
            if(first!=player){                                   //and the placed piece owner went second,
              for(int k=0;k<spacepoint[0];k++){                  //then the move is given value according to how low the space is (lower is better)
                threecount++;
              }
            }
          }else{                                                 //if the open space is odd
            if(first==player){                                   //and the owner of the placed piece went first,
              for(int k=0;k<spacepoint[0];k++){                  //then the move is given value according to how low the space is
                threecount++;
              }
            }
          }
        }
        exitcount++;
        if(exitcount==4){                                        //when all relevant directions have been checked the value is returned
          return threecount;
        }
        count=1;
        spacehit=false;
      }
    }
    return 0;
  }
  
  
  
  
//----------------------METHOD THAT DETERMINES AI MOVE---------------------//  
  public static int[] score(int mode,int player, int[][] grid, int depth){
    int[] value=new int[2];
    int otherplayer;
    int randnum;
    int best[]=new int[2];
    int threecount=0;
    
    //AI's MOVE - makes a new grid for every possible AI move and then evaluates them, if there is no win, it runs the method again
    //on the other player at one less depth, until it reaches a depth of 1 where it returns 0.
    //At every depth it finds the best of the 7 values and returns it to the above depth, until reaches the top depth and returns the best move
    
    if(player==3){                                 //if it is the AI's turn
      otherplayer=1;                               //set otherplayer to the otherplayer
      best[1]=-1000;                               //initialize the best value to 'negative infinity'
      for(int i=0;i<7;i++){                        
        int[][] newgrid=new int[6][7];
        for(int j = 0; j < 6; j++){
          newgrid[j] = grid[j].clone();            //copy the array to a new array 
        }
        
        int dropheight=drop(i,player,newgrid);     //drop a piece in all 7 positions, one position for each array
        
        if(dropheight!=-1){
          int winner=pointcheck(dropheight,i,player,4,grid);               //check if someone wins, returns the winning player
          threecount=threecheck(dropheight,i,player,otherplayer,grid); //check for three in a rows, returns the value of the three in a rows according to certain criteria in threecheck method
          
          if(winner==3){
            value[1]=1000;                      //if AI wins, the value is 1000
          }else if(winner==1){
            value[1]=-1000;                     //if the player wins, the value is -1000
          }else if(depth==1){
            value[1]=0;                         //if no one wins and there are no more permutations to be made, the value is 0
          }else{
            value=score(1,1,newgrid,depth-1);   //runs the scoring method on the opposite player with one less depth
            if(depth>totaldepth-2){
              if(i==3){
                value[1]=value[1]+10;              //add value to the move the closer it is to the middle
              }else if(i==2||i==4){
                value[1]=value[1]+5;
              }
              if(first==3){
                if(dropheight%2!=0&&dropheight<5){
                  value[1]=value[1]+10;           //add value to odd rows if AI is first
                }
              }else{
                if(dropheight%2==0){
                  value[1]=value[1]+10;           //add value to even rows if AI is second
                }
              }
              value[1]=value[1]+(10*threecount*depth);  //add value to the move according to the value of the 3s created
            }
          }
        }else{
          value[1]=-1001;                       //if the column is full, rate it lower than a loss
        }
        
//        if(depth==totaldepth){
//          System.out.println(value[1]);         
//        }
        
        if(best[1]<value[1]){                   //find the highest value and store the value and its column
          best[1]=value[1];
          best[0]=i;
        }else if (best[1]==value[1]&&depth==totaldepth){  //if the current highest value is equal to the one its comparing to:
          if(columnClicked==i){                           //default to stacking on top of the opponent's play
            best[1]=value[1];
            best[0]=i;
          }else{                                          //if that is not possible choose a play randomly
            randnum=rand.nextInt(2);
            if(randnum==1){
              best[1]=value[1];
              best[0]=i;
            }
          }
        }
      }
      
      //if the best move is a loss after (depth) moves, reduce the overall depth by 1 and run the code again so it doesnt give up when it knows it will lose
      if(depth==totaldepth){
        if(best[1]==-1000){
          totaldepth=depth-1;
          best=score(mode,3,grid,totaldepth);
        }
      }
      
    }
    
    //PLAYER 1's MOVE - same as code for AI's move except all values are inverse and it finds the worst move for the AI rather
    //than the best move
    
    if(player==1){                             //if it is the player's turn
      otherplayer=3;                           //set otherplayer to the otherplayer
      best[1]=1000;                            //initialize best value to 'positive infinity'
      for(int i=0;i<7;i++){
        int[][] newgrid=new int[6][7];
        for(int j = 0; j < 6; j++){
          newgrid[j] = grid[j].clone();            //copy the array to a new array
        }
        
        int dropheight=drop(i,player,newgrid);                         
        if(depth>totaldepth-2){
          threecount=threecheck(dropheight,i,player,otherplayer,grid); //check for three in a rows, returns the value of the three in a rows according to certain criteria in threecheck method
        }
        if(dropheight!=-1){
          int winner=pointcheck(dropheight,i,player,4,grid);
          if(winner==3){
            value[1]=1000;
          }else if(winner==1){
            value[1]=-1000;
          }else if(depth==1){
            value[1]=0;
          }else{
            value=score(1,3,newgrid,depth-1);
            value[1]=value[1]-(10*threecount*depth);
          }
        }else{
          value[1]=1001;                      //if column is full set the value higher than a win for the AI(as in the player will never make this move)
        }
        
        if(best[1]>value[1]){                 //finds lowest value (relative to AI board value)
          best[1]=value[1];
          best[0]=i;
        }
        
      }
      
    }
    
    return best;                             //returns best column and its value
    
  }
  
  public class Worker extends SwingWorker <String, String>{
    @Override
    protected String doInBackground() throws Exception {
      //This is what's called in the .execute method
      for (int a = 0; a < 7; a++) {
        column[a].setEnabled(false);
      }
      
      //CHECK THE Y-COORDINATE OF THE PIECE THAT IS BEING DROPPED
      int y = drop(columnClicked,player,grid);
      
      //IF THE THE Y-COORDINATE IS A VALID VALUE ENTER THIS IF STATMENT
      if (y != -1) {
        spacecount++;
        colheights[columnClicked]++;
        if (player == 1) {
          if (mode == 0) {
            player = 2;
          }
          else player = 3;
          
          delay (0,columnClicked, "Player1.png", y);
          
          coderino (columnClicked,y,1,grid,mode);
        }
        else if (player == 2) { 
          
          if (mode == 0) {
            player = 1;
          }
          
          delay (0,columnClicked,"Player2.png", y);
          coderino (columnClicked,y,2,grid,mode);
        }
        if (win) {
          for (int a = 0; a < 7; a++) {
            column[a].setEnabled(false);
          }
        }
      }
      if(spacecount>=42&&(!win)){
        JOptionPane.showMessageDialog(frame, "Its a tie!");
        win = true;
      }
      
      //IF IT IS THE AI'S TURN AND THERE HASN'T BEEN A WIN ENTER THIS IF STATEMENT
      if (player == 3 && (!win)) {
        spacecount++;
        best = score(mode,player,grid,totaldepth);

        dropheight=drop(best[0],3,grid);
        colheights[best[0]]++;
        if(spacecount==42-totaldepth){
          totaldepth=totaldepth-2;
        }
        delay (0,best[0],"Player2.png",dropheight);
        
        if(pointcheck(dropheight,best[0],3,4,grid)==3){
          JOptionPane.showMessageDialog(frame, "COMPUTER WINS!");
          win = true;
        }
        
        player = 1;
      }

      showgrid(grid);
      for (int a = 0; a < 7; a++) {
        if(!win) {
          column[a].setEnabled(true);
        }
      }

      //CHECK IF THE BOARD IS FULL AND IF THERE HAS NOT BEEN A WIN
      if(spacecount>=42&&(!win)){
        JOptionPane.showMessageDialog(frame, "Its a tie!");
        win = true;
      }
      return null;
    }
    
    //------------------DROP ANIMATION METHOD----------------------//
    private void delay(int row, int col, String pic, int y){
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (row <= y) {
        System.out.println ("PLAYER IS: " + pic);
        System.out.println ("THE FINAL ROW VALUE IS: " +row);
        System.out.println ("THE FINAL Y VALUE IS: " +y);
        System.out.println ("THE FINAL COLUMN VALUE IS: " +col);
        try {
          Image img = ImageIO.read(getClass().getResource(pic));
          guigrid[row][col].setIcon(new ImageIcon(img));
        } catch (IOException ex) {
        }
        if ((row-1) >= 0) {
          guigrid[row-1][col].setIcon(null);
        }
        //--Delay for user to display--//
        delay (row+1,col,pic,y);
      }      
    }//end of delay method    
  }
  
  
//--------------DROP ANIMATION METHOD USED ONLY IF AI GOES FIRST---------------//
  public static void AIdelay (final int y, final int x, final String pic, final int row) {
    System.out.println ("Y: " + y);
    timer = new Timer(10, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //if there is no need to stop then it will continue 
        if ((y < 6) &&(guigrid[y][x].getIcon() == (null))) {
          try {
            Image img = ImageIO.read(getClass().getResource(pic));
            guigrid[y][x].setIcon(new ImageIcon(img));
          } catch (IOException ex) {
          }
          if (y-1 >= 0) {
            guigrid[y-1][x].setIcon(null);
          }
          AIdelay (y+1, x, pic, row);
          return;
        }
        //set the desired features to the finishPlow button
        
        //if there is a need to stop the timer will stop
        else  ((Timer)e.getSource()).stop();
        
      }
    });
    timer.start();
  }
}