package dev.fastchunks;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Конфиг: config/fastchunks.properties. profile = low | medium | high | custom */
public final class Config {
    public static String profile = "medium";
    public static int packetsPerTick = 4;
    public static int particlesPerTick = 40;
    public static int entityMaxDistance = 64;
    public static boolean throttleChunkPackets = true;
    public static boolean limitParticles = true;
    public static boolean cullFarEntities = true;

    private Config() {}

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("fastchunks.properties");
    }

    /** Применяет числа профиля (для custom ничего не меняет). */
    public static void applyProfile(String name) {
        profile = name;
        switch (name) {
            case "low" -> { packetsPerTick = 2; particlesPerTick = 15; entityMaxDistance = 40; }
            case "medium" -> { packetsPerTick = 4; particlesPerTick = 40; entityMaxDistance = 64; }
            case "high" -> { packetsPerTick = 8; particlesPerTick = 80; entityMaxDistance = 96; }
            default -> { }
        }
    }

    public static void load() {
        Properties props = new Properties();
        if (Files.exists(file())) {
            try (InputStream in = Files.newInputStream(file())) { props.load(in); } catch (IOException ignored) {}
        }
        String p = props.getProperty("profile", "medium").trim().toLowerCase();
        if (!p.equals("low") && !p.equals("medium") && !p.equals("high") && !p.equals("custom")) p = "medium";
        applyProfile(p);
        packetsPerTick = geti(props, "packetsPerTick", packetsPerTick);
        particlesPerTick = geti(props, "particlesPerTick", particlesPerTick);
        entityMaxDistance = geti(props, "entityMaxDistance", entityMaxDistance);
        throttleChunkPackets = Boolean.parseBoolean(props.getProperty("throttleChunkPackets", "true"));
        limitParticles = Boolean.parseBoolean(props.getProperty("limitParticles", "true"));
        cullFarEntities = Boolean.parseBoolean(props.getProperty("cullFarEntities", "true"));
        save();
    }

    public static void save() {
        Properties out = new Properties();
        out.setProperty("profile", profile);
        out.setProperty("packetsPerTick", String.valueOf(packetsPerTick));
        out.setProperty("particlesPerTick", String.valueOf(particlesPerTick));
        out.setProperty("entityMaxDistance", String.valueOf(entityMaxDistance));
        out.setProperty("throttleChunkPackets", String.valueOf(throttleChunkPackets));
        out.setProperty("limitParticles", String.valueOf(limitParticles));
        out.setProperty("cullFarEntities", String.valueOf(cullFarEntities));
        try (OutputStream os = Files.newOutputStream(file())) {
            out.store(os, "FastChunks. profile: low|medium|high|custom");
        } catch (IOException ignored) {}
    }

    private static int geti(Properties p, String k, int def) {
        try { return Integer.parseInt(p.getProperty(k, String.valueOf(def)).trim()); }
        catch (NumberFormatException e) { return def; }
    }
}
