package com.jacoblehenbauer.android.statsforswfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                Snackbar.make(view, "Search using the bar above", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.vehicle_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.vehicle_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

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
            public final TextView mContentView;
            public Vehicle mItem;

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
    }

    /**
     * A SWFC Vehicle.
     */
    public static class Vehicle {

        @Trim
        public final String id;

        public final String name;

        public final int cost;

        public final int acc;

        public final int atk;

        public final int def;

        public final int xsize;

        public final int ysize;

        public final int numPilots;

        public final int numCoPilots;

        public final int numParts;

        public final int atkFreq;

        public final String atkPattern;

        public final boolean isRare;

        public Vehicle(String id,
                       String name,
                       int cost,
                       int acc,
                       int atk,
                       int def,
                       int xsize,
                       int ysize,
                       int numPilots,
                       int numCoPilots,
                       int numParts,
                       int atkFreq,
                       String atkPattern,
                       boolean isRare){
            this.id = id;
            this.name = name;
            this.cost = cost;
            this.acc = acc;
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
            this.acc = 0;
            this.atk = 0;
            this.def = 0;
            this.xsize = 0;
            this.ysize = 0;
            this.numPilots = 0;
            this.numCoPilots = 0;
            this.numParts = 0;
            this.atkFreq = 0;
            this.atkPattern = null;
            this.isRare = false;
        }


        @Override
        public String toString() {
            String details = this.name + " "
                    + "\n" + "Level 5 Max Attack: " + String.valueOf(this.atk)
                    + "\n" + "Level 5 Max Defense: " + String.valueOf(this.def)
                    + "\n" + "Acc: " + String.valueOf(this.acc)
                    + "\n" + "Cost: " + String.valueOf(this.cost)
                    + "\n" + "X Size: " + String.valueOf(this.xsize)
                    + "\n" + "Y Size: " + String.valueOf(this.ysize)
                    + "\n" + "Pilots: " + String.valueOf(this.numPilots)
                    + "\n" + "Copilots: " + String.valueOf(this.numCoPilots)
                    + "\n" + "Parts: " + String.valueOf(this.numParts)
                    + "\n" + "Attack Frequency: " + String.valueOf(this.atkFreq) + "per turn"
                    + "\n" + "Attack Pattern: " + String.valueOf(this.atkPattern);

            /**
             * if(this.atkPattern == "antiArmor"){
             * //append antiArmor image/text
             * }
             * else(this.atkPattern == "pierce"){
             * //append Pierce image/text
             * }
             *
             */
            return details;
        }
    }

    //add a Vehicle to the array and map of current Vehicles
    private static void addVehicle(Vehicle item) {
        Vehicle Vehicle = new Vehicle();
        ITEMS.add(Vehicle);
        ITEM_MAP.put(Vehicle.id, Vehicle);
    }

    private void makeVehicleList(){
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
        if(VehicleFile != null){VehicleReader = new InputStreamReader(VehicleFile);}


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
        while(!Vehicles.isEmpty()){
            addVehicle(Vehicles.get(0));
            Vehicles.remove(0);
        }
    }

}