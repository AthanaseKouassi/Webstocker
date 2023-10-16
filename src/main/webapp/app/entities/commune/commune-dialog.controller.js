(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CommuneDialogController', CommuneDialogController);

    CommuneDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Commune', 'Ville'];

    function CommuneDialogController ($scope, $stateParams, $uibModalInstance, entity, Commune, Ville) {
        var vm = this;
        vm.commune = entity;
        vm.villes = Ville.query();
        vm.load = function(id) {
            Commune.get({id : id}, function(result) {
                vm.commune = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:communeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.commune.id !== null) {
                Commune.update(vm.commune, onSaveSuccess, onSaveError);
            } else {
                Commune.save(vm.commune, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
