package unal.edu.co.reto9

data class AcaciasPOI(
    val nombre: String = "",
    val categoria: String = "Sin categoría",
    val direccion: String = "Sin dirección",
    val latitudn: String? = null,
    val longitudw: String? = null,
    val telefono: String = "No disponible"
)