package ua.kpi.its.lab.security.repo

import org.springframework.data.jpa.repository.JpaRepository
import ua.kpi.its.lab.security.entity.Satellite
import ua.kpi.its.lab.security.entity.Processor

interface SatelliteRepository : JpaRepository<Satellite, Long> {

}

interface ProcessorRepository : JpaRepository<Processor, Long> {

}