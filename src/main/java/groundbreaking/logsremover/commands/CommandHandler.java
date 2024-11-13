package groundbreaking.logsremover.commands;

import groundbreaking.logsremover.LogsRemover;
import groundbreaking.logsremover.utils.UpdatesChecker;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CommandHandler implements CommandExecutor, TabCompleter {

    private final LogsRemover plugin;
    private boolean waitConfirm;

    public CommandHandler(final LogsRemover plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof final Player player) {
            if (player.hasPermission("logsremover.reload")) {
                return this.reload(sender);
            } else {
                player.sendMessage(this.plugin.getNoPermissionMessage());
            }
        }

        if (args.length < 1) {
            return this.usage(sender);
        }

        return switch(args[0].toLowerCase()) {
            case "reload" ->
                    this.reload(sender);
            case "removeall" ->
                    this.removeall(sender);
            case "confirm" ->
                    this.confirm(sender);
            case "update" ->
                    this.update(sender);
            default ->
                    this.usage(sender);
        };
    }

    private boolean reload(final CommandSender sender) {
        final long startTime = System.currentTimeMillis();
        this.plugin.setupValues();
        final long endTime = System.currentTimeMillis();
        final long resultTime = endTime - startTime;
        sender.sendMessage("§2[LogsRemover] §aPlugin was successfully reload in " + resultTime + "ms.");
        return true;
    }

    private boolean removeall(final CommandSender sender) {
        if (!this.waitConfirm) {
            sender.sendMessage("§4[LogsRemover] §c=================================================================");
            sender.sendMessage("§4[LogsRemover] §c| Are you sure you want to clear the database of verified players");
            sender.sendMessage("§4[LogsRemover] §c| This action is irreversible!");
            sender.sendMessage("§4[LogsRemover] §c|");
            sender.sendMessage("§4[LogsRemover] §c| If yes, execute \"§4/logsremover confirm§c\"");
            this.waitConfirm = true;
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
                    this.waitConfirm = false,
                    400L // 20 seconds
            );
        } else {
            sender.sendMessage("§4[LogsRemover] §cYou have already executed this command! Use: \"§4/logsremover confirm§c\" to confirm remove!");
        }
        return true;
    }

    private boolean confirm(final CommandSender sender) {
        if (!this.waitConfirm) {
            sender.sendMessage("§4[LogsRemover] §cFirst you have to execute \"§4/logsremover removeall§c\"!");
            return true;
        }

        this.plugin.getFileRemover().removeAll();
        this.waitConfirm = false;

        sender.sendMessage("§4[LogsRemover] §cAll log files were successfully removed!");
        return true;
    }

    private boolean update(final CommandSender sender) {
        if (!UpdatesChecker.hasUpdate()) {
            sender.sendMessage("§2[LogsRemover] §aYou are already up to date!");
            return true;
        }

        final BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
        final UpdatesChecker updatesChecker = new UpdatesChecker(this.plugin);
        scheduler.runTaskAsynchronously(this.plugin, updatesChecker::downloadJar);
        return true;
    }

    private boolean usage(final CommandSender sender) {
        sender.sendMessage("§c[LogsRemover] §4Usage:");
        sender.sendMessage("§c[LogsRemover] §c/logsremover reload §f- reload the plugin");
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§c[LogsRemover] §c/logsremover removeall §f- remove all log files");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            final List<String> list = new ArrayList<>();
            list.add("reload");
            list.add("help");
            list.add(waitConfirm ? "confirm" : "removeall");
            if (UpdatesChecker.getDownloadLink() != null) {
                list.add("update");
            }
            return list;
        }
        return Collections.emptyList();
    }
}
