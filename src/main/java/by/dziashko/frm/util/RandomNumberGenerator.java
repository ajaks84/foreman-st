package by.dziashko.frm.util;

import java.util.Random;

public class RandomNumberGenerator {

    public int getRandomOneToThree (){
        Random random = new Random();
        int randomWintNextIntWithinARange = random.nextInt(4 - 1) + 1;
        return randomWintNextIntWithinARange;
    }


}
