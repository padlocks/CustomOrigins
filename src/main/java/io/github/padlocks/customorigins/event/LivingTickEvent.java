package io.github.padlocks.customorigins.event;

import java.util.Collection;

import net.minecraft.entity.LivingEntity;
import user11681.anvil.event.AnvilEvent;

public abstract class LivingTickEvent extends LivingEntityEvent {
    public LivingTickEvent(final LivingEntity entity) {
        super(entity);
    }

    public static class Pre extends LivingTickEvent {
        public Pre(final LivingEntity entity) {
            super(entity);
        }
    }

    public static class Post extends LivingTickEvent {
        public Post(final LivingEntity entity) {
            super(entity);
        }
    }
}