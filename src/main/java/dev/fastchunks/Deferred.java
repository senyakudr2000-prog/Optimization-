package dev.fastchunks;

import java.util.ArrayDeque;

/** Общая очередь отложенных пакетов мира (порядок сохраняется) + счётчики на тик. */
public final class Deferred {
    private static final ArrayDeque<Runnable> QUEUE = new ArrayDeque<>();
    private static int usedThisTick = 0;
    private static int particlesThisTick = 0;
    private static boolean replaying = false;

    private Deferred() {}

    public static boolean isReplaying() { return replaying; }

    /** true = пакет надо отложить (он уже поставлен в очередь). */
    public static boolean deferIfNeeded(Runnable replay) {
        if (!Config.throttleChunkPackets || replaying) return false;
        if (!QUEUE.isEmpty() || usedThisTick >= Config.packetsPerTick) {
            QUEUE.add(replay);
            return true;
        }
        usedThisTick++;
        return false;
    }

    public static boolean allowParticle() {
        if (!Config.limitParticles) return true;
        return ++particlesThisTick <= Config.particlesPerTick;
    }

    /** Вызывается в конце клиентского тика. */
    public static void onTickEnd() {
        usedThisTick = 0;
        particlesThisTick = 0;
        if (QUEUE.isEmpty()) return;
        replaying = true;
        try {
            // при большой очереди чуть ускоряемся, чтобы чанки не висели вечно
            int budget = Config.packetsPerTick + Math.min(QUEUE.size() / 20, Config.packetsPerTick);
            while (budget-- > 0 && !QUEUE.isEmpty()) {
                try { QUEUE.poll().run(); }
                catch (Throwable t) { FastChunks.LOG.warn("Deferred packet failed", t); }
                usedThisTick++;
            }
        } finally {
            replaying = false;
        }
    }

    /** Сброс при выходе из мира. */
    public static void clear() { QUEUE.clear(); }
}
