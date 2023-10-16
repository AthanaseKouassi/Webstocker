(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignelivraisonDialogController', LignelivraisonDialogController);

    LignelivraisonDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lignelivraison', 'Lot', 'Livraison'];

    function LignelivraisonDialogController ($scope, $stateParams, $uibModalInstance, entity, Lignelivraison, Lot, Livraison) {
        var vm = this;
        vm.lignelivraison = entity;
        vm.lots = Lot.query();
        vm.livraisons = Livraison.query();
        vm.load = function(id) {
            Lignelivraison.get({id : id}, function(result) {
                vm.lignelivraison = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:lignelivraisonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lignelivraison.id !== null) {
                Lignelivraison.update(vm.lignelivraison, onSaveSuccess, onSaveError);
            } else {
                Lignelivraison.save(vm.lignelivraison, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
