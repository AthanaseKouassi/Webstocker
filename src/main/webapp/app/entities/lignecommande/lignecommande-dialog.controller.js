(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignecommandeDialogController', LignecommandeDialogController);

    LignecommandeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lignecommande', 'Commande', 'Produit'];

    function LignecommandeDialogController ($scope, $stateParams, $uibModalInstance, entity, Lignecommande, Commande, Produit) {
        var vm = this;
        vm.lignecommande = entity;
        vm.commandes = Commande.query();
        vm.produits = Produit.query();
        vm.load = function(id) {
            Lignecommande.get({id : id}, function(result) {
                vm.lignecommande = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:lignecommandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lignecommande.id !== null) {
                Lignecommande.update(vm.lignecommande, onSaveSuccess, onSaveError);
            } else {
                Lignecommande.save(vm.lignecommande, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateFabrication = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
