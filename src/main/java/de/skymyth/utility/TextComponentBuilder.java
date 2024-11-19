package de.skymyth.utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class TextComponentBuilder {

    private final TextComponent textComponent;

    public TextComponentBuilder(@Nonnull final TextComponent textComponent) {
        this.textComponent = textComponent;
    }

    public TextComponentBuilder(@Nonnull final String message) {
        this.textComponent = new TextComponent(message);
    }

    public TextComponentBuilder(@Nonnull final TextComponentBuilder textComponentBuilder) {
        this.textComponent = textComponentBuilder.toTextComponent();
    }

    public TextComponentBuilder(@Nonnull final String message, @Nonnull final ChatColor chatColor) {
        this.textComponent = new TextComponent(message);
        this.textComponent.setColor(chatColor);
    }

    public TextComponentBuilder setColor(@Nonnull final ChatColor chatColor) {
        if (this.textComponent != null)
            this.textComponent.setColor(chatColor);
        return this;
    }

    public TextComponentBuilder setBold(final boolean bold) {
        if (this.textComponent != null)
            this.textComponent.setBold(bold);
        return this;
    }

    public TextComponentBuilder setItalic(final boolean italic) {
        if (this.textComponent != null)
            this.textComponent.setItalic(italic);
        return this;
    }

    public TextComponentBuilder setObfuscated(final boolean obfuscated) {
        if (this.textComponent != null)
            this.textComponent.setObfuscated(obfuscated);
        return this;
    }

    public TextComponentBuilder setStrikethrough(final boolean strikethrough) {
        if (this.textComponent != null)
            this.textComponent.setStrikethrough(strikethrough);
        return this;
    }

    public TextComponentBuilder setUnderlined(final boolean underlined) {
        if (this.textComponent != null)
            this.textComponent.setUnderlined(underlined);
        return this;
    }

    public TextComponentBuilder setClickEvent(@Nonnull final ClickEvent clickEvent) {
        if (this.textComponent != null)
            this.textComponent.setClickEvent(clickEvent);
        return this;
    }

    public TextComponentBuilder setHoverEvent(@Nonnull final HoverEvent hoverEvent) {
        if (this.textComponent != null)
            this.textComponent.setHoverEvent(hoverEvent);
        return this;
    }

    public TextComponentBuilder setInsertion(@Nonnull final String insertion) {
        if (this.textComponent != null)
            this.textComponent.setInsertion(insertion);
        return this;
    }

    public TextComponentBuilder setExtra(@Nonnull final List<BaseComponent> extra) {
        if (this.textComponent != null)
            this.textComponent.setExtra(extra);
        return this;
    }

    public TextComponentBuilder setText(@Nonnull final String text) {
        if (this.textComponent != null)
            this.textComponent.setText(text);
        return this;
    }

    public TextComponentBuilder addExtra(@Nonnull final String text) {
        if (textComponent != null)
            textComponent.addExtra(text);
        return this;
    }

    public TextComponentBuilder addExtra(@Nonnull final BaseComponent component) {
        if (this.textComponent != null)
            this.textComponent.addExtra(component);
        return this;
    }

    public TextComponentBuilder replace(@Nonnull final String regex, final String text) {
        if (this.textComponent != null)
            this.textComponent.setText(this.textComponent.getText().replace(regex, text));
        return this;
    }

    public TextComponent toTextComponent() {
        return this.textComponent;
    }

    public void append(TextComponent textComponent) {
        this.textComponent.addExtra(textComponent);
    }

    public void append(TextComponentBuilder textComponentBuilder) {
        this.textComponent.addExtra(textComponentBuilder.toTextComponent());
    }
}
