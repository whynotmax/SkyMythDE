package de.skymyth.punish.model.result;

import de.skymyth.punish.model.Punish;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PunishCheckResult {

    boolean punished;
    Punish punish;


}
