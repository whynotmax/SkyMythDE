package de.skymyth.giveaway.title.part;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TitlePart {

    final char character;
    boolean revealed;

    public static List<TitlePart> createTitleParts(String[] nameParts) {
        List<TitlePart> titleParts = new ArrayList<>();
        for (String namePart : nameParts) {
            titleParts.add(new TitlePart(namePart.charAt(0), false));
        }
        return titleParts;
    }
}
