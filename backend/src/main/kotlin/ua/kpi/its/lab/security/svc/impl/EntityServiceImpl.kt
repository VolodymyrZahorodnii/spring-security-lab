package ua.kpi.its.lab.security.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.security.dto.SatelliteRequest
import ua.kpi.its.lab.security.dto.SatelliteResponse
import ua.kpi.its.lab.security.dto.ProcessorResponse
import ua.kpi.its.lab.security.entity.Processor
import ua.kpi.its.lab.security.entity.Satellite
import ua.kpi.its.lab.security.repo.SatelliteRepository
import ua.kpi.its.lab.security.svc.SatelliteService
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class SatelliteServiceImpl @Autowired constructor(
    private val repository: SatelliteRepository
): SatelliteService {

    override fun create(satellite: SatelliteRequest): SatelliteResponse {
        val processor = satellite.processor
        val newProcessor = Processor(
            name = processor.name,
            manufacturer = processor.manufacturer,
            cores = processor.cores, // Adjusted property name
            frequency = processor.frequency, // Adjusted property name
            socket = processor.socket,
            productionDate = this.stringToDate(processor.productionDate), // Adjusted property name
            mmxSupport = processor.mmxSupport // Adjusted property name
        )
        var newSatellite = Satellite(
            name = satellite.name,
            country = satellite.country,
            launchDate = this.stringToDate(satellite.launchDate),
            purpose = satellite.purpose,
            weight = satellite.weight,
            height = satellite.height,
            isGeostationary = satellite.isGeostationary,
            processor = newProcessor
        )
        newProcessor.satellite = newSatellite
        newSatellite = this.repository.save(newSatellite)
        return satelliteEntityToDto(newSatellite)
    }

    override fun read(): List<SatelliteResponse> {
        return repository.findAll().map { satelliteEntityToDto(it) }
    }

    override fun readById(id: Long): SatelliteResponse {
        val satellite = getSatelliteById(id)
        return satelliteEntityToDto(satellite)
    }

    override fun updateById(id: Long, satellite: SatelliteRequest): SatelliteResponse {
        val oldSatellite = getSatelliteById(id)
        val processor = satellite.processor

        oldSatellite.apply {
            name = satellite.name
            country = satellite.country
            launchDate = stringToDate(satellite.launchDate)
            purpose = satellite.purpose
            weight = satellite.weight
            height = satellite.height
            isGeostationary = satellite.isGeostationary
        }
        oldSatellite.processor.apply {
            name = processor.name
            manufacturer = processor.manufacturer
            cores = processor.cores // Adjusted property name
            frequency = processor.frequency // Adjusted property name
            socket = processor.socket
            productionDate = stringToDate(processor.productionDate) // Adjusted property name
            mmxSupport = processor.mmxSupport // Adjusted property name
        }
        val newSatellite = repository.save(oldSatellite)
        return satelliteEntityToDto(newSatellite)
    }

    override fun deleteById(id: Long): SatelliteResponse {
        val satellite = getSatelliteById(id)
        repository.delete(satellite)
        return satelliteEntityToDto(satellite)
    }

    private fun getSatelliteById(id: Long): Satellite {
        return repository.findById(id).orElseThrow {
            IllegalArgumentException("Satellite not found by id = $id")
        }
    }

    private fun satelliteEntityToDto(satellite: Satellite): SatelliteResponse {
        return SatelliteResponse(
            id = satellite.id,
            name = satellite.name,
            country = satellite.country,
            launchDate = dateToString(satellite.launchDate),
            purpose = satellite.purpose,
            weight = satellite.weight,
            height = satellite.height,
            isGeostationary = satellite.isGeostationary,
            processor = processorEntityToDto(satellite.processor)
        )
    }

    private fun processorEntityToDto(processor: Processor): ProcessorResponse {
        return ProcessorResponse(
            id = processor.id,
            name = processor.name,
            manufacturer = processor.manufacturer,
            cores = processor.cores, // Adjusted property name
            frequency = processor.frequency, // Adjusted property name
            socket = processor.socket,
            productionDate = dateToString(processor.productionDate), // Adjusted property name
            mmxSupport = processor.mmxSupport // Adjusted property name
        )
    }

    private fun dateToString(date: Date): String {
        val instant = date.toInstant()
        val dateTime = instant.atOffset(ZoneOffset.UTC).toLocalDateTime()
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    private fun stringToDate(date: String): Date {
        val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val instant = dateTime.toInstant(ZoneOffset.UTC)
        return Date.from(instant)
    }
}