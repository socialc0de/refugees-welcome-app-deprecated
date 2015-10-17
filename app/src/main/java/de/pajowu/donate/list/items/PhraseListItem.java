package de.pajowu.donate.list.items;

import java.util.HashMap;

public class PhraseListItem {
    private HashMap<String, String> phrases;

    public PhraseListItem(HashMap<String, String> phs) {
        phrases = phs;

    }

    public HashMap<String, String> getPhrases() {
        return phrases;
    }

    public void setPhrases(HashMap<String, String> phrases) {
        this.phrases = phrases;
    }
}
