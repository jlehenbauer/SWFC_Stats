package com.jacoblehenbauer.android.statsforswfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.univocity.parsers.annotations.NullString;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An activity representing a list of Cards. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CardDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CardListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    //private EditText filterText = (EditText) findViewById(R.id.search_box);
    //private ListView cardList = (ListView) findViewById(R.id.card_list);
    //ArrayAdapter<String> adapter = null;
    /**
     * An array holding the cards.
     */
    public static List<Card> ITEMS = new ArrayList<Card>();

    /**
     * A map of cards, by ID.
     */
    public static Map<String, Card> ITEM_MAP = new HashMap<String, Card>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        /**
         * listView and text items to be used to make the card list filterable
         */
        //adapter = new ArrayAdapter<String>(this, R.layout.card_list_content, R.id.name);
        //cardList.setAdapter(adapter);
        //filterText.addTextChangedListener(filterTextWatcher);


        //populate the default card list
        makeCardList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Search using the bar above", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final View recyclerView = findViewById(R.id.card_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        /**
         * Set up each of the filter buttons,
         * which use the updateCardList to
         * redefine ITEMS and notify the
         * recyclerView adapter.
         */
        Button fiveStarButton = (Button) findViewById(R.id.fiveStarButton);
        assert fiveStarButton != null;
        fiveStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Card> cards = ITEMS;
                updateCardList(5, cards);
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        Button fourStarButton = (Button) findViewById(R.id.fourStarButton);
        assert fourStarButton != null;
        fourStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Card> cards = ITEMS;
                updateCardList(4, cards);
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        Button threeStarButton = (Button) findViewById(R.id.threeStarButton);
        assert threeStarButton != null;
        threeStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Card> cards = ITEMS;
                updateCardList(3, cards);
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        Button twoStarButton = (Button) findViewById(R.id.twoStarButton);
        assert twoStarButton != null;
        twoStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Card> cards = ITEMS;
                updateCardList(2, cards);
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        Button oneStarButton = (Button) findViewById(R.id.oneStarButton);
        assert oneStarButton != null;
        oneStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Card> cards = ITEMS;
                updateCardList(1, cards);
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        Button resetStarsButton = (Button) findViewById(R.id.resetStarsButton);
        assert resetStarsButton != null;
        resetStarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCardList();
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        if (findViewById(R.id.card_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }

    /**
     * Text watcher for the filtering of listView items in the card List
     *
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
        }
    };**/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void navigateUpFromSameTask(CardListActivity cardListActivity) {
        Intent intent;
        intent = new Intent(this, TopMenu.class);
        this.navigateUpTo(intent);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Card> cardList;

        public SimpleItemRecyclerViewAdapter(List<Card> items) {
            cardList = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = cardList.get(position);

            holder.mIdView.setText(cardList.get(position).name);
            holder.mContentView.setText(String.valueOf(cardList.get(position).getStarsShort()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CardDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        CardDetailFragment fragment = new CardDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.card_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CardDetailActivity.class);
                        intent.putExtra(CardDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Card mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.name);
                mContentView = (TextView) view.findViewById(R.id.stars);;
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }

        public void updateData(Map<String, Card> cards){
            ITEM_MAP.clear();
            ITEM_MAP.putAll(cards);
            notifyDataSetChanged();
        }
    }

    /**
     * A SWFC card.
     */
    public static class Card {

        @Trim
        @Parsed(field = "Image", defaultNullRead = "Image not found.")
        public final String image;

        @Trim
        @Parsed(field = "id", defaultNullRead = "00")
        public final String id;

        @Trim
        @Parsed(field = "Name")
        public final String name;

        @Trim
        @Parsed(field = "Stars", defaultNullRead = "1")
        public final int stars;

        @Trim
        @Parsed(field = "Alignment")
        public final String alignment;

        @Trim
        @Parsed(field = "Range", defaultNullRead = "n")
        public final char range;

        @Trim
        @Parsed(field = "DBase", defaultNullRead = "0")
        public final int dBase;

        @Trim
        @Parsed(field = "ABase", defaultNullRead = "0")
        public final int aBase;

        @Trim
        @Parsed(field = "DBaseMax", defaultNullRead = "0")
        public final int dBaseMax;

        @Trim
        @Parsed(field = "ABaseMax", defaultNullRead = "0")
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
        @Parsed(field = "Acc", defaultNullRead = "0")
        public final int acc;

        @Trim
        @Parsed(field = "Eva", defaultNullRead = "0")
        public final int eva;

        @Trim
        @Parsed(field = "Cost", defaultNullRead = "0")
        public final int cost;

        @Trim
        @Parsed(field = "Skill")
        public final String skill;

        @Trim
        @Parsed(field = "Details")
        public final String details;

        public Card(String id,
                    String image,
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
            this.image = image;
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
            this.d2_3max = (int) Math.floor((d1Max*1.07) + (dBaseMax*0.07));
            this.a2_3max = (int) Math.floor((a1Max*1.07) + (aBaseMax*0.07));
            this.d2_4max = (int) Math.floor(d1Max*1.14);
            this.a2_4max = (int) Math.floor(a1Max*1.14);
            this.d4_7max = (int) Math.floor((d2_3max*1.07) + (dBaseMax*0.07));
            this.a4_7max = (int) Math.floor((a2_3max*1.07) + (aBaseMax*0.07));
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
            this.image = null;
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
            this.d2_3max = (int) Math.floor((d1Max*1.07) + (dBaseMax*0.07));
            this.a2_3max = (int) Math.floor((a1Max*1.07) + (aBaseMax*0.07));
            this.d2_4max = (int) Math.floor(d1Max*1.14);
            this.a2_4max = (int) Math.floor(a1Max*1.14);
            this.d4_7max = (int) Math.floor((d2_3max*1.07) + (dBaseMax*0.07));
            this.a4_7max = (int) Math.floor((a2_3max*1.07) + (aBaseMax*0.07));
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

        public String getStarsShort() {
            String numStars = "\u2605 ";
            int i = this.stars;
            numStars = String.valueOf(i) + numStars;
            return numStars;
        }


        @Override
        public String toString() {
            String details = this.name + " "
                    + "\n" + this.getStars()
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

    //add a card to the array and map of current cards
    private static void addCard(Card item) {
        Card card = new Card(item.id,
                item.image,
                item.name,
                item.stars,
                item.alignment,
                item.range,
                item.dBase,
                item.aBase,
                item.dBaseMax,
                item.aBaseMax,
                item.acc,
                item.eva,
                item.cost,
                item.skill,
                item.details);
        ITEMS.add(card);
        ITEM_MAP.put(card.id, card);
    }

    //remove a card from the item map in order to view different sets of cards
    public static void removeCard(Card item){
        ITEMS.remove(item);
    }

    private void makeCardList(){
        InputStream cardFile = null;
        try {
            cardFile = this.getAssets().open("SWFC-Card_Stats.csv");
        } catch (IOException e) {
            Toast.makeText(CardListActivity.this, "The stats file was not found...", Toast.LENGTH_SHORT).show();
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

    public void updateCardList(int filter, List<Card> cards){
        if(ITEMS.size() != ITEM_MAP.size()){
            makeCardList();
        }
        for(int i = cards.size()-1; i > 0; i--){
            if (cards.get(i).stars != filter){
                ITEMS.remove(i);
            }
        }
    }

    public void refreshCardList(){
        ITEMS.clear();
        ITEM_MAP.clear();
        makeCardList();
    }

}
