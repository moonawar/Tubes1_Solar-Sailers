package Models;

public class World {

  public Position centerPoint;
  public Integer radius;
  public Integer currentTick;

  public World(){
    centerPoint = new Position();
    radius =0;
    currentTick =0;

  }
  public World(Position a, Integer r, Integer t){
    this.centerPoint = a;
    this.radius = r;
    this.currentTick = t;
  }

  public Position getCenterPoint() {
    return centerPoint;
  }

  public void setCenterPoint(Position centerPoint) {
    this.centerPoint = centerPoint;
  }

  public Integer getRadius() {
    return radius;
  }

  public void setRadius(Integer radius) {
    this.radius = radius;
  }

  public Integer getCurrentTick() {
    return currentTick;
  }

  public void setCurrentTick(Integer currentTick) {
    this.currentTick = currentTick;
  }
}
