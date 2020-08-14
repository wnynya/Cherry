package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Msg;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class DisplayShield {

    public static ItemStack get(DisplayPattern pattern) {
        ItemStack i = new ItemStack(pattern.getMaterial());
        ItemMeta m = i.getItemMeta();
        BannerMeta bm = (BannerMeta) m;
        bm.setPatterns(pattern.getPatterns());
        bm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        i.setItemMeta(bm);
        return i;
    }

    public static class DisplayPattern {
        private DyeColor color;
        private ArrayList<Pattern> patterns;
        private Material material;
        public DisplayPattern(DyeColor c) {
            this.color = c;
        }
        public DisplayPattern(DyeColor c, ArrayList<Pattern> p) {
            this.color = c;
            this.patterns = p;
        }
        public DisplayPattern(Material m, ArrayList<Pattern> p) {
            this.material = m;
            this.patterns = p;
        }
        public void addPattern(Pattern p) {
            patterns.add(p);
        }
        public void setPatterns(ArrayList<Pattern> p) {
            patterns = p;
        }
        public DyeColor getColor() {
            return color;
        }
        public ArrayList<Pattern> getPatterns() {
            return patterns;
        }
        public Material getMaterial() {
            return material;
        }
    }

}
