package net.silentchaos512.gemschaos.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.FMLHandshakeHandler;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.silentchaos512.gems.network.LoginPacket;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.chaosbuff.ChaosBuffManager;

import java.util.Objects;

public final class ChaosNetwork {
    private static final ResourceLocation NAME = ChaosMod.getId("network");

    public static SimpleChannel channel;
    static {
        channel = NetworkRegistry.ChannelBuilder.named(NAME)
                .clientAcceptedVersions(s -> Objects.equals(s, "1"))
                .serverAcceptedVersions(s -> Objects.equals(s, "1"))
                .networkProtocolVersion(() -> "1")
                .simpleChannel();

        channel.messageBuilder(LoginPacket.Reply.class, 1)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(buffer -> new LoginPacket.Reply())
                .encoder((msg, buffer) -> {})
                .consumer(FMLHandshakeHandler.indexFirst((hh, msg, ctx) -> msg.handle(ctx)))
                .add();
        channel.messageBuilder(SyncChaosBuffsPacket.class, 2)
                .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
                .decoder(SyncChaosBuffsPacket::fromBytes)
                .encoder(SyncChaosBuffsPacket::toBytes)
                .markAsLoginPacket()
                .consumer(FMLHandshakeHandler.biConsumerFor((hh, msg, ctx) -> {
                    ChaosBuffManager.handlePacket(msg, ctx);
                    channel.reply(new LoginPacket.Reply(), ctx.get());
                }))
                .add();
    }

    private ChaosNetwork() {}

    public static void init() {}
}
