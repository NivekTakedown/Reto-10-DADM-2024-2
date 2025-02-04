package unal.edu.co.reto9

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class POIManager(private val mapView: MapView) {
    private val APP_TOKEN = "NJiF443Cbj3dTR4pftrJG8AmB"

    fun fetchPOIs() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getAcaciasPOIs(APP_TOKEN)
                
                if (response.isSuccessful) {
                    val pois = response.body() ?: emptyList()
                    
                    withContext(Dispatchers.Main) {
                        clearExistingMarkers()
                        pois.forEach { poi ->
                            try {
                                val lat = poi.latitudn?.toDoubleOrNull()
                                val lon = poi.longitudw?.toDoubleOrNull()
                                
                                if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {
                                    addMarker(
                                        latitude = lat,
                                        longitude = lon,
                                        name = poi.nombre,
                                        description = poi.direccion,
                                        category = poi.categoria,
                                        phone = poi.telefono
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("POIManager", "Error processing POI: ${poi.nombre}", e)
                            }
                        }
                        mapView.invalidate()
                    }
                }
            } catch (e: Exception) {
                Log.e("POIManager", "Error fetching POIs", e)
            }
        }
    }

    private fun addMarker(
        latitude: Double,
        longitude: Double, 
        name: String,
        description: String,
        category: String,
        phone: String
    ) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = name
        marker.snippet = """
            ${category}
            ${description}
            Tel: ${phone}
        """.trimIndent()
        
        marker.infoWindow = CustomInfoWindow(mapView)
        mapView.overlays.add(marker)
    }

    private fun clearExistingMarkers() {
        // Create a list of markers to remove
        val markersToRemove = mapView.overlays.filterIsInstance<Marker>()
        
        // Remove all markers from overlays
        mapView.overlays.removeAll(markersToRemove)
        
        // Refresh the map
        mapView.invalidate()
    }

}