(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ReglementDialogController', ReglementDialogController);

    ReglementDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Reglement', 'Facture'];

    function ReglementDialogController ($scope, $stateParams, $uibModalInstance, entity, Reglement, Facture) {
        var vm = this;
        vm.reglement = entity;
        vm.factures = Facture.query();
        vm.load = function(id) {
            Reglement.get({id : id}, function(result) {
                vm.reglement = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:reglementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.reglement.id !== null) {
                Reglement.update(vm.reglement, onSaveSuccess, onSaveError);
            } else {
                Reglement.save(vm.reglement, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateReglement = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
