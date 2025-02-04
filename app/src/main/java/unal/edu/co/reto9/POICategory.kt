package unal.edu.co.reto9

enum class POICategory(val display: String) {
    ENTRETENIMIENTO("Entretenimiento"),
    COMIDA("Comida"),
    COMPRAS("Compras"),
    HOSPEDAJE("Hospedaje"),
    FLORISTERIAS("Floristerias"),
    DROGUERIAS("Droguerías"),
    COMUNICACIONES("Comunicaciones y Tecnologías"),
    SIN_CATEGORIA("Sin categorizar");
    
    companion object {
        fun fromString(value: String): POICategory {
            return values().find { it.display == value } ?: SIN_CATEGORIA
        }
    }
}