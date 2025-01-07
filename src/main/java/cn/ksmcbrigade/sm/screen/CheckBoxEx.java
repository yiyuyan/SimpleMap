package cn.ksmcbrigade.sm.screen;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class CheckBoxEx extends Checkbox {

    private final Consumer<CheckBoxEx> onPress;

    public CheckBoxEx(int p_93826_, int p_93827_, int p_93828_, int p_93829_, Component p_93830_, boolean p_93831_,Consumer<CheckBoxEx> onPress) {
        super(p_93826_, p_93827_, p_93828_, p_93829_, p_93830_, p_93831_);
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.onPress.accept(this);
    }
}
