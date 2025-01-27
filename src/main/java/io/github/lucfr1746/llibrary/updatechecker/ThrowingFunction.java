package io.github.lucfr1746.llibrary.updatechecker;

@FunctionalInterface
interface ThrowingFunction<T,R,E extends Exception> {
    R apply(T t) throws E;
}
