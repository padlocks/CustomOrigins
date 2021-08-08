tag @s add hedefff
execute as @e[type=bee,distance=..20] run data merge entity @s {AngerTime:630}
execute as @e[type=bee,distance=..20] run data modify entity @s AngryAt set from entity @e[tag=hedefff,limit=1,sort=nearest] UUID
tag @s remove hedefff