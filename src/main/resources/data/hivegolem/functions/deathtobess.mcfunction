resource set @s hivegolem:bees_armor 0
resource set @s hivegolem:bees_count 1
resource set @s hivegolem:bees_honey 100

scoreboard players operation #ariiiiiip aribee = @s aricount
execute as @e[tag=hivegolems] if score @s aribee = #ariiiiiip aribee run kill @s
