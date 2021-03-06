package zerrium;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Substats{ //Manage substats
    final private HashMap<Material, Long> craft;
    final private HashMap<Material, Long> mine;
    final private HashMap<Material, Long> place;
    final private HashMap<EntityType, Long> kill;
    final private HashMap<EntityType, Long> kill_by;
    final private OfflinePlayer p;
    final private ZPlayer zp;

    protected Substats(ZPlayer zp){ //Preparation
        this.craft = new HashMap<>();
        this.mine = new HashMap<>();
        this.place = new HashMap<>();
        this.kill = new HashMap<>();
        this.kill_by = new HashMap<>();
        this.p = Bukkit.getOfflinePlayer(zp.uuid);
        this.zp = zp;
    }

    protected void substats_Material(){ //Substats for crafting, mining and placing blocks or items
        for(Material m: Material.values()) {
            long a = this.p.getStatistic(Statistic.CRAFT_ITEM, m);
            long b = this.p.getStatistic(Statistic.MINE_BLOCK, m);
            long c = this.p.getStatistic(Statistic.USE_ITEM, m);
            if (a != 0) {
                zp.x.put("z:craft_kind", zp.x.get("z:craft_kind")+1);
                zp.x.put("z:crafted", zp.x.get("z:crafted")+a);
                this.craft.put(m, a);
            }
            if (b != 0) {
                zp.x.put("z:mine_kind", zp.x.get("z:mine_kind")+1);
                zp.x.put("z:mined", zp.x.get("z:mined")+b);
                this.mine.put(m, b);
            }
            if (c != 0 && !ZFilter.is_tool(m)) {
                zp.x.put("z:place_kind", zp.x.get("z:place_kind")+1);
                zp.x.put("z:placed", zp.x.get("z:placed")+c);
                this.place.put(m, c);
            }else{
                if(m.toString().contains("_PICKAXE")){
                    zp.x.put("z:pickaxe", zp.x.get("z:pickaxe")+c);
                }else if(m.toString().contains("_AXE")){
                    zp.x.put("z:axe", zp.x.get("z:axe")+c);
                }else if(m.toString().contains("_SHOVEL")){
                    zp.x.put("z:shovel", zp.x.get("z:shovel")+c);
                }else if(m.toString().contains("_HOE")){
                    zp.x.put("z:hoe", zp.x.get("z:hoe")+c);
                }else if(m.toString().contains("_SWORD")){
                    zp.x.put("z:sword", zp.x.get("z:sword")+c);
                }else if(m.equals(Material.BOW)){
                    zp.x.put("z:bow", zp.x.get("z:bow")+c);
                }else if(m.equals(Material.TRIDENT)){
                    zp.x.put("z:trident", zp.x.get("z:trident")+c);
                }
            }
        }
        if(Zstats.debug) System.out.println("Materials substat done");
    }

    protected void substats_Entity(){ //Substats for killing or killed by entities
        for(EntityType t: EntityType.values()){
            try {
                if(t.isAlive()) {
                    long a = p.getStatistic(Statistic.KILL_ENTITY, t);
                    long b = p.getStatistic(Statistic.ENTITY_KILLED_BY, t);
                    if (a != 0) {
                        zp.x.put("z:mob_kind", zp.x.get("z:mob_kind") + 1);
                        this.kill.put(t, a);
                    }
                    if (b != 0) {
                        zp.x.put("z:slain_kind", zp.x.get("z:slain_kind") + 1);
                        this.kill_by.put(t, b);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if(Zstats.debug) System.out.println("EntityType substat done");
    }

    protected void sort_substats(){ //Sort all substats
        if(Zstats.debug) System.out.println("Sorting substats...");
        int i;
        if(zp.x.get("z:craft_kind") != 0){
            LinkedHashMap temp = ZFilter.sortByValues(this.craft);
            Iterator x = temp.entrySet().iterator();
            i = 0;
            while(x.hasNext() && i<3){
                Map.Entry e = (Map.Entry) x.next();
                zp.craft.put((Material) e.getKey(), (Long) e.getValue());
                i++;
            }
        }
        if(zp.x.get("z:place_kind") != 0){
            LinkedHashMap temp = ZFilter.sortByValues(this.place);
            Iterator x = temp.entrySet().iterator();
            i = 0;
            while(x.hasNext() && i<3){
                Map.Entry e = (Map.Entry) x.next();
                zp.place.put((Material) e.getKey(), (Long) e.getValue());
                i++;
            }
        }
        if(zp.x.get("z:mine_kind") != 0){
            LinkedHashMap temp = ZFilter.sortByValues(this.mine);
            Iterator x = temp.entrySet().iterator();
            i = 0;
            while(x.hasNext() && i<3){
                Map.Entry e = (Map.Entry) x.next();
                zp.mine.put((Material) e.getKey(), (Long) e.getValue());
                i++;
            }
        }
        if(zp.x.get("z:mob_kind") != 0){
            LinkedHashMap temp = ZFilter.sortByValues(this.kill);
            Iterator x = temp.entrySet().iterator();
            i = 0;
            while(x.hasNext() && i<3){
                Map.Entry e = (Map.Entry) x.next();
                zp.mob.put((EntityType) e.getKey(), (Long) e.getValue());
                i++;
            }
        }
        if(zp.x.get("z:slain_kind") != 0){
            LinkedHashMap temp = ZFilter.sortByValues(this.kill_by);
            Iterator x = temp.entrySet().iterator();
            i = 0;
            while(x.hasNext() && i<3){
                Map.Entry e = (Map.Entry) x.next();
                zp.slain.put((EntityType) e.getKey(), (Long) e.getValue());
                i++;
            }
        }
        if(Zstats.debug) System.out.println("Sorting substats done");
    }
}
