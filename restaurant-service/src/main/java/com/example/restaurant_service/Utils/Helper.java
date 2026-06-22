package com.example.restaurant_service.Utils;

import java.util.function.Consumer;

public final class Helper {

    private Helper() {}

    public static <T> void applyIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}