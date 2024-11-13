package de.skymyth.clan;

import de.skymyth.SkyMythPlugin;
import de.skymyth.clan.model.Clan;
import de.skymyth.clan.repository.ClanRepository;

import java.util.ArrayList;
import java.util.UUID;

public class ClanManager {

    SkyMythPlugin plugin;
    ClanRepository repository;

    public ClanManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = this.plugin.getMongoManager().create(ClanRepository.class);
    }

    public void createClan(UUID uuid, String clanName) {
        Clan clan = new Clan();
        clan.setClanName(clanName);
        clan.setClanLeader(uuid);
        clan.setMaxMembers(10);
        clan.setMembers(new ArrayList<>());
        this.repository.save(clan);
    }

    public Clan getClanByName(String clanName) {
        return null;
    }

    public Clan getClanByLeader(UUID uuid) {
        return null;
    }

    public boolean existsClan(String clanName) {
        return this.repository.findFirstById(clanName) != null;
    }




}
