scoreboard players set @a[tag=looking_ariii,limit=1] ariibulundu 0
playsound minecraft:block.beehive.enter player @a[distance=..10] ~ ~ ~
power grant @s hivegolem:bees_death
resource change @a[tag=looking_ariii,limit=1] hivegolem:bees_count 1
execute if data entity @s {HasNectar:1b} run resource change @a[tag=looking_ariii,limit=1] hivegolem:bees_honey 200

