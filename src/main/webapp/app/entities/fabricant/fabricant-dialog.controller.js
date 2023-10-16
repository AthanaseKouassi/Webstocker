(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('FabricantDialogController', FabricantDialogController);

    FabricantDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fabricant', 'Commande', 'Produit'];

    function FabricantDialogController ($scope, $stateParams, $uibModalInstance, entity, Fabricant, Commande, Produit) {
        var vm = this;
        vm.fabricant = entity;
        vm.commandes = Commande.query();
        vm.produits = Produit.query();
        vm.load = function(id) {
            Fabricant.get({id : id}, function(result) {
                vm.fabricant = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:fabricantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.fabricant.id !== null) {
                Fabricant.update(vm.fabricant, onSaveSuccess, onSaveError);
            } else {
                Fabricant.save(vm.fabricant, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
