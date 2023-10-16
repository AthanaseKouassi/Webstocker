(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('InventaireDialogController', InventaireDialogController);

    InventaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inventaire', 'Produit', 'Magasin'];

    function InventaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Inventaire, Produit, Magasin) {
        var vm = this;

        vm.inventaire = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.produits = Produit.query();
        vm.magasins = Magasin.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.inventaire.id !== null) {
                Inventaire.update(vm.inventaire, onSaveSuccess, onSaveError);
            } else {
                Inventaire.save(vm.inventaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webstockerApp:inventaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateInventaire = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
