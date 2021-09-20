package kz.mtszn.dto;

public enum LocalValue {
    ru("ru"),
    kz("kz");

    private final String language;

    LocalValue(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
