package project_1.dto;

public class FileDetail {
    private String name;
    private String lang;
    private int lines;

    public FileDetail() {}

    public FileDetail(String name, String lang, int lines) {
        this.name = name;
        this.lang = lang;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }
}