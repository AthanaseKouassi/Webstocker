(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('MissionDialogController', MissionDialogController);

    MissionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Mission', 'LigneMissionActivite', 'LigneBudget', 'Localite', 'Cellule'];

    function MissionDialogController ($scope, $stateParams, $uibModalInstance, entity, Mission, LigneMissionActivite, LigneBudget, Localite, Cellule) {
        var vm = this;
        vm.mission = entity;
        vm.lignemissionactivites = LigneMissionActivite.query();
        vm.lignebudgets = LigneBudget.query();
        vm.localites = Localite.query();
        vm.cellules = Cellule.query();
        vm.load = function(id) {
            Mission.get({id : id}, function(result) {
                vm.mission = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:missionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.mission.id !== null) {
                Mission.update(vm.mission, onSaveSuccess, onSaveError);
            } else {
                Mission.save(vm.mission, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateDebut = false;
        vm.datePickerOpenStatus.dateFin = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
