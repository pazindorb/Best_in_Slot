package pl.bloniarz.bis.model.dao.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "STATISTICS_EQUATIONS")
public class StatsEquationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equation_id")
    private long id;

    private String name;

    private double x0;
    private double x1;
    private double x2;
    private double x3;

    @ManyToMany(mappedBy = "stats")
    private List<ItemEntity> item;

}
