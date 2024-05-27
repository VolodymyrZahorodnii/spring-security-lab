package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable
data class SatelliteRequest(
    var name: String,
    var country: String,
    var launchDate: String,
    var purpose: String,
    var weight: Double,
    var height: Double,
    var isGeostationary: Boolean,
    var processor: ProcessorRequest
)

@Serializable
data class SatelliteResponse(
    var id: Long,
    var name: String,
    var country: String,
    var launchDate: String,
    var purpose: String,
    var weight: Double,
    var height: Double,
    var isGeostationary: Boolean,
    var processor: ProcessorResponse
)

@Serializable
data class ProcessorRequest(
    var name: String,
    var manufacturer: String,
    var cores: Int,
    var frequency: Double,
    var socket: String,
    var productionDate: String,
    var mmxSupport: Boolean
)

@Serializable
data class ProcessorResponse(
    var id: Long,
    var name: String,
    var manufacturer: String,
    var cores: Int,
    var frequency: Double,
    var socket: String,
    var productionDate: String,
    var mmxSupport: Boolean
)