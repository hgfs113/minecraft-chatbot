package dev.vva;

import dev.vva.api.ApiService;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class VillagerConversationScreen extends Screen {
    private final VillagerEntity villager;
    private final ApiService apiService;

    private TextFieldWidget messageInput;
    private ButtonWidget sendButton;
    private String villagerResponse = "";
    private boolean isWaitingForResponse = false;

    public VillagerConversationScreen(VillagerEntity villager, ApiService apiService) {
        super(Text.literal("Conversation with Villager"));
        this.villager = villager;
        this.apiService = apiService;
    }

    @Override
    protected void init() {
        super.init();

        // Message input field
        this.messageInput = new TextFieldWidget(
                this.textRenderer,
                this.width / 2 - 150,
                this.height / 2 + 20,
                300,
                20,
                Text.literal("Type your message...")
        );
        this.messageInput.setMaxLength(256);
        this.messageInput.setPlaceholder(Text.literal("Type your message..."));
        this.addDrawableChild(messageInput);
        this.addSelectableChild(this.messageInput);
        this.setInitialFocus(this.messageInput);

        // Send button
        this.sendButton = ButtonWidget.builder(
                        Text.literal("Send"),
                        button -> sendMessage()
                )
                .dimensions(this.width / 2 - 50, this.height / 2 + 50, 100, 20)
                .build();
        this.addDrawableChild(this.sendButton);

        // Close button
        ButtonWidget closeButton = ButtonWidget.builder(
                        Text.literal("Close"),
                        button -> this.close()
                )
                .dimensions(this.width / 2 - 50, this.height / 2 + 80, 100, 20)
                .build();
        this.addDrawableChild(closeButton);
    }

    private void sendMessage() {
        String message = this.messageInput.getText().trim();
        if (message.isEmpty() || isWaitingForResponse) {
            return;
        }

        this.isWaitingForResponse = true;
        this.sendButton.active = false;
        this.villagerResponse = "Thinking...";

        // Clear input
        this.messageInput.setText("");

        // Call API in a separate thread to avoid blocking UI
        new Thread(() -> {
            try {
                String response = apiService.stubSend(message);
                // Update UI on main thread
                this.client.execute(() -> {
                    this.villagerResponse = response;
                    this.isWaitingForResponse = false;
                    this.sendButton.active = true;
                });
            } catch (Exception e) {
                this.client.execute(() -> {
                    this.villagerResponse = "Error: Could not get response";
                    this.isWaitingForResponse = false;
                    this.sendButton.active = true;
                });
            }
        }).start();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // Title
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                this.height / 2 - 60,
                0xFFFFFF
        );

        // Villager name
        Text villagerName = Text.literal("Villager").formatted(Formatting.YELLOW);
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                villagerName,
                this.width / 2,
                this.height / 2 - 40,
                0xFFFFFF
        );

        // Villager response area
        if (!villagerResponse.isEmpty()) {
            // Response background
            context.fill(
                    this.width / 2 - 150,
                    this.height / 2 - 20,
                    this.width / 2 + 150,
                    this.height / 2 + 10,
                    0x88000000
            );

            // Response text (word wrap for long responses)
            Text responseText = Text.literal(villagerResponse).formatted(Formatting.WHITE);
            context.drawText(
                    this.textRenderer,
                    responseText,
                    this.width / 2 - 145,
                    this.height / 2 - 15,
                    290,
                    false
//                    0xFFFFFF
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 && this.messageInput.isFocused()) { // Enter key
            sendMessage();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
