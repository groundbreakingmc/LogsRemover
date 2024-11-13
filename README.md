# LogsRemover [![CodeFactor](https://www.codefactor.io/repository/github/groundbreakingmc/logsremover/badge)](https://www.codefactor.io/repository/github/groundbreakingmc/logsremover)
A Minecraft plugin for automatically removing old log files.

## Features
- **Automatic Log File Removal**
  Easily deletes old log files based on your specified time threshold.

- **Update Checker**
  Notifies you of available updates, with an option to automatically download and apply them.

## Commands
- `/logsremover reload`
  Reloads the plugin configuration.
  **Permission:** `logsremover.reload`

- `/logsremover help`
  Displays a list of available commands.

- `/logsremover removeall`
  Deletes all log files in the specified directory.
  **Note:** Requires confirmation with `/logsremover confirm` and can be used only from console.

- `/logsremover update`
  Downloads and applies the latest available update.

## Installation
1. Download the latest release from [Releases](https://github.com/groundbreakingmc/LogsRemover/releases).
2. Place the `LogsRemover.jar` file in your server's `plugins` directory.
3. Restart or reload your server to activate the plugin.

## Configuration
You can customize the plugin behavior by editing the `config.yml` file located in the `plugins/LogsRemover` folder.
```yaml
# ██╗░░░░░░█████╗░░██████╗░░██████╗██████╗░███████╗███╗░░░███╗░█████╗░██╗░░░██╗███████╗██████╗░
# ██║░░░░░██╔══██╗██╔════╝░██╔════╝██╔══██╗██╔════╝████╗░████║██╔══██╗██║░░░██║██╔════╝██╔══██╗
# ██║░░░░░██║░░██║██║░░██╗░╚█████╗░██████╔╝█████╗░░██╔████╔██║██║░░██║╚██╗░██╔╝█████╗░░██████╔╝
# ██║░░░░░██║░░██║██║░░╚██╗░╚═══██╗██╔══██╗██╔══╝░░██║╚██╔╝██║██║░░██║░╚████╔╝░██╔══╝░░██╔══██╗
# ███████╗╚█████╔╝╚██████╔╝██████╔╝██║░░██║███████╗██║░╚═╝░██║╚█████╔╝░░╚██╔╝░░███████╗██║░░██║
# ╚══════╝░╚════╝░░╚═════╝░╚═════╝░╚═╝░░╚═╝╚══════╝╚═╝░░░░░╚═╝░╚════╝░░░░╚═╝░░░╚══════╝╚═╝░░╚═╝
#
# Contact dev(discord): groundbreakingmc
# Any suggestions or wanna report any error, use: https://github.com/groundbreakingmc/LogsRemover/issues

# Specify here how old does your log file have to be, to be deleted
# (if you don't write the letter, it will be counted as hours)
# m = month | w = week | d = day | h = hours
# Examples:
# If you want to delete files older than 1 month and 2 weeks, specify -> 1m 2w
# If you want to delete files older than 1 week and 3 days, specify -> 1w 3d
# If you want to delete files older than 1 month, 1 week, 3 days and 2 hours, specify -> 1m 1w 3d 2h
delete-if-older-then: 1w

# Specify here your logs directory
logs-directory: 'logs'

# Updates checker
updates:
  # Does plugin need to check for updates?
  check: true
  # Does plugin need to download new version if possible?
  auto-update: false

# don't touch it
config-version: 1.0

no-permission: "§cSorry, but you don't have permission to execute this command."```
