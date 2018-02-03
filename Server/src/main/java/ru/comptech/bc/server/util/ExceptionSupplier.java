package ru.comptech.bc.server.util;

public interface ExceptionSupplier<T> {

    T run() throws Exception;
}
