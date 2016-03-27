package com.aclastudios.spaceconquest.Sprites;

import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.aclastudios.spaceconquest.Sprites.Resource.GunPowder;
import com.aclastudios.spaceconquest.Sprites.Resource.Iron;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Lakshita on 3/26/2016.
 */
public class ResourceManager {

    private int iron_count;
    private ArrayList<Iron> iron_array;
    private int gunpowder_count;
    private ArrayList<GunPowder> gunpowder_array;
    PlayScreen screen;

    public ResourceManager(PlayScreen screen){
        this.screen = screen;
        iron_count=0;
        iron_array=new ArrayList<Iron>();
        gunpowder_count=0;
        gunpowder_array=new ArrayList<GunPowder>();
    }

    public int getIron_count() {
        return iron_count;
    }

    public int getGunpowder_count() {
        return gunpowder_count;
    }

    public Iron getIron_array(int i) {
        return iron_array.get(i);
    }

    public GunPowder getGunpowder_array(int i) {
        return gunpowder_array.get(i);
    }

    public void generateResources(float x, float y, float width, float height){
        Random rand = new Random();
        Iron iron = new Iron(screen, (int)((rand.nextInt((int) width) + x)*SpaceConquest.MAP_SCALE) , (int)((rand.nextInt((int)(height*SpaceConquest.MAP_SCALE))+y)*SpaceConquest.MAP_SCALE));
        iron_array.add(iron);
        iron_count++;
        GunPowder gunpd = new GunPowder(screen, (int)((rand.nextInt((int) width) + x)*SpaceConquest.MAP_SCALE) , (int)((rand.nextInt((int)(height*SpaceConquest.MAP_SCALE))+y)*SpaceConquest.MAP_SCALE));
        gunpowder_array.add(gunpd);
        gunpowder_count++;
    }

    public void updateIron(float dt){
        for (int n=0; n<iron_array.size();n++){
            Iron I = iron_array.get(n);
            I.update(dt);
            if (I.ifDestroyed()){
                iron_array.remove(n);
                iron_count--;
            }
        }
    }
    public void updateGunPowder(float dt){
        for (int n=0; n<gunpowder_array.size();n++){
            GunPowder gp = gunpowder_array.get(n);
            gp.update(dt);
            if (gp.ifDestroyed()){
                gunpowder_array.remove(n);
                gunpowder_count--;
            }
        }
    }

}
