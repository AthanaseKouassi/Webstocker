SELECT bon_de_sortie.type_sortie,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , 
            daate_creation, nom_magasin, nom_produit, SUM(quantite) AS quantite, SUM(prix_vente) AS prix_vente 
            FROM magasin,bon_de_sortie, produit, ligne_bon_de_sortie 
            WHERE magasin.id = bon_de_sortie.magasin_id AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id 
            AND ligne_bon_de_sortie.produit_id = produit.id AND bon_de_sortie.type_sortie = "PROMOTION" 
            AND magasin.nom_magasin = "magasin central" AND bon_de_sortie.daate_creation BETWEEN 2016-07-01  AND 2016-07-31 
            GROUP BY produit.nom_produit


SELECT bon_de_sortie.type_sortie,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , daate_creation, nom_magasin, nom_produit, SUM(quantite) AS quantite, SUM(prix_vente) AS prix_vente FROM magasin,bon_de_sortie, produit, ligne_bon_de_sortie WHERE magasin.id = bon_de_sortie.magasin_id AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id AND ligne_bon_de_sortie.produit_id = produit.id AND bon_de_sortie.type_sortie = ? AND magasin.nom_magasin = ? AND bon_de_sortie.daate_creation BETWEEN ? AND ? GROUP BY produit.nom_produit
