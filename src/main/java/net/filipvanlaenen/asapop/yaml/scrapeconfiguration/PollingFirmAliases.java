package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

public class PollingFirmAliases {
    private String[] aliases;
    private String name;

    public String[] getAliases() {
        return aliases;
    }

    public String getName() {
        return name;
    }

    public void setAliases(final String[] aliases) {
        this.aliases = aliases;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
