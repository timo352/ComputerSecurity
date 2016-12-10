/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CipherGUI;

import java.util.Comparator;

/**
 *
 * @author Timothy
 */
class FrequencyComparator implements Comparator<FrequencyString> {

    private boolean switch0;

    public FrequencyComparator() {
        this(false);
    }

    public FrequencyComparator(boolean b) {
        switch0 = b;
    }

    public int compare(FrequencyString o1, FrequencyString o2) {
        if(switch0){
            FrequencyString temp = o2;
            o2 = o1;
            o1 = temp;
        }
        return Integer.compare(o1.numTimesAppears(), o2.numTimesAppears());
    }
}
