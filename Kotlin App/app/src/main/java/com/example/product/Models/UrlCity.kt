package com.example.product.Models

data class UrlCity (
        val features: List<Feature>
)

data class Feature (
        val properties: Properties,
)


data class Properties (
        val geocoding: PropertiesGeocoding
)

data class PropertiesGeocoding (
        val admin: Admin
)

data class Admin (
        val level4: String,
        val level5: String,
        val level6: String
)