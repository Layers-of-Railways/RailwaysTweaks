package dev.ithundxr.railwaystweaks.mixinsupport;

import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;

import java.util.function.BooleanSupplier;

public class Object2BooleanCache<T> {
    private final int size;
    private final Object2ByteLinkedOpenHashMap<T> cache;

    public Object2BooleanCache(int size) {
        this.size = size;
        this.cache = new CacheMap<>(size, 0.25f);
        this.cache.defaultReturnValue((byte) 127);
    }

    public boolean get(T k, BooleanSupplier compute) {
        byte result = cache.getByte(k);
        if (result == 127) {
            boolean v = compute.getAsBoolean();
            if (cache.size() >= size) {
                cache.removeLastByte();
            }
            cache.putAndMoveToFirst(k, (byte) (v ? 1 : 0));
            return v;
        }
        return result == 1;
    }

    private static class CacheMap<T> extends Object2ByteLinkedOpenHashMap<T> {
        public CacheMap(int size, float loadFactor) {
            super(size, loadFactor);
        }

        @Override
        protected void rehash(int newN) {}
    }
}
