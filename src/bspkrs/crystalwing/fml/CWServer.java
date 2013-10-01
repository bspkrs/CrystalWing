package bspkrs.crystalwing.fml;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import bspkrs.fml.util.ForgePacketHelper;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class CWServer implements IPacketHandler
{
    private static CWServer instance;
    
    public static CWServer instance()
    {
        if (instance == null)
            new CWServer();
        
        return instance;
    }
    
    public CWServer()
    {
        instance = this;
    }
    
    public void onPlayerLoggedIn(Player player)
    {
        PacketDispatcher.sendPacketToPlayer(ForgePacketHelper.createPacket("CrystalWing", 1, TreeCapitatorMod.instance.nbtManager().getPacketArray()), player);
    }
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetType = ForgePacketHelper.readPacketID(data);
        
        if (packetType == 0)
        {
            PacketDispatcher.sendPacketToPlayer(ForgePacketHelper.createPacket("CrystalWing", 0, null), player);
            onPlayerLoggedIn(player);
        }
    }
}
