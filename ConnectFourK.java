import java.util.*;

class ConnectFourK{
  static int count=1;
  static int[][] grid = new int[6][7];
  static int totaldepth=6;
  static int first=3;
  static int spacecount=0;
  static Random rand = new Random ();
  public static void main(String[] args){
    coderino();
  }
  
  public static void coderino(){
    int win=-1;
    
    int player=1;
    boolean computer=false;
    int[] best=new int[2];
    int dropheight=-1;
    int mode=1;
    
    if (first==3){
      player=3;
    }
    
    Scanner myScanner = new Scanner (System.in);
    
    for (int i = 0; i< 6; i++) {
      for (int a = 0; a < 7; a++) {
        grid[i][a] = 0;
      }
    }
    
    showgrid(grid);
    computer=true;
    
    while (win<0) {
      System.out.println("Player "+player+"'s turn.");
      if(player==1){
        int col = myScanner.nextInt();
        if(col>=0&&col<7){
          dropheight=drop(col,player,grid);
          spacecount++;
          if(dropheight>=0){
            
            win=pointcheck(dropheight,col,player,4,grid);
            
            if(player==1){
              if(computer){
                player=3;
              }else{
                player=2;
              }
            }else{
              player=1;
            }
          }else{
            System.out.println("There is no space");
          }
        }else{
          System.out.println("Enter a valid col");
        }
      }
      // Artificial Someintelligence
      if(player==3&&win<0){
        spacecount++;
        //6 6 6 5 5 5 3 4 4 4 3 3 2 2 2 0 1 1 1 0
        best=score(mode,player,grid,totaldepth);
        dropheight=drop(best[0],3,grid);
        
        if(spacecount==42-totaldepth){
          totaldepth=totaldepth-2;
        }
        
        win=pointcheck(dropheight,best[0],player,4,grid);
        
        player=1;
      }
      showgrid(grid);
      if(spacecount==42&&win<0){
        win=0;
      }
    }
    if(win>0){
      if(win==3){
        System.out.println("YOU LOSE");
      }else{
        System.out.println("Player "+win+" wins~!");
      }
    }else{
      System.out.println("TIE");
    }
  }
  
  public static void showgrid(int[][] grid){
    for (int i = 0; i< 6; i++) {
      for (int a = 0; a < 7; a++) {
        System.out.print(grid[i][a]+"  ");
      }
      System.out.println();
    }
  }
  
