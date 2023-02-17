# Tubes1_Solar-Sailers

## Table of Contents

- [Description](#description)
- [Program Requirements & Installation](#program_requirements_&_Installation)
- [Get Started](#Get_Started)
- [Author](#author)

## Description
This is a bot program based on greedy strategies for Galaxio game from Entelect Challenge 2021.
Greedy strategies used in this program was implemented by decomposing the problem into 10 sub-problems.
The sub-problems then solved by a specific greedy strategy. 
Below are the sub problems and strategies used:

1. Choose State
- Since the problem is solved by decomposing it into sub-problems in the form of action states, 
a certain strategy is needed to select the state where the action will be carried out this tick (time unit in Galaxio). 
- Strategy: Selecting the state with the highest priority score in the current game state. 
Each priority score calculated individually in each corresponding  state.

2. GatherFood
- Strategy: Looking for as much food as possible while heading to an area that has a lot of food and avoiding objects that can harm bots.

3. FireTeleport
- Strategy: Fires a teleporter at the smallest enemy which is smaller than the size of a bot - shooting costs, with offset heading.

4. Teleport
- Strategy: Performs the Teleport action when the teleporter is in the range of an enemy smaller than the bot's current size.

5. TorpedoAttack
- Strategy: Shoot at enemy within a certain distance.

6. UseShield
- Strategy: Activate shield when there are many torpedoes (more than 3) aimed at the bot.

7. Run
- Strategy: Dodge threats within a certain distance.

8. PickUpSupernova
- Strategy: Pick-up supernova within a certain distance radius which is quite close.

9. FireSupernova
- Strategy: Fires towards the location of the biggest enemy.

10. DetonateSupernova
- Strategy: Performs the Detonate action when the supernova is far enough from the bot, is around the biggest enemy, or is about to leave the boundary map.

## Program Requirements & Installation
1. Galaxio's starter-pack
* https://github.com/EntelectChallenge/2021-Galaxio/releases/tag/2021.3.2
2. .NET Core 3.1
* Windows: https://dotnet.microsoft.com/download/dotnet/thank-you/sdk-3.1.407-windows-x64-installer
* Linux: https://docs.microsoft.com/en-us/dotnet/core/install/linux
* MacOS: https://dotnet.microsoft.com/download/dotnet/thank-you/sdk-3.1.407-macos-x64-installer
3. Java
* https://www.oracle.com/id/java/technologies/downloads/

## Get Started

To run the game, first unzip the starter-pack that has been downloaded. Then, do the following steps.
> **Note:** If you are on Unix-based operating systems there is an editable run.sh script included in the starter-pack
1. Run the runner with the following command in the "runner-publish" folder.
  ```markdown
  % dotnet GameRunner.dll
  ```
3. Run the engine with the following command in the "engine-publish" folder.
  ```markdown
  % dotnet Engine.dll
  ```
5. Run logger with the following command in the "logger-publish" folder.
  ```markdown
  % dotnet Logger.dll
  ```
6. Run the bot with the following command in the "reference-bot-publish" folder.
  ```markdown
  % dotnet ReferenceBot.dll // to run reference bot provided by the starter-pack.
  % java -jar “<path_to_jar>” // to run this bot, the jar file is located in target folder in this repository.

  ```

## Author
- Addin Munawwar Yusuf (13521085)
- Puti Nabilla Aidira (13521088)
- Althaaf Khasyi Atisomya (13521130)
