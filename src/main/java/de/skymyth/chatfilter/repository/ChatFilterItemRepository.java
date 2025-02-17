package de.skymyth.chatfilter.repository;

import de.skymyth.chatfilter.model.ChatFilterItem;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("chatfilter")
public interface ChatFilterItemRepository extends Repository<ChatFilterItem, String> {
}
