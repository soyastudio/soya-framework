package soya.framework.action.dsl;

import soya.framework.action.ActionClass;

import java.util.*;

public final class Dictionary {

    public static final String[] PREPOSITIONS = {
            "about",
            "above",
            "across",
            "after",
            "against",
            "along",
            "among",
            "around",
            "as",
            "at",
            "before",
            "behind",
            "below",
            "beneath",
            "beside",
            "between",
            "beyond",
            "by",
            "despite",
            "down",
            "during",
            "except",
            "for",
            "from",
            "in",
            "inside",
            "into",
            "like",
            "near",
            "of",
            "off",
            "on",
            "onto",
            "opposite",
            "out",
            "outside",
            "over",
            "past",
            "round",
            "since",
            "than",
            "through",
            "to",
            "toward",
            "under",
            "underneath",
            "unlike",
            "until",
            "up",
            "upon",
            "via",
            "with",
            "within",
            "without"
    };

    private static Set<String> prepositions = new HashSet<>();
    private static Set<String> keywords = new LinkedHashSet<>();
    private static Map<String, String> synonyms = new HashMap<>();

    static {
        Arrays.stream(PREPOSITIONS).forEach(e -> {
            prepositions.add(e.toUpperCase());
        });

        Arrays.stream(Activity.values()).forEach(e -> {
            keywords.add(e.name());
        });

        Arrays.stream(Preposition.values()).forEach(e -> {
            keywords.add(e.name());
        });

        Arrays.stream(ActionClass.actionNames()).forEach(e -> {
            System.out.println("========================= " + e);
        });
    }

    public static String[] getKeywords() {
        return keywords.toArray(new String[keywords.size()]);
    }

    public static String getKeyword(String word) {
        String w = word.toUpperCase();
        if(keywords.contains(w)) {
            return w;

        } else if (synonyms.containsKey(w)) {
            return synonyms.get(w);

        } else {
            return null;
        }
    }

    public static boolean isProposition(String word) {
        return prepositions.contains(word.toUpperCase());
    }

    public static boolean isActivity(String kw) {
        try {
            return Activity.valueOf(kw) != null;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isKeyword(String word) {
        return keywords.contains(word);
    }

    enum Activity {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        TRANSFORM,
        PROCESS,
        SEND
    }

    enum Preposition {
        WITHIN,     // within specific domain
        FROM,       // from source
        TO,         // to destination
        AS,         // as temp name
        FOR,        // for specific type
        ON         // on event
    }
}
