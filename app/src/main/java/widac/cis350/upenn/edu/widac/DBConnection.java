package widac.cis350.upenn.edu.widac;

import widac.cis350.upenn.edu.widac.models.Sample;

/**
 * Created by ashutosh on 2/23/17.
 */

public class DBConnection {

    public void addNewSample(Sample newSample) {
//        Sample newSample = new Sample(344, 120, 1, 2, "ceramic", 0.002);
    }

    public void updateSampleMaterial(String compositeKey, String newMaterial) {
//        String compositeKey = "344-120-1-2";
//        String newMaterial = "ceramic";
    }

    public void updateSampleWeight(String compositeKey, double newWeight) {
//        String compositeKey = "344-120-1-2";
//        double newWeight = 0.002;
    }

    public Sample retrieveSample(String compositeKey) {
            return new Sample(344, 120, 1, 2, "ceramic", 0.002);
    }

}
