(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BonDeSortieDialogController', BonDeSortieDialogController);

    BonDeSortieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BonDeSortie', 'Magasin', 'User', 'Client'];

    function BonDeSortieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BonDeSortie, Magasin, User, Client) {
        var vm = this;

        vm.bonDeSortie = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.magasins = Magasin.query();
        vm.users = User.query();
        vm.clients = Client.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bonDeSortie.id !== null) {
                BonDeSortie.update(vm.bonDeSortie, onSaveSuccess, onSaveError);
            } else {
                BonDeSortie.save(vm.bonDeSortie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webstockerApp:bonDeSortieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.daateCreation = false;

        vm.datePickerOpenStatus.dateReception = false;

        vm.datePickerOpenStatus.dateReceptionTransfert = false;


        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
