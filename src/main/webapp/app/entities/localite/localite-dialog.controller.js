(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LocaliteDialogController', LocaliteDialogController);

    LocaliteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Localite', 'Magasin', 'Client', 'Mission', 'Commune'];

    function LocaliteDialogController ($scope, $stateParams, $uibModalInstance, entity, Localite, Magasin, Client, Mission, Commune) {
        var vm = this;
        vm.localite = entity;
        vm.magasins = Magasin.query();
        vm.clients = Client.query();
        vm.missions = Mission.query();
        vm.communes = Commune.query();
        vm.load = function(id) {
            Localite.get({id : id}, function(result) {
                vm.localite = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:localiteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.localite.id !== null) {
                Localite.update(vm.localite, onSaveSuccess, onSaveError);
            } else {
                Localite.save(vm.localite, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
