## Manhunt 

Manhunt is a Minecraft Fabric mod that lets you play the classic Speedrunner-versus-Hunters challenge with other players in the same world. The runner tries to defeat the Ender Dragon, while the hunters track the runner down and try to stop them.

The mod adds role selection, start controls, a tracking compass, a match timer, spectator tools, and win messages so players can focus on the game instead of managing everything manually.

## Features

### Player roles
Players can be assigned one of three roles:

- **Runner** — tries to survive, reach the End, and defeat the Ender Dragon.
- **Hunter** — tracks the runner and tries to kill them before they finish the game.
- **Spectator** — watches the match without taking part as a runner or hunter.

The Player Selection screen lets an admin assign players to roles. The first player to join the server is made an admin, and the role-management screen is intended for admins.

### Player Selection screen
- Open the Player Selection screen from the pause menu.
- Assign online players as the runner, hunters, or spectators.
- The screen separates players by role so the current setup is easy to check.
- Role selection is locked once the match is running.

### Starting a match
Use `/manhunt start` after choosing a runner and at least one hunter. The command requests the start; the match begins when the runner is at least 15 blocks away from a hunter. This gives the runner a chance to get moving before the chase officially begins.

Players who are still unassigned when the match starts are moved into the spectator role. Players who join while a match is already running and are not the runner or a hunter are also made spectators.

### Tracking compass
Hunters receive the Manhunt compass when the admin runs `/manhunt start`. The compass helps hunters track the runner. The mod also restores a hunter's compass after they respawn if they no longer have one.

### Match HUD and timer
The mod includes a Manhunt HUD with a match timer. The timer tracks the time spent in the match, and the HUD is designed to make the current chase easy to follow.

### Spectator mode and camera controls
Spectators can watch the runner or switch between available hunter viewpoints. They can use the on-screen Previous and Next controls, or the left and right arrow keys. Spectators can also return to their own point of view. The spectator controls are meant to appear only for players assigned to the spectator role.

### Win conditions
- **Runner wins:** the Ender Dragon dies while the Manhunt is active, regardless of who dealt the final blow.
- **Hunters win:** the runner dies during the active match.

When a match ends, the mod stops the game and broadcasts a result message to the players.

### Status and administration
- `/manhunt status` lets players check the current Manhunt status and assigned roles.
- Admin-only controls are used to manage and start or stop a match.
- Each server session keeps its own Manhunt game state.

## Commands

| Command | What it does |
| --- | --- |
| `/manhunt start` | Requests the match to start. The chase begins when the runner is at least 15 blocks away from a hunter. |
| `/manhunt stop` | Stops the current Manhunt. |
| `/manhunt status` | Shows the current match status and roles. |
| `/manhunt runner <player>` | Assigns a player as the runner, if command-based role assignment is enabled in the current build. |
| `/manhunt hunter <player>` | Assigns a player as a hunter, if command-based role assignment is enabled in the current build. |
| `/manhunt admin` | Displays admin information. |

The Player Selection screen is the main way to assign roles. Command availability can depend on the current build.

## How to play

1. Install Fabric Loader and Fabric API for a compatible Minecraft version.
2. Put the Manhunt mod JAR in the Minecraft `mods` folder.
3. Start a world or server with the mod installed.
4. Open the pause menu and select **Player Selection**.
5. Choose one runner and at least one hunter. Assign any viewers as spectators.
6. Run `/manhunt start`.
7. The hunters receive their compasses. The game waits for the runner to get at least 15 blocks away from a hunter, then the match begins.
8. The runner tries to defeat the Ender Dragon. The hunters try to kill the runner first.

## Installation

1. Install **Fabric Loader** for the Minecraft version supported by this build.
2. Install the matching **Fabric API**.
3. Copy the Manhunt `.jar` file into your Minecraft `mods` folder.
4. Launch Minecraft using the Fabric profile.

For multiplayer, make sure the mod and compatible dependencies are installed on the server and on the clients that need its client-side screens and HUD.

## Compatibility

This project is currently configured for:

- **Minecraft Java Edition:** 26.1
- **Fabric Loader:** 0.19.5 or newer
- **Fabric API:** `0.145.1+26.1`
- **Java:** 25 or newer

Use versions that match the Minecraft version shown above. A JAR built for a different Minecraft version may not load correctly.

## Development

The project uses Fabric Loom and Gradle. To build it from the project directory, run:

```bash
./gradlew build
```

On Windows, run:

```bat
.\gradlew.bat build
```

The built mod JAR will be placed in `build/libs`. Use the regular mod JAR, not the `-sources.jar` file.

## Notes

- This README describes the project's intended gameplay and configuration based on the current 26.1 setup.
- If a command or feature behaves differently in your current build, check the implementation in the project before relying on it in a public server.
- Keep a backup before changing Minecraft, Fabric Loader, Fabric API, or Loom versions.

## License

The mod metadata currently declares an MIT-style license. Check the repository's `LICENSE` file for the exact license terms.
"""
