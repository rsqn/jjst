package tech.rsqn.utils.jjst.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread safe and fast none blocking when not write cache util,
 * using {@link ReentrantReadWriteLock} and {@line HashMap}.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class ContentCache<K, V> {

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Map<K, V> cache = new HashMap<>();

    public V get(final K key) {
        try {
            readLock().lock();
            return cache.get(key);
        } finally {
            readLock().unlock();
        }
    }

    public boolean containsKey(final K key) {
        try {
            readLock().lock();
            return cache.containsKey(key);
        } finally {
            readLock().unlock();
        }
    }

    public void put(final K key, final V content) {
        try {
            writeLock().lock();
            cache.put(key, content);
        } finally {
            writeLock().unlock();
        }
    }

    public void remove(final K key) {
        try {
            writeLock().lock();
            cache.remove(key);
        } finally {
            writeLock().unlock();
        }
    }

    public void clear() {
        try {
            writeLock().lock();
            cache.clear();
        } finally {
            writeLock().unlock();
        }
    }

    protected Lock writeLock() {
        return lock.writeLock();
    }

    protected Lock readLock() {
        return lock.readLock();
    }
}
