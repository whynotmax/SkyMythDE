package de.skymyth.clan;

import de.skymyth.SkyMythPlugin;
import de.skymyth.clan.model.Clan;
import de.skymyth.clan.repository.ClanRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClanManager {

    SkyMythPlugin plugin;
    ClanRepository repository;
    Map<String, Clan> clanMap;

    public ClanManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = this.plugin.getMongoManager().create(ClanRepository.class);
        this.clanMap = this.repository.findAll().stream().collect(Collectors.toMap(clan -> clan.getName().toLowerCase(), clan -> clan));
    }

    public void createClan(UUID uuid, String clanName) {
        Clan clan = new Clan();
        clan.setName(clanName);
        clan.setLeader(uuid);
        clan.setMaxMembers(10);
        clan.setMembers(new ArrayList<>());
        this.repository.save(clan);
        this.clanMap.put(clanName.toLowerCase(), clan);
    }

    public void deleteClan(String clanName) {
        this.repository.delete(this.clanMap.get(clanName));
        this.clanMap.remove(clanName);
    }

    /**
     * Stores the clan directly into mongo
     *
     * @param clan
     */
    public void saveClan(Clan clan) {
        this.repository.save(clan);
    }


    public void deleteClan(Clan clan) {
        this.repository.delete(clan);
        this.clanMap.remove(clan.getName().toLowerCase());
    }


    public boolean existsClan(String clanName) {
        return this.clanMap.get(clanName.toLowerCase()) != null;
    }

    public Clan getClan(UUID uuid) {
        for (Clan value : clanMap.values()) {
            if (value.getLeader() == null) continue;
            if (value.getLeader().equals(uuid) || value.getMembers().contains(uuid)) {
                return value;
            }
        }
        return null;
    }

    public boolean isInClan(UUID uuid) {
        return this.getClan(uuid) != null;
    }


}
