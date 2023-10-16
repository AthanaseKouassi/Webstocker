package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lignelivraison.
 */
@Entity
@Table(name = "lignelivraison")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignelivraison")
public class Lignelivraison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "quantite_lot_livre", nullable = false)
    private Integer quantiteLotLivre;

    @Column(name = "quantite_carton_lot")
    private Integer quantiteCartonLot;

    @ManyToOne(cascade = CascadeType.ALL)
    private Lot lot;

    @ManyToOne
    private Livraison livraison;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantiteLotLivre() {
        return quantiteLotLivre;
    }

    public void setQuantiteLotLivre(Integer quantiteLotLivre) {
        this.quantiteLotLivre = quantiteLotLivre;
    }

    public Integer getQuantiteCartonLot() {
        return quantiteCartonLot;
    }

    public void setQuantiteCartonLot(Integer quantiteCartonLot) {
        this.quantiteCartonLot = quantiteCartonLot;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lignelivraison lignelivraison = (Lignelivraison) o;
        if(lignelivraison.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lignelivraison.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lignelivraison{" +
            "id=" + id +
            ", quantiteLotLivre='" + quantiteLotLivre + "'" +
            ", quantiteCartonLot='" + quantiteCartonLot + "'" +
            '}';
    }
}
