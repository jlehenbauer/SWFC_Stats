package com.jacoblehenbauer.android.statsforswfc;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.jacoblehenbauer.android.statsforswfc.CardListActivity;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class CardContent {

    /**
     * An array holding the cards.
     */
    public static final List<Card> ITEMS = new ArrayList<Card>();

    /**
     * A map of cards, by ID.
     */
    public static final Map<String, Card> ITEM_MAP = new HashMap<String, Card>();

    /**
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createCard(i));
        }
    }

    static{
        //create some sample cards
        addCard(new Card("l1", "Sample Jedi", 5, "Light", 'S', 130, 100, 185, 135, 170, 80, 12, null, "This is a sample Jedi Light card to test the display effects of the objects"));
        addCard(new Card("d1", "Sample Sith", 5, "Dark", 'S', 100, 130, 130, 185, 170, 80, 12, null, "This is a sample Sith Dark card to test the display effects of the objects"));
    }


    //public void createCardContent(String csv_filepath)
     static{
        //read in card items from csv

        InputStream cardFile;
        //find the file to read the cards from and create a reader
        cardFile = null;
        try {
            cardFile = new InputStream(csv_filepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader cardReader;
        cardReader = null;
        if(cardFile != null){cardReader = new InputStreamReader(cardFile);}


        BeanListProcessor<Card> rowProcessor = new BeanListProcessor<>(Card.class);

        //new CSV Parser settings
        CsvParserSettings settings = new CsvParserSettings();
        settings.setRowProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(true);

        //create the csv parser
        CsvParser parser = new CsvParser(settings);
        parser.parse(cardReader);

        //create a list of cards from the processor
        List<Card> cards = rowProcessor.getBeans();
        if (cardFile != null) {
            try {
                cardFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (cardReader != null) {
            try {
                cardReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //create a new Card for each card in the list
        while(!cards.isEmpty()){
            addCard(cards.get(0));
            cards.remove(0);
        }
    }

    //add a card to the array and map of current cards
    private static void addCard(Card item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     *
    private static Card createCard(int position) {
        return new Card();
    }
     **/


    /**
     * A SWFC card.
     */
    public static class Card {
        public final String id;

        @Trim
        @Parsed(field = "Name")
        public final String name;

        @Trim
        @Parsed(field = "Stars")
        public final int stars;

        @Trim
        @Parsed(field = "Alignment")
        public final String alignment;

        @Trim
        @Parsed(field = "Range")
        public final char range;

        @Trim
        @Parsed(field = "ABase")
        public final int dBase;

        @Trim
        @Parsed(field = "ABase")
        public final int aBase;

        @Trim
        @Parsed(field = "DBaseMax")
        public final int dBaseMax;

        @Trim
        @Parsed(field = "ABaseMax")
        public final int aBaseMax;
        public final int d1Max;
        public final int a1Max;
        public final int d2_3max;
        public final int a2_3max;
        public final int d2_4max;
        public final int a2_4max;
        public final int d4_7max;
        public final int a4_7max;
        public final int d8_15max;
        public final int a8_15max;

        @Trim
        @Parsed(field = "Acc")
        public final int acc;

        @Trim
        @Parsed(field = "Eva")
        public final int eva;

        @Trim
        @Parsed(field = "Cost")
        public final int cost;

        @Trim
        @Parsed(field = "Skill")
        public final String skill;

        @Trim
        @Parsed(field = "Details")
        public final String details;

        public Card(String id,
                    String name,
                    int stars,
                    String alignment,
                    char range,
                    int dBase,
                    int aBase,
                    int dBaseMax,
                    int aBaseMax,
                    int acc,
                    int eva,
                    int cost,
                    String skill,
                    String details) {
            this.id = id;
            this.name = name;
            this.stars = stars;
            this.alignment = alignment;
            this.range = range;
            this.dBase = dBase;
            this.aBase = aBase;
            this.dBaseMax = dBaseMax;
            this.aBaseMax = aBaseMax;
            this.d1Max = (int) Math.floor(dBaseMax*1.14);
            this.a1Max = (int) Math.floor(aBaseMax*1.14);
            this.d2_3max = (int) Math.floor(d1Max*1.07 + dBaseMax*1.07);
            this.a2_3max = (int) Math.floor(a1Max*1.07 + aBaseMax*1.07);
            this.d2_4max = (int) Math.floor(d1Max*1.14);
            this.a2_4max = (int) Math.floor(a1Max*1.14);
            this.d4_7max = (int) Math.floor(d2_3max*1.07 + dBaseMax*1.07);
            this.a4_7max = (int) Math.floor(a2_3max*1.07 + aBaseMax*1.07);
            this.d8_15max = (int) Math.floor(d2_4max*1.14);
            this.a8_15max = (int) Math.floor(a2_4max*1.14);
            this.acc = acc;
            this.eva = eva;
            this.cost = cost;
            this.skill = skill;
            this.details = details;
        }

        public Card(){
            this.id = null;
            this.name = null;
            this.stars = 0;
            this.alignment = null;
            this.range = 0;
            this.dBase = 0;
            this.aBase = 0;
            this.dBaseMax = 0;
            this.aBaseMax = 0;
            this.d1Max = (int) Math.floor(dBaseMax*1.14);
            this.a1Max = (int) Math.floor(aBaseMax*1.14);
            this.d2_3max = (int) Math.floor(d1Max*1.07 + dBaseMax*1.07);
            this.a2_3max = (int) Math.floor(a1Max*1.07 + aBaseMax*1.07);
            this.d2_4max = (int) Math.floor(d1Max*1.14);
            this.a2_4max = (int) Math.floor(a1Max*1.14);
            this.d4_7max = (int) Math.floor(d2_3max*1.07 + dBaseMax*1.07);
            this.a4_7max = (int) Math.floor(a2_3max*1.07 + aBaseMax*1.07);
            this.d8_15max = (int) Math.floor(d2_4max*1.14);
            this.a8_15max = (int) Math.floor(a2_4max*1.14);
            this.acc = 0;
            this.eva = 0;
            this.cost = 0;
            this.skill = null;
            this.details = null;
        }

        public String getStars() {
            String numStars = "\u2605 ";
            for (int i = this.stars; i > 1; i--) numStars += "\u2605 ";
            return numStars;
        }


        @Override
        public String toString() {
            String details = this.name + " " + this.getStars()
                    + "\n" + this.alignment
                    + "\n" + "Range: " + String.valueOf(this.range)
                    + "\n" + "Base Defense: " + String.valueOf(this.dBase)
                    + "\n" + "Base Attack: " + String.valueOf(this.aBase)
                    + "\n" + "Max Base Defense: " + String.valueOf(this.dBaseMax)
                    + "\n" + "Max Base Attack: " + String.valueOf(this.aBaseMax)
                    + "\n" + "Evo 1 Max Defense: " + String.valueOf(this.d1Max)
                    + "\n" + "Evo 1 Max Attack: " + String.valueOf(this.a1Max)
                    + "\n" + "4/7 Evo Max Defense: " + String.valueOf(this.d4_7max)
                    + "\n" + "4/7 Evo Max Attack: " + String.valueOf(this.a4_7max)
                    + "\n" + "8/15 Evo Max Defense: " + String.valueOf(this.d8_15max)
                    + "\n" + "8/15 Evo Max Attack: " + String.valueOf(this.a8_15max)
                    + "\n" + "Acc: " + String.valueOf(this.acc)
                    + "\n" + "Eva: " + String.valueOf(this.eva)
                    + "\n" + "Cost: " + String.valueOf(this.cost)
                    + "\n" + "Skill: " + this.skill
                    + "\n" + "Details:" + this.details;
            return details;
        }
    }
}
