(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieDeleteController',CategorieDeleteController);

    CategorieDeleteController.$inject = ['$uibModalInstance', 'entity', 'Categorie'];

    function CategorieDeleteController($uibModalInstance, entity, Categorie) {
        var vm = this;
        vm.categorie = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Categorie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
