
summon minecraft:bee ~ ~ ~ {Tags:["hivegolems"],CannotEnterHiveTicks:2147483647}
scoreboard players operation #ariiiiii aricount = @s aricount
execute as @e[limit=1,sort=nearest,tag=hivegolems,type=bee] run scoreboard players operation @s aribee = #ariiiiii aricount


