(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ObjectifsDialogController', ObjectifsDialogController);

    ObjectifsDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Objectifs', 'Produit'];

    function ObjectifsDialogController ($scope, $stateParams, $uibModalInstance, entity, Objectifs, Produit) {
        var vm = this;
        vm.objectifs = entity;
        vm.produits = Produit.query();
        vm.load = function(id) {
            Objectifs.get({id : id}, function(result) {
                vm.objectifs = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:objectifsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.objectifs.id !== null) {
                Objectifs.update(vm.objectifs, onSaveSuccess, onSaveError);
            } else {
                Objectifs.save(vm.objectifs, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.periode = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
