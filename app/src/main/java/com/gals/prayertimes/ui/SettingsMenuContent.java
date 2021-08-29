package com.gals.prayertimes.ui;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class SettingsMenuContent {

    public static class SettingsMenuItem {
        public final int id;
        public final int img;
        public final String name;


        public SettingsMenuItem(int id, int img, String name) {
            this.id = id;
            this.img = img;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
