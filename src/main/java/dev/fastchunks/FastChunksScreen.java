package dev.fastchunks;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.IntConsumer;

/** Экран настроек без внешних зависимостей (только ваниль). */
public class FastChunksScreen extends Screen {
    private static final String[] PROFILES = {"low", "medium", "high", "custom"};
    private final Screen parent;

    public FastChunksScreen(Screen parent) {
        super(Text.literal("FastChunks"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 200, x = this.width / 2 - w / 2, y = 40, step = 24;

        addDrawableChild(ButtonWidget.builder(profileText(), b -> {
            int i = 0;
            for (int k = 0; k < PROFILES.length; k++) if (PROFILES[k].equals(Config.profile)) i = k;
            Config.applyProfile(PROFILES[(i + 1) % PROFILES.length]);
            rebuild();
        }).dimensions(x, y, w, 20).build());
        y += step;

        addDrawableChild(new IntSlider(x, y, w, "Пакетов чанков за тик", 1, 20, Config.packetsPerTick,
                v -> { Config.packetsPerTick = v; Config.profile = "custom"; }));
        y += step;
        addDrawableChild(new IntSlider(x, y, w, "Частиц за тик", 5, 200, Config.particlesPerTick,
                v -> { Config.particlesPerTick = v; Config.profile = "custom"; }));
        y += step;
        addDrawableChild(new IntSlider(x, y, w, "Дистанция сущностей", 16, 160, Config.entityMaxDistance,
                v -> { Config.entityMaxDistance = v; Config.profile = "custom"; }));
        y += step;

        addDrawableChild(toggle(x, y, w, "Растягивать чанки", Config.throttleChunkPackets,
                v -> Config.throttleChunkPackets = v));
        y += step;
        addDrawableChild(toggle(x, y, w, "Лимит частиц", Config.limitParticles,
                v -> Config.limitParticles = v));
        y += step;
        addDrawableChild(toggle(x, y, w, "Скрывать далёкие сущности", Config.cullFarEntities,
                v -> Config.cullFarEntities = v));
        y += step + 6;

        addDrawableChild(ButtonWidget.builder(Text.literal("Готово"), b -> close())
                .dimensions(x, y, w, 20).build());
    }

    private Text profileText() {
        return Text.literal("Профиль: " + Config.profile);
    }

    private void rebuild() {
        this.clearChildren();
        this.init();
    }

    private ButtonWidget toggle(int x, int y, int w, String label, boolean value,
                                java.util.function.Consumer<Boolean> setter) {
        boolean[] state = {value};
        return ButtonWidget.builder(toggleText(label, value), b -> {
            state[0] = !state[0];
            setter.accept(state[0]);
            b.setMessage(toggleText(label, state[0]));
        }).dimensions(x, y, w, 20).build();
    }

    private static Text toggleText(String label, boolean on) {
        return Text.literal(label + ": " + (on ? "Вкл" : "Выкл"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public void close() {
        Config.save();
        if (this.client != null) this.client.setScreen(parent);
    }

    private static final class IntSlider extends SliderWidget {
        private final String label;
        private final int min, max;
        private final IntConsumer setter;

        IntSlider(int x, int y, int w, String label, int min, int max, int value, IntConsumer setter) {
            super(x, y, w, 20, Text.empty(), (value - min) / (double) (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;
            updateMessage();
        }

        private int current() {
            return min + (int) Math.round(this.value * (max - min));
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.literal(label + ": " + current()));
        }

        @Override
        protected void applyValue() {
            setter.accept(current());
        }
    }
}
