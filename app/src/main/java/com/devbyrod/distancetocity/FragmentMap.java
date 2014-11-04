package com.devbyrod.distancetocity;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class FragmentMap extends Fragment {

    private OnFragmentInteractionListener mListener;
    private GoogleMap mGoogleMap;
    private ClusterManager<Venue> mClusterManager;

    public FragmentMap() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        //get the map object from the layout
        mGoogleMap = ( (MapFragment)getFragmentManager().findFragmentById(R.id.map) ).getMap();

        mGoogleMap.setMyLocationEnabled( true );

        //cluster for markers
        mClusterManager = new ClusterManager<Venue>( getActivity().getApplicationContext(), mGoogleMap );
        mGoogleMap.setOnCameraChangeListener(mClusterManager);
        //mGoogleMap.setOnMarkerClickListener(mClusterManager);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setLocationInMap( QueriedCity queriedCity, List<Venue> venuesList ){

        if(queriedCity == null){

            return;
        }

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom( new LatLng( queriedCity.lat, queriedCity.lon ), 18 );

        mGoogleMap.moveCamera( location );

        //zoom map to our the searched city
        /*CameraPosition cameraPosition = new CameraPosition.Builder().target( new LatLng( queriedCity.lat, queriedCity.lon ) ).zoom(18).build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        if( venuesList == null ){

            return;
        }

        mClusterManager.addItems( venuesList );    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public QueriedCity getQueriedCity();
    }

}
