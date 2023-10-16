(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LotDialogController', LotDialogController);

    LotDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lot', 'Produit', 'Lignelivraison'];

    function LotDialogController ($scope, $stateParams, $uibModalInstance, entity, Lot, Produit, Lignelivraison) {
        var vm = this;
        vm.lot = entity;
        vm.produits = Produit.query();
        vm.lignelivraisons = Lignelivraison.query();
        vm.load = function(id) {
            Lot.get({id : id}, function(result) {
                vm.lot = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:lotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lot.id !== null) {
                Lot.update(vm.lot, onSaveSuccess, onSaveError);
            } else {
                Lot.save(vm.lot, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateFabrication = false;
        vm.datePickerOpenStatus.datePeremption = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
