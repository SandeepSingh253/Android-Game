package com.team.classicrealm.BlockBreaker;

public class BlockBreakerBrick {

    private boolean visible;

    public int row, column, width, height;

   public BlockBreakerBrick(int row, int column, int width, int height) {
       visible = true;
       this.row = row;
       this.column = column;
       this.width = width;
       this.height = height;
   }

   public void setInvisible() {
       visible = false;
   }

   public boolean isVisible() {
       return visible;
   }
}
