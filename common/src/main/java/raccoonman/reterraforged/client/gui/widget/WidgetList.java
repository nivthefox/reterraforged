package raccoonman.reterraforged.client.gui.widget;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import raccoonman.reterraforged.client.gui.screen.presetconfig.PresetEditorPage;

public class WidgetList<T extends AbstractWidget> extends ContainerObjectSelectionList<WidgetList.Entry<T>> {

    public WidgetList(Minecraft minecraft, int i, int j, int k, int l, int slotHeight) {
        super(minecraft, i, j, k, l, slotHeight);
        
        this.setRenderSelection(false);
    }
    
    public void select(T widget) {
    	for(Entry<T> entry : this.children()) {
    		if(entry.widget.equals(widget)) {
    			this.setSelected(entry);
    			return;
    		}
    	}
    }
    
    public <W extends T> W addWidget(W widget) {
        super.addEntry(new Entry<>(widget));
        return widget;
    }

    @Override
    public int getRowWidth() {
        return this.width - 20;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRowRight();
    }

    @Override
    protected boolean isSelectedItem(int i) {
        return Objects.equals(this.getSelected(), this.children().get(i));
    }

    public static class Entry<T extends AbstractWidget> extends ContainerObjectSelectionList.Entry<Entry<T>> {
        private T widget;

        public Entry(T widget) {
            this.widget = widget;
        }

        public T getWidget() {
        	return this.widget;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.widget);
        }

        @Override
        public void render(PoseStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            int optionWidth = Math.min(396, width);
            int padding = (width - optionWidth) / 2;
            widget.setX(left + padding);
            widget.setY(top);
            widget.visible = true;
            widget.setWidth(optionWidth);
            widget.height = height - 1;
            // TODO: don't do this
            if (widget instanceof PresetEditorPage.Preview) {
                widget.height = (widget.getWidth());
            }
            widget.render(matrixStack, mouseX, mouseY, partialTicks);
        }

		@Override
		public List<? extends NarratableEntry> narratables() {
			return Collections.singletonList(this.widget);
		}
    }
}
