package bspkrs.crystalwing.fml;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import bspkrs.fml.util.ForgePacketHelper;
import bspkrs.util.BSLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class CWClient implements IPacketHandler
{
    public boolean                   serverDetected;
    private static CWClient instance;
    
    public static CWClient instance()
    {
        if (instance == null)
            new CWClient();
        
        return instance;
    }
    
    public CWClient()
    {
        instance = this;
        serverDetected = false;
    }
    
    public void onClientConnect()
    {
        serverDetected = false;
        PacketDispatcher.sendPacketToServer(ForgePacketHelper.createPacket("TreeCapitator", 0, null));
    }
    
    public void setServerDetected()
    {
        serverDetected = true;
        
        if (serverDetected)
            BSLog.info("TreeCapitator server detected; client-side features enabled.");
    }
    
    public void onServerConfigReceived(NBTTagCompound nbtTCSettings, NBTTagCompound nbtTreeRegistry, NBTTagCompound nbtToolRegistry)
    {   
        
    }
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetType = ForgePacketHelper.readPacketID(data);
        
        if (packetType == 0)
        {
            CWClient.instance().setServerDetected();
        }
        else if (packetType == 1)
        {
            @SuppressWarnings("rawtypes")
            Class[] decodeAs = { NBTTagCompound.class, NBTTagCompound.class, NBTTagCompound.class };
            Object[] packetReadout = ForgePacketHelper.readPacketData(data, decodeAs);
            onServerConfigReceived((NBTTagCompound) packetReadout[0], (NBTTagCompound) packetReadout[1], (NBTTagCompound) packetReadout[2]);
        }
    }
}
