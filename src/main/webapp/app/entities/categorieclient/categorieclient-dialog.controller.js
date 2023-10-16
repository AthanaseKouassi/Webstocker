(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieclientDialogController', CategorieclientDialogController);

    CategorieclientDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categorieclient', 'Client', 'Prix'];

    function CategorieclientDialogController ($scope, $stateParams, $uibModalInstance, entity, Categorieclient, Client, Prix) {
        var vm = this;
        vm.categorieclient = entity;
        vm.clients = Client.query();
        vm.prixs = Prix.query();
        vm.load = function(id) {
            Categorieclient.get({id : id}, function(result) {
                vm.categorieclient = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('webstockerApp:categorieclientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.categorieclient.id !== null) {
                Categorieclient.update(vm.categorieclient, onSaveSuccess, onSaveError);
            } else {
                Categorieclient.save(vm.categorieclient, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
