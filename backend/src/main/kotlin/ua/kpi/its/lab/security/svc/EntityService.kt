package ua.kpi.its.lab.security.svc

import ua.kpi.its.lab.security.dto.SatelliteRequest
import ua.kpi.its.lab.security.dto.SatelliteResponse

interface SatelliteService {
    /**
     * Creates a new Satellite record.
     *
     * @param satellite: The SatelliteRequest instance to be inserted
     * @return: The recently created SatelliteResponse instance
     */
    fun create(satellite: SatelliteRequest): SatelliteResponse

    /**
     * Reads all created Satellite records.
     *
     * @return: List of created SatelliteResponse records
     */
    fun read(): List<SatelliteResponse>

    /**
     * Reads a Satellite record by its id.
     * The order is determined by the order of creation.
     *
     * @param id: The id of SatelliteRequest record
     * @return: The SatelliteResponse instance at index
     */
    fun readById(id: Long): SatelliteResponse

    /**
     * Updates a SatelliteRequest record data.
     *
     * @param id: The id of the Satellite instance to be updated
     * @param satellite: The SatelliteRequest with new Satellite values
     * @return: The updated SatelliteResponse record
     */
    fun updateById(id: Long, satellite: SatelliteRequest): SatelliteResponse

    /**
     * Deletes a SatelliteRequest record by its index.
     * The order is determined by the order of creation.
     *
     * @param id: The id of Satellite record to delete
     * @return: The deleted SatelliteResponse instance at index
     */
    fun deleteById(id: Long): SatelliteResponse
}