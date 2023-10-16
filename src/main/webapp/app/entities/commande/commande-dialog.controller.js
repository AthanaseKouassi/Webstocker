(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CommandeDialogController', CommandeDialogController);

    CommandeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Commande', 'Livraison', 'Lignecommande', 'Bailleur', 'Fabricant', 'Produit'];

    function CommandeDialogController ($scope, $stateParams, $uibModalInstance, entity, Commande, Livraison, Lignecommande, Bailleur, Fabricant, Produit) {
        var vm = this;
        vm.commande = entity;
        vm.livraisons = Livraison.query();
        vm.lignecommandes = Lignecommande.query();
        vm.bailleurs = Bailleur.query();
        vm.fabricants = Fabricant.query();
        vm.produits = Produit.query();
        vm.load = function(id) {
            Commande.get({id : id}, function(result) {
                vm.commande = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:commandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.commande.id !== null) {
                Commande.update(vm.commande, onSaveSuccess, onSaveError);
            } else {
                Commande.save(vm.commande, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateCommande = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
