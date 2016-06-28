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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Card detail screen.
 * This fragment is either contained in a {@link CardListActivity}
 * in two-pane mode (on tablets) or a {@link CardDetailActivity}
 * on handsets.
 */
public class CardDetailFragment extends Fragment {


    /**
     * Analytics
     */
    private Tracker mTracker;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "id";

    /**
     * The card content this fragment is presenting.
     */
    private CardListActivity.Card card;

    /**
     * Data Passer for communication with CardDetailActivity and data for it to pass
     */
    protected OnDataPass dataPasser;
    protected static CardListActivity.Card passedCard;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the card content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            card = CardListActivity.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            passedCard = CardListActivity.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            onAttach(getActivity());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(card.name + " " + card.getStarsShort());
            }
            //dataPasser.onDataPass(passedCard);


            /**
             * Send Analytics report for the visible Card
             */
            AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
            mTracker = application.getDefaultTracker();
            sendScreenName();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_detail, container, false);

        // Show the card content as text in a TextView.

        /**
         * Get preference value for downloading images via networks
         */
        SharedPreferences dataPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        Boolean downloadImages = dataPref.getBoolean("imagesEnabled", true);

        if (card != null && card.image == null){
            ((TextView) rootView.findViewById(R.id.card_detail)).setText("Image not available." + "\n \n" + card.toString());
        }
        else if (card != null && downloadImages){
            Picasso.with(getContext())
                    .load(card.image)
                    .resize(440,600)
                    .into((ImageView) rootView.findViewById(R.id.card_image));
            ((TextView) rootView.findViewById(R.id.card_detail)).setText(card.toString());
        }
        else //print only the card information
            ((TextView) rootView.findViewById(R.id.card_detail)).setText(card.toString());

        return rootView;
    }

    public static CardListActivity.Card getCard(){
        return passedCard;
    }

    public interface OnDataPass{
        public void onDataPass(CardListActivity.Card passedCard);
    }

    /**
    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        dataPasser = (OnDataPass) a;
    }
    **/

    /**
    * Record a screen view hit for the visible Card Detail Fragment
    */
    private void sendScreenName() {
        if (card != null) {
            // [START screen_view_hit]
            mTracker.setScreenName("Card Detail: " + card.name);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            // [END screen_view_hit]
        }
    }
}
