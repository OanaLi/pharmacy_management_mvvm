package model;

import java.util.ArrayList;
import java.util.List;

public enum Producer {

    TERAPIA,
    ZENTIVA,
    BIOFARM,
    HELICOR;

    public static List<String> getProducersList() {
        List<String> producers = new ArrayList<>();
        for (Producer producer : Producer.values()) {
            producers.add(producer.name());
        }
        return producers;
    }


}

