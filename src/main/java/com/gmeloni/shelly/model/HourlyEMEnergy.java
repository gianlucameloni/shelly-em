package com.gmeloni.shelly.model;

import com.gmeloni.shelly.dto.db.DailyAggregate;
import com.gmeloni.shelly.dto.db.EnergyTotals;
import com.gmeloni.shelly.dto.db.HourlyAggregate;
import com.gmeloni.shelly.dto.db.MonthlyAggregate;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_em_energy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
@NamedNativeQuery(
        name = "SelectHourlyAggregateByYearMonthDay",
        query = """
                select
                    hour(from_timestamp) as hour,
                    grid_energy_in,
                    grid_energy_out,
                    pv_energy_in,
                    pv_energy_out
                from
                    hourly_em_energy
                where
                    year(from_timestamp) = year(to_date(:filterDate,'yyyy-MM-dd')) and
                    month(from_timestamp) = month(to_date(:filterDate,'yyyy-MM-dd')) and
                    day(from_timestamp) = day(to_date(:filterDate,'yyyy-MM-dd'))
                order by
                    hour desc
                """,
        resultSetMapping = "SelectHourlyAggregateByYearMonthDayMapping"
)
@SqlResultSetMapping(
        name = "SelectHourlyAggregateByYearMonthDayMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "hour", type = String.class),
                                @ColumnResult(name = "grid_energy_in", type = Double.class),
                                @ColumnResult(name = "grid_energy_out", type = Double.class),
                                @ColumnResult(name = "pv_energy_in", type = Double.class),
                                @ColumnResult(name = "pv_energy_out", type = Double.class),
                        },
                        targetClass = HourlyAggregate.class
                )
        }
)
@NamedNativeQuery(
        name = "SelectTotalsByYearMonthDay",
        query = """
                select
                    sum(grid_energy_in) as grid_energy_in,
                    sum(grid_energy_out) as grid_energy_out,
                    sum(pv_energy_in) as pv_energy_in,
                    sum(pv_energy_out) as pv_energy_out
                from
                    hourly_em_energy
                where
                    year(from_timestamp) = year(to_date(:filterDate,'yyyy-MM-dd')) and
                    month(from_timestamp) = month(to_date(:filterDate,'yyyy-MM-dd')) and
                    day(from_timestamp) = day(to_date(:filterDate,'yyyy-MM-dd'))
                """,
        resultSetMapping = "SelectTotalsByYearMonthDayMapping"
)
@SqlResultSetMapping(
        name = "SelectTotalsByYearMonthDayMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "grid_energy_in", type = Double.class),
                                @ColumnResult(name = "grid_energy_out", type = Double.class),
                                @ColumnResult(name = "pv_energy_in", type = Double.class),
                                @ColumnResult(name = "pv_energy_out", type = Double.class),
                        },
                        targetClass = EnergyTotals.class
                )
        }
)
@NamedNativeQuery(
        name = "SelectDailyAggregateByYearMonth",
        query = """
                select
                    day(from_timestamp) as day,
                    sum(grid_energy_in) as grid_energy_in,
                    sum(grid_energy_out) as grid_energy_out,
                    sum(pv_energy_in) as pv_energy_in,
                    sum(pv_energy_out) as pv_energy_out
                from
                    hourly_em_energy
                where
                    year(from_timestamp) = year(to_date(:filterDate,'yyyy-MM-dd')) and
                    month(from_timestamp) = month(to_date(:filterDate,'yyyy-MM-dd'))
                group by
                    day(from_timestamp)
                order by
                    day desc
                """,
        resultSetMapping = "SelectDailyAggregateByYearMonthMapping"
)
@SqlResultSetMapping(
        name = "SelectDailyAggregateByYearMonthMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "day", type = String.class),
                                @ColumnResult(name = "grid_energy_in", type = Double.class),
                                @ColumnResult(name = "grid_energy_out", type = Double.class),
                                @ColumnResult(name = "pv_energy_in", type = Double.class),
                                @ColumnResult(name = "pv_energy_out", type = Double.class),
                        },
                        targetClass = DailyAggregate.class
                )
        }
)
@NamedNativeQuery(
        name = "SelectTotalsByYearMonth",
        query = """
                select
                    sum(grid_energy_in) as grid_energy_in,
                    sum(grid_energy_out) as grid_energy_out,
                    sum(pv_energy_in) as pv_energy_in,
                    sum(pv_energy_out) as pv_energy_out
                from
                    hourly_em_energy
                where
                    year(from_timestamp) = year(to_date(:filterDate,'yyyy-MM-dd')) and
                    month(from_timestamp) = month(to_date(:filterDate,'yyyy-MM-dd'))
                """,
        resultSetMapping = "SelectTotalsByYearMonthMapping"
)
@SqlResultSetMapping(
        name = "SelectTotalsByYearMonthMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "grid_energy_in", type = Double.class),
                                @ColumnResult(name = "grid_energy_out", type = Double.class),
                                @ColumnResult(name = "pv_energy_in", type = Double.class),
                                @ColumnResult(name = "pv_energy_out", type = Double.class),
                        },
                        targetClass = EnergyTotals.class
                )
        }
)
@NamedNativeQuery(
        name = "SelectMonthlyAggregateByYear",
        query = """
                select
                    month(from_timestamp) as month,
                    sum(grid_energy_in) as grid_energy_in,
                    sum(grid_energy_out) as grid_energy_out,
                    sum(pv_energy_in) as pv_energy_in,
                    sum(pv_energy_out) as pv_energy_out
                from
                    hourly_em_energy
                where
                    year(from_timestamp) = year(to_date(:filterDate,'yyyy-MM-dd'))
                group by
                    year(from_timestamp), month(from_timestamp)
                order by
                    month desc
                """,
        resultSetMapping = "SelectMonthlyAggregateByYearMapping"
)
@SqlResultSetMapping(
        name = "SelectMonthlyAggregateByYearMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "month", type = String.class),
                                @ColumnResult(name = "grid_energy_in", type = Double.class),
                                @ColumnResult(name = "grid_energy_out", type = Double.class),
                                @ColumnResult(name = "pv_energy_in", type = Double.class),
                                @ColumnResult(name = "pv_energy_out", type = Double.class),
                        },
                        targetClass = MonthlyAggregate.class
                )
        }
)
@NamedNativeQuery(
        name = "SelectTotalsByYear",
        query = """
                select
                    sum(grid_energy_in) as grid_energy_in,
                    sum(grid_energy_out) as grid_energy_out,
                    sum(pv_energy_in) as pv_energy_in,
                    sum(pv_energy_out) as pv_energy_out
                from
                    hourly_em_energy
                where
                    year(from_timestamp) = year(to_date(:filterDate,'yyyy-MM-dd'))
                """,
        resultSetMapping = "SelectTotalsByYearMapping"
)
@SqlResultSetMapping(
        name = "SelectTotalsByYearMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "grid_energy_in", type = Double.class),
                                @ColumnResult(name = "grid_energy_out", type = Double.class),
                                @ColumnResult(name = "pv_energy_in", type = Double.class),
                                @ColumnResult(name = "pv_energy_out", type = Double.class),
                        },
                        targetClass = EnergyTotals.class
                )
        }
)
@NamedNativeQuery(
        name = "SelectTotals",
        query = """
                select
                    sum(grid_energy_in) as grid_energy_in,
                    sum(grid_energy_out) as grid_energy_out,
                    sum(pv_energy_in) as pv_energy_in,
                    sum(pv_energy_out) as pv_energy_out
                from
                    hourly_em_energy
                """,
        resultSetMapping = "SelectTotalsMapping"
)
@SqlResultSetMapping(
        name = "SelectTotalsMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "grid_energy_in", type = Double.class),
                                @ColumnResult(name = "grid_energy_out", type = Double.class),
                                @ColumnResult(name = "pv_energy_in", type = Double.class),
                                @ColumnResult(name = "pv_energy_out", type = Double.class),
                        },
                        targetClass = EnergyTotals.class
                )
        }
)
public class HourlyEMEnergy implements Serializable {
    @Id
    @Column(name = "from_timestamp")
    private LocalDateTime fromTimestamp;
    @Column(name = "to_timestamp")
    private LocalDateTime toTimestamp;
    @Column(name = "grid_energy_in")
    private Double gridEnergyIn;
    @Column(name = "grid_energy_out")
    private Double gridEnergyOut;
    @Column(name = "pv_energy_in")
    private Double pvEnergyIn;
    @Column(name = "pv_energy_out")
    private Double pvEnergyOut;
    @Column(name = "max_grid_voltage")
    private Double maxGridVoltage;
    @Column(name = "min_grid_voltage")
    private Double minGridVoltage;
    @Column(name = "max_pv_voltage")
    private Double maxPvVoltage;
    @Column(name = "min_pv_voltage")
    private Double minPvVoltage;
}
