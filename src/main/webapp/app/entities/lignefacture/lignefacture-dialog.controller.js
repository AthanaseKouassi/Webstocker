(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignefactureDialogController', LignefactureDialogController);

    LignefactureDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lignefacture', 'Lot', 'Facture'];

    function LignefactureDialogController ($scope, $stateParams, $uibModalInstance, entity, Lignefacture, Lot, Facture) {
        var vm = this;
        vm.lignefacture = entity;
        vm.lots = Lot.query();
        vm.factures = Facture.query();
        vm.load = function(id) {
            Lignefacture.get({id : id}, function(result) {
                vm.lignefacture = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:lignefactureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lignefacture.id !== null) {
                Lignefacture.update(vm.lignefacture, onSaveSuccess, onSaveError);
            } else {
                Lignefacture.save(vm.lignefacture, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
