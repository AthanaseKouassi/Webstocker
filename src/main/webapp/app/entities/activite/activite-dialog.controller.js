(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ActiviteDialogController', ActiviteDialogController);

    ActiviteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Activite', 'LigneMissionActivite', 'TypeActivite'];

    function ActiviteDialogController ($scope, $stateParams, $uibModalInstance, entity, Activite, LigneMissionActivite, TypeActivite) {
        var vm = this;
        vm.activite = entity;
        vm.lignemissionactivites = LigneMissionActivite.query();
        vm.typeactivites = TypeActivite.query();
        vm.load = function(id) {
            Activite.get({id : id}, function(result) {
                vm.activite = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:activiteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.activite.id !== null) {
                Activite.update(vm.activite, onSaveSuccess, onSaveError);
            } else {
                Activite.save(vm.activite, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
