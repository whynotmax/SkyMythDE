package de.skymyth.tpa;

import de.skymyth.tpa.model.TeleportRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeleportManager {

    private static final List<TeleportRequest> teleportRequests = new ArrayList<>();

    public static void sendRequest(UUID sender, UUID target, boolean here) {
        teleportRequests.add(new TeleportRequest(sender, target, here, System.currentTimeMillis()));
    }

    public static boolean hasRequest(UUID sender, UUID target) {
        return teleportRequests.stream()
                .anyMatch(teleportRequest -> teleportRequest.getFrom().equals(sender) && teleportRequest.getTo().equals(target));
    }

    public static TeleportRequest getRequest(UUID sender, UUID target) {
        return teleportRequests.stream()
                .filter(teleportRequest -> teleportRequest.getFrom().equals(sender) && teleportRequest.getTo().equals(target))
                .findFirst()
                .orElse(null);
    }

    public static void removeRequest(UUID uniqueId, UUID uniqueId1) {
        teleportRequests.removeIf(teleportRequest -> teleportRequest.getFrom().equals(uniqueId) && teleportRequest.getTo().equals(uniqueId1));
        teleportRequests.removeIf(teleportRequest -> teleportRequest.getFrom().equals(uniqueId1) && teleportRequest.getTo().equals(uniqueId));
    }
}
