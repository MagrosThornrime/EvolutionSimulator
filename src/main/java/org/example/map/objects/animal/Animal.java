package org.example.map.objects.animal;

import org.example.map.WorldMap;
import org.example.map.objects.animal.genes.Genes;
import org.example.map.options.IMapElement;
import org.example.utils.MapDirection;
import org.example.utils.Vector2d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Animal implements IMapElement {
    private Vector2d position;
    private MapDirection direction;
    private Genes genotype;
    private int energy;

    private int age;

    private final List<IAnimalObserver> observers = new ArrayList<>();

    public Animal(Vector2d position, int energy, Genes genotype) {
        // Basic parameters.
        this.age = 0;
        this.position = position;
        this.direction = MapDirection.NORTH.randomDirection();
        this.energy = energy;
        this.genotype = genotype;
    }

    @Override
    public String toString() {
        return switch (this.direction) {
            case NORTH -> "↑";
            case EAST -> "→";
            case SOUTH -> "↓";
            case WEST -> "←";
            case NORTHEAST -> "↗";
            case NORTHWEST -> "↖";
            case SOUTHEAST -> "↘";
            case SOUTHWEST -> "↙";
        } + "\t" + energy;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection() {
        return direction;
    }

    public int getEnergy() {
        return energy;
    }

    public Genes getGenotype() {
        return genotype;
    }

    public void addObserver(IAnimalObserver observer){
        observers.add(observer);
    }
    public void deleteObserver(IAnimalObserver observer){
        observers.remove(observer);
    }

    public void place(){
        for(IAnimalObserver observer: observers){
            observer.animalPlaced(this);
        }
    }

    public void move(){
        int nextGene = genotype.getMoveDirection();
        direction = MapDirection.fromInt(nextGene);
        position = position.add(direction.toUnitVector());
        for(IAnimalObserver observer: observers){
            observer.animalMoved(this);
        }
    }

    public void checkIfDied(){
        if(energy == 0){
            for(IAnimalObserver observer: observers){
                observer.animalDied(this);
            }
        }
    }
}
