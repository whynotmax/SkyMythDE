package de.skymyth.chatfilter.model;

import de.skymyth.punish.model.reason.PunishReason;
import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChatFilterItem {

    @Id
    String word;

    boolean exactMatch;          //If true, the word will be checked exactly
    boolean ignoreCase;          //If true, the word will be checked without case sensitivity
    boolean replaceLeetSpeak;    //If true, the word will be replaced with a normal word

    boolean autoMute;            //If true, the player will be muted
    PunishReason autoMuteReason; //Can be null ONLY if autoMute is false

}
