package ua.kpi.its.lab.security.controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.SatelliteRequest
import ua.kpi.its.lab.security.dto.SatelliteResponse
import ua.kpi.its.lab.security.svc.SatelliteService
import java.time.Instant


@RestController
@RequestMapping("/satellites")
class SatelliteController @Autowired constructor(
    private val satelliteService: SatelliteService
) {
    /**
     * Gets the list of all satellites
     *
     * @return: List of SatelliteResponse
     */
    @GetMapping(path = ["", "/"])
    fun satellites(): List<SatelliteResponse> = satelliteService.read()

    /**
     * Reads the satellite by its id
     *
     * @param id: id of the satellite
     * @return: SatelliteResponse for the given id
     */
    @GetMapping("{id}")
    fun readSatellite(@PathVariable("id") id: Long): ResponseEntity<SatelliteResponse> {
        return try {
            val satellite = satelliteService.readById(id)
            ResponseEntity.ok(satellite)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Creates a new satellite instance
     *
     * @param satellite: SatelliteRequest with set properties
     * @return: SatelliteResponse for the created satellite
     */
    @PostMapping(path = ["", "/"])
    fun createSatellite(@RequestBody satellite: SatelliteRequest): SatelliteResponse {
        return satelliteService.create(satellite)
    }

    /**
     * Updates existing satellite instance
     *
     * @param satellite: SatelliteRequest with properties set
     * @return: SatelliteResponse of the updated satellite
     */
    @PutMapping("{id}")
    fun updateSatellite(
        @PathVariable("id") id: Long,
        @RequestBody satellite: SatelliteRequest
    ): ResponseEntity<SatelliteResponse> {
        return try {
            val updatedSatellite = satelliteService.updateById(id, satellite)
            ResponseEntity.ok(updatedSatellite)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Deletes existing satellite instance
     *
     * @param id: id of the satellite
     * @return: SatelliteResponse of the deleted satellite
     */
    @DeleteMapping("{id}")
    fun deleteSatellite(
        @PathVariable("id") id: Long
    ): ResponseEntity<SatelliteResponse> {
        return try {
            val deletedSatellite = satelliteService.deleteById(id)
            ResponseEntity.ok(deletedSatellite)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
}

@RestController
@RequestMapping("/auth")
class AuthenticationTokenController @Autowired constructor(
    private val encoder: JwtEncoder
) {
    private val authTokenExpiry: Long = 3600L // in seconds

    @PostMapping("token")
    fun token(auth: Authentication): String {
        val now = Instant.now()
        val scope = auth
            .authorities
            .joinToString(" ", transform = GrantedAuthority::getAuthority)
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(authTokenExpiry))
            .subject(auth.name)
            .claim("scope", scope)
            .build()
        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}