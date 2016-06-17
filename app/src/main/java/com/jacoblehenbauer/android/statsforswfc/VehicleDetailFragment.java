package com.jacoblehenbauer.android.statsforswfc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A fragment representing a single Vehicle detail screen.
 * This fragment is either contained in a {@link VehicleListActivity}
 * in two-pane mode (on tablets) or a {@link VehicleDetailActivity}
 * on handsets.
 */
public class VehicleDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "id";

    /**
     * The vehicle this fragment is presenting.
     */
    private VehicleListActivity.Vehicle vehicle;
    public static VehicleListActivity.Vehicle passedVehicle;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VehicleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the vehicle content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            vehicle = VehicleListActivity.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            passedVehicle = VehicleListActivity.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            onAttach(getActivity());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(vehicle.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vehicle_detail, container, false);

        /**
         * Get preference value for downloading images via networks
         */
        SharedPreferences dataPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        Boolean downloadImages = dataPref.getBoolean("imagesEnabled", true);

        // Show the vehicle content as text in a TextView.
        if(downloadImages) {
            if (vehicle != null && vehicle.image == null) {
                ((TextView) rootView.findViewById(R.id.vehicle_detail)).setText("Image not available." + "\n \n" + vehicle.toString());
            }
            else if (vehicle != null) {
                Picasso.with(getContext())
                        .load(vehicle.image)
                        .resize(440, 600)
                        .into((ImageView) rootView.findViewById(R.id.vehicle_image));

                //show attack pattern
                if (vehicle.atkPattern.equals("Anti-Armor")) {
                    Picasso.with(getContext())
                            .load(R.drawable.antiarmor)
                            .resize(400, 400)
                            .into((ImageView) rootView.findViewById(R.id.atk_ptrn_image));
                } else if (vehicle.atkPattern.equals("1-on-1")) {
                    Picasso.with(getContext())
                            .load(R.drawable.one_on_one)
                            .resize(400, 400)
                            .into((ImageView) rootView.findViewById(R.id.atk_ptrn_image));
                } else if (vehicle.atkPattern.equals("Cross")) {
                    Picasso.with(getContext())
                            .load(R.drawable.cross)
                            .resize(400, 400)
                            .into((ImageView) rootView.findViewById(R.id.atk_ptrn_image));
                } else if (vehicle.atkPattern.equals("Sweep")) {
                    Picasso.with(getContext())
                            .load(R.drawable.sweep)
                            .resize(400, 400)
                            .into((ImageView) rootView.findViewById(R.id.atk_ptrn_image));
                } else if (vehicle.atkPattern.equals("Semi-Circle")) {
                    Picasso.with(getContext())
                            .load(R.drawable.semicircle)
                            .resize(400, 400)
                            .into((ImageView) rootView.findViewById(R.id.atk_ptrn_image));
                } else if (vehicle.atkPattern.equals("Pierce") && vehicle.xsize == 2) {
                    Picasso.with(getContext())
                            .load(R.drawable.pierce2)
                            .resize(400, 400)
                            .into((ImageView) rootView.findViewById(R.id.atk_ptrn_image));
                } else if (vehicle.atkPattern.equals("Pierce") && vehicle.xsize == 3) {
                    Picasso.with(getContext())
                            .load(R.drawable.pierce3)
                            .resize(400, 400)
                            .into((ImageView) rootView.findViewById(R.id.atk_ptrn_image));
                }
                //Print the rest of the vehicle card details
                ((TextView) rootView.findViewById(R.id.vehicle_detail)).setText(vehicle.toString());
            }
        }
        else //Print only the vehicle card details
            ((TextView) rootView.findViewById(R.id.vehicle_detail)).setText(vehicle.toString());

        return rootView;
    }

    public static VehicleListActivity.Vehicle getVehicle(){ return passedVehicle; }

    public interface OnDataPass{
        public void onDataPass(VehicleListActivity.Vehicle passedVehicle);
    }
}
