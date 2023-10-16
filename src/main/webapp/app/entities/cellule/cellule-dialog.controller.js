(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CelluleDialogController', CelluleDialogController);

    CelluleDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cellule', 'User', 'Mission'];

    function CelluleDialogController ($scope, $stateParams, $uibModalInstance, entity, Cellule, User, Mission) {
        var vm = this;
        vm.cellule = entity;
        vm.users = User.query();
        vm.missions = Mission.query();
        vm.load = function(id) {
            Cellule.get({id : id}, function(result) {
                vm.cellule = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:celluleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.cellule.id !== null) {
                Cellule.update(vm.cellule, onSaveSuccess, onSaveError);
            } else {
                Cellule.save(vm.cellule, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
