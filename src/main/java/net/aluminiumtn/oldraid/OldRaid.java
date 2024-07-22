package net.aluminiumtn.oldraid;

import net.fabricmc.api.ModInitializer;

public class OldRaid implements ModInitializer {
    @Override
    public void onInitialize() {
        OldRaidHandler.register();
        System.out.println("OldRaid has been initialized.");
    }
}
