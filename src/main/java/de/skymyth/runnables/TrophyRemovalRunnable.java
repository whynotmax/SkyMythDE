package de.skymyth.runnables;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public record TrophyRemovalRunnable(SkyMythPlugin plugin) implements Runnable {

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (hour != 8 && minute != 0) {
            return;
        }
        if (second >= 10) {
            return;
        }
        List<User> users = new ArrayList<>(plugin.getUserManager().getAllUsers());
        users.forEach(user -> {
            if (user.isWasOnlineToday()) return;
            if (user.getTrophies() > 0) {
                user.setTrophies(user.getTrophies() - 2);
            }
        });
        plugin.getUserManager().saveUsersOnlyToDatabase(users);
    }
}
