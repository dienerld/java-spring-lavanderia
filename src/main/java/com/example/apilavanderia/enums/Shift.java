package com.example.apilavanderia.enums;

public enum Shift {
    MORNING {
        @Override
        public String hourToString() {
            return "06:00 a 12:00";
        }
    }, AFTERNOON {
        @Override
        public String hourToString() {
            return "12:00 a 18:00";
        }
    };

    public abstract String hourToString();
}
