package utilities.enums;

import java.util.Arrays;

public enum Browser {

    CHROME("Chrome"),
    FIREFOX("Firefox"),
    IE("IE");

    private String browser;

    private Browser(final String browser) {
        this.browser = browser;
    }

    @Override
    public String toString() {
        return this.browser;
    }

    public static Browser getByName(final String name) {
        return Arrays.stream(Browser.values())
                .filter(browser -> name.equalsIgnoreCase(browser.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No browser matches " + name + " input."));
    }
}
