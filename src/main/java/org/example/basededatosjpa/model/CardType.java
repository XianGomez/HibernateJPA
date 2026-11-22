package org.example.basededatosjpa.model;

import java.util.Locale;

public enum CardType {
    ESO,
    BAC,
    FP;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
