# L2X9RebootCore
###### Please note that this plugin is meant only for l2x9.me, and I probably won't be providing support

## Features
- Chat Features
    - Player ignoring
    - Message and reply
    - Chat toggling
    - Chat cooldown (configurable)
    - Word blocking (configurable)
    - Link blocking (based on a list of all TLDs)
    - '>' for green chat
- Commands
    - crash | Crash the client of a player (Admin)
    - discord | Send a link to your server's discord server
    - help | Fully configurable help command
    - open | Open the inventory or ender chest of a specified player (Admin)
    - say | Fully configurable say command
    - spawn | Spawn n of the specified entity (Admin)
    - speed | Change your creative flight speed (Admin)
    - uptime | Shows the uptime of the server
    - uuid | Shows the UUID of the player specified (Admin)
    - world | Teleport your self to another dimension (Admin)
- Homes | A simple home plugin
    - sethome | Set a home at your current location
    - home | Teleport to a home you have set before
    - delhome | Delete a home
- Misc
    - Entity per chunk limit | Limit how many of a certain entity can be in a chunk
    - Auto restart | Automatically restart the server once a day
    - Custom join messages
    - Anti nether roof
    - Old School Kill | Allow players to kill themselves with /kill
- Patches
    - BoatFly
    - Damage | Prevent 32k damage
    - DispenserCrash | Prevent the dispenser shulker crash
    - ElytraSpeedLimit | Limit how fast players can go with an elytra
    - EndGateway | Prevent the end gateway vehicle crash
    - EntityCollide | Prevent the minecart collision crash
    - FakePlugins | Prevent .plugins
    - LeverRateLimit | Prevent the redstone dust crash
    - LightLag | Prevent the light lag exploit
    - MapLag | Prevent the map cursor crash exploit
    - PacketPerSecond | Prevent 99% of packet based crash exploits
    - ProjectileCrash | Prevents the snowball in unloaded chunks crash by ray tracing projectiles and preventing them from going into unloaded chunks
    - ProjectileVelocity | Prevent the "32k bow" exploit
    - Redstone | Prevent about 98% of all redstone based lag machines. Demo [here](https://youtu.be/7_H4m-GJtqs)
- RandomSpawn | A basic random spawn that prevents players from spawning in water and lava
- TabList | A basic tablist

All user facing messages from this plugin can be localized to any language supported by minecraft.
This includes the TabList and HelpMessages. The default messages can be found [here](https://github.com/254nm/L2X9RebootCore/blob/master/src/main/resources/localization/en_us.yml)



## How to compile

### Linux
```bash
./gradlew shadowJar
```
### Windows 🤮

```
.\gradlew.bat shadowJar
```
