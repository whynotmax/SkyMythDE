package de.skymyth.baseprotector.model.radius;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BaseProtectorRadius {

    RADIUS_5X5(5, -1),
    RADIUS_10X10(10, 500),
    RADIUS_15X15(15, 1000),
    RADIUS_20X20(20, 2000),
    RADIUS_30X30(30, 4000),
    RADIUS_40X40(40, 8000),
    RADIUS_50X50(50, 10000);


    long radius;
    long price;


}
