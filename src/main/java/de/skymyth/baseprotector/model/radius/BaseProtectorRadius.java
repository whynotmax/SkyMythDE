package de.skymyth.baseprotector.model.radius;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BaseProtectorRadius {

    RADIUS_5X5(5),
    RADIUS_10X10(10),
    RADIUS_15X15(15),
    RADIUS_20X20(20);


    long radius;

}
