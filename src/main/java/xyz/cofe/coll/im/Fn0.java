package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Функция без аргументов
 * @param <RES> результат функции
 */
public interface Fn0<RES> extends Serializable, Supplier<RES> {
    RES apply();
}
