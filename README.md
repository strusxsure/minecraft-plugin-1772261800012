# SkyRocket Plugin

A Minecraft Spigot/Paper plugin that launches players into the air and prevents fall damage for 5 seconds.

## Features
- `/boom` command launches the player into the air
- 5-second immunity to fall damage after using the command
- Console logging of command usage
- Proper error handling and input validation

## Installation
1. Build the plugin using Maven: `mvn clean package`
2. Copy the generated JAR file from `target/SkyRocket-1.0.jar` to your server's `plugins` folder
3. Restart your server

## Usage
- Players can use `/boom` to launch themselves into the air
- Players are immune to fall damage for 5 seconds after using the command
- Command usage is logged in the server console

## Permissions
- `skyrocket.boom` - Allows use of the /boom command (default: true)

## Building