  public static int pointcheck(int x, int y, int player, int nums, int[][] grid){
    for(int i=x-1;i<=x+1;i++){
      for(int j=y-1;j<=y+1;j++){            //nested for loop running in a 9x9 grid with current point as center
        if((i!=x || j!=y)){                   //excludes center point and top row
          if(i>=0&&i<6&&j>=0&&j<7){    //makes sure it is not out of bounds
            if(grid[i][j]==player){            //checks if the values around the current point are the current player's
              check(x,y,i-x,j-y,player,grid);                    //if it finds a match, run the check method on that point in both directions
              check(x,y,x-i,y-j,player,grid);
              if(count>=nums){
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
  
  public static void check(int a, int b, int x, int y,int player, int[][] grid){
    if(a+x>=0&&b+y>=0&&a+x<6&&b+y<7&&grid[a+x][b+y]==player){
      count++;
      check(a+x, b+y, x,y,player,grid);
    }
  }
  
  public static int threecheck(int x, int y, int player,int otherplayer, int[][] grid){
    int exitcount=0;
    int count=1;
    int threevalue=0;
    int [] spacepoint={-1,-1};

    for(int i=x-1;i<=x+1;i++){
      for(int j=y-1;j<=y+1;j++){            //nested for loop running in a 9x9 grid with current point as center
        for(int k=1;k<4;k++){
          if(x+((i-x)*k)>=0&&y+((j-y)*k)>=0&&x+((i-x)*k)<6&&y+((j-y)*k)<7){
            if(grid[x+((i-x)*k)][y+((j-y)*k)]==player){
              count++;
            }else if(grid[x+((i-x)*k)][y+((j-y)*k)]==otherplayer){
              k=4;
            }else{
              spacepoint[0]=x+((i-x)*k);
              spacepoint[1]=y+((j-y)*k);
            }
          }
        }
        for(int k=1;k<4;k++){
          if(x+((x-i)*k)>=0&&y+((y-j)*k)>=0&&x+((x-i)*k)<6&&y+((y-j)*k)<7){
            if(grid[x+((x-i)*k)][y+((y-j)*k)]==player){
              count++;
            }else if(grid[x+((x-i)*k)][y+((y-j)*k)]==otherplayer){
              k=4;
            }else{
              spacepoint[0]=x+((x-i)*k);
              spacepoint[1]=y+((y-j)*k);
            }
          }
        }
        

        if(count==3&&spacepoint[0]>=0){
          if(spacepoint[1]%2==0){
            if(first!=player){
              threevalue=threevalue+10;
            }else{
              threevalue++;
            }
          }else{
            if(first==player){
              threevalue=threevalue+10;
            }else{
              threevalue++;
            }
          }
        }
        exitcount++;
        if(exitcount==4){
          return threevalue;
        }
        count=1;
      }
    }
    return 0;
  }
  
  public static int[] score(int mode,int player, int[][] grid, int depth){
    int[] value=new int[2];
    int otherplayer;
    int randnum;
    
    int best[]=new int[2];
    
    if(player==3){
      otherplayer=1;
      best[1]=-1000;
      for(int i=0;i<7;i++){
        int[][] newgrid=new int[6][7];
        for(int j = 0; j < 6; j++){
          newgrid[j] = grid[j].clone();            //copy the array to a new array
        }
        
        int dropheight=drop(i,player,newgrid);
        if(dropheight!=-1){
          int winner=pointcheck(dropheight,i,player,4,grid);
          int threecount=threecheck(dropheight,i,player,otherplayer,grid);
          if(winner==3){
            value[1]=1000;
          }else if(winner==1){
            value[1]=-1000;
          }else if(depth==1){
            value[1]=0;           
          }else{
            value=score(1,1,newgrid,depth-1);
            if(i==3){
              value[1]=value[1]+5;          //value center column higher
            }
            value[1]=value[1]+(10*threecount);
          }
        }else{
          value[1]=-1001;
        }
        
        if(depth==totaldepth){
          System.out.println(value[1]);
        }
        
        if(best[1]<value[1]){
          best[1]=value[1];
          best[0]=i;
        }else if (best[1]==value[1]&&depth==totaldepth){
          randnum=rand.nextInt(3);
          if(randnum==1){
            best[1]=value[1];
            best[0]=i;
          }
        }
      }
      
      //if the best move is a loss after (depth) moves, reduce the depth by 1(remember to reset totaldepth later)
      if(depth==totaldepth){
        if(best[1]==-1000){
          totaldepth=depth-1;
          best=score(mode,3,grid,totaldepth);
        }
      }
      
    }
    
    if(player==1){
      otherplayer=3;
      best[1]=1000;
      for(int i=0;i<7;i++){
        int[][] newgrid=new int[6][7];
        for(int j = 0; j < 6; j++){
          newgrid[j] = grid[j].clone();            //copy the array to a new array
        }
        
        int dropheight=drop(i,player,newgrid);
        int threecount=threecheck(dropheight,i,player,otherplayer,grid);
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
            value[1]=value[1]-(10*threecount);
          }
        }else{
          value[1]=1001;
        }
        
        if(best[1]>value[1]){
          best[1]=value[1];
          best[0]=i;
        }
        
      }
      
    }
    
    return best;
    
  }
  
  public static int drop(int col, int player, int[][] grid){
    for (int j = 6-1; j >=0 ; j--) {
      if (grid[j][col] == 0) {
        grid[j][col] = player;
        return j;
      }
    }
    return -1;
  }
}

