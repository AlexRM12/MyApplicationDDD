package com.example.myapplicationd

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplicationd.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() ,LocationListener {


    private val CODIGO_PERMISOS_UBICACION_SEGUNDO_PLANO = 2106
    private var haConcedidoPermisos = false
    private var _binding: FragmentFirstBinding? = null
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener() {
            getLocation()
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getLocation() {
        verificarPermisos()
        locationManager = (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager)!!
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION

            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (locationManager!!.getAllProviders().contains("network")) {
            if (isGPSEnabled) {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    5f,
                    this

                )
                //   locationManager.getCurrentLocation(
                //       LocationManager.GPS_PROVIDER,requireContext().mainExecutor,
                Log.i(
                    "",
                    "locationManager!!.getAllProviders().contains()"
                )


            } else {
                Log.i("", "requestLocationUpdates fet")
            }
        } else {
            Log.i("", "requestLocationUpdates error")

        }
    }


    private fun verificarPermisos() {
        val permisos = arrayListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

            )
        val permisosComoArray = permisos.toTypedArray()
        if (tienePermisos(permisosComoArray)) {
            haConcedidoPermisos = true
            // onPermisosConcedidos()
            //   Log.d(Chart.LOG_TAG, "Los permisos ya fueron concedidos")
        } else {
            solicitarPermisos(permisosComoArray)
        }
    }

    private fun solicitarPermisos(permisos: Array<String>) {
        // Log.d(Chart.LOG_TAG, "Solicitando permisos...")
        requestPermissions(
            permisos,
            CODIGO_PERMISOS_UBICACION_SEGUNDO_PLANO
        )
    }

    private fun tienePermisos(permisos: Array<String>): Boolean {
        return permisos.all {
            return ContextCompat.checkSelfPermission(
                requireActivity(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
    binding.textviewFirst.setText(
        location.latitude.toString() +"\n"+
        location.longitude.toString())
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

}

