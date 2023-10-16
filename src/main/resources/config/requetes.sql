ALTER TABLE `prix` CHANGE `prix_unitaire` `prix_unitaire` DECIMAL(10,2) NOT NULL;


ALTER TABLE `ligne_bon_de_sortie` CHANGE `prix_vente` `prix_vente` DECIMAL(10,2) NULL DEFAULT NULL;

ALTER TABLE `reglement` CHANGE `montant_reglement` `montant_reglement` DECIMAL(10,2) NOT NULL
