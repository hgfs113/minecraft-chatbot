package dev.vva.handlers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vva.Mymod;
import dev.vva.api.ApiService;
import dev.vva.api.TalkRequest;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static dev.vva.handlers.HandlerUtils.buildEnvironmentInfo;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VillagerTalkCommand {

    private static final ApiService apiService = new ApiService();

    private static final String TEMP_CONVERSATION_ID = "001";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("vi_talk")
                        .then(argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    var message = StringArgumentType.getString(context, "message");
                                    var source = context.getSource();

                                    // Log the message to console
                                    Mymod.LOGGER.info("Villager Talk: {}", message);
                                    var env = buildEnvironmentInfo(source);
                                    Mymod.LOGGER.info("Villager Talk env: {}", env);

                                    // Send a feedback message to the player
                                    var request = new TalkRequest(TEMP_CONVERSATION_ID, message, env, "нейтральный");
                                    source.sendFeedback(() -> Text.literal(String.format("Игрок: %s\n", message)), false);
                                    apiService.sendMessage(request, res -> {
                                        Mymod.LOGGER.info("Villager Talk response: {}", res);
                                        source.sendFeedback(() -> Text.literal(String.format("Житель: %s\n", res)), false);
                                    });
                                    return 1;
                                }))
        );
    }
}
