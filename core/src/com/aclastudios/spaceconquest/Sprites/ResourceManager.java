package com.aclastudios.spaceconquest.Sprites;

import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.aclastudios.spaceconquest.Sprites.Resource.GunPowder;
import com.aclastudios.spaceconquest.Sprites.Resource.Iron;
import com.aclastudios.spaceconquest.Sprites.Resource.Oil;
import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Lakshita on 3/26/2016.
 */
public class ResourceManager {

    private int iron_count;
    private ArrayList<Iron> iron_array;
    private int gunpowder_count;
    private ArrayList<GunPowder> gunpowder_array;
    private int oil_count;
    private ArrayList<Oil> oil_array;
    PlayScreen screen;
    private int userID;
    private SpaceConquest game;
    private String allres;

    public ResourceManager(PlayScreen screen, SpaceConquest game, int userID){
        this.game = game;
        this.screen = screen;
        iron_count=0;
        iron_array=new ArrayList<Iron>();
        gunpowder_count=0;
        gunpowder_array=new ArrayList<GunPowder>();
        oil_count = 0;
        oil_array = new ArrayList<Oil>();
        this.userID = userID;

    }


    public int getIron_count() {
        return iron_count;
    }

    public int getGunpowder_count() {
        return gunpowder_count;
    }

    public int getOil_count() {
        return oil_count;
    }

    public Iron getIron_array(int i) {
        return iron_array.get(i);
    }

    public GunPowder getGunpowder_array(int i) {
        return gunpowder_array.get(i);
    }

    public Oil getOil_array(int i){
        return oil_array.get(i);
    }

    public void getResourceString(String resources){
        allres = resources;
        System.out.println("Broadcast msg: "+allres);
    }

    public void generateResources(float x, float y, float width, float height){
        if (userID==0){
            Random rand = new Random();
            if (iron_count<7)
                generateIron(x, y, width, height, rand);
            if (gunpowder_count<7)
                generateGunPowder(x, y, width, height, rand);
            if (oil_count<7)
                generateOil(x, y, width, height, rand);
            game.playServices.BroadcastMessage("Resources:" + coordinatesR());

        }
        else{
            try {
                System.out.println("in generate resources for player");
                System.out.println("Allres: "+allres);
                String[] igo = allres.split("R");
                System.out.println("IGO 0:"+igo[1]);
                System.out.println("IGO 1:"+igo[2]);
                System.out.println("IGO 2:"+igo[3]);
                String[] irons = igo[1].split(",");
                String[] gunps = igo[2].split(",");
                String[] oils = igo[3].split(",");

                for (int i = 0; i < irons.length; i++) {
                    Iron iron = new Iron(screen, Float.parseFloat(irons[i].split(" ")[0]), Float.parseFloat(irons[i].split(" ")[1]));
                    iron_array.add(iron);
                    iron_count++;
                }
                for (int i = 0; i < gunps.length; i++) {
                    GunPowder gunpd = new GunPowder(screen, Float.parseFloat(gunps[i].split(" ")[0]), Float.parseFloat(gunps[i].split(" ")[1]));
                    gunpowder_array.add(gunpd);
                    gunpowder_count++;
                }
                for (int i = 0; i < oils.length; i++) {
                    Oil oil = new Oil(screen, Float.parseFloat(oils[i].split(" ")[0]), Float.parseFloat(oils[i].split(" ")[1]));
                    oil_array.add(oil);
                    oil_count++;
                }
            }catch (Exception e){
                System.out.println("Check resource if error: ");
                e.printStackTrace();
            }
        }

    }

    private void generateIron(float x, float y, float width, float height, Random rand){
        Iron iron = new Iron(screen, (int) ((rand.nextInt((int) width) + x) * SpaceConquest.MAP_SCALE), (int) ((rand.nextInt((int) (height * SpaceConquest.MAP_SCALE)) + y) * SpaceConquest.MAP_SCALE));
        System.out.println("IRON: "+userID+": " + iron.getX() + ", " + iron.getY());
        iron_array.add(iron);
        iron_count++;
    }
    private void generateGunPowder(float x, float y, float width, float height, Random rand){
        GunPowder gunpd = new GunPowder(screen, (int) ((rand.nextInt((int) width) + x) * SpaceConquest.MAP_SCALE), (int) ((rand.nextInt((int) (height * SpaceConquest.MAP_SCALE)) + y) * SpaceConquest.MAP_SCALE));
        System.out.println("GUNPD: " + gunpd.getX() + ", " + gunpd.getY());
        gunpowder_array.add(gunpd);
        gunpowder_count++;
    }
    private void generateOil(float x, float y, float width, float height, Random rand){
        Oil oil = new Oil(screen, (int) ((rand.nextInt((int) width) + x) * SpaceConquest.MAP_SCALE), (int) ((rand.nextInt((int) (height * SpaceConquest.MAP_SCALE)) + y) * SpaceConquest.MAP_SCALE));
        System.out.println("OIL: " + oil.getX() + ", " + oil.getY());
        oil_array.add(oil);
        oil_count++;
    }

    public void updateIron(float dt){
        for (int n=0; n<iron_array.size();n++){
            Iron I = iron_array.get(n);
            I.update(dt);
            if (I.ifDestroyed()){
                System.out.println("iron");
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
                System.out.println("gp");
                gunpowder_array.remove(n);
                gunpowder_count--;
            }
        }
    }
    public void updateOil(float dt){
        for (int n=0; n<oil_array.size();n++){
            Oil ol = oil_array.get(n);
            ol.update(dt);
            if (ol.ifDestroyed()){
                System.out.println("oil");
                oil_array.remove(n);
                oil_count--;
            }
        }
    }

    public String coordinatesR (){
        String iron = "R";
        for (int i=0;i<iron_array.size();i++){
            iron += iron_array.get(i).getX() + " " + iron_array.get(i).getY() + ",";    // Ri- x1 coord : y1 coord , x2 coord : y2 coord , so on
        }
        String gunpowder = "R";
        for (int i=0;i<gunpowder_array.size();i++){
            gunpowder += gunpowder_array.get(i).getX() + " " + gunpowder_array.get(i).getY() + ",";    // g- x1 coord : y1 coord , x2 coord : y2 coord , so on
        }
        String oil = "R";
        for (int i=0;i<oil_array.size();i++){
            oil += oil_array.get(i).getX() + " " + oil_array.get(i).getY() + ",";    // o- x1 coord : y1 coord , x2 coord : y2 coord , so on
        }

        String resourceCoodinates = iron + gunpowder + oil;

        System.out.println("Resources:"+resourceCoodinates);
        return resourceCoodinates;
    }

}
