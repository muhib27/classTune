package com.classtune.app.schoolapp.model;

/**
 * Created by BLACK HAT on 18-May-15.
 */
public class SpellingbeeDataModel {


    private int id = 0;
    private String word;
    private String wordTwo = "";
    private String banglaMeaning;
    private String definition;
    private String sentence;
    private String wType;
    private String level;

    public String getLevel() {
        return level.trim();
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getwType() {
        return wType.trim();
    }

    public void setwType(String wType) {
        this.wType = wType;
    }

    public String getSentence() {
        return sentence.trim();
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getDefinition() {
        return definition.trim();
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getBanglaMeaning() {
        return banglaMeaning.trim();
    }

    public void setBanglaMeaning(String banglaMeaning) {
        this.banglaMeaning = banglaMeaning;
    }

    public String getWord() {
        return word.trim();
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordTwo() {
        return wordTwo.trim();
    }

    public void setWordTwo(String wordTwo) {
        this.wordTwo = wordTwo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpellingbeeDataModel(int id, String word, String wordTwo, String banglaMeaning, String definition, String sentence, String wType, String level)
    {
        this.id = id;
        this.word = word;
        this.wordTwo = wordTwo;
        this.banglaMeaning = banglaMeaning;
        this.definition = definition;
        this.sentence = sentence;
        this.wType = wType;
        this.level = level;
    }

    public SpellingbeeDataModel()
    {

    }
}
