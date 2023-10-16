(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BailleurDialogController', BailleurDialogController);

    BailleurDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bailleur', 'Commande'];

    function BailleurDialogController ($scope, $stateParams, $uibModalInstance, entity, Bailleur, Commande) {
        var vm = this;
        vm.bailleur = entity;
        vm.commandes = Commande.query();
        vm.load = function(id) {
            Bailleur.get({id : id}, function(result) {
                vm.bailleur = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:bailleurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.bailleur.id !== null) {
                Bailleur.update(vm.bailleur, onSaveSuccess, onSaveError);
            } else {
                Bailleur.save(vm.bailleur, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
