package io.github.padlocks.customorigins.event;

import java.util.Arrays;
import java.util.Collection;

import user11681.anvil.entrypoint.CommonListenerInitializer;

public class CommonListeners implements CommonListenerInitializer {
    @Override
    public Collection<Class<?>> get() {
        return Arrays.asList(
            EntityEventListeners.class
        );
    }
}