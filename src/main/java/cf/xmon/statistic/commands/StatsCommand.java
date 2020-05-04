package cf.xmon.statistic.commands;

import cf.xmon.statistic.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
public class StatsCommand implements CommandExecutor {
    private static TreeMap<String, Integer> statystyki = new TreeMap<>();
    //Method for sorting the TreeMap based on values
    public static <K, V extends Comparable<V>> Map<K, V>
    sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =
                new Comparator<K>() {
                    public int compare(K k1, K k2) {
                        int compare =
                                map.get(k1).compareTo(map.get(k2));
                        if (compare == 0)
                            return 1;
                        else
                            return compare;
                    }
                };

        Map<K, V> sortedByValues =
                new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("xmon.stats")){
            if (strings.length == 0){
                commandSender.sendMessage("stats [typ] [entitytype/material] [entitytype/material(że typ)]");
            }else {
                if (strings.length == 1) {
                    Statistic statistic = Statistic.valueOf(strings[0]);
                    Arrays.stream(Bukkit.getOfflinePlayers()).forEach(x -> {
                        statystyki.put(x.getName(), x.getStatistic(statistic));
                    });
                }else if (strings.length == 3){
                    if (strings[1].equalsIgnoreCase("entitytype")){
                        EntityType entityType = EntityType.valueOf(strings[2]);
                        Statistic statistic = Statistic.valueOf(strings[0]);
                        Arrays.stream(Bukkit.getOfflinePlayers()).forEach(x -> {
                            statystyki.put(x.getName(), x.getStatistic(statistic, entityType));
                        });
                    }else if (strings[1].equalsIgnoreCase("material")){
                        Material material = Material.valueOf(strings[2]);
                        Statistic statistic = Statistic.valueOf(strings[0]);
                        Arrays.stream(Bukkit.getOfflinePlayers()).forEach(x -> {
                            statystyki.put(x.getName(), x.getStatistic(statistic, material));
                        });
                    }else{
                        commandSender.sendMessage("stats [typ] [entitytype/material] [entitytype/material(że typ)]");
                    }
                }else {
                    commandSender.sendMessage("stats [typ] [entitytype/material] [entitytype/material(że typ)]");
                }
                try {
                    Map sortedMap = sortByValues(statystyki);
                    List<String> ss = new ArrayList<>();
                    sortedMap.forEach((x, y) ->{
                        ss.add(y + " | Gracz " + x + " posiada dla tej statystyki " + y);
                    });
                    Collections.sort(ss);
                    Files.write(Paths.get(Main.getPlugin(Main.class).getDataFolder().getAbsolutePath() + "/" + System.currentTimeMillis() + "-" + strings[0] + ".txt"),
                            ss,
                            StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                commandSender.sendMessage("wporzo!");
            }
        }else{
            commandSender.sendMessage("brak permisiji!");
        }
        return false;
    }
}
