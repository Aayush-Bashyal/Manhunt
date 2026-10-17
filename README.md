## Manhunt 

A Minecraft Fabric mod for playing Manhunt with friends.

Manhunt brings the classic runner-versus-hunters challenge into Minecraft. One player becomes the Runner and tries to defeat the Ender Dragon, while the Hunters track them down and try to stop them. The mod adds role selection, a tracking compass, match controls, a timer HUD, and spectator features to make setting up a game easier.

---

## About

I made this mod to try building my own Minecraft game mode with Fabric. The idea is to keep the normal Minecraft experience, but add the things needed to run a Manhunt match without having to manage every part manually.

Players can be assigned different roles before the match starts. When the admin starts the game, the Hunters get their compasses, and the match begins after the Runner has moved far enough away.

---

## Features

* Runner, Hunter, and Spectator roles
* Player Selection screen
* Admin controls for managing a match
* Match start command
* 15-block distance condition before the chase begins
* Tracking compass for Hunters
* Compass restored for Hunters after respawning
* Manhunt HUD and match timer
* Spectator camera switching
* Previous and Next spectator controls
* Left and right arrow key controls for spectator POV
* Automatic spectator assignment for unassigned players when the match starts
* Players joining during a match can become spectators
* Runner wins when the Ender Dragon dies during an active match
* Hunters win if the Runner dies
* Match result messages
* Match status command

---

## Roles

### Runner

The Runner's goal is to survive, gather resources, reach the End, and defeat the Ender Dragon before the Hunters catch them.

### Hunters

The Hunters try to find and kill the Runner before the Runner defeats the Ender Dragon. Hunters receive a Manhunt compass when the admin uses the start command. The mod can also restore the compass after a Hunter respawns if they no longer have one.

### Spectators

Spectators can watch the match without playing as the Runner or a Hunter. They can switch the camera between the Runner and Hunters using the spectator controls or the left and right arrow keys.

---

## Player Selection

The Player Selection screen lets the admin set up the match before it begins.

From the screen, players can be assigned as the Runner, Hunters, or Spectators. The interface separates players by role so the setup is easier to check. Role selection is locked once the match is running.

The first player to join the server is assigned as an admin.

---

## Starting a Match

1. Open the Player Selection screen from the pause menu.
2. Choose one Runner and at least one Hunter.
3. Assign any players who want to watch as Spectators.
4. Run `/manhunt start`.
5. The Hunters receive their tracking compasses.
6. The game waits until the Runner is at least 15 blocks away from a Hunter.
7. Once the distance condition is met, the match begins.

Any online players who are still unassigned when the match starts are made Spectators. Players who join during an active match and are not the Runner or a Hunter can also be assigned as Spectators.

---

## Tracking Compass

The Manhunt compass helps the Hunters track the Runner. Hunters receive it when the admin runs `/manhunt start`, before the chase officially begins. If a Hunter respawns without the compass during an active match, the mod can give it back.

---

## HUD and Timer

The Manhunt HUD displays the match timer while a game is running. It is designed to keep the match information visible while players continue playing normally.

---

## Spectator Controls

Spectators can switch between available viewpoints to follow the action.

* Use the on-screen **Previous** and **Next** controls to change the camera target.
* Use the **Left Arrow** and **Right Arrow** keys to switch viewpoints.
* Return to your own point of view when you want to stop watching another player.

These controls are intended for players assigned to the Spectator role.

---

## Win Conditions

* **Runner wins:** The Ender Dragon dies while the Manhunt is active, no matter who dealt the final blow.
* **Hunters win:** The Runner dies during the active match.

The mod ends the match and broadcasts a result message when a win condition is triggered.

---

## Commands

| Command | Description |
| --- | --- |
| `/manhunt start` | Requests the match to start. The chase begins when the Runner is at least 15 blocks away from a Hunter. |
| `/manhunt status` | Shows the current Manhunt status and player roles. |
| `/manhunt stop` | Stops the current match, if available in the current build. |
| `/manhunt admin` | Displays admin information. |

The Player Selection screen is the main way to assign roles. Available commands may depend on the current build.

---

## Minecraft Version

* **Minecraft:** 26.1
* **Mod Loader:** Fabric
* **Fabric Loader:** 0.19.5
* **Fabric API:** 0.145.1+26.1
* **Java:** 25 or newer

Make sure you use the mod build and Fabric dependencies that match your Minecraft version.

---

## Installation

1. Install Fabric Loader for Minecraft 26.1.
2. Install the matching Fabric API.
3. Download the Manhunt mod `.jar` file.
4. Place the `.jar` file in your Minecraft `mods` folder.
5. Launch Minecraft using the Fabric profile.

For multiplayer, install the required mod files on the server and on the clients that need the Manhunt screens and HUD.

---

## Building the Mod

This project uses Gradle and Fabric Loom.

On Windows, open a terminal in the project folder and run:

```bat
.\gradlew.bat build
```

On Linux or macOS, run:

```bash
./gradlew build
```

The built mod JAR will be in `build/libs`. Use the normal mod JAR, not the `-sources.jar` file.

---

## Screenshots

Add screenshots of the Player Selection screen, Manhunt HUD, compass, and spectator controls here.

<img width="1366" height="697" alt="2026-10-01_21 26 06" src="https://github.com/user-attachments/assets/53307f77-acf9-4639-b4d0-357fbd83c300" />
<img width="1366" height="769" alt="2026-10-16_08 47 29" src="https://github.com/user-attachments/assets/f3aa9c4f-dadc-43b3-b6dd-41eb883a2544" />
<img width="1366" height="769" alt="2026-10-16_08 47 31" src="https://github.com/user-attachments/assets/0b246240-0855-4cee-a2f2-f0eeec1cd338" />
<img width="1366" height="769" alt="2026-10-16_08 48 34" src="https://github.com/user-attachments/assets/1b85efe1-fd8f-493d-84f1-cdb955ce0e1f" />
<img width="854" height="480" alt="2026-10-08_20 07 28" src="https://github.com/user-attachments/assets/11853be2-bc17-425a-9775-2c6b2fca8ce8" />


---

## AI Use

I use AI as a helper when I get stuck on coding problems or need help understanding Fabric code and error messages. I work on the mod myself and test changes in Minecraft.

---

## Credits

Made by **Aayush Bashyal**.

This project is an ongoing learning project, and I plan to keep improving it as I learn more about Minecraft modding.
"""
