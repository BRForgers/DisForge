package me.jonatsp.disforge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.server.MinecraftServer;

public class ShrugCommand extends CommandBase {

    @Override
    public String getName() {
        return "shrug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "shrug";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -10;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getCommandSenderEntity() instanceof EntityPlayerMP) {
            ((EntityPlayerMP) sender).connection.processChatMessage(new CPacketChatMessage("¯\\_(ツ)_/¯"));
        }
    }

}
