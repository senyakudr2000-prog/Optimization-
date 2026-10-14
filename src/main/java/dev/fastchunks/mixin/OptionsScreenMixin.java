package dev.fastchunks.mixin;

import dev.fastchunks.FastChunksScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Добавляет кнопку FastChunks в левый верхний угол меню «Настройки». */
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) { super(title); }

    @Inject(method = "init", at = @At("TAIL"), require = 0)
    private void fc$addButton(CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("FastChunks"),
                b -> this.client.setScreen(new FastChunksScreen(self)))
                .dimensions(6, 6, 90, 20).build());
    }
}
