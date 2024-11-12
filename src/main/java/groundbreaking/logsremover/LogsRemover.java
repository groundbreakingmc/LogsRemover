package groundbreaking.logsremover;

import groundbreaking.logsremover.commands.CommandHandler;
import groundbreaking.logsremover.utils.FileRemover;
import groundbreaking.logsremover.utils.config.ConfigLoader;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public final class LogsRemover extends JavaPlugin {

    private final FileRemover fileRemover = new FileRemover(this);

    private long removeAfter;
    private String logsDirectory;
    private String noPermissionMessage;

    @Override
    public void onEnable() {
        new Metrics(this, 23889);

        this.setupValues();

        final CommandHandler commandHandler = new CommandHandler(this);
        final PluginCommand command = super.getCommand("logsremover");
        command.setExecutor(commandHandler);
        command.setTabCompleter(commandHandler);

        final BukkitScheduler scheduler = super.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(this, this.fileRemover::removeTime, 0L, 72000L /* 1 hour */);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void setupValues() {
        final FileConfiguration config = new ConfigLoader(this).loadAndGet("config", 1.0);
        final String specifiedTime = config.getString("delete-if-older-then");
        this.removeAfter = this.convertToHours(specifiedTime);
        final String specifiedLogsDirectory = config.getString("logs-directory");
        this.logsDirectory = System.getProperty("user.dir") + File.separator + specifiedLogsDirectory;
        this.noPermissionMessage = config.getString("no-permission");
    }

    private long convertToHours(final String timeString) {
        final Pattern pattern = Pattern.compile("(\\d+)([mwdh])");
        final Matcher matcher = pattern.matcher(timeString);

        long totalHours = 0;
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);
            switch (unit) {
                case 'm' ->
                    totalHours += value / 60;
                case 'w' ->
                    totalHours += value * 7 * 24L;
                case 'd' ->
                    totalHours += value * 24L;
                case 'h' ->
                    totalHours += value;
            }
        }

        return totalHours;
    }
}
