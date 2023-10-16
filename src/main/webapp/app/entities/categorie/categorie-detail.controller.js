(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieDetailController', CategorieDetailController);

    CategorieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Categorie', 'Produit'];

    function CategorieDetailController($scope, $rootScope, $stateParams, entity, Categorie, Produit) {
        var vm = this;
        vm.categorie = entity;
        vm.load = function (id) {
            Categorie.get({id: id}, function(result) {
                vm.categorie = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:categorieUpdate', function(event, result) {
            vm.categorie = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
