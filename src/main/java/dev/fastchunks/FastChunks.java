package dev.fastchunks;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastChunks implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("fastchunks");

    @Override
    public void onInitializeClient() {
        Config.load();
        LOG.info("FastChunks loaded: profile={}, packetsPerTick={}, particlesPerTick={}, entityDist={}",
                Config.profile, Config.packetsPerTick, Config.particlesPerTick, Config.entityMaxDistance);
    }
}
