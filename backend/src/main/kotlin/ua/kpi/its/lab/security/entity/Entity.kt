package ua.kpi.its.lab.security.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "satellites")
class Satellite(
    @Column
    var name: String,

    @Column
    var country: String,

    @Column
    var launchDate: Date,

    @Column
    var purpose: String,

    @Column
    var weight: Double,

    @Column
    var height: Double,

    @Column
    var isGeostationary: Boolean,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "processor_id", referencedColumnName = "id")
    var processor: Processor
) : Comparable<Satellite> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1

    override fun compareTo(other: Satellite): Int {
        val equal = this.name == other.name && this.launchDate.time == other.launchDate.time
        return if (equal) 0 else 1
    }

    override fun toString(): String {
        return "Satellite(name=$name, launchDate=$launchDate, processor=$processor)"
    }
}

@Entity
@Table(name = "processors")
class Processor(
    @Column
    var name: String,

    @Column
    var manufacturer: String,

    @Column
    var cores: Int,

    @Column
    var frequency: Double,

    @Column
    var socket: String,

    @Column
    var productionDate: Date,

    @Column
    var mmxSupport: Boolean,

    @OneToOne(mappedBy = "processor")
    var satellite: Satellite? = null
) : Comparable<Processor> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1

    override fun compareTo(other: Processor): Int {
        val equal = this.name == other.name && this.cores == other.cores
        return if (equal) 0 else 1
    }

    override fun toString(): String {
        return "Processor(name=$name, cores=$cores)"
    }
}