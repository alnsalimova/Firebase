package ru.mirea.salimovaar.mireaproject.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.salimovaar.mireaproject.R;
import ru.mirea.salimovaar.mireaproject.databinding.FragmentMapBinding;


    public class map extends Fragment {
        private MapView mapView = null;
        boolean isWork;
        private static final int REQUEST_CODE_PERMISSION = 100;
        private Context mContext;

        private FragmentMapBinding binding;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            binding = FragmentMapBinding.inflate(inflater, container, false);
            // Inflate the layout for this fragment
            mContext = inflater.getContext();
            int positionPermissionStatus = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
            int positionPermissionStatus2 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
            if (positionPermissionStatus == PackageManager.PERMISSION_GRANTED
                    && positionPermissionStatus2 == PackageManager.PERMISSION_GRANTED) {
                isWork = true;
            } else {
                // Выполняется запрос к пользователь на получение необходимых разрешений
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);
            }
            mapView = binding.mapView;
            mapView.setZoomRounding(true);
            mapView.setMultiTouchControls(true);

            IMapController mapController = mapView.getController();
            mapController.setZoom(15.0);
            GeoPoint startPoint = new GeoPoint(55.794229, 37.700772);
            mapController.setCenter(startPoint);

            MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new
                    GpsMyLocationProvider(mContext), mapView);
            locationNewOverlay.enableMyLocation();
            mapView.getOverlays().add(locationNewOverlay);

            CompassOverlay compassOverlay = new CompassOverlay(mContext, new
                    InternalCompassOrientationProvider(mContext), mapView);
            compassOverlay.enableCompass();
            mapView.getOverlays().add(compassOverlay);

            //final Context context = this.getApplicationContext();
            final DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
            scaleBarOverlay.setCentred(true);
            scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
            mapView.getOverlays().add(scaleBarOverlay);

            place("МДТ им. Ермоловой. Худ рук - Олег Меньшиков",55.757891, 37.612407);
            place("МХТ им. Чехова. Худ рук - Константин Хабенский",55.760236, 37.612991);
            place("Большой театр. Основан в 1776 году",55.760221, 37.618561);
            return binding.getRoot();
        }
        public void place(String text, Double aLatitude, Double aLongitude){
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(aLatitude, aLongitude));
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    Toast.makeText(mContext,text,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

            });
            mapView.getOverlays().add(marker);

            marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));

            marker.setTitle("Title");
        }

        @Override
        public void onResume() {
            super.onResume();
            Configuration.getInstance().load(mContext,
                    PreferenceManager.getDefaultSharedPreferences(mContext));
            if (mapView != null) {
                mapView.onResume();
            }
        }
        @Override
        public void onPause() {
            super.onPause();
            Configuration.getInstance().save(mContext,

                    PreferenceManager.getDefaultSharedPreferences(mContext));

            if (mapView != null) {
                mapView.onPause();
            }
        }
    }
