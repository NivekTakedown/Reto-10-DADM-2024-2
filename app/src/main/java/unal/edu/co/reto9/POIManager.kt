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
    private var currentPOIs: List<AcaciasPOI> = emptyList()
    fun fetchPOIs(selectedCategories: Set<String> = emptySet()) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getAcaciasPOIs(APP_TOKEN)
                
                if (response.isSuccessful) {
                    currentPOIs = response.body() ?: emptyList()
                    
                    withContext(Dispatchers.Main) {
                        displayFilteredPOIs(selectedCategories)
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
        phone: String,
        poi: AcaciasPOI
    ) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        
        marker.title = name
        marker.snippet = """
            Categoría: ${category}
            
            Dirección: ${description}
            
            Teléfono: ${phone}
            ${poi.whatsapp?.let { "WhatsApp: $it\n" } ?: ""}
            ${poi.email?.let { "Email: $it\n" } ?: ""}
            ${poi.horarioatencion?.let { "Horario: $it\n" } ?: ""}
            ${poi.propietario?.let { "Propietario: $it" } ?: ""}
        """.trimIndent()
        
        mapView.overlays.add(marker)
    }
    private fun displayFilteredPOIs(selectedCategories: Set<String>) {
        clearExistingMarkers()
        
        val filteredPOIs = if (selectedCategories.isEmpty()) {
            currentPOIs
        } else {
            currentPOIs.filter { poi ->
                selectedCategories.contains(poi.categoria)
            }
        }

        filteredPOIs.forEach { poi ->
            try {
                val lat = poi.latitudn?.toDoubleOrNull()
                val lon = poi.longitudw?.toDoubleOrNull()
                
                if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {
                    addMarker(
                        poi = poi,
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

    fun updateFilters(selectedCategories: Set<String>) {
        displayFilteredPOIs(selectedCategories)
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