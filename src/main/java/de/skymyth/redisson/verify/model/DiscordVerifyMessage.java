package de.skymyth.redisson.verify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DiscordVerifyMessage {

    UUID uniqueId;
    String verificationCode;

    boolean completed;
    String discordName;
    long discordId;

}
