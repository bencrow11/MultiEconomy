package com.bencrow11.multieconomy.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;

public interface SubCommandInterface {
	LiteralCommandNode<ServerCommandSource> build();
}
