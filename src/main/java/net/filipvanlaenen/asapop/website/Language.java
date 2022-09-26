package net.filipvanlaenen.asapop.website;

public enum Language {
    Dutch("nl"), English("en"), Esperanto("eo"), French("fr"), Norwegian("no");

    private String id;

    Language(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
