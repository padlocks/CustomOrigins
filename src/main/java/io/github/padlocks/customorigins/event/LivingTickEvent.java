package io.github.padlocks.customorigins.event;

import net.minecraft.entity.LivingEntity;

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