package cf.xmon.statistic;

import cf.xmon.statistic.commands.StatsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("stats").setExecutor(new StatsCommand());
    }
}
