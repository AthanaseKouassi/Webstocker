(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('TypeActiviteDialogController', TypeActiviteDialogController);

    TypeActiviteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeActivite', 'Activite'];

    function TypeActiviteDialogController ($scope, $stateParams, $uibModalInstance, entity, TypeActivite, Activite) {
        var vm = this;
        vm.typeActivite = entity;
        vm.activites = Activite.query();
        vm.load = function(id) {
            TypeActivite.get({id : id}, function(result) {
                vm.typeActivite = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:typeActiviteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.typeActivite.id !== null) {
                TypeActivite.update(vm.typeActivite, onSaveSuccess, onSaveError);
            } else {
                TypeActivite.save(vm.typeActivite, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
