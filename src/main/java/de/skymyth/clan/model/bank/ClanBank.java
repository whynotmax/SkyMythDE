package de.skymyth.clan.model.bank;

import eu.koboo.en2do.repository.entity.Transient;
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
public class ClanBank {

    long balance;
    long lastDeposit;
    long lastWithdraw;

    @Transient
    public boolean canDeposit(long amount) {
        return balance + amount <= 25_000_000;
    }

    @Transient
    public void deposit(long amount) {
        if (!canDeposit(amount)) {
            throw new IllegalArgumentException("The amount is too high");
        }
        balance += amount;
        lastDeposit = System.currentTimeMillis();
    }

    @Transient
    public boolean canWithdraw(long amount) {
        return balance - amount >= 0;
    }

    @Transient
    public void withdraw(long amount) {
        if (!canWithdraw(amount)) {
            throw new IllegalArgumentException("The amount is too high");
        }
        balance -= amount;
        lastWithdraw = System.currentTimeMillis();
    }

}
