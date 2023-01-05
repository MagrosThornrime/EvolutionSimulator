package org.example.map.objects.plants;

import org.example.utils.Vector2d;

import java.util.*;

public class PlantsEquator implements IPlants, IPlantObserver{

    private final Set<Vector2d> equator = new HashSet<>();

    private final Set<Vector2d> nonEquator = new HashSet<>();

    private int equatorStartRow;
    private int mapWidth;
    private int equatorEndRow;

    private int equatorRows;

    private int mapHeight;

    private final Random generator = new Random();

//    private void findEquator(){
//        List<Integer> equator = new ArrayList<>();
//        int equatorRows;
//        int startUp, startDown;
//        if(mapHeight % 2 == 1) {
//            int middle = mapHeight / 2;
//            equator.add(middle);
//            startUp = middle - 1;
//            startDown = middle + 1;
//            equatorRows = mapHeight * 2 / 5 - 1;
//        }
//        else {
//            startDown = mapHeight / 2;
//            startUp = startDown - 1;
//            equatorRows = mapHeight * 2 / 5;
//        }
//        for(int i=0; i<equatorRows/2; i++){
//            equator.add(startUp - i);
//            equator.add(startDown + i);
//        }
//    }

    private void calcEquator(){
        // TODO test and maybe debug
        if(mapHeight % 2 == 1){
            int middle = mapHeight / 2;
            equatorRows = mapHeight / 5;
            int rowsLeft = equatorRows;
            equatorStartRow = middle - equatorRows / 2;
            rowsLeft -= equatorRows / 2 + 1;
            equatorEndRow = middle + rowsLeft;
        }
        else{
            equatorRows = mapHeight / 5;
            int rowsLeft = equatorRows;
            equatorEndRow = mapHeight / 2 + equatorRows / 2 - 1;
            rowsLeft -= equatorRows / 2;
            equatorStartRow = equatorEndRow - rowsLeft;
        }
        System.out.println(equatorStartRow);
        System.out.println(equatorEndRow);
        System.out.println(equatorRows);
    }

    private boolean isInEquator(int row){
        return row >= equatorStartRow && row <= equatorEndRow;
    }

    public PlantsEquator(int mapWidth, int mapHeight){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        calcEquator();
    }

    private boolean equatorAccessible(){
        return equator.size() < equatorRows * mapWidth;
    }

    private boolean nonEquatorAccessible(){
        return nonEquator.size() < (mapHeight - equatorRows) * mapWidth;
    }

    private int yEquator(){
        return generator.nextInt(equatorEndRow - equatorStartRow + 1) + equatorStartRow;
    }

    private Vector2d positionInsideEquator() throws IllegalArgumentException{
        if(!equatorAccessible()){
            throw new IllegalArgumentException("Can't place plant on preferred position");
        }
        Vector2d position;
//        int i = 0;
        do{
//            System.out.println(i);
            int y = yEquator();
            int x = xRandom();
            position = new Vector2d(x, y);
//            i++;
        }while(equator.contains(position));
        return position;
    }

    private int yUnderEquator(){
        return generator.nextInt(equatorStartRow);
    }

    private int yBelowEquator(){
        return generator.nextInt(mapHeight - equatorEndRow - 1) + equatorEndRow + 1;
    }

    private int xRandom(){
        return generator.nextInt(mapWidth);
    }

    private Vector2d positionOutsideEquator(){
        if(!nonEquatorAccessible()){
            throw new IllegalArgumentException("Can't place plant on non-preferred position");
        }
        Vector2d position;
//        System.out.println("outside");
//        int i = 0;
        do{
//            System.out.println(i);
            int x = xRandom();
            int y;
            if(generator.nextBoolean()){
                y = yBelowEquator();
            }
            else{
                y = yUnderEquator();
            }
            position = new Vector2d(x, y);
//            i++;
        }while(nonEquator.contains(position));
        return position;
    }

    @Override
    public Vector2d grow(boolean preferred) throws IllegalArgumentException{
        if(preferred){
            return positionInsideEquator();
        }
        return positionOutsideEquator();
    }

    @Override
    public void plantEaten(Plant plant){
        Vector2d position = plant.getPosition();
        equator.remove(position);
        nonEquator.remove(position);
    }

    @Override
    public void plantPlaced(Plant plant){
        Vector2d position = plant.getPosition();
        if(isInEquator(position.y)){
            equator.add(position);
        }
        else {
            nonEquator.add(position);
        }
    }
}
