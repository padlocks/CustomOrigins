package io.github.padlocks.customorigins.event;

import java.util.Arrays;
import java.util.Collection;

import user11681.anvil.entrypoint.CommonEventInitializer;
import user11681.anvil.event.AnvilEvent;

public class CommonEvents implements CommonEventInitializer {
    @Override
    public Collection<Class<? extends AnvilEvent>> get() {
        return Arrays.asList(
            LivingTickEvent.Pre.class,
            LivingTickEvent.Post.class
        );
    }
}
