(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneMissionActiviteDialogController', LigneMissionActiviteDialogController);

    LigneMissionActiviteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LigneMissionActivite', 'Activite', 'Mission'];

    function LigneMissionActiviteDialogController ($scope, $stateParams, $uibModalInstance, entity, LigneMissionActivite, Activite, Mission) {
        var vm = this;
        vm.ligneMissionActivite = entity;
        vm.activites = Activite.query();
        vm.missions = Mission.query();
        vm.load = function(id) {
            LigneMissionActivite.get({id : id}, function(result) {
                vm.ligneMissionActivite = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:ligneMissionActiviteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ligneMissionActivite.id !== null) {
                LigneMissionActivite.update(vm.ligneMissionActivite, onSaveSuccess, onSaveError);
            } else {
                LigneMissionActivite.save(vm.ligneMissionActivite, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateResultat = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
