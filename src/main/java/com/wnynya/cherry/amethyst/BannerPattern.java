package com.wnynya.cherry.amethyst;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class BannerPattern {

    public enum Mono implements Patterns {
        NUMERIC_0 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_LEFT));
                a.add(new Pattern(color, PatternType.STRIPE_RIGHT));
                return a;
            }
        },
        NUMERIC_1 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                a.add(new Pattern(color, PatternType.STRIPE_CENTER));
                a.add(new Pattern(color, PatternType.SQUARE_TOP_LEFT));
                return a;
            }
        },
        NUMERIC_2 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_DOWNLEFT));
                return a;
            }
        },
        NUMERIC_3 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
                a.add(new Pattern(color, PatternType.STRIPE_RIGHT));
                return a;
            }
        },
        NUMERIC_4 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_LEFT));
                a.add(new Pattern(bg, PatternType.HALF_HORIZONTAL_MIRROR));
                a.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
                a.add(new Pattern(color, PatternType.STRIPE_RIGHT));
                return a;
            }
        },
        NUMERIC_5 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_DOWNRIGHT));
                return a;
            }
        },
        NUMERIC_6 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_RIGHT));
                a.add(new Pattern(bg, PatternType.HALF_HORIZONTAL));
                a.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
                a.add(new Pattern(color, PatternType.STRIPE_LEFT));
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                return a;
            }
        },
        NUMERIC_7 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_RIGHT));
                return a;
            }
        },
        NUMERIC_8 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_RIGHT));
                a.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
                a.add(new Pattern(color, PatternType.STRIPE_LEFT));
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                return a;
            }
        },
        NUMERIC_9 {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.STRIPE_LEFT));
                a.add(new Pattern(bg, PatternType.HALF_HORIZONTAL_MIRROR));
                a.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
                a.add(new Pattern(color, PatternType.STRIPE_RIGHT));
                a.add(new Pattern(color, PatternType.STRIPE_TOP));
                a.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
                return a;
            }
        },
        TRIANGLE_UP {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.TRIANGLE_BOTTOM));
                return a;
            }
        },
        TRIANGLE_DOWN {
            @Override
            public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color) {
                ArrayList<Pattern> a =  new ArrayList<Pattern>();
                a.add(new Pattern(color, PatternType.TRIANGLE_TOP));
                return a;
            }
        },
    }

    public interface Patterns {
        public ArrayList<Pattern> getPatterns(DyeColor bg, DyeColor color);
    }

    public static ArrayList<Pattern> getNumeric(int n, DyeColor bg, DyeColor color) {
        switch (n) {
            case 0: return Mono.NUMERIC_0.getPatterns(bg, color);
            case 1: return Mono.NUMERIC_1.getPatterns(bg, color);
            case 2: return Mono.NUMERIC_2.getPatterns(bg, color);
            case 3: return Mono.NUMERIC_3.getPatterns(bg, color);
            case 4: return Mono.NUMERIC_4.getPatterns(bg, color);
            case 5: return Mono.NUMERIC_5.getPatterns(bg, color);
            case 6: return Mono.NUMERIC_6.getPatterns(bg, color);
            case 7: return Mono.NUMERIC_7.getPatterns(bg, color);
            case 8: return Mono.NUMERIC_8.getPatterns(bg, color);
            case 9: return Mono.NUMERIC_9.getPatterns(bg, color);
            default: return Mono.NUMERIC_0.getPatterns(bg, color);
        }
    }

}
