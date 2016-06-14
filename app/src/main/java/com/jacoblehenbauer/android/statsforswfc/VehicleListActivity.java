package com.jacoblehenbauer.android.statsforswfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An activity representing a list of Vehicles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VehicleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class VehicleListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    //private EditText filterText = (EditText) findViewById(R.id.search_box);
    //private ListView VehicleList = (ListView) findViewById(R.id.Vehicle_list);
    //ArrayAdapter<String> adapter = null;
    /**
     * An array holding the Vehicles.
     */
    public static final List<Vehicle> ITEMS = new ArrayList<Vehicle>();

    /**
     * A map of Vehicles, by ID.
     */
    public static final Map<String, Vehicle> ITEM_MAP = new HashMap<String, Vehicle>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        /**
         * listView and text items to be used to make the Vehicle list filterable
         */
        //adapter = new ArrayAdapter<String>(this, R.layout.Vehicle_list_content, R.id.name);
        //VehicleList.setAdapter(adapter);
        //filterText.addTextChangedListener(filterTextWatcher);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Filter vehicles using the bar above", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final View recyclerView = findViewById(R.id.vehicle_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.vehicle_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        /**
         * Setup for the buttons to display rare and all vehicles
         */
        Button rareButton = (Button) findViewById(R.id.rareButton);
        assert rareButton != null;
        rareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Vehicle> cards = ITEMS;
                updateVehicleList(true, cards);
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        Button standardButton = (Button) findViewById(R.id.standardButton);
        assert standardButton != null;
        standardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Vehicle> cards = ITEMS;
                updateVehicleList(false, cards);
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

        Button resetButton = (Button) findViewById(R.id.resetButton);
        assert resetButton != null;
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshVehicleList();
                ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
            }
        });

    }

    /**
     * Text watcher for the filtering of listView items in the Vehicle List
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


    private void navigateUpFromSameTask(VehicleListActivity VehicleListActivity) {
        Intent intent;
        intent = new Intent(this, TopMenu.class);
        this.navigateUpTo(intent);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        makeVehicleList();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Vehicle> VehicleList;

        public SimpleItemRecyclerViewAdapter(List<Vehicle> items) {
            VehicleList = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vehicle_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = VehicleList.get(position);

            holder.mIdView.setText(VehicleList.get(position).name);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(VehicleDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        VehicleDetailFragment fragment = new VehicleDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.vehicle_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, VehicleDetailActivity.class);
                        intent.putExtra(VehicleDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return VehicleList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public Vehicle mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.vehicle_id);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }
    }

    /**
     * A SWFC Vehicle.
     */
    public static class Vehicle {

        @Trim
        @Parsed(field = "Id")
        public final String id;

        @Trim
        @Parsed(field = "Name")
        public final String name;

        @Trim
        @Parsed(field = "Cost", defaultNullRead = "0")
        public final int cost;

        @Trim
        @Parsed(field = "Build Cost", defaultNullRead = "0")
        public final int buildCost;

        @Trim
        @Parsed(field = "Build Time", defaultNullRead = "0")
        public final int buildTime;

        @Trim
        @Parsed(field = "HP", defaultNullRead = "0")
        public final int hp;

        @Trim
        @Parsed(field = "Image")
        public final String image;

        @Trim
        @Parsed(field = "Acc")
        public final int acc;

        @Trim
        @Parsed(field = "Eva")
        public final int eva;

        @Trim
        @Parsed(field = "l5Atk")
        public final int atk;

        @Trim
        @Parsed(field = "l5Def")
        public final int def;

        @Trim
        @Parsed(field = "xsize")
        public final int xsize;

        @Trim
        @Parsed(field = "ysize")
        public final int ysize;

        @Trim
        @Parsed(field = "Pilots")
        public final int numPilots;

        @Trim
        @Parsed(field = "Copilots")
        public final int numCoPilots;

        @Trim
        @Parsed(field = "Parts")
        public final int numParts;

        @Trim
        @Parsed(field = "numTurns")
        public final String atkFreq;

        @Trim
        @Parsed(field = "Attack Pattern")
        public final String atkPattern;

        @Trim
        @Parsed(field = "isRare")
        public final boolean isRare;

        public Vehicle(String id,
                       String name,
                       int cost,
                       int buildCost,
                       int buildTime,
                       int hp,
                       String image,
                       int acc,
                       int eva,
                       int atk,
                       int def,
                       int xsize,
                       int ysize,
                       int numPilots,
                       int numCoPilots,
                       int numParts,
                       String atkFreq,
                       String atkPattern,
                       boolean isRare){
            this.id = id;
            this.name = name;
            this.cost = cost;
            this.buildCost = buildCost;
            this.buildTime = buildTime;
            this.hp = hp;
            this.image = image;
            this.acc = acc;
            this.eva = eva;
            this.atk = atk;
            this.def = def;
            this.xsize = xsize;
            this.ysize = ysize;
            this.numPilots = numPilots;
            this.numCoPilots = numCoPilots;
            this.numParts = numParts;
            this.atkFreq = atkFreq;
            this.atkPattern = atkPattern;
            this.isRare = isRare;
        }
        public Vehicle(){
            this.id = null;
            this.name = null;
            this.cost = 0;
            this.buildTime = 0;
            this.buildCost = 0;
            this.hp = 0;
            this.image = null;
            this.acc = 0;
            this.eva = 0;
            this.atk = 0;
            this.def = 0;
            this.xsize = 0;
            this.ysize = 0;
            this.numPilots = 0;
            this.numCoPilots = 0;
            this.numParts = 0;
            this.atkFreq = null;
            this.atkPattern = null;
            this.isRare = false;
        }


        @Override
        public String toString() {
            int hours = buildTime / 60; //since both are ints, you get an int
            int min = buildTime % 60;
            String minutes;
            if(min<10){minutes = "0" + min;}
            else minutes = String.valueOf(min);
            String printedBuildTime = hours + ":" + minutes;
            String details = this.name + " "
                    + "\n" + "Attack Frequency: " + atkFreq
                    + "\n" + "Attack Pattern: " + atkPattern
                    + "\n" + "Level 5 Max Attack: " + NumberFormat.getNumberInstance(Locale.US).format(this.atk)
                    + "\n" + "Level 5 Max Defense: " + NumberFormat.getNumberInstance(Locale.US).format(this.def)
                    + "\n" + "Acc: " + String.valueOf(this.acc)
                    + "\n" + "Eva: " + String.valueOf(this.eva)
                    + "\n" + "Cost: " + String.valueOf(this.cost)
                    + "\n" + "X Size: " + String.valueOf(this.xsize)
                    + "\n" + "Y Size: " + String.valueOf(this.ysize)
                    + "\n" + "Pilots: " + String.valueOf(this.numPilots)
                    + "\n" + "Copilots: " + String.valueOf(this.numCoPilots)
                    + "\n" + "Parts: " + String.valueOf(this.numParts)
                    + "\n" + "Build Cost: " + NumberFormat.getNumberInstance(Locale.US).format(this.buildCost) + " credits"
                    + "\n" + "Build Time: " + printedBuildTime;

            return details;
        }
    }

    //add a Vehicle to the array and map of current Vehicles
    private static void addVehicle(Vehicle item) {
        Vehicle vehicle = new Vehicle(item.id,
                item.name,
                item.cost,
                item.buildCost,
                item.buildTime,
                item.hp,
                item.image,
                item.acc,
                item.eva,
                item.atk,
                item.def,
                item.xsize,
                item.ysize,
                item.numPilots,
                item.numCoPilots,
                item.numParts,
                item.atkFreq,
                item.atkPattern,
                item.isRare);
        ITEMS.add(vehicle);
        ITEM_MAP.put(vehicle.id, vehicle);
    }

    //remove a Vehicle to the array of current vehicles for display purposes
    public static void removeVehicle(Vehicle item){
        ITEMS.remove(item);
    }

    private void makeVehicleList() {
        InputStream VehicleFile = null;
        try {
            VehicleFile = this.getAssets().open("SWFC-Vehicle_Stats.csv");
        } catch (IOException e) {
            Toast.makeText(VehicleListActivity.this, "The stats file was not found...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        InputStreamReader VehicleReader;
        VehicleReader = null;
        if (VehicleFile != null) {
            VehicleReader = new InputStreamReader(VehicleFile);
        }


        BeanListProcessor<Vehicle> rowProcessor = new BeanListProcessor<>(Vehicle.class);

        //new CSV Parser settings
        CsvParserSettings settings = new CsvParserSettings();
        settings.setRowProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(true);

        //create the csv parser
        CsvParser parser = new CsvParser(settings);
        parser.parse(VehicleReader);

        //create a list of Vehicles from the processor
        List<Vehicle> Vehicles = rowProcessor.getBeans();
        if (VehicleFile != null) {
            try {
                VehicleFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (VehicleReader != null) {
            try {
                VehicleReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //create a new Vehicle for each Vehicle in the list
        while (!Vehicles.isEmpty()) {
            addVehicle(Vehicles.get(0));
            Vehicles.remove(0);
        }
    }

    public void updateVehicleList(boolean rarity, List<Vehicle> vehicles) {
        if (ITEMS.size() != ITEM_MAP.size()) {
            refreshVehicleList();
        }
        for (int i = vehicles.size() - 1; i >= 0; i--) {
            if (vehicles.get(i).isRare != rarity) {
                removeVehicle(vehicles.get(i));
            }
        }
    }

    public void refreshVehicleList() {
        ITEMS.clear();
        ITEM_MAP.clear();
        makeVehicleList();
    }

}