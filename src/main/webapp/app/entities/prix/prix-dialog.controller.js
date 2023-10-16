(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('PrixDialogController', PrixDialogController);

    PrixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prix', 'Produit', 'Categorieclient'];

    function PrixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Prix, Produit, Categorieclient) {
        var vm = this;

        vm.prix = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.produits = Produit.query();
        vm.categorieclients = Categorieclient.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.prix.id !== null) {
                Prix.update(vm.prix, onSaveSuccess, onSaveError);
            } else {
                Prix.save(vm.prix, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webstockerApp:prixUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateFixation = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
