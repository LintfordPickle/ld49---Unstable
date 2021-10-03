LD49 - Unstable
=======================

# Strontium Submergence

## Description

You are a submarine commander, captaining a new top secret nuclear powered submarine. This new power source provides almost unlimited energy, but it is a little *unstable*, and somewhat prone to exploding if shaken too much.

If the --coolant-- reaches cricital levels, you'll almost certinaly die. So don't get shot too often or hit any walls.

In extreme emergencies, you can unleash a wave of energy to clear a path, but this will cause the reactor to go critical.

## TODOS

 * Levels
    * Scripted spawns
    * scrolling background
 * Win / Lose Conditions
 * Hud
    * HP
    * Coolant
 * Title Screen
 * Add explosions
 * Add screenshake
 * Add 'water' explosions and trails
 * Bubbles only when *moving* through water
 * Score
 * ~~Setup a player submarine (AABB)~~
 * ~~Get player movement working~~
    * ~~underwater only~~
    * ~~WASD~~
 * ~~Get player shooting working~~
 * ~~Add enemy Subs~~
 * ~~Add enemy shooting~~
 * Levels (Tiled?)
    * Scrolling
    * ~~Enemy Spawning (basic)~~
 * ~~Collisions~~


IDEAS
=======================

2d Side Scrolling Submarine Schmup
    480x270 (1920x1080 / 4)

Unstable:
Don't bang into walls: 
 -- nuclear powered? bang

-----
Coolant slowly stabilizes over time, but every subsequent knock, bang or bump increases it further. If the coolant becomes too unstable, you lose HP.

The coolant is visible on the ship - starts green and turns red.


------------
Game Components

 - Level editor :(
 - Pathing for enemies
 - Collisions
 -- Circle
 -- AABB

------------
Components:

Player:
 - Submarine
 -- 

Guns:
 - Torpedoes FRONT (underwater)
 - Missiles UP (Surface)
 - Barrel Bombs DOWN (underwater)

Pickups / Upgrades :
 - Health : HP
 - Guns
 - hull : Armor

Badies:
 - submarines
 - boats
 - planes
 - mines

Goals:
 - Score (shmup)
 - Distance travelled (endless runner)