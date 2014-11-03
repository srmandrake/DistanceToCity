package com.devbyrod.distancetocity;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Rodrigo on 10/31/2014.
 */
public class ListAdapter extends ArrayAdapter<Venue> {

    private Context mContext;
    private List<Venue> mVenueList;

    public ListAdapter(List<Venue> venueList, Context context ){

        super( context, R.layout.listview_row, venueList );
        this.mContext = context;
        this.mVenueList = venueList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        VenueHolder venueHolder = new VenueHolder();

        //optimize inflating process
        if( convertView == null ){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate( R.layout.listview_row, null );

            venueHolder.txt_venue_name = (TextView)view.findViewById( R.id.txt_venue_name );
            venueHolder.txt_venue_category = (TextView)view.findViewById( R.id.txt_venue_category );
            venueHolder.txt_venue_address = (TextView)view.findViewById( R.id.txt_venue_address );
            venueHolder.txt_venue_distance = (TextView)view.findViewById( R.id.txt_venue_distance );

            view.setTag( venueHolder );
        }
        else{

            venueHolder = (VenueHolder)view.getTag();
        }

        Venue venue = this.mVenueList.get(position);
        venueHolder.txt_venue_name.setText( venue.getName() );
        venueHolder.txt_venue_category.setText( venue.getCategory() );
        venueHolder.txt_venue_address.setText( venue.getAddress() );
        venueHolder.txt_venue_distance.setText( new DecimalFormat("#.##").format( venue.getDistance() ) + " Km. from you" );

        //return super.getView(position, convertView, parent);
        return view;
    }

    //Private, static class to implement View-Holder pattern
    // to avoid consuming resources after inflating the view on each call
    private static class VenueHolder{

        public TextView txt_venue_name;
        public TextView txt_venue_category;
        public TextView txt_venue_address;
        public TextView txt_venue_distance;
    }
}
