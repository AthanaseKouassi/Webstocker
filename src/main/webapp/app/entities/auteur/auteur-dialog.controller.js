(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('AuteurDialogController', AuteurDialogController);

    AuteurDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Auteur'];

    function AuteurDialogController ($scope, $stateParams, $uibModalInstance, entity, Auteur) {
        var vm = this;
        vm.auteur = entity;
        vm.load = function(id) {
            Auteur.get({id : id}, function(result) {
                vm.auteur = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:auteurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.auteur.id !== null) {
                Auteur.update(vm.auteur, onSaveSuccess, onSaveError);
            } else {
                Auteur.save(vm.auteur, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
