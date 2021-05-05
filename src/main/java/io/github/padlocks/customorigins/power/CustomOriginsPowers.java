package io.github.padlocks.customorigins.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import net.minecraft.util.Identifier;

public class CustomOriginsPowers {
    public static final PowerType<Power> BUMBLE = new PowerTypeReference(
            new Identifier("customorigins", "bumble"));
    public static final PowerType<Power> QUEEN_BEE = new PowerTypeReference(new Identifier("customorigins", "queen_bee"));
    public static final PowerType<Power> SLIPPERY = new PowerTypeReference(new Identifier("customorigins", "slippery"));
}
