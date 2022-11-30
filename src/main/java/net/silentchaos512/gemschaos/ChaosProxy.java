package net.silentchaos512.gemschaos;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class ChaosProxy {
    public ChaosProxy() {
    }

    @Nullable
    public Player getClientPlayer() {
        return null;
    }

    public static class Client extends ChaosProxy {
        public Client() {
        }

        @Nullable
        @Override
        public Player getClientPlayer() {
            return Minecraft.getInstance().player;
        }
    }

    public static class Server extends ChaosProxy {
        public Server() {
        }
    }
}
