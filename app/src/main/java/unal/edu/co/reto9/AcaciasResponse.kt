package unal.edu.co.reto9

data class AcaciasPOI(
    val numero: String = "",
    val tipodeentidad: String? = null,
    val nombre: String = "",
    val categoria: String = "Sin categoría",
    val horarioatencion: String? = null,
    val direccion: String = "Sin dirección",
    val latitudn: String? = null,
    val longitudw: String? = null,
    val telefono: String = "No disponible",
    val whatsapp: String? = null,
    val email: String? = null,
    val propietario: String? = null
)