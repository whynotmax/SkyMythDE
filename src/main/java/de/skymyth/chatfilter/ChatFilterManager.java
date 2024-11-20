package de.skymyth.chatfilter;

import de.skymyth.SkyMythPlugin;
import de.skymyth.chatfilter.model.ChatFilterItem;
import de.skymyth.chatfilter.repository.ChatFilterItemRepository;
import lombok.Getter;
import org.redisson.PubSubMessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class ChatFilterManager {

    SkyMythPlugin plugin;
    ChatFilterItemRepository repository;

    List<ChatFilterItem> chatFilterItems;

    public ChatFilterManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(ChatFilterItemRepository.class);
        this.chatFilterItems = new ArrayList<>(this.repository.findAll());
    }

    public void updateChatFilterItem(String word, Consumer<ChatFilterItem> consumer) {
        ChatFilterItem chatFilterItem = getChatFilterItem(word);
        if (chatFilterItem == null) {
            return;
        }
        chatFilterItems.remove(chatFilterItem);
        consumer.accept(chatFilterItem);
        chatFilterItems.add(chatFilterItem);
        this.repository.save(chatFilterItem);
    }

    public void addChatFilterItem(ChatFilterItem chatFilterItem) {
        this.chatFilterItems.add(chatFilterItem);
        this.repository.save(chatFilterItem);
    }

    public ChatFilterItem getChatFilterItem(String word) {
        for (ChatFilterItem chatFilterItem : this.chatFilterItems) {
            if (chatFilterItem.getWord().equals(word)) {
                return chatFilterItem;
            }
        }
        return null;
    }

    public boolean isChatFilterItem(String word) {
        for (ChatFilterItem chatFilterItem : this.chatFilterItems) {
            if (chatFilterItem.getWord().equals(word)) {
                return true;
            }
        }
        return false;
    }

    public void removeChatFilterItem(ChatFilterItem chatFilterItem) {
        this.chatFilterItems.remove(chatFilterItem);
        this.repository.delete(chatFilterItem);
    }

    public List<ChatFilterItem> getBlockedContent(String message) {
        List<ChatFilterItem> blockedContent = new ArrayList<>();
        String[] words = message.split(" ");
        for (String word : words) {
            if (containsChatFilterItem(word)) {
                for (ChatFilterItem chatFilterItem : this.chatFilterItems) {
                    if (chatFilterItem.getWord().equals(word)) {
                        blockedContent.add(chatFilterItem);
                    }
                }
            }
        }
        return blockedContent;
    }

    public String replaceBlockedWords(String message, String replacement) {
        String[] words = message.split(" ");
        for (String word : words) {
            if (containsChatFilterItem(word)) {
                message = message.replace(word, replacement.replace("%word%", word));
            }
        }
        return message;
    }

    public boolean checkMessage(String message) {
        String[] words = message.split(" ");
        for (String word : words) {
            if (containsChatFilterItem(word)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsChatFilterItem(String word) {
        boolean contains = false;
        for (ChatFilterItem chatFilterItem : this.chatFilterItems) {
            boolean exactMatch = chatFilterItem.isExactMatch();
            boolean ignoreCase = chatFilterItem.isIgnoreCase();
            boolean replaceLeetSpeak = chatFilterItem.isReplaceLeetSpeak();
            if (exactMatch && replaceLeetSpeak) {
                String withoutLeetSpeak = word.replaceAll("[@]", "a").replaceAll("[3]", "e").replaceAll("[1]", "i").replaceAll("[!]", "i").replaceAll("[|]", "i").replaceAll("[5]", "s").replaceAll("[7]", "t").replaceAll("[8]", "b").replaceAll("[9]", "g").replaceAll("[4]", "a").replaceAll("[6]", "b").replaceAll("[2]", "z");
                if (chatFilterItem.getWord().equals(withoutLeetSpeak)) {
                    contains = true;
                    break;
                }
            }
            if (exactMatch) {
                if (chatFilterItem.getWord().equals(word)) {
                    contains = true;
                    break;
                }
            }
            if (exactMatch && replaceLeetSpeak) {
                String withoutLeetSpeak = word.replaceAll("[@]", "a").replaceAll("[3]", "e").replaceAll("[1]", "i").replaceAll("[!]", "i").replaceAll("[|]", "i").replaceAll("[5]", "s").replaceAll("[7]", "t").replaceAll("[8]", "b").replaceAll("[9]", "g").replaceAll("[4]", "a").replaceAll("[6]", "b").replaceAll("[2]", "z");
                if (chatFilterItem.getWord().equals(withoutLeetSpeak)) {
                    contains = true;
                    break;
                }
            }
            if (exactMatch && ignoreCase) {
                if (chatFilterItem.getWord().equalsIgnoreCase(word)) {
                    contains = true;
                    break;
                }
            }
            if (ignoreCase) {
                if (chatFilterItem.getWord().equalsIgnoreCase(word)) {
                    contains = true;
                    break;
                }
            }
            if (replaceLeetSpeak) {
                String withoutLeetSpeak = word.replaceAll("[@]", "a").replaceAll("[3]", "e").replaceAll("[1]", "i").replaceAll("[!]", "i").replaceAll("[|]", "i").replaceAll("[5]", "s").replaceAll("[7]", "t").replaceAll("[8]", "b").replaceAll("[9]", "g").replaceAll("[4]", "a").replaceAll("[6]", "b").replaceAll("[2]", "z");
                if (chatFilterItem.getWord().equals(withoutLeetSpeak)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }


}
