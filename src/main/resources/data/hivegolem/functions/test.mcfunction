particle minecraft:dust 1 0.75 0 1 ~ ~ ~ 0 0 0 0 1 force
scoreboard players set @s ariibulundu 1
execute as @e[dx=0,type=bee] positioned ~-0.7 ~-0.7 ~-0.7 if entity @s[dx=0] positioned ~0.7 ~0.7 ~0.7 run function hivegolem:bee_action_two
execute if score @s ariibulundu matches 1 if entity @s[distance=..6] positioned ^ ^ ^0.5 if block ~ ~ ~ #hivegolem:non_solid run function hivegolem:test