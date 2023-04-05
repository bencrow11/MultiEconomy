package com.bencrow11.multieconomy.command.subcommand;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;

public interface SubCommand {
	public LiteralCommandNode<ServerCommandSource> build();
}